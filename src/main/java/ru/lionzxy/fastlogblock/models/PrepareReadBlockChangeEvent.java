package ru.lionzxy.fastlogblock.models;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TLongObjectMap;
import net.minecraft.util.math.BlockPos;
import ru.lionzxy.fastlogblock.io.log.BlockChangeType;

import java.sql.Timestamp;

public class PrepareReadBlockChangeEvent {
    private final int posX;
    private final int posY;
    private final int posZ;
    private final long blockId;
    private final int playerid;
    private final long timestamp;
    private final BlockChangeType blockChangeType;

    public PrepareReadBlockChangeEvent(final int posX, final int posY, final int posZ,
                                       final long blockid,
                                       final int playerid,
                                       final long timestamp,
                                       final BlockChangeType blockChangeType) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.blockId = blockid;
        this.playerid = playerid;
        this.timestamp = timestamp;
        this.blockChangeType = blockChangeType;
    }

    public long getBlockId() {
        return blockId;
    }

    public int getPlayerid() {
        return playerid;
    }

    public BlockChangeEventModel toBlockChangeEventModel(final TIntObjectMap<ASCIString> idToNick, final TLongObjectMap<ASCIString> idToBlock) {
        return new BlockChangeEventModel(new BlockPos(posX, posY, posZ),
                idToBlock.get(blockId).toString(),
                idToNick.get(playerid).toString(),
                new Timestamp(timestamp),
                blockChangeType);

    }
}
