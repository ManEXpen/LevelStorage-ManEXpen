package manexpen.levelstorage.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityPlayerHelper {
    public static void repelEntityInRange(World w, EntityPlayer p, float range) {
        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(p.posX - range, p.posY - range, p.posZ - range,
                p.posX + range, p.posY + range, p.posZ + range);
        WorldHelper.repelEntitiesInAABBFromPoint(w, box, p.posX, p.posY, p.posZ, true);
    }
}
