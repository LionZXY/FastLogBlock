package ru.lionzxy.fastlogblock.models;

import jline.internal.Nullable;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public interface IFindResultListener {
    void onResultAsync(List<BlockChangeEventModel> blockChangeEventModels, @Nullable EntityPlayer entityPlayer);
}
