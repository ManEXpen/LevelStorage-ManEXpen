package manexpen.levelstorage.util.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper {

	private static String REPELMODE = "REPELMODE";

	public static void NBTWriteToBoots(ItemStack itemstack, boolean isEnable) {
		NBTTagCompound nbt = itemstack.getTagCompound();

		if (nbt == null) {
			nbt = new NBTTagCompound();
			itemstack.setTagCompound(nbt);
		}

		nbt.setBoolean(REPELMODE, isEnable);
	}

	public static boolean NBTReadFromBootsIsRepelEnable(ItemStack itemstack){
		NBTTagCompound nbt = itemstack.getTagCompound();
		return nbt == null ? false : nbt.getBoolean(REPELMODE);
	}

}
