package ru.lionzxy.fastlogblock.io.filesplitter.impl;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
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
    public File getFileByPosAndWorld(final BlockPos blockPos, World world) {
        if (world == null) {
            return new File(modFolder, "block.bytelog");
        }

        File saveFile = DimensionManager.getCurrentSaveRootDirectory();
        if (saveFile == null) {
            saveFile = new File("save0");
        }
        final File saveFolder = new File(modFolder, saveFile.getName());
        String worldSave = world.provider.getSaveFolder();
        if (worldSave == null) {
            worldSave = "DIM0";
        }
        final File dimFolder = new File(saveFolder, new File(worldSave).getName());
        return new File(dimFolder, "block.bytelog");
    }
}
