package ru.lionzxy.fastlogblock.io.filesplitter.impl;

import net.minecraft.util.math.BlockPos;
import ru.lionzxy.fastlogblock.io.filesplitter.IFileSplitter;

import java.io.File;

public class SingleFileSplitter extends IFileSplitter {
    private final File logFile;

    public SingleFileSplitter(final File modFolder) {
        super(modFolder);
        logFile = new File(modFolder, "block.bytelog");
    }

    @Override
    public File[] getAllLogFile() {
        return new File[]{logFile};
    }

    @Override
    public File getFileByPos(final BlockPos blockPos) {
        return logFile;
    }
}
