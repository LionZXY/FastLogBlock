package ru.lionzxy.fastlogblock.models;

import net.minecraft.util.math.BlockPos;

public class FindTask {
    private final BlockPos blockPos;
    private final IFindResultListener findListener;

    public FindTask(BlockPos blockPos, IFindResultListener findListener) {
        this.blockPos = blockPos;
        this.findListener = findListener;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public IFindResultListener getFindListener() {
        return findListener;
    }
}
