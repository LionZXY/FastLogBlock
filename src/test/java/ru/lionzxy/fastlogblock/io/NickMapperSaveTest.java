package ru.lionzxy.fastlogblock.io;

import junit.framework.TestCase;
import ru.lionzxy.fastlogblock.io.mappers.NickMapper;
import ru.lionzxy.fastlogblock.models.ASCIString;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class NickMapperSaveTest extends TestCase {
    private NickMapper nickMapper;
    private File testFile;

    private void before() throws IOException {
        testFile = new File("./test.bytelog");
        this.nickMapper = new NickMapper(testFile);
    }

    public void testSave() throws IOException {
        before();
        nickMapper.getOrPutUser(new ASCIString("LionZXY"));
        nickMapper.getOrPutUser(new ASCIString("LionZXY"));
        nickMapper.getOrPutUser(new ASCIString("3"));
        nickMapper.getOrPutUser(new ASCIString("LionZXY"));
        nickMapper.getOrPutUser(new ASCIString("2"));
        nickMapper.getOrPutUser(new ASCIString("2"));
        nickMapper.getOrPutUser(new ASCIString("2"));
        nickMapper.getOrPutUser(new ASCIString("LionZXY"));
        nickMapper.getOrPutUser(new ASCIString("2"));
        nickMapper.getOrPutUser(new ASCIString("2"));
        nickMapper.getOrPutUser(new ASCIString("LionZXY"));
        nickMapper.getOrPutUser(new ASCIString("2"));
        nickMapper.getOrPutUser(new ASCIString("2"));


        final int first = nickMapper.getOrPutUser(new ASCIString("LionZXY"));
        final int second = nickMapper.getOrPutUser(new ASCIString("2"));
        final int third = nickMapper.getOrPutUser(new ASCIString("3"));
        nickMapper.sync();
        nickMapper = new NickMapper(testFile);
        assertEquals(first, nickMapper.getOrPutUser(new ASCIString("LionZXY")));
        assertEquals(second, nickMapper.getOrPutUser(new ASCIString("2")));
        assertEquals(third, nickMapper.getOrPutUser(new ASCIString("3")));
        final int max = Collections.max(Arrays.asList(first, second, third));
        assertEquals(max + 1, nickMapper.getOrPutUser(new ASCIString("other")));
        after();
    }

    private void after() throws IOException {
        assertTrue(testFile.delete());
    }
}
