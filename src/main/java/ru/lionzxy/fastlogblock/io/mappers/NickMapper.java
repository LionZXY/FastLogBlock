package ru.lionzxy.fastlogblock.io.mappers;

import com.google.common.annotations.VisibleForTesting;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntHashMap;
import gnu.trove.list.array.TByteArrayList;
import ru.lionzxy.fastlogblock.io.base.IterrateByteFile;
import ru.lionzxy.fastlogblock.models.ASCIString;
import ru.lionzxy.fastlogblock.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.lionzxy.fastlogblock.utils.Constants.DEVIDER_SYMBOL;

public class NickMapper extends IterrateByteFile {
    private final AtomicInteger maxId = new AtomicInteger(Integer.MIN_VALUE);
    private final TObjectIntHashMap uuidToId = new TObjectIntHashMap();
    private final TIntObjectHashMap idToObject = new TIntObjectHashMap();

    @VisibleForTesting
    public NickMapper() {
        super(null);
    }

    public NickMapper(final File mapFile) throws IOException {
        super(mapFile);
        this.iterateByFile(this::putFromByte);
    }

    private void putFromByte(final TByteArrayList byteArrayList) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayList.toArray());
        final int userid = byteBuffer.getInt();
        int nameSize = byteArrayList.size() - Integer.BYTES - 1;

        if (nameSize <= 0) {
            return;
        }

        final byte[] tmpbuffer = new byte[nameSize];
        byteBuffer.get(tmpbuffer);
        final ASCIString asciString = new ASCIString(tmpbuffer);
        uuidToId.put(asciString, userid);
        idToObject.put(userid, asciString);
        if (userid > maxId.get()) {
            maxId.set(userid);
        }
    }

    @Override
    protected void writeToFile(final OutputStream outputStream) throws IOException {
        idToObject.forEachEntry((i, o) -> {
            final ByteBuffer byteBuffer = ByteBuffer.allocate(5 + ((ASCIString) o).getShortString().length);
            byteBuffer.putInt(i);
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


    public int getOrPutUser(final ASCIString newUser) {
        readWriteLock.readLock().lock();
        try {
            final int userid = uuidToId.get(newUser);
            if (userid != 0) {
                return userid;
            }
        } finally {
            readWriteLock.readLock().unlock();
        }

        return putUser(newUser);
    }

    public ASCIString getById(final int id) {
        readWriteLock.readLock().lock();
        try {
            return (ASCIString) idToObject.get(id);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }


    private int putUser(final ASCIString newUser) {
        readWriteLock.writeLock().lock();
        try {
            int userid = uuidToId.get(newUser);
            if (userid != 0) {
                return userid;
            }

            userid = maxId.incrementAndGet();
            if (userid == 0) {
                userid++;
                maxId.getAndIncrement();
            }
            uuidToId.put(newUser, userid);
            idToObject.put(userid, newUser);
            markDirty.set(true);
            return userid;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @VisibleForTesting
    public int size() {
        try {
            readWriteLock.readLock().lock();
            if (uuidToId.size() != idToObject.size()) {
                return -1;
            }
            return uuidToId.size();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    protected boolean checkLineEnd(final TByteArrayList arrayList, final byte endByte) {
        return endByte == Constants.DEVIDER_SYMBOL;
    }
}
