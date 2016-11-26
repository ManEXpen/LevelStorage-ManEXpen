package manexpen.levelstorage.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
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
        return (int) Math.round(Math.sqrt(x * x + y * y + z * z));
    }

    public static MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityLivingBase entity, boolean scanFluids, int reach) {
        final float var4 = 1.0F;
        float var5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * var4;
        float var6 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * var4;
        double var7 = entity.prevPosX + (entity.posX - entity.prevPosX) * var4;
        double var9 = entity.prevPosY + (entity.posY - entity.prevPosY) * var4 + 1.62D - entity.yOffset;
        double var11 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * var4;
        Vec3 var13 = Vec3.createVectorHelper(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * 0.017453292F - (float) Math.PI);
        float var15 = MathHelper.sin(-var6 * 0.017453292F - (float) Math.PI);
        float var16 = -MathHelper.cos(-var5 * 0.017453292F);
        float var17 = MathHelper.sin(-var5 * 0.017453292F);
        float var18 = var15 * var16;
        float var20 = var14 * var16;

        Vec3 var23 = var13.addVector(var18 * reach, var17 * reach, var20 * reach);
        return world.rayTraceBlocks(var13, var23, scanFluids);
    }

}
