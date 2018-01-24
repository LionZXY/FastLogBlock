package ru.lionzxy.fastlogblock.config;


import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.lionzxy.fastlogblock.FastLogBlock;
import ru.lionzxy.fastlogblock.io.filesplitter.FileSplitterEnum;

@Config(modid = FastLogBlock.MODID)
@Config.LangKey("fastlogblock.config.title")
public class LogConfig {
    @Config.Comment("Filepath from minecraft root folder to block log path")
    public static final String logFolderPath = "./blocklog/";

    @Config.Comment("File splitter type. SINGLE for single-file strategy, BLOCKHASH for file=HASH(BlockPos) strategy")
    public static FileSplitterEnum fileSplitterType = FileSplitterEnum.BLOCKHASH;

    @Config.Comment("Utils information for migration")
    public static int logSchemeVersion = 1;

    @Config.Comment("Path to mapper file from logFolderPath")
    public static final String nickToIntFilePath = "nicktoid.prop";

    @Config.Comment("Path to mapper file from logFolderPath")
    public static String blockToIntFilePath = "blocktoid.prop";

    @Config.Comment("Regular expression for block change event ignore")
    public static String[] ignoreBlockNamesRegExp = new String[]{"<minecraft:tallgrass:*>"};

    public static final HashConfig HASH_CONFIG = new HashConfig();

    public static class HashConfig {
        @Config.Comment("Max logfile count")
        public final int fileCount = 16;

        @Config.Comment("Pattern for log filename. %d - file number. Default: part%d.bytelog")
        public final String fileNamePattern = "part%d.bytelog";
    }

    @Mod.EventBusSubscriber(modid = FastLogBlock.MODID)
    private static class EventHandler {

        /**
         * Inject the new values and save to the config file when the config has been changed from the GUI.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(FastLogBlock.MODID)) {
                ConfigManager.sync(FastLogBlock.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
