package ru.lionzxy.fastlogblock;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import ru.lionzxy.fastlogblock.handlers.EventHandlingManager;

import java.io.File;
import java.io.IOException;

@Mod(modid = FastLogBlock.MODID, version = FastLogBlock.VERSION)
public class FastLogBlock {
    public static final File rootMinecraftPath = new File("./");
    public static final String MODID = "fastlogblock";
    public static final String VERSION = "1.0";
    private EventHandlingManager eventHandlingManager;

    @EventHandler
    public void init(final FMLInitializationEvent event) throws IOException {
        FMLLog.log.info("Initializing eventHandlingManager...");
        eventHandlingManager = new EventHandlingManager();
        FMLLog.log.info("Done!");
        MinecraftForge.EVENT_BUS.register(eventHandlingManager);
    }
}
