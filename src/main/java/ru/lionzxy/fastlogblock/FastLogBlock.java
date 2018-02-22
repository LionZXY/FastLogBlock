package ru.lionzxy.fastlogblock;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import ru.lionzxy.fastlogblock.handlers.EventHandlingManager;
import ru.lionzxy.fastlogblock.ui.InfoItem;

import java.io.IOException;

@Mod(modid = FastLogBlock.MODID, version = FastLogBlock.VERSION, updateJSON = "https://raw.githubusercontent.com/LionZXY/FastLogBlock/master/update.json")
public class FastLogBlock {
    public static final String MODID = "fastlogblock";
    public static final String VERSION = "1.0.1";
    private EventHandlingManager eventHandlingManager;
    private InfoItem infoitem;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        FMLLog.log.info("Initializing eventHandlingManager...");
        eventHandlingManager = new EventHandlingManager();
        this.infoitem = new InfoItem(eventHandlingManager);
        FMLLog.log.info("Done!");
        MinecraftForge.EVENT_BUS.register(eventHandlingManager);
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event){
        ModelLoader.setCustomModelResourceLocation(infoitem, 0, new ModelResourceLocation(infoitem.getRegistryName(), "inventory"));
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(infoitem);
    }

    @EventHandler
    public void serverStopped(final FMLServerStoppedEvent event) {
        eventHandlingManager.stop();
    }
}
