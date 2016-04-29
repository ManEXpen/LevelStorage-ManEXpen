package manexpen.levelstorage.items;

import cpw.mods.fml.common.registry.GameRegistry;
import manexpen.levelstorage.ItemList;
import manexpen.levelstorage.Proxy;
import manexpen.levelstorage.api.IHasRecipe;
import manexpen.levelstorage.util.ResourceParameter;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SimpleItems extends Item implements IHasRecipe {
	private EnumItems kind;

	public SimpleItems(EnumItems kind) {
		this.kind = kind;
		switch (kind) {
		case AntimatterIPlate:
			setTextureName(ResourceParameter.getTexturePathFor("plateAntimatterIridium"));
			setUnlocalizedName("AntimatterIridiumPlate");
			break;
		case AntimatterGlob:
			setTextureName(ResourceParameter.getTexturePathFor("itemAntimatterGlob"));
			setUnlocalizedName("AntimatterGlob");
			break;
		case AntimatterTinyPile:
			setTextureName(ResourceParameter.getTexturePathFor("itemAntimatterTinyPile"));
			setUnlocalizedName("AntimatterTinyPile");
			break;
		case EnergeticChestplate:
			setTextureName(ResourceParameter.getTexturePathFor("itemArmorForcefieldChestplate"));
			setUnlocalizedName("ArmorForcefieldChestplate");
			break;
		case LevitationBoots:
			setTextureName(ResourceParameter.getTexturePathFor("itemArmorLevitationBoots"));
			setUnlocalizedName("ArmorLevitationBoots");
			break;
		case SupersonicLeggings:
			setTextureName(ResourceParameter.getTexturePathFor("itemArmorSupersonicLeggings"));
			setUnlocalizedName("ArmorSupersonicLeggings");
			break;
		case TeslaHelmet:
			setTextureName(ResourceParameter.getTexturePathFor("itemArmorTeslaHelmet"));
			setUnlocalizedName("ArmorTeslaHelmet");
			break;
		case SuperCondctorParts:
			setTextureName(ResourceParameter.getTexturePathFor("itemSuperconductor"));
			setUnlocalizedName("SuperConductorParts");
			break;
		}
		setCreativeTab(Proxy.CreativeTab);
	}

	@Override
	public void addCraftingRecipe() {
		switch (this.kind) {
		case AntimatterGlob:
			GameRegistry.addRecipe(new ItemStack(ItemList.AntimatterGlob), "XXX", "XXX", "XXX", 'X',
					ItemList.AntimatterTinyPile);
			break;
		case AntimatterIPlate:
			GameRegistry.addShapelessRecipe(new ItemStack(ItemList.AntimatterIPlate), new Object[]{IridiumPlate, ItemList.AntimatterGlob, ItemList.AntimatterGlob});
			break;
		case AntimatterTinyPile:
			GameRegistry.addRecipe(new ItemStack(ItemList.AntimatterTinyPile), "XX", "XX", 'X', uuMatterCell);
			break;
		case EnergeticChestplate:
			GameRegistry.addRecipe(new ItemStack(ItemList.EnergeticChestplate), "XXX", "YZY", "AYA", 'X', teslaCoil,
					'Y', IridiumPlate, 'Z', quantumBodyarmor, 'A', ItemList.EEMatterOrb);
			break;
		case LevitationBoots:
			GameRegistry.addRecipe(new ItemStack(ItemList.LevitationBoots), "XXX", "XYX", "ZXZ", 'X', IridiumPlate, 'Y',
					quantumBoots, 'Z', ItemList.EEMatterOrb);
			break;
		case SuperCondctorParts:
			GameRegistry.addRecipe(new ItemStack(ItemList.SuperConductorParts), "XXX", "YZY", "XXX", 'X',
					GlassFiberCable, 'Y', IridiumPlate, 'Z', advancedMachine);
			break;
		case SupersonicLeggings:
			GameRegistry.addRecipe(new ItemStack(ItemList.SupersonicLeggings), "XXX", "YZY", "AYA", 'X',
					Blocks.glowstone, 'Y', IridiumPlate, 'Z', quantumLeggings, 'A', ItemList.EEMatterOrb);
			break;
		case TeslaHelmet:
			GameRegistry.addRecipe(new ItemStack(ItemList.TeslaHelmet), "XYX", "YZY", "AYA", 'X', teslaCoil, 'Y',
					IridiumPlate, 'Z', quantumHelmet, 'A', ItemList.EEMatterOrb);
			break;
		}

	}
}
