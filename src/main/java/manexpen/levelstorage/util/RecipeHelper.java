package manexpen.levelstorage.util;

import VRGenerator.VisibleRayGenerator;
import ic2.api.item.IC2Items;
import net.minecraft.item.ItemStack;

public interface RecipeHelper {
	ItemStack IridiumPlate = IC2Items.getItem("iridiumPlate");
	ItemStack GlassFiberCable = IC2Items.getItem("glassFiberCableItem");
	ItemStack lapotronCrystal = IC2Items.getItem("lapotronCrystal");
	ItemStack teslaCoil = IC2Items.getItem("teslaCoil");
	ItemStack advancedMachine = IC2Items.getItem("advancedMachine");
	ItemStack teleporter = IC2Items.getItem("teleporter");
	ItemStack rubber = IC2Items.getItem("rubber");
	ItemStack uuMatterCell = IC2Items.getItem("uuMatterCell");
	ItemStack quantumHelmet = IC2Items.getItem("quantumHelmet");
	ItemStack quantumBodyarmor = IC2Items.getItem("quantumBodyarmor");
	ItemStack quantumLeggings = IC2Items.getItem("quantumLeggings");
	ItemStack quantumBoots = IC2Items.getItem("quantumBoots");
	ItemStack sorlar20 = new ItemStack(VisibleRayGenerator.solarBlock, 1, 9);
	ItemStack sorlar53 = new ItemStack(VisibleRayGenerator.solarBlock, 1, 13);
}
