package manexpen.levelstorage.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import manexpen.levelstorage.entity.EntityElectromagneticBombs;
import manexpen.levelstorage.util.ResourceParameter;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderElectromagneticBombs extends Render{

	private static final ResourceLocation bulletTextures = ResourceParameter.getResourceLocation("model/forcefield_plasma.png");
	protected ModelBase modelBullet;
	private static float lastLightMapX,lastLightMapY;

	public RenderElectromagneticBombs(ModelBase mb) {
		super();
		this.modelBullet = mb;
		this.shadowSize = 0.0F;
	}

	public void renderArrow(EntityElectromagneticBombs entity, double x, double y, double z, float par8, float par9)
    {
	    this.bindEntityTexture(entity);
        GL11.glEnable(GL11.GL_BLEND);
        disableLightmap();
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9 - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        byte b0 = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (float)(b0 * 10) / 32.0F;
        float f5 = (float)(5 + b0 * 10) / 32.0F;
        float f6 = 0.0F;
        float f7 = 0.15625F;
        float f8 = (float)(5 + b0 * 10) / 32.0F;
        float f9 = (float)(10 + b0 * 10) / 32.0F;
        float f10 = 0.05625F;
        float renderSize = entity.getRenderSize();
        double length = 6 * Vec3.createVectorHelper(entity.motionX,entity.motionY,entity.motionZ).lengthVector() + 10;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(f10, f10, f10);
        GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(f10, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(1, -renderSize, -renderSize, (double)f6, (double)f8);
        tessellator.addVertexWithUV(1, -renderSize, renderSize, (double)f7, (double)f8);
        tessellator.addVertexWithUV(1, renderSize, renderSize, (double)f7, (double)f9);
        tessellator.addVertexWithUV(1, renderSize, -renderSize, (double) f6, (double) f9);
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(length-1, -renderSize, renderSize, (double)f7, (double)f8);
        tessellator.addVertexWithUV(length-1, renderSize, renderSize, (double)f7, (double)f9);
        tessellator.addVertexWithUV(length-1, renderSize, -renderSize, (double) f6, (double) f9);
        tessellator.addVertexWithUV(length-1, -renderSize, -renderSize, (double)f6, (double)f8);
        tessellator.draw();

        for (int i = 0; i < 2; ++i)
        {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f10);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(0, -renderSize, 0.0D, (double) f2, (double) f4);
            tessellator.addVertexWithUV(length, -renderSize, 0.0D, (double) f3, (double) f4);
            tessellator.addVertexWithUV(length, renderSize, 0.0D, (double) f3, (double) f5);
            tessellator.addVertexWithUV(0, renderSize, 0.0D, (double) f2, (double) f5);
            tessellator.draw();
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        enableLightmap();
    }

	protected ResourceLocation getArrowTextures(EntityElectromagneticBombs par1EntityArrow)
    {
        return bulletTextures;
    }

    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.getArrowTextures((EntityElectromagneticBombs)par1Entity);
    }

    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderArrow((EntityElectromagneticBombs)par1Entity, par2, par4, par6, par8, par9);
    }

    public static void disableLightmap()
	{
		lastLightMapX = OpenGlHelper.lastBrightnessX;
		lastLightMapY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
	}

    public static void enableLightmap()
	{
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastLightMapX, lastLightMapY);
	}
}
