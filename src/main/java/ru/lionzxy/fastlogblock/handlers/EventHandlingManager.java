package ru.lionzxy.fastlogblock.handlers;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.lionzxy.fastlogblock.config.LogConfig;
import ru.lionzxy.fastlogblock.io.ReadRunnable;
import ru.lionzxy.fastlogblock.models.BlockChangeEventModel;
import ru.lionzxy.fastlogblock.models.BlockChangeEventModelWithWorld;
import ru.lionzxy.fastlogblock.models.FindTask;
import ru.lionzxy.fastlogblock.models.FindTaskResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class EventHandlingManager {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final BlockingQueue<FindTaskResult> findTaskResults = new LinkedBlockingQueue<>();
    private final ReadRunnable readRunnable;
    private final SplitterRunnable splitterRunnable;

    public EventHandlingManager() throws IOException {
        this.splitterRunnable = new SplitterRunnable();
        this.readRunnable = splitterRunnable.getReadRunnable();
        splitterRunnable.runWorkers(executor);
        executor.execute(splitterRunnable);
        executor.execute(readRunnable);
    }

    @SubscribeEvent
    public void onBlockBreak(final BlockEvent.BreakEvent event) {
        logEvent((BlockChangeEventModelWithWorld) BlockChangeEventModel.getChangeEvent(event), event);
    }

    @SubscribeEvent
    public void onBlockPlace(final BlockEvent.PlaceEvent event) {
        logEvent((BlockChangeEventModelWithWorld) BlockChangeEventModel.getChangeEvent(event), event);
    }

    @SubscribeEvent
    public void flushUIWait(TickEvent.ServerTickEvent event) {
        if (findTaskResults.isEmpty()) {
            return;
        }
        FindTaskResult findTaskResult;
        while ((findTaskResult = findTaskResults.poll()) != null) {
            for (BlockChangeEventModel blockEvent : findTaskResult.getBlockChangeEventModels()) {
                notifyAboutEvent(blockEvent, findTaskResult.getEntityPlayer());
            }
            if (findTaskResult.getBlockChangeEventModels().isEmpty()) {
                findTaskResult.getEntityPlayer().sendMessage(new TextComponentTranslation("message.fastlogblock:blockinfo.event.empty"));
            } else {
                findTaskResult.getEntityPlayer().sendMessage(new TextComponentTranslation("message.fastlogblock:blockinfo.event.done"));
            }
        }
    }

    public void handleLogByPos(EntityPlayer entityPlayer, BlockPos blockPos, World world) {
        final FindTask findTask = new FindTask(blockPos, (list, player) -> {
            findTaskResults.add(new FindTaskResult(list, player));
        }, world);
        findTask.setEntityPlayer(entityPlayer);
        readRunnable.addTaskForSearch(findTask);
    }

    public void stop() {
        executor.shutdownNow();
    }

    private void notifyAboutEvent(BlockChangeEventModel blockEvent, EntityPlayer entityPlayer) {
        final String dateformat = I18n.format("message.fastlogblock:blockinfo.event.dateformat");
        final EntityPlayer playerEvent = entityPlayer.getEntityWorld().getPlayerEntityByUUID(UUID.fromString(blockEvent.getPlayernick().toString()));
        String nickname;
        if (playerEvent == null) {
            if (entityPlayer.getEntityWorld().getMinecraftServer().isSinglePlayer()) {
                nickname = entityPlayer.getDisplayNameString();
            } else {
                nickname = "Unknown(UUID: " + blockEvent.getPlayernick() + ")";
            }
        } else {
            nickname = playerEvent.getDisplayNameString();
        }
        final String[] args = new String[]{new SimpleDateFormat(dateformat).format(new Date(blockEvent.getTimestamp().getTime())),
                nickname,
                blockEvent.getNameblock().toString()};
        TextComponentTranslation textComponent;
        switch (blockEvent.getBlockChangeType()) {
            default:
            case INSERT:
                textComponent = new TextComponentTranslation("message.fastlogblock:blockinfo.event.insert", (Object[]) args);
                break;
            case REMOVE:
                textComponent = new TextComponentTranslation("message.fastlogblock:blockinfo.event.remove", (Object[]) args);
        }
        entityPlayer.sendMessage(textComponent);
    }


    private void logEvent(BlockChangeEventModelWithWorld changeEvent, BlockEvent event) {
        if (!LogConfig.loggingEnable) {
            return;
        }

        if (changeEvent == null) {
            return;
        }

        FMLLog.log.debug(changeEvent.toString());
        splitterRunnable.addEvent(changeEvent);
    }
}
