package manexpen.levelstorage.util;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.util.ResourceLocation;

public class RenderHelper {
    public static void bindTexture(ResourceLocation resLoc) {
        FMLClientHandler.instance().getClient().renderEngine
                .bindTexture(resLoc);
    }

}
