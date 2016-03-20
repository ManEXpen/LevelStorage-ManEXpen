package manexpen.levelstorage.util;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class WorldHelper {
	public static void repelEntitiesInAABBFromPoint(World world, AxisAlignedBB effectBounds, double x, double y,
			double z, boolean isSWRG) {
		List<Entity> list = world.getEntitiesWithinAABB(Entity.class, effectBounds);

		for (Entity ent : list) {

			if (ent instanceof EntityArrow && ((EntityArrow) ent).onGround) {
				continue;
			}
			Vec3 p = Vec3.createVectorHelper(x, y, z);
			Vec3 t = Vec3.createVectorHelper(ent.posX, ent.posY, ent.posZ);
			double distance = p.distanceTo(t) + 0.1D;

			Vec3 r = Vec3.createVectorHelper(t.xCoord - p.xCoord, t.yCoord - p.yCoord, t.zCoord - p.zCoord);

			ent.motionX += r.xCoord / 1.5D / distance;
			ent.motionY += r.yCoord / 1.5D / distance;
			ent.motionZ += r.zCoord / 1.5D / distance;
		}
	}
}
