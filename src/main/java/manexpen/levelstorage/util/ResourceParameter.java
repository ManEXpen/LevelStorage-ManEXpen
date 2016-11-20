package manexpen.levelstorage.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import manexpen.levelstorage.LevelStorage;
import net.minecraft.util.ResourceLocation;

public class ResourceParameter {

    public static final String ANTIMATTER_HELMET_TEXTURE = getTexturePathFor("itemArmorAntimatterHelmet");
    public static final String ANTIMATTER_CHESTPLATE_TEXTURE = getTexturePathFor("itemArmorAntimatterChestplate");
    public static final String ANTIMATTER_LEGGINGS_TEXTURE = getTexturePathFor("itemArmorAntimatterLeggings");
    public static final String ANTIMATTER_BOOTS_TEXTURE = getTexturePathFor("itemArmorAntimatterBoots");


    @SideOnly(Side.CLIENT)
    public static String getTexturePathFor(String name) {
        return LevelStorage.MODNAME.toLowerCase() + ":" + name;
    }

    @SideOnly(Side.CLIENT)
    public static ResourceLocation getResourceLocation(String path) {
        return new ResourceLocation(LevelStorage.MODNAME.toLowerCase(), path);
    }
}
