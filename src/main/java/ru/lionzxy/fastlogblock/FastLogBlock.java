package ru.lionzxy.fastlogblock;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.lionzxy.fastlogblock.config.LogConfig;
import ru.lionzxy.fastlogblock.models.BlockChangeEventModel;

import java.io.File;
import java.io.IOException;

@Mod(modid = FastLogBlock.MODID, version = FastLogBlock.VERSION)
public class FastLogBlock {
    public static final File rootMinecraftPath = new File("./");
    public static final String MODID = "fastlogblock";
    public static final String VERSION = "1.0";
    @Mod.Instance
    public static FastLogBlock fastLogBlock;
    public File logFolderFile;

    @EventHandler
    public void init(final FMLInitializationEvent event) throws IOException {
        logFolderFile = new File(rootMinecraftPath, LogConfig.logFolderPath);
        FMLLog.log.info("FullPath: " + logFolderFile.getAbsolutePath());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBlockBreak(final BlockEvent.BreakEvent event) {
        final BlockChangeEventModel blockChangeEventModel = BlockChangeEventModel.getChangeEvent(event);

        if (blockChangeEventModel == null) {
            return;
        }

        FMLLog.log.info(blockChangeEventModel.toString());
    }

    @SubscribeEvent
    public void onBlockPlace(final BlockEvent.PlaceEvent event) {
        final BlockChangeEventModel blockChangeEventModel = BlockChangeEventModel.getChangeEvent(event);

        if (blockChangeEventModel == null) {
            return;
        }

        FMLLog.log.info(blockChangeEventModel.toString());
    }
}
