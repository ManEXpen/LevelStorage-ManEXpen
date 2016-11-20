package manexpen.levelstorage;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import manexpen.levelstorage.util.ArmorFunctions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = LevelStorage.MODNAME, name = LevelStorage.MODNAME, version = LevelStorage.VERSION, dependencies = "required-after:IC2;required-after:VisibleRayGenerator")
/**
 *@author ManEXpen
 * */
public class LevelStorage {
    public static final String MODNAME = "LevelStorage";
    public static final String VERSION = "1.7.10-1.1.1";

    /**
     * ロガー
     */
    public static final Logger log = LogManager.getLogger("LevelStorage");

    @Instance(MODNAME)
    public static LevelStorage instance;

    @SidedProxy(clientSide = "manexpen.levelstorage.ClientProxy", serverSide = "manexpen.levelstorage.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        log.info("Loading is Started");
        proxy.init(instance);
        FMLCommonHandler.instance().bus().register(this);
        log.info("Loading is done!");
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent e) {
        ArmorFunctions.ResetSomeList();
    }

    @EventHandler
    public void serverStop(FMLServerStoppingEvent e) {
        ArmorFunctions.ResetSomeList();
    }

    @SubscribeEvent
    public void keyHandlingEvent(InputEvent.KeyInputEvent event) {

    }

}
