package ru.lionzxy.fastlogblock.io.log;

import ru.lionzxy.fastlogblock.io.mappers.BlockMapper;
import ru.lionzxy.fastlogblock.io.mappers.NickMapper;
import ru.lionzxy.fastlogblock.models.BlockChangeEventModel;
import ru.lionzxy.fastlogblock.utils.Constants;
import ru.lionzxy.fastlogblock.utils.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class LogWritter {
    private final BlockMapper blockMapper;
    private final NickMapper nickMapper;
    private final File file;
    private BufferedOutputStream os;

    public LogWritter(final File file, final BlockMapper blockMapper, final NickMapper nickMapper) throws IOException {
        this.file = file;
        this.blockMapper = blockMapper;
        this.nickMapper = nickMapper;

        FileUtils.createFileIfNotExist(file);

        os = new BufferedOutputStream(new FileOutputStream(file, true));
    }

    /**
     * Name	posX posY posZ typeaction playerid blockid timestamp
     *
     * @param blockChangeEventModel
     */
    public void putEvent(final BlockChangeEventModel blockChangeEventModel) {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(Constants.SIZE_LOGLINE);
        byteBuffer.putInt(blockChangeEventModel.getPosX());
        byteBuffer.putInt(blockChangeEventModel.getPosY());
        byteBuffer.putInt(blockChangeEventModel.getPosZ());
        byteBuffer.put(blockChangeEventModel.getBlockChangeType().getTypeId());
        byteBuffer.putInt(nickMapper.getOrPutUser(blockChangeEventModel.getPlayernick()));
        byteBuffer.putLong(blockMapper.getOrPutBlock(blockChangeEventModel.getNameblock()));
        byteBuffer.putLong(blockChangeEventModel.getTimestamp().getTime());
        byteBuffer.put(Constants.DEVIDER_SYMBOL);

        try {
            os.write(byteBuffer.array());
        } catch (final IOException e) {
            try {
                sync();
                os.write(byteBuffer.array());
            } catch (final IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void sync() throws IOException {
        FileUtils.createFileIfNotExist(file);

        try {
            os.flush();
        } catch (final IOException e) {
            os = new BufferedOutputStream(new FileOutputStream(file, true));
            os.flush();
        }
    }
}
