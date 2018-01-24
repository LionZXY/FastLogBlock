package ru.lionzxy.fastlogblock.io.log;

public enum BlockChangeType {
    INSERT(0),
    REMOVE(1),
    UPDATE(2),
    UNKNOWN(100);
    private final byte typeId;

    BlockChangeType(final int typeid) {
        this.typeId = (byte) typeid;
    }

    public byte getTypeId() {
        return typeId;
    }

    public static BlockChangeType valueOf(final byte typeId) {
        for (final BlockChangeType blockChangeType : BlockChangeType.values()) {
            if (blockChangeType.getTypeId() == typeId) {
                return blockChangeType;
            }
        }
        return null;
    }
}
