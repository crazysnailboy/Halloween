package net.crazysnailboy.mods.halloween.client.renderer.entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import net.crazysnailboy.mods.halloween.client.model.ModelCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCurse;
import net.crazysnailboy.mods.halloween.entity.effect.EntityCurse.EnumCurseType;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RenderCurse extends Render<EntityCurse>
{

	private static final ResourceLocation CREEPER_TEXTURES = new ResourceLocation("textures/entity/creeper/creeper.png");
	private static final ResourceLocation GHAST_TEXTURES = new ResourceLocation("textures/entity/ghast/ghast.png");
	private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");
	private static final ResourceLocation SLIME_TEXTURES = new ResourceLocation("textures/entity/slime/slime.png");
	private static final ResourceLocation SPIDER_TEXTURES = new ResourceLocation("textures/entity/spider/spider.png");
	private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");

	private ModelBase mainModel;
    private final ModelBase defaultCurseModel;
    private final ModelBase ghastCurseModel;
    private final ModelBase spiderCurseModel;
    private final ModelBase zombieCurseModel;


	public RenderCurse(RenderManager renderManager)
	{
		super(renderManager);
		this.ghastCurseModel = new ModelCurse(EnumCurseType.GHAST);
		this.spiderCurseModel = new ModelCurse(EnumCurseType.SPIDER);
		this.zombieCurseModel = new ModelCurse(EnumCurseType.ZOMBIE);
		this.defaultCurseModel = new ModelCurse();
	}


	@Override
	public void doRender(EntityCurse entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GL11.glPushMatrix();
		GL11.glDisable(2884 /*GL_CULL_FACE*/);

		try
		{
			float rotation = entity.getRotation();
			float rotationYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;

			float ageInTicks = (float)(entity.ticksExisted + partialTicks);
			float netHeadYaw = rotationYaw - rotation;
			float headPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
			float scale = 0.0625F;

			GL11.glTranslatef((float)x, (float)y, (float)z);

			for (int i = 0; i < 3; i++)
			{
				GL11.glPushMatrix();

				GL11.glRotatef(180.0F - rotation, 0.0F, rotation, 0.0F);
				rotation += 120.0F;

				GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
				GL11.glScalef(-1.0F, -1.0F, 1.0F);
				this.preRenderCallback(entity, partialTicks);
				GL11.glTranslatef(0.0F, -24.0F * scale - 0.0078125F, 0.0F);

				this.bindEntityTexture(entity);
				GL11.glScalef(1.01F, 1.01F, 1.01F);
				//GL11.glDisable(2896 /*GL_LIGHTING*/);
				GL11.glEnable(3042 /*GL_BLEND*/);
				GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
				GL11.glBlendFunc(1, 1);

				this.switchModel(entity);
				this.mainModel.render(entity, 0.0F, 0.0F, ageInTicks, netHeadYaw, headPitch, scale);

				GL11.glPopMatrix();
			}
			GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
			GL11.glDisable(3042 /*GL_BLEND*/);
			//GL11.glEnable(2896 /*GL_LIGHTING*/);
			GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
			GL11.glDisable(2884 /*GL_CULL_FACE*/);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		GL13.glClientActiveTexture(33985 /*GL_TEXTURE1_ARB*/);
		GL13.glActiveTexture(33985 /*GL_TEXTURE1_ARB*/);
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
		GL13.glClientActiveTexture(33984 /*GL_TEXTURE0_ARB*/);
		GL13.glActiveTexture(33984 /*GL_TEXTURE0_ARB*/);
		GL11.glEnable(2884 /*GL_CULL_FACE*/);
		GL11.glPopMatrix();
	}

	private void preRenderCallback(EntityCurse entity, float partialTickTime)
	{
		float dash = entity.getDash() * 0.75F;
		float lift = entity.getLift() * 0.75F;
		float swell = entity.getSwell() * 0.5F;

		GL11.glTranslatef(0.0F, 0.375F - lift + swell, 0.5F - dash);
		float swollen = 0.375F + (dash * 0.5F) + (lift * 0.625F) + swell;
		GL11.glScalef(swollen, swollen, swollen);

		float coller = 0.8F + (swell * 0.5F);
		GL11.glColor4f(coller, coller, coller, 1.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCurse entity)
	{
		switch (entity.getCurseType())
		{
			case CREEPER: return CREEPER_TEXTURES;
			case GHAST: return GHAST_TEXTURES;
			case SKELETON: return SKELETON_TEXTURES;
			case SLIME: return SLIME_TEXTURES;
			case SPIDER: return SPIDER_TEXTURES;
			case ZOMBIE: return ZOMBIE_TEXTURES;
		}
		return null;
	}

	private void switchModel(EntityCurse entity)
	{
		EnumCurseType curseType = entity.getCurseType();
		switch(curseType)
		{
//			case CREEPER: break;
			case GHAST: this.mainModel = this.ghastCurseModel; break;
//			case SKELETON: break;
//			case SLIME: break;
			case SPIDER: this.mainModel = this.spiderCurseModel; break;
			case ZOMBIE: this.mainModel = this.zombieCurseModel; break;
			default: this.mainModel = this.defaultCurseModel;
		}
	}

}