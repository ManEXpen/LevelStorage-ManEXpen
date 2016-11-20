package manexpen.levelstorage.packet;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import manexpen.levelstorage.LevelStorage;
import packet.KeyPressedHandler;
import packet.MessageKeyPressed;

/**
 * Created by manex on 2016/11/16.
 */
public class LSPacketHandler {
    private static int id = 0;
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LevelStorage.MODNAME);

    public static void init() {
        INSTANCE.registerMessage(KeyPressedHandler.class, MessageKeyPressed.class, id++, Side.SERVER);
    }
}
