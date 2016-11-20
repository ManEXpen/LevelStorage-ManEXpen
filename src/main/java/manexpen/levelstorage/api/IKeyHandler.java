package manexpen.levelstorage.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by manex on 2016/11/17.
 */
public interface IKeyHandler {
    @SideOnly(Side.CLIENT)
    void onPressedKey();

    @SideOnly(Side.SERVER)
    void onRecieveKeyPacket(World world, EntityPlayer player, ItemStack itemStack, EnumKey keyType);
}
