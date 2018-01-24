package ru.lionzxy.fastlogblock.io;

import com.google.testing.threadtester.*;
import junit.framework.TestCase;
import ru.lionzxy.fastlogblock.io.mappers.NickMapper;
import ru.lionzxy.fastlogblock.models.ASCIString;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class NickMapperAsyncTest extends TestCase {
    private volatile NickMapper nickMapper;

    public void testNickMapper() {
        final AnnotatedTestRunner runner = new AnnotatedTestRunner();
        runner.runTests(this.getClass(), NickMapper.class);
    }

    @ThreadedBefore
    public void before() throws IOException {
        nickMapper = new NickMapper();
    }

    @ThreadedMain
    public void main() {
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
    }


    @ThreadedSecondary
    public void secondary() {
        nickMapper.getOrPutUser(new ASCIString("2"));
        nickMapper.getOrPutUser(new ASCIString("LionZXY"));
        nickMapper.getOrPutUser(new ASCIString("3"));
        nickMapper.getOrPutUser(new ASCIString("3"));
        nickMapper.getOrPutUser(new ASCIString("2"));
        nickMapper.getOrPutUser(new ASCIString("LionZXY"));
        nickMapper.getOrPutUser(new ASCIString("2"));
        nickMapper.getOrPutUser(new ASCIString("LionZXY"));
        nickMapper.getOrPutUser(new ASCIString("3"));
        nickMapper.getOrPutUser(new ASCIString("2"));
        nickMapper.getOrPutUser(new ASCIString("LionZXY"));

    }

    @ThreadedAfter
    public void after() {
        final int first = nickMapper.getOrPutUser(new ASCIString("LionZXY"));
        final int second = nickMapper.getOrPutUser(new ASCIString("2"));
        final int third = nickMapper.getOrPutUser(new ASCIString("3"));
        assertEquals(3, nickMapper.size());
        assertEquals(Integer.MIN_VALUE + 3, Collections.max(Arrays.asList(first, second, third)).intValue());
    }
}
