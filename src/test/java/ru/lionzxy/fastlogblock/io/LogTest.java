package ru.lionzxy.fastlogblock.io;

import junit.framework.TestCase;
import net.minecraft.util.math.BlockPos;
import ru.lionzxy.fastlogblock.models.BlockChangeType;
import ru.lionzxy.fastlogblock.io.log.LogReader;
import ru.lionzxy.fastlogblock.io.log.LogWritter;
import ru.lionzxy.fastlogblock.io.mappers.BlockMapper;
import ru.lionzxy.fastlogblock.io.mappers.NickMapper;
import ru.lionzxy.fastlogblock.models.BlockChangeEventModel;
import ru.lionzxy.fastlogblock.utils.TestUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class LogTest extends TestCase {
    private BlockMapper blockMapper;
    private NickMapper nickMapper;
    private final File logFile = new File("log.bytelog");
    private LogWritter logWritter;


    private void before() throws IOException {
        TestUtils.removeByteLog();
        new File("blockmap.bytelog").delete();
        new File("nickmap.bytelog").delete();
        logFile.delete();
        blockMapper = new BlockMapper(new File("blockmap.bytelog"));
        nickMapper = new NickMapper(new File("nickmap.bytelog"));
        logWritter = new LogWritter(logFile, blockMapper, nickMapper);
    }

    private void fillLog() {
        logWritter.putEvent(new BlockChangeEventModel(new BlockPos(-2, 3, 5),
                "<minecraft:block:1>", "LionZXY",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT));
        logWritter.putEvent(new BlockChangeEventModel(new BlockPos(8, 9, 10),
                "<mode:block>", "SomeNickname",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT));
        logWritter.putEvent(new BlockChangeEventModel(new BlockPos(10, 3, 2),
                "<mod:blok:2>", "LionZXY",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT));
        logWritter.putEvent(new BlockChangeEventModel(new BlockPos(20, 40, 5),
                "<somemode:block:3>", "SomeNickname2",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT));
        logWritter.putEvent(new BlockChangeEventModel(new BlockPos(-2, 3, 5),
                "<oh:sad:0>", "Typical",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT));
        logWritter.putEvent(new BlockChangeEventModel(new BlockPos(-2, 3, 5),
                "<minecraft:block:1>", "LionZXY",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT));
        logWritter.putEvent(new BlockChangeEventModel(new BlockPos(-2, 3, 5),
                "<minecraft:block:1>", "LionZXY",
                new Timestamp(System.currentTimeMillis()), BlockChangeType.INSERT));
    }

    public void testLog() throws IOException {
        before();
        for (int i = 0; i < 1000; i++) {
            fillLog();
        }
        logWritter.sync();

        final LogReader logReader = new LogReader(logFile, blockMapper, nickMapper);

        final List<BlockChangeEventModel> blockChangeEventModels = logReader.readEventByPos(new BlockPos(10, 3, 2));

        assertEquals(1000, blockChangeEventModels.size());

        after();
    }


    private void after() throws IOException {
        blockMapper.sync();
        nickMapper.sync();
        logWritter.sync();

        new File("blockmap.bytelog").delete();
        new File("nickmap.bytelog").delete();
        logFile.delete();
        TestUtils.removeByteLog();
    }

}
