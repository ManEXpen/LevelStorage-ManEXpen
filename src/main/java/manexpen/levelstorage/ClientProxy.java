package manexpen.levelstorage;

import cpw.mods.fml.client.registry.RenderingRegistry;
import manexpen.levelstorage.armor.antimatter.dep.ItemArmorAntimatterBase;
import manexpen.levelstorage.entity.EntityElectromagneticBombs;
import manexpen.levelstorage.render.EntityElectromagneticBombsModel;
import manexpen.levelstorage.render.RenderElectromagneticBombs;
import manexpen.levelstorage.util.ResourceParameter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

public class ClientProxy extends CommonProxy {
    public static CreativeTabs CreativeTab = new LSCreativeTab();

    public static final ResourceLocation TESLA_RAY_1 = ResourceParameter.getResourceLocation("misc/tesla.png");
    public static final ResourceLocation TESLA_RAY_2 = ResourceParameter.getResourceLocation("misc/tesla.png");


    @Override
    public void init(LevelStorage ls) {
        super.init(ls);
        ItemArmorAntimatterBase.RENDER_ID = RenderingRegistry.addNewArmourRendererPrefix("antimatter");
        RenderingRegistry.registerEntityRenderingHandler(EntityElectromagneticBombs.class,
                new RenderElectromagneticBombs(new EntityElectromagneticBombsModel()));

        LSKeyboard.KeyRegister();
        LSKeyboard.registerKeyHandler(ItemList.AntimatterHelmet);
        LSKeyboard.registerKeyHandler(ItemList.AntimatterChestPlate);
        LSKeyboard.registerKeyHandler(ItemList.AntimatterLeggings);
        LSKeyboard.registerKeyHandler(ItemList.AntimatterBoots);
    }
}
