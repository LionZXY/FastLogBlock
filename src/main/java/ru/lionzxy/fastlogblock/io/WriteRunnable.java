package ru.lionzxy.fastlogblock.io;

import ru.lionzxy.fastlogblock.FastLogBlock;
import ru.lionzxy.fastlogblock.config.LogConfig;
import ru.lionzxy.fastlogblock.io.mappers.NickMapper;

import java.io.File;
import java.io.IOException;

public class WriteRunnable implements Runnable {

    public WriteRunnable() throws IOException {
        NickMapper nickMapper = new NickMapper(new File(FastLogBlock.fastLogBlock.logFolderFile,
                LogConfig.nickToIntFilePath));
        //nickMapper.putNewUser(new ASCIString("LionZXY"));
        nickMapper.sync();
    }

    @Override
    public void run() {

    }
}
