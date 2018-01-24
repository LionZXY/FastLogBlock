package ru.lionzxy.fastlogblock.models;

import jline.internal.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;

import java.sql.Timestamp;

public class BlockChangeEventModel {
    private final int posX, posY, posZ;
    private final ASCIString nameblock;
    private final ASCIString playernick;
    private final BlockChangeType blockChangeType;
    private final Timestamp timestamp;

    public BlockChangeEventModel(final BlockPos blockPos,
                                 final String nameblock,
                                 final String playernick,
                                 final Timestamp timestamp,
                                 final BlockChangeType blockChangeType) {
        this.posX = blockPos.getX();
        this.posY = blockPos.getY();
        this.posZ = blockPos.getZ();

        this.nameblock = new ASCIString(nameblock);
        this.playernick = new ASCIString(playernick);

        this.blockChangeType = blockChangeType;
        this.timestamp = timestamp;
    }

    @Nullable
    public static BlockChangeEventModel getChangeEvent(final BlockEvent blockEvent) {
        final ResourceLocation rl = blockEvent.getState().getBlock().getRegistryName();

        final int meta = blockEvent.getState().getBlock().getMetaFromState(blockEvent.getState());

        if (rl == null) {
            return null;
        }

        final String blockid = "<" + rl.getResourceDomain()
                + ":" + rl.getResourcePath() +
                ":" + meta + ">";

        final BlockChangeType blockChangeType;
        final String playername;
        if (blockEvent instanceof BlockEvent.BreakEvent) {
            blockChangeType = BlockChangeType.REMOVE;
            playername = ((BlockEvent.BreakEvent) blockEvent).getPlayer().getUniqueID().toString();
        } else if (blockEvent instanceof BlockEvent.PlaceEvent) {
            blockChangeType = BlockChangeType.INSERT;
            playername = ((BlockEvent.PlaceEvent) blockEvent).getPlayer().getUniqueID().toString();

        } else {
            return null;
        }

        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return new BlockChangeEventModel(blockEvent.getPos(), blockid, playername, timestamp, blockChangeType);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosZ() {
        return posZ;
    }

    public BlockPos getBlockPos() {
        return new BlockPos(posX, posY, posZ);
    }

    public ASCIString getNameblock() {
        return nameblock;
    }

    public ASCIString getPlayernick() {
        return playernick;
    }

    public BlockChangeType getBlockChangeType() {
        return blockChangeType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "BlockChangeEventModel{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", posZ=" + posZ +
                ", nameblock=" + nameblock +
                ", playernick=" + playernick +
                ", blockChangeType=" + blockChangeType +
                ", timestamp=" + timestamp +
                '}';
    }
}
