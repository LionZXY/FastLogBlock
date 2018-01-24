package ru.lionzxy.fastlogblock.models;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.sql.Timestamp;

public class BlockChangeEventModelWithWorld extends BlockChangeEventModel {
    private final World world;

    public BlockChangeEventModelWithWorld(BlockPos blockPos, String nameblock, String playernick, Timestamp timestamp, BlockChangeType blockChangeType, World world) {
        super(blockPos, nameblock, playernick, timestamp, blockChangeType);
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
