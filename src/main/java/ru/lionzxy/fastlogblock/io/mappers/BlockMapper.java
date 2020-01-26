package ru.lionzxy.fastlogblock.io.mappers;

import com.google.common.annotations.VisibleForTesting;
import gnu.trove.TObjectLongHashMap;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.set.TLongSet;
import ru.lionzxy.fastlogblock.io.base.IterrateByteFile;
import ru.lionzxy.fastlogblock.models.ASCIString;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static ru.lionzxy.fastlogblock.utils.Constants.DEVIDER_SYMBOL;


/**
 * id : blockname
 */
public class BlockMapper extends IterrateByteFile {
    private final AtomicLong maxId = new AtomicLong(Long.MIN_VALUE);
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final TObjectLongHashMap blockToId = new TObjectLongHashMap();

    @VisibleForTesting
    public BlockMapper() {
        super(null);
    }

    public BlockMapper(final File mapFile) throws IOException {
        super(mapFile);
        this.iterateByFile(this::putFromByte);
    }

    @Override
    protected void writeToFile(final OutputStream outputStream) throws IOException {
        blockToId.forEachEntry((o, i) -> {
            final ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES + 1 + ((ASCIString) o).getShortString().length);
            byteBuffer.putLong(i);
            byteBuffer.put(((ASCIString) o).getShortString());
            byteBuffer.put(DEVIDER_SYMBOL);
            try {
                outputStream.write(byteBuffer.array());
            } catch (final IOException e) {
                e.printStackTrace();
            }
            return true;
        });
        outputStream.flush();
    }

    private void putFromByte(final TByteArrayList byteArrayList) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayList.toArray());
        final long blockid = byteBuffer.getLong();
        int nameSize = byteArrayList.size() - Long.BYTES - 1;

        if (nameSize <= 0) {
            return;
        }

        final byte[] tmpbuffer = new byte[nameSize];
        byteBuffer.get(tmpbuffer);
        final ASCIString asciString = new ASCIString(tmpbuffer);
        blockToId.put(asciString, blockid);
        if (blockid > maxId.get()) {
            maxId.set(blockid);
        }
    }

    public long getOrPutBlock(final ASCIString newBlock) {
        readWriteLock.readLock().lock();
        try {
            final long blockid = blockToId.get(newBlock);
            if (blockid != 0) {
                return blockid;
            }
        } finally {
            readWriteLock.readLock().unlock();
        }

        return putBlock(newBlock);
    }

    private long putBlock(final ASCIString newBlock) {
        readWriteLock.writeLock().lock();
        try {
            long blockid = blockToId.get(newBlock);
            if (blockid != 0) {
                return blockid;
            }

            blockid = maxId.incrementAndGet();
            if (blockid == 0) {
                blockid++;
                maxId.getAndIncrement();
            }
            blockToId.put(newBlock, blockid);
            markDirty.set(true);
            return blockid;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public TLongObjectMap<ASCIString> idsToNames(final TLongSet longSet) {
        final TLongObjectMap<ASCIString> toReturn = new TLongObjectHashMap<>();

        try {
            readWriteLock.readLock().lock();
            blockToId.forEachEntry((blockname, id) -> {
                if (!longSet.contains(id)) {
                    return true;
                }
                toReturn.put(id, (ASCIString) blockname);
                return true;
            });
        } finally {
            readWriteLock.readLock().unlock();
        }

        return toReturn;
    }

    @VisibleForTesting
    public int size() {
        try {
            readWriteLock.readLock().lock();
            return blockToId.size();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    protected boolean checkLineEnd(final TByteArrayList arrayList, final byte endByte) {
        return endByte == DEVIDER_SYMBOL;
    }
}
