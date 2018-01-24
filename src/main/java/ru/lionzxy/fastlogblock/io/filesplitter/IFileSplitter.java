package ru.lionzxy.fastlogblock.io.filesplitter;

import net.minecraft.util.math.BlockPos;

import java.io.File;

public abstract class IFileSplitter {
    protected final File modFolder;

    public IFileSplitter(final File modFolder) {
        this.modFolder = modFolder;
    }

    public abstract File[] getAllLogFile();

    public abstract File getFileByPos(BlockPos blockPos);
}
