package ru.lionzxy.fastlogblock.utils;

import net.minecraft.entity.player.EntityPlayer;

public class MinecraftUtils {
    public static boolean isOp(EntityPlayer player) {
        player.getEntityWorld().getMinecraftServer();
        return false;
    }
}
