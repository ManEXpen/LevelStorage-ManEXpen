package manexpen.levelstorage.util;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;

public class CommonHelper {
	public static void spawnLightning(World w, int x, int y, int z, boolean exp) {
		EntityLightningBolt lightning = new EntityLightningBolt(w, x, y, z);
		w.addWeatherEffect(lightning);
		if (exp) {
			w.createExplosion(lightning, x, y, z,
					(float) (Math.random() * 1.0f), true);
		}
		w.spawnEntityInWorld(lightning);
	}

	public static int getDistance(double startX, double startY,
			double startZ, double endX, double endY, double endZ) {
		double x = endX - startX;
		double y = endY - startY;
		double z = endZ - startZ;
		return (int)Math.round(Math.sqrt(x * x + y * y + z * z));
	}

}
