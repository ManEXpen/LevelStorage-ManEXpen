package manexpen.levelstorage.armor.antimatter;

import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import manexpen.levelstorage.ItemList;
import manexpen.levelstorage.api.IHasRecipe;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class ItemArmorAntimatterLeggings extends ItemArmorAntimatterBase implements IHasRecipe {

	private final static int INDEX = 1;

	public ItemArmorAntimatterLeggings() {
		super(ItemArmorAntimatterBase.LEGGINGS);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onUpdate(LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer)
			return;
		if (!(event.entityLiving instanceof EntityMob))
			return;

		List<EntityPlayer> playerList = event.entityLiving.worldObj.playerEntities;
		for (EntityPlayer ep : playerList) {
			ItemStack armor = ep.inventory.armorInventory[INDEX];
			if (armor == null)
				continue;
			if (!(armor.getItem() instanceof ItemArmorAntimatterLeggings)) {
				if (ep.stepHeight == 1.004F) {
					ep.stepHeight = 0.5F;
					return;
				}
			}
			ep.stepHeight = 1.004F;
		}
	}

	@Override
	public void addCraftingRecipe() {
		GameRegistry.addRecipe(new ItemStack(ItemList.AntimatterLeggings),
				"XYX",
				"ZAZ",
				"ZBZ",
				'X', IridiumPlate,
				'Y', teleporter,
				'Z', ItemList.AntimatterIPlate,
				'A', ItemList.SupersonicLeggings,
				'B', lapotronCrystal);
	}
}
