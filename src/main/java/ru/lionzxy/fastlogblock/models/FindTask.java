package ru.lionzxy.fastlogblock.models;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FindTask {
    private final BlockPos blockPos;
    private final IFindResultListener findListener;
    private final World world;

    public FindTask(BlockPos blockPos, IFindResultListener findListener, World world) {
        this.blockPos = blockPos;
        this.findListener = findListener;
        this.world = world;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public IFindResultListener getFindListener() {
        return findListener;
    }

    public World getWorld() {
        return world;
    }
}
