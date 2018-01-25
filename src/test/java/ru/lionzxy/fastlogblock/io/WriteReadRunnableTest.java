package ru.lionzxy.fastlogblock.io;

import junit.framework.TestCase;
import net.minecraft.util.math.BlockPos;
import ru.lionzxy.fastlogblock.io.filesplitter.impl.BlockHashFileSplitter;
import ru.lionzxy.fastlogblock.io.mappers.BlockMapper;
import ru.lionzxy.fastlogblock.io.mappers.NickMapper;
import ru.lionzxy.fastlogblock.models.BlockChangeEventModelWithWorld;
import ru.lionzxy.fastlogblock.models.BlockChangeType;
import ru.lionzxy.fastlogblock.models.FindTask;
import ru.lionzxy.fastlogblock.utils.TestUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class WriteReadRunnableTest extends TestCase {
    private BlockMapper blockMapper;
    private NickMapper nickMapper;
    private final File logFile = new File("multithreadlog.bytelog");
    private WriteRunnable writeRunnable;
    private ReadRunnable readRunnable;

    private void before() throws IOException {
        TestUtils.removeByteLog();
        blockMapper = new BlockMapper(new File("blockmap.bytelog"));
        nickMapper = new NickMapper(new File("nickmap.bytelog"));
        writeRunnable = new WriteRunnable(new BlockHashFileSplitter(new File("./")), nickMapper, blockMapper);
        readRunnable = new ReadRunnable(new BlockHashFileSplitter(new File("./")), nickMapper, blockMapper);
    }

    private void fillLog() {
        writeRunnable.putEvent(new BlockChangeEventModelWithWorld(new BlockPos(-2, 3, 5),
                "<minecraft:block:1>", "LionZXY",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT, null));
        writeRunnable.putEvent(new BlockChangeEventModelWithWorld(new BlockPos(8, 9, 10),
                "<mode:block>", "SomeNickname",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT, null));
        writeRunnable.putEvent(new BlockChangeEventModelWithWorld(new BlockPos(10, 3, 2),
                "<mod:blok:2>", "LionZXY",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT, null));
        writeRunnable.putEvent(new BlockChangeEventModelWithWorld(new BlockPos(20, 40, 5),
                "<somemode:block:3>", "SomeNickname2",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT, null));
        writeRunnable.putEvent(new BlockChangeEventModelWithWorld(new BlockPos(-2, 3, 5),
                "<oh:sad:0>", "Typical",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT, null));
        writeRunnable.putEvent(new BlockChangeEventModelWithWorld(new BlockPos(-2, 3, 5),
                "<minecraft:block:1>", "LionZXY",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT, null));
        writeRunnable.putEvent(new BlockChangeEventModelWithWorld(new BlockPos(-2, 3, 5),
                "<minecraft:block:1>", "LionZXY",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT, null));
    }

    public void testRunnable() throws Exception {
        before();
        fillLog();
        Thread thread1 = Executors.defaultThreadFactory().newThread(writeRunnable);
        Thread thread2 = Executors.defaultThreadFactory().newThread(readRunnable);
        thread1.start();
        thread2.start();

        while (!writeRunnable.isEmpty()) {
            Thread.sleep(100);
        }

        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        readRunnable.addTaskForSearch(new FindTask(new BlockPos(-2, 3, 5), (list, player) -> {
            atomicBoolean.set(4 == list.size());
        }, null));

        while (!readRunnable.isEmpty()) {
            Thread.sleep(100);
        }

        assertTrue(atomicBoolean.get());
        thread1.interrupt();
        thread2.interrupt();
        after();
    }

    private void after() throws IOException {
        blockMapper.sync();
        nickMapper.sync();

        new File("blockmap.bytelog").delete();
        new File("nickmap.bytelog").delete();
        logFile.delete();
        TestUtils.removeByteLog();
    }
}
