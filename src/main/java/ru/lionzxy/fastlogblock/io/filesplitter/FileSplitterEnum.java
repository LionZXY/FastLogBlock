package ru.lionzxy.fastlogblock.io.filesplitter;

import ru.lionzxy.fastlogblock.FastLogBlock;
import ru.lionzxy.fastlogblock.io.filesplitter.impl.BlockHashFileSplitter;
import ru.lionzxy.fastlogblock.io.filesplitter.impl.SingleFileSplitter;

public enum FileSplitterEnum {
    SINGLE(new SingleFileSplitter(FastLogBlock.rootMinecraftPath)),
    BLOCKHASH(new BlockHashFileSplitter(FastLogBlock.rootMinecraftPath));

    private final IFileSplitter fileSplitter;

    FileSplitterEnum(final IFileSplitter fileSplitter) {
        this.fileSplitter = fileSplitter;
    }

    public IFileSplitter getFileSplitter() {
        return fileSplitter;
    }
}
