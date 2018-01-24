package ru.lionzxy.fastlogblock.handlers;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.lionzxy.fastlogblock.models.BlockChangeEventModel;
import ru.lionzxy.fastlogblock.models.BlockChangeEventModelWithWorld;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EventHandlingManager {
    private final Executor executor = Executors.newCachedThreadPool();
    private final SplitterRunnable splitterRunnable;

    public EventHandlingManager() throws IOException {
        this.splitterRunnable = new SplitterRunnable();
        splitterRunnable.runWorkers(executor);
        executor.execute(splitterRunnable);
    }

    @SubscribeEvent
    public void onBlockBreak(final BlockEvent.BreakEvent event) {
        final BlockChangeEventModelWithWorld blockChangeEventModel = (BlockChangeEventModelWithWorld) BlockChangeEventModel.getChangeEvent(event);

        if (blockChangeEventModel == null) {
            return;
        }

        FMLLog.log.debug(blockChangeEventModel.toString());
        splitterRunnable.addEvent(blockChangeEventModel);
    }

    @SubscribeEvent
    public void onBlockPlace(final BlockEvent.PlaceEvent event) {
        final BlockChangeEventModelWithWorld blockChangeEventModel = (BlockChangeEventModelWithWorld) BlockChangeEventModel.getChangeEvent(event);

        if (blockChangeEventModel == null) {
            return;
        }

        FMLLog.log.debug(blockChangeEventModel.toString());
        splitterRunnable.addEvent(blockChangeEventModel);
    }
}
