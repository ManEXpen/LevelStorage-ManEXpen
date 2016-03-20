package manexpen.levelstorage;

import java.lang.reflect.Field;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import manexpen.levelstorage.api.IHasRecipe;
import manexpen.levelstorage.armor.antimatter.ItemArmorAntimatterBase;
import manexpen.levelstorage.entity.EntityElectromagneticBombs;
import manexpen.levelstorage.render.EntityElectromagneticBombsModel;
import manexpen.levelstorage.render.RenderElectromagneticBombs;
import manexpen.levelstorage.util.ResourceParameter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class Proxy {
	public static CreativeTabs CreativeTab = new LSCreativeTab();

	public static final ResourceLocation TESLA_RAY_1 = ResourceParameter.getResourceLocation("misc/tesla.png");
	public static final ResourceLocation TESLA_RAY_2 = ResourceParameter.getResourceLocation("misc/tesla.png");

	public static void init(LevelStorage instance) {
		ItemArmorAntimatterBase.RENDER_ID = RenderingRegistry.addNewArmourRendererPrefix("antimatter");

		EntityRegistry.registerModEntity(EntityElectromagneticBombs.class, "EntityElectromagneticBombs", 512, instance,
				128, 5, true);
		RenderingRegistry.registerEntityRenderingHandler(EntityElectromagneticBombs.class,
				new RenderElectromagneticBombs(new EntityElectromagneticBombsModel()));

		LSKeyboard.KeyRegister();
		SomeItemRegistry();
		LanguageRegister();
		eventRegister();
	}

	public static void recipeRegister(Object obj) {
		if (obj instanceof IHasRecipe)
			((IHasRecipe) obj).addCraftingRecipe();
	}

	public static void GameRegister(Object obj, String itemName) {
		Item currItem = (Item) obj;
		GameRegistry.registerItem(currItem, itemName);
	}

	public static void SomeItemRegistry() {
		Field[] items = ItemList.class.getDeclaredFields();
		Object obj = null;
		for (Field f : items) {
			try {
				obj = f.get(null);
				GameRegister(obj, f.getName());
				recipeRegister(obj);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static void LanguageRegister() {
		LanguageRegistry.addName(ItemList.AntimatterHelmet, "AntimatterSuit Helmet");
		LanguageRegistry.addName(ItemList.AntimatterChestPlate, "Antimatter ChestPlate");
		LanguageRegistry.addName(ItemList.AntimatterLeggings, "AntimatterSuit Leggings");
		LanguageRegistry.addName(ItemList.AntimatterBoots, "AntimatterSuit Boots");
		LanguageRegistry.addName(ItemList.AntimatterHelmetPlus, "Antimatter Helmet Plus");
		LanguageRegistry.instance().addStringLocalization("itemGroup.LevelStorage", "LevelStorage");
		LanguageRegistry.addName(ItemList.AntimatterIPlate, "AntimatterIridiumPlate");
		LanguageRegistry.addName(ItemList.AntimatterGlob, "AntimatterGlob");
		LanguageRegistry.addName(ItemList.EEMatterOrb, "EEMatterOrb");
		LanguageRegistry.addName(ItemList.AntimatterTinyPile, "Antimatter Iridium Reinforced Plate");
		LanguageRegistry.addName(ItemList.TeslaHelmet, "Tesla Helmet");
		LanguageRegistry.addName(ItemList.EnergeticChestplate, "Energetic Chestplate");
		LanguageRegistry.addName(ItemList.LevitationBoots, "Supersonic Leggings");
		LanguageRegistry.addName(ItemList.LevitationBoots, "Levitation Boots");
		LanguageRegistry.addName(ItemList.SuperConductorParts, "Super Conductor Parts");
	}

	public static void eventRegister() {
	}

}
