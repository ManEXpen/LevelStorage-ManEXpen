package manexpen.levelstorage.render;

import manexpen.levelstorage.entity.EntityElectromagneticBombs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CallMWRay {
    public static void shotMW(World world, EntityPlayer player) {
        EntityElectromagneticBombs EEMB = new EntityElectromagneticBombs(world, player, 4F, 1.0F, 0.0F, 0.0F, 0.0F);
        if (!world.isRemote) {
            world.spawnEntityInWorld(EEMB);
        }
    }
}
