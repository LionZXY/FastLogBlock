package ru.lionzxy.fastlogblock.io.base;

import gnu.trove.list.array.TByteArrayList;
import net.minecraftforge.fml.common.FMLLog;
import ru.lionzxy.fastlogblock.utils.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public abstract class IterrateByteFile {
    protected final File file;
    protected final AtomicBoolean markDirty = new AtomicBoolean(false);
    protected final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public IterrateByteFile(final File file) {
        this.file = file;
    }

    public void iterateByFile(final Consumer<TByteArrayList> callback) throws IOException {
        FileUtils.createFileIfNotExist(file);

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            readWriteLock.writeLock().lock();
            try {
                iterateByByte(bufferedInputStream, callback);
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }
    }

    private void iterateByByte(final InputStream is, final Consumer<TByteArrayList> callback) throws IOException {
        final byte[] buffer = new byte[FileUtils.BYTEBUFFERSIZE];
        int len;

        final TByteArrayList arrayList = new TByteArrayList();
        while ((len = is.read(buffer)) != -1) {
            for (int i = 0; i < len; i++) {
                arrayList.add(buffer[i]);

                if (checkLineEnd(arrayList, buffer[i])) {
                    callback.accept(arrayList);
                    arrayList.clear(arrayList.size());
                }
            }
        }
    }

    public void sync() throws IOException {
        if (markDirty.compareAndSet(false, false)) {
            return;
        }

        FMLLog.log.debug("Sync nickToId map...");

        Files.move(file.toPath(), new File(file.getParent(), file.getName() + ".backup").toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        FileUtils.createFileIfNotExist(file);

        try (final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            readWriteLock.readLock().lock();
            try {
                writeToFile(bufferedOutputStream);
            } finally {
                readWriteLock.readLock().lock();
            }
        }

        FMLLog.log.debug("Sync finished!");
    }

    protected abstract boolean checkLineEnd(TByteArrayList arrayList, byte endByte);

    protected abstract void writeToFile(OutputStream outputStream) throws IOException;

}
