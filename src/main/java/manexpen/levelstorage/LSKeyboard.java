package manexpen.levelstorage;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import manexpen.levelstorage.util.EnumKey;
import net.minecraft.client.settings.KeyBinding;

public class LSKeyboard {
	private static LSKeyboard instance = new LSKeyboard();

	private static KeyBinding BEAM_SHOOT_KEY = new KeyBinding("BEAM  SHOOT", Keyboard.KEY_L, "LevelStorage");
	private static KeyBinding CHANGE_REPEL_MODE = new KeyBinding("Change Repel Mode", Keyboard.KEY_K, "LevelStorage");

	private LSKeyboard() {
	}

	public void KeyRegister() {
		ClientRegistry.registerKeyBinding(BEAM_SHOOT_KEY);
		ClientRegistry.registerKeyBinding(CHANGE_REPEL_MODE);
	}

	public static LSKeyboard getInstance() {
		return instance;
	}

	public boolean getIsKeyPressed(EnumKey key) {
		switch (key) {
		case BEAMSHOOT:
			return BEAM_SHOOT_KEY.getIsKeyPressed();
		case CHANGE_REPEL_MODE:
			return CHANGE_REPEL_MODE.getIsKeyPressed();
		default:
			return false;
		}
	}
}
