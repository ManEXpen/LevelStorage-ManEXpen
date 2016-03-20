package manexpen.levelstorage.armor.antimatter;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import manexpen.levelstorage.ItemList;
import manexpen.levelstorage.api.IHasRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenDesert;

public class ItemArmorAntimatterHelmet extends ItemArmorAntimatterBase implements IHasRecipe{

	public ItemArmorAntimatterHelmet() {
		super(ItemArmorAntimatterBase.HELMET);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player,
			ItemStack itemStack) {
		super.onArmorTick(world, player, itemStack);

		int toCharge = 0;

		if (isSunVisible(player.worldObj, (int) player.posX,
				(int) player.posY + 1, (int) player.posZ)) {
			toCharge += 2048;
		}

		for (ItemStack is : player.inventory.armorInventory) {
			if (is == null)
				continue;
			if (is.getItem() instanceof IElectricItem) {
				toCharge -= ElectricItem.manager.charge(is, toCharge, 4, true, false);
				if (toCharge == 0)
					break;
			}
		}
		for (ItemStack is : player.inventory.mainInventory) {
			if (is == null)
				continue;
			if (is.getItem() instanceof IElectricItem) {
				toCharge -= ElectricItem.manager.charge(is, toCharge, 4, true, false);
				if (toCharge == 0)
					break;
			}
		}
	}

	public static boolean isSunVisible(World world, int x, int y, int z) {
		return (world.isDaytime())
				&& (!world.provider.hasNoSky)
				&& (world.canBlockSeeTheSky(x, y, z))
				&& (((world.getWorldChunkManager().getBiomeGenAt(x, z) instanceof BiomeGenDesert)) || ((!world
						.isRaining()) && (!world.isThundering())));
	}

	@Override
	public void addCraftingRecipe() {
		GameRegistry.addRecipe(new ItemStack(ItemList.AntimatterHelmet),
				"XYX",
				"XZX",
				"XAX",
				'X', ItemList.AntimatterIPlate,
				'Y', sorlar20,
				'Z', ItemList.AntimatterHelmet,
				'A', ItemList.EEMatterOrb);
	}

}
