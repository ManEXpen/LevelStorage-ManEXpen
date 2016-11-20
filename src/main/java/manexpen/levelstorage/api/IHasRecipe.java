package manexpen.levelstorage.api;

import VRGenerator.VisibleRayGenerator;
import ic2.api.item.IC2Items;
import net.minecraft.item.ItemStack;

/**
 * If an item/block gets loaded without this interface implemented, it is marked
 * ItemAntimatterBase a item/block that has no recipe. Otherwise, {@link #addCraftingRecipe()}
 * is invoked when needed.
 *
 * @author mak326428
 */
public interface IHasRecipe {

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

    /**
     * Invoked by {@link ModUniversalInitializer} when an item is past its
     * initialization state and recipe is to be added.
     */
    void addCraftingRecipe();
}
