package ru.lionzxy.fastlogblock.models;

import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class FindTaskResult {
    private final List<BlockChangeEventModel> blockChangeEventModels;
    private final EntityPlayer entityPlayer;

    public FindTaskResult(List<BlockChangeEventModel> blockChangeEventModels, EntityPlayer entityPlayer) {
        this.blockChangeEventModels = blockChangeEventModels;
        this.entityPlayer = entityPlayer;
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    public List<BlockChangeEventModel> getBlockChangeEventModels() {

        return blockChangeEventModels;
    }
}
