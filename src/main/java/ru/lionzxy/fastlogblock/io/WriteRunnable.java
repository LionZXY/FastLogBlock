package ru.lionzxy.fastlogblock.io;

import ru.lionzxy.fastlogblock.io.filesplitter.IFileSplitter;
import ru.lionzxy.fastlogblock.io.log.LogWritter;
import ru.lionzxy.fastlogblock.io.mappers.BlockMapper;
import ru.lionzxy.fastlogblock.io.mappers.NickMapper;
import ru.lionzxy.fastlogblock.models.BlockChangeEventModelWithWorld;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class WriteRunnable implements Runnable {
    private final BlockingQueue<BlockChangeEventModelWithWorld> eventQueue = new LinkedBlockingQueue<>();
    private final Map<File, LogWritter> writterMap = new HashMap<>();
    private final NickMapper nickMapper;
    private final BlockMapper blockMapper;
    private final IFileSplitter fileSplitter;
    private final AtomicBoolean withoutWork = new AtomicBoolean(true);

    public WriteRunnable(final IFileSplitter fileSplitter, final NickMapper nickMapper, final BlockMapper blockMapper) {
        this.nickMapper = nickMapper;
        this.blockMapper = blockMapper;
        this.fileSplitter = fileSplitter;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                do {
                    final BlockChangeEventModelWithWorld event = eventQueue.take();
                    withoutWork.set(false);
                    final File file = fileSplitter.getFileByPosAndWorld(event.getBlockPos(), event.getWorld());
                    LogWritter writter = writterMap.get(file);
                    if (writter == null) {
                        writter = new LogWritter(file, blockMapper, nickMapper);
                        writterMap.put(file, writter);
                    }
                    writter.putEvent(event);
                } while (!eventQueue.isEmpty());

                writterMap.values().forEach(it -> {
                    try {
                        it.sync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                withoutWork.set(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void putEvent(BlockChangeEventModelWithWorld blockChangeEventModel) {
        eventQueue.add(blockChangeEventModel);
    }

    public boolean isEmpty() {
        return eventQueue.isEmpty() && withoutWork.get();
    }
}
