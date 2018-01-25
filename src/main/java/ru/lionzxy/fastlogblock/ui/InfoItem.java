package ru.lionzxy.fastlogblock.ui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import ru.lionzxy.fastlogblock.FastLogBlock;
import ru.lionzxy.fastlogblock.handlers.EventHandlingManager;

import java.util.Objects;

public class InfoItem extends Item {
    private static final String ITEMNAME = "infoitem";
    private final EventHandlingManager eventHandlingManager;

    public InfoItem(EventHandlingManager eventHandlingManager) {
        this.eventHandlingManager = eventHandlingManager;
        setRegistryName(FastLogBlock.MODID, ITEMNAME);
        final ResourceLocation registryName = Objects.requireNonNull(getRegistryName());
        setUnlocalizedName(registryName.toString());
        setCreativeTab(CreativeTabs.MISC);
    }


    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return EnumActionResult.FAIL;
        }
        player.sendMessage(new TextComponentTranslation("message.fastlogblock:blockinfo.start", pos.getX(), pos.getY(), pos.getZ()));
        eventHandlingManager.handleLogByPos(player, pos, worldIn);
        return EnumActionResult.SUCCESS;
    }
}
