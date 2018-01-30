package ru.lionzxy.fastlogblock.utils;

import net.minecraft.entity.player.EntityPlayer;
import ru.lionzxy.fastlogblock.config.LogConfig;

public class MinecraftUtils {
    public static boolean canShowLog(EntityPlayer player) {
        return !LogConfig.onlyForOP || player.getEntityWorld().getMinecraftServer().isSinglePlayer() || player.canUseCommand(player.getEntityWorld().getMinecraftServer().getOpPermissionLevel(), "");
    }
}
