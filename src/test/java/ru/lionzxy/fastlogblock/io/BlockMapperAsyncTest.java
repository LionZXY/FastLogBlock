package ru.lionzxy.fastlogblock.io;

import com.google.testing.threadtester.*;
import junit.framework.TestCase;
import ru.lionzxy.fastlogblock.io.mappers.BlockMapper;
import ru.lionzxy.fastlogblock.models.ASCIString;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class BlockMapperAsyncTest extends TestCase {
    private volatile BlockMapper blockMapper;

    public void testNickMapper() {
        final AnnotatedTestRunner runner = new AnnotatedTestRunner();
        runner.runTests(this.getClass(), BlockMapper.class);
    }

    @ThreadedBefore
    public void before() throws IOException {
        blockMapper = new BlockMapper();
    }

    @ThreadedMain
    public void main() {
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
    }


    @ThreadedSecondary
    public void secondary() {
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));

    }

    @ThreadedAfter
    public void after() {
        final long first = blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        final long second = blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        final long third = blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        assertEquals(3, blockMapper.size());
        assertEquals(Long.MIN_VALUE + 3, Collections.max(Arrays.asList(first, second, third)).longValue());
    }
}
