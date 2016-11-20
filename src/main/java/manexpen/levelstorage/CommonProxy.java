package manexpen.levelstorage;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import manexpen.levelstorage.api.IHasRecipe;
import manexpen.levelstorage.api.IKeyHandler;
import manexpen.levelstorage.entity.EntityElectromagneticBombs;
import manexpen.levelstorage.packet.LSPacketHandler;
import net.minecraft.item.Item;

import java.lang.reflect.Field;

public class CommonProxy {
    public void init(LevelStorage ls) {
        EntityRegistry.registerModEntity(EntityElectromagneticBombs.class, "EntityElectromagneticBombs", EntityRegistry.findGlobalUniqueEntityId(), ls,
                128, 5, true);

        SomeItemRegistry();
        LanguageRegister();

        LSPacketHandler.init();
    }

    public static void recipeRegister(Object obj, String itemName) {
        if (obj instanceof IHasRecipe) {
            LevelStorage.log.info("Register Recipe: " + itemName);
            ((IHasRecipe) obj).addCraftingRecipe();
        }
    }

    public static void GameRegister(Object obj, String itemName) {
        Item currItem = (Item) obj;
        LevelStorage.log.info("Register: " + itemName);
        GameRegistry.registerItem(currItem, itemName);

        if (obj instanceof IKeyHandler) LSKeyboard.registerKeyHandler((IKeyHandler) obj);
    }

    public static void SomeItemRegistry() {
        Field[] items = ItemList.class.getDeclaredFields();
        Object obj;
        for (Field f : items) {
            try {
                obj = f.get(null);
                GameRegister(obj, f.getName());
                recipeRegister(obj, f.getName());
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
}
