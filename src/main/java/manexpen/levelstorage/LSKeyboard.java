package manexpen.levelstorage;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import manexpen.levelstorage.util.EnumKey;
import net.minecraft.client.settings.KeyBinding;

public class LSKeyboard {
	private static LSKeyboard instance = new LSKeyboard();

	private static KeyBinding BEAM_SHOOT_KEY = new KeyBinding("BEAM  SHOOT", Keyboard.KEY_L, "LevelStorage");
	private static KeyBinding RAY_SHOOT_KEY = new KeyBinding("RAY SHOOT", Keyboard.KEY_R, "LevelStorage");

	private LSKeyboard() {
	}

	public static void KeyRegister() {
		ClientRegistry.registerKeyBinding(RAY_SHOOT_KEY);
		ClientRegistry.registerKeyBinding(BEAM_SHOOT_KEY);
	}

	public static LSKeyboard getInstance() {
		return instance;
	}

	public boolean getIsKeyPressed(EnumKey key) {
		switch (key) {
		case RAYSHOOT:
			return RAY_SHOOT_KEY.getIsKeyPressed();
		case BEAMSHOOT:
			return BEAM_SHOOT_KEY.getIsKeyPressed();
		default:
			return false;
		}
	}

	/*
	 * おしっぱでもtrue*/
	public boolean getIsPressed(EnumKey key){
		switch (key) {
		case RAYSHOOT:
			return RAY_SHOOT_KEY.isPressed();
		case BEAMSHOOT:
			return BEAM_SHOOT_KEY.getIsKeyPressed();
		default:
			return false;
		}
	}
}
