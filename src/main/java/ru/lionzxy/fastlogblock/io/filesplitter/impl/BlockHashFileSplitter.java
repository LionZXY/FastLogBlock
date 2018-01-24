package ru.lionzxy.fastlogblock.io.filesplitter.impl;

import net.minecraft.util.math.BlockPos;
import ru.lionzxy.fastlogblock.config.LogConfig;
import ru.lionzxy.fastlogblock.io.filesplitter.IFileSplitter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BlockHashFileSplitter extends IFileSplitter {
    private static final int MAGIC_HASH_NUMBER = 31;

    public BlockHashFileSplitter(final File modFolder) {
        super(modFolder);
    }

    private static int hashByBlock(final BlockPos blockPos) {
        final int signedHash = ((blockPos.getX() * MAGIC_HASH_NUMBER +
                blockPos.getY()) * MAGIC_HASH_NUMBER +
                blockPos.getZ()) * MAGIC_HASH_NUMBER;
        final int hash = Math.abs(signedHash) % LogConfig.HASH_CONFIG.fileCount;
        return hash < 0 ? 0 : hash;
    }

    @Override
    public File[] getAllLogFile() {
        final List<File> fileList = new ArrayList<>(LogConfig.HASH_CONFIG.fileCount);
        for (int i = 0; i < LogConfig.HASH_CONFIG.fileCount; i++) {
            fileList.add(new File(this.modFolder,
                    String.format(LogConfig.HASH_CONFIG.fileNamePattern, i)
            ));
        }
        return fileList.toArray(new File[fileList.size()]);
    }

    @Override
    public File getFileByPos(final BlockPos blockPos) {
        return new File(String.format(LogConfig.HASH_CONFIG.fileNamePattern, hashByBlock(blockPos)));
    }


}
