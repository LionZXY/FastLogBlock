package ru.lionzxy.fastlogblock.handlers;

import net.minecraftforge.fml.common.FMLLog;
import ru.lionzxy.fastlogblock.config.LogConfig;
import ru.lionzxy.fastlogblock.io.ReadRunnable;
import ru.lionzxy.fastlogblock.io.WriteRunnable;
import ru.lionzxy.fastlogblock.io.filesplitter.IFileSplitter;
import ru.lionzxy.fastlogblock.io.filesplitter.impl.BlockHashFileSplitter;
import ru.lionzxy.fastlogblock.io.filesplitter.impl.SingleFileSplitter;
import ru.lionzxy.fastlogblock.io.mappers.BlockMapper;
import ru.lionzxy.fastlogblock.io.mappers.NickMapper;
import ru.lionzxy.fastlogblock.models.BlockChangeEventModelWithWorld;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SplitterRunnable implements Runnable {
    private final BlockingQueue<BlockChangeEventModelWithWorld> eventQueue = new LinkedBlockingQueue<>();
    private final WriteRunnable[] writeWorkers = new WriteRunnable[LogConfig.writeWorkersCount];
    private final Map<File, WriteRunnable> fileToWriteWorker = new HashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    private final IFileSplitter fileSplitter;
    private final NickMapper nickMapper;
    private final BlockMapper blockMapper;

    public SplitterRunnable() throws IOException {
        final File rootFile = new File(LogConfig.logFolderPath);

        switch (LogConfig.fileSplitterType) {
            case SINGLE:
                fileSplitter = new SingleFileSplitter(rootFile);
                break;
            default:
            case BLOCKHASH:
                fileSplitter = new BlockHashFileSplitter(rootFile);
                break;
        }
        this.nickMapper = new NickMapper(new File(rootFile, LogConfig.nickToIntFilePath));
        this.blockMapper = new BlockMapper(new File(rootFile, LogConfig.blockToLongFilePath));

        for (int i = 0; i < LogConfig.writeWorkersCount; i++) {
            writeWorkers[i] = new WriteRunnable(fileSplitter, nickMapper, blockMapper);
        }
    }

    public void runWorkers(Executor executor) {
        for (int i = 0; i < LogConfig.writeWorkersCount; i++) {
            executor.execute(writeWorkers[i]);
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                processEvent(eventQueue.take());
                nickMapper.sync();
                blockMapper.sync();
            } catch (InterruptedException ie) {
                FMLLog.log.info("Stop SplitterRunnable");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addEvent(BlockChangeEventModelWithWorld blockChangeEventModelWithWorld) {
        eventQueue.add(blockChangeEventModelWithWorld);
    }

    public ReadRunnable getReadRunnable() {
        return new ReadRunnable(fileSplitter, nickMapper, blockMapper);
    }

    private void processEvent(BlockChangeEventModelWithWorld event) {
        final File file = fileSplitter.getFileByPosAndWorld(event.getBlockPos(), event.getWorld());

        if (event.isIgnore()) {
            return;
        }

        WriteRunnable writeRunnable = fileToWriteWorker.get(file);
        if (writeRunnable != null) {
            writeRunnable.putEvent(event);
            return;
        }

        if (counter.get() >= LogConfig.writeWorkersCount) {
            counter.set(0);
        }

        writeRunnable = writeWorkers[counter.getAndIncrement()];
        fileToWriteWorker.put(file, writeRunnable);
        writeRunnable.putEvent(event);
    }
}
