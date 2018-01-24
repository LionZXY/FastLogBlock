package ru.lionzxy.fastlogblock.io;

import ru.lionzxy.fastlogblock.io.filesplitter.IFileSplitter;
import ru.lionzxy.fastlogblock.io.log.LogReader;
import ru.lionzxy.fastlogblock.io.mappers.BlockMapper;
import ru.lionzxy.fastlogblock.io.mappers.NickMapper;
import ru.lionzxy.fastlogblock.models.BlockChangeEventModel;
import ru.lionzxy.fastlogblock.models.FindTask;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReadRunnable implements Runnable {
    private final BlockingQueue<FindTask> findTasks = new LinkedBlockingQueue<>();
    private final NickMapper nickMapper;
    private final BlockMapper blockMapper;
    private final IFileSplitter fileSplitter;
    private final AtomicBoolean withoutWork = new AtomicBoolean(true);

    public ReadRunnable(final IFileSplitter fileSplitter, final NickMapper nickMapper, final BlockMapper blockMapper) {
        this.nickMapper = nickMapper;
        this.blockMapper = blockMapper;
        this.fileSplitter = fileSplitter;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final FindTask findTask = findTasks.take();
                withoutWork.set(false);
                final File file = fileSplitter.getFileByPosAndWorld(findTask.getBlockPos(), findTask.getWorld());
                final LogReader logReader = new LogReader(file, blockMapper, nickMapper);
                final List<BlockChangeEventModel> blockChangeEventModels = logReader.readEventByPos(findTask.getBlockPos());
                findTask.getFindListener().onResultAsync(blockChangeEventModels);
            } catch (Exception e) {
                e.printStackTrace();
            }
            withoutWork.set(true);
        }
    }

    public void addTaskForSearch(FindTask findTask) {
        findTasks.add(findTask);
    }

    public boolean isEmpty() {
        return findTasks.isEmpty() && withoutWork.get();
    }
}
