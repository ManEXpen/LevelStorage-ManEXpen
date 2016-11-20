package manexpen.levelstorage;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import manexpen.levelstorage.api.IKeyHandler;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class LSKeyboard {

    public static KeyBinding BEAM_SHOOT_KEY = new KeyBinding("BEAM  SHOOT", Keyboard.KEY_L, "LevelStorage");
    public static KeyBinding CHANGE_REPEL_MODE = new KeyBinding("Change Repel Mode", Keyboard.KEY_K, "LevelStorage");
    public static KeyBinding TELEPORT = new KeyBinding("Do Teleport", Keyboard.KEY_T, "LevelStorage");

    private static ArrayList<IKeyHandler> keyHandlers = new ArrayList<>();

    private LSKeyboard() {
    }

    static void KeyRegister() {
        ClientRegistry.registerKeyBinding(BEAM_SHOOT_KEY);
        ClientRegistry.registerKeyBinding(CHANGE_REPEL_MODE);
        ClientRegistry.registerKeyBinding(TELEPORT);
        FMLCommonHandler.instance().bus().register(new LSKeyboard());
    }

    public static void registerKeyHandler(IKeyHandler keyHandler) {
        keyHandlers.add(keyHandler);
    }

    @SubscribeEvent
    public void KeyHandlingEvent(InputEvent.KeyInputEvent event) {
        keyHandlers.forEach(IKeyHandler::onPressedKey);
    }

}
