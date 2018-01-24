package ru.lionzxy.fastlogblock.io;

import junit.framework.TestCase;
import ru.lionzxy.fastlogblock.io.mappers.BlockMapper;
import ru.lionzxy.fastlogblock.models.ASCIString;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class BlockMapperSaveTest extends TestCase {
    private BlockMapper blockMapper;
    private File testFile;

    private void before() throws IOException {
        testFile = new File("./test.bytelog");
        this.blockMapper = new BlockMapper(testFile);
    }

    private void fill() {
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

    public void testSave() throws IOException {
        before();

        final long first = blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>"));
        final long second = blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>"));
        final long third = blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>"));
        blockMapper.sync();
        blockMapper = new BlockMapper(testFile);
        assertEquals(first, blockMapper.getOrPutBlock(new ASCIString("<minecraft:test:1>")));
        assertEquals(second, blockMapper.getOrPutBlock(new ASCIString("<alpha:something:1>")));
        assertEquals(third, blockMapper.getOrPutBlock(new ASCIString("<openblock:so:3214>")));
        final long max = Collections.max(Arrays.asList(first, second, third));
        assertEquals(max + 1, blockMapper.getOrPutBlock(new ASCIString("other")));
        after();
    }

    private void after() throws IOException {
        assertTrue(testFile.delete());
    }
}
