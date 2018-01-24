package ru.lionzxy.fastlogblock.io.filesplitter;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.File;

public abstract class IFileSplitter {
    protected final File modFolder;

    public IFileSplitter(final File modFolder) {
        this.modFolder = modFolder;
    }

    public abstract File[] getAllLogFile();

    public abstract File getFileByPosAndWorld(BlockPos blockPos, World world);
}
