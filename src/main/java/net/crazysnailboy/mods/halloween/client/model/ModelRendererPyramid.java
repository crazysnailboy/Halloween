package net.crazysnailboy.mods.halloween.client.model;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ModelRendererPyramid extends ModelRenderer
{

//	public float textureWidth;
//	public float textureHeight;
	private PositionTextureVertex field_35978_r[];
	private TexturedQuad faces[];
	private int textureOffsetX;
	private int textureOffsetY;
//	public float rotationPointX;
//	public float rotationPointY;
//	public float rotationPointZ;
//	public float rotateAngleX;
//	public float rotateAngleY;
//	public float rotateAngleZ;
//	public float offsetX;
//	public float offsetY;
//	public float offsetZ;
	public float field_35973_l;
	public float field_35974_m;
	public float field_35972_n;
	private boolean compiled;
	private int displayList;
//	public boolean mirror;
//	public boolean showModel;
//	public boolean isHidden;


	public ModelRendererPyramid(ModelBase modelbase, int texOffX, int texOffY)
	{
		super(modelbase, texOffX, texOffY);

//		textureWidth = 64F;
//		textureHeight = 32F;
		this.compiled = false;
		this.displayList = 0;
//		mirror = false;
//		showModel = true;
//		isHidden = false;
		this.textureOffsetX = texOffX;
		this.textureOffsetY = texOffY;
		modelbase.boxList.add(this);
	}


	@Override
	public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth)
	{
		this.addBox(offX, offY, offZ, width, height, depth, 0.0F);
		return this;
	}

	@Override
	public void addBox(float offX, float offY, float offZ, int width, int height, int depth, float scaleFactor)
	{
		this.addBox(offX, offY, offZ, width, height, depth, 0.0F, 0.0F, 0.0F);
	}

	public void addBox(float offX, float offY, float offZ, int width, int height, int depth, float f3, float carl, float sagan)
	{
		this.offsetX = offX;
		this.offsetY = offY;
		this.offsetZ = offZ;
		this.field_35973_l = offX + (float)width;
		this.field_35974_m = offY + (float)height;
		this.field_35972_n = offZ + (float)depth;
		this.field_35978_r = new PositionTextureVertex[8];
		this.faces = new TexturedQuad[6];
		float f4 = offX + (float)width;
		float f5 = offY + (float)height;
		float f6 = offZ + (float)depth;
		offX -= f3;
		offY -= f3;
		offZ -= f3;
		f4 += f3;
		f5 += f3;
		f6 += f3;
		if (this.mirror)
		{
			float f7 = f4;
			f4 = offX;
			offX = f7;
		}

		float robert = carl;
		if (carl == 1F)
		{
			carl -= 0.5F;
			robert += 1F;
		}

		PositionTextureVertex positiontexturevertex = new PositionTextureVertex(offX - carl, offY, offZ - robert, 0.0F, 0.0F);
		PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f4 + carl, offY, offZ - robert, 0.0F, 8F);
		PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(f4 + sagan, f5, offZ - sagan, 8F, 8F);
		PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(offX - sagan, f5, offZ - sagan, 8F, 0.0F);
		PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(offX - carl, offY, f6 + robert, 0.0F, 0.0F);
		PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f4 + carl, offY, f6 + robert, 0.0F, 8F);
		PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(f4 + sagan, f5, f6 + sagan, 8F, 8F);
		PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(offX - sagan, f5, f6 + sagan, 8F, 0.0F);

		this.field_35978_r[0] = positiontexturevertex;
		this.field_35978_r[1] = positiontexturevertex1;
		this.field_35978_r[2] = positiontexturevertex2;
		this.field_35978_r[3] = positiontexturevertex3;
		this.field_35978_r[4] = positiontexturevertex4;
		this.field_35978_r[5] = positiontexturevertex5;
		this.field_35978_r[6] = positiontexturevertex6;
		this.field_35978_r[7] = positiontexturevertex7;

		this.faces[0] = new TexturedQuad(new PositionTextureVertex[] {
			positiontexturevertex5, positiontexturevertex1, positiontexturevertex2, positiontexturevertex6
		}, this.textureOffsetX + depth + width, this.textureOffsetY + depth, this.textureOffsetX + depth + width + depth, this.textureOffsetY + depth + height, this.textureWidth, this.textureHeight);
		this.faces[1] = new TexturedQuad(new PositionTextureVertex[] {
			positiontexturevertex, positiontexturevertex4, positiontexturevertex7, positiontexturevertex3
		}, this.textureOffsetX + 0, this.textureOffsetY + depth, this.textureOffsetX + depth, this.textureOffsetY + depth + height, this.textureWidth, this.textureHeight);
		this.faces[2] = new TexturedQuad(new PositionTextureVertex[] {
			positiontexturevertex5, positiontexturevertex4, positiontexturevertex, positiontexturevertex1
		}, this.textureOffsetX + depth, this.textureOffsetY + 0, this.textureOffsetX + depth + width, this.textureOffsetY + depth, this.textureWidth, this.textureHeight);
		this.faces[3] = new TexturedQuad(new PositionTextureVertex[] {
			positiontexturevertex2, positiontexturevertex3, positiontexturevertex7, positiontexturevertex6
		}, this.textureOffsetX + depth + width, this.textureOffsetY + 0, this.textureOffsetX + depth + width + width, this.textureOffsetY + depth, this.textureWidth, this.textureHeight);
		this.faces[4] = new TexturedQuad(new PositionTextureVertex[] {
			positiontexturevertex1, positiontexturevertex, positiontexturevertex3, positiontexturevertex2
		}, this.textureOffsetX + depth, this.textureOffsetY + depth, this.textureOffsetX + depth + width, this.textureOffsetY + depth + height, this.textureWidth, this.textureHeight);
		this.faces[5] = new TexturedQuad(new PositionTextureVertex[] {
			positiontexturevertex4, positiontexturevertex5, positiontexturevertex6, positiontexturevertex7
		}, this.textureOffsetX + depth + width + depth, this.textureOffsetY + depth, this.textureOffsetX + depth + width + depth + width, this.textureOffsetY + depth + height, this.textureWidth, this.textureHeight);
		if (this.mirror)
		{
			for (int l = 0; l < this.faces.length; l++)
			{
				this.faces[l].flipFace();
			}

		}
	}

	@Override
	public void render(float f)
	{
		if (this.isHidden)
		{
			return;
		}
		if (!this.showModel)
		{
			return;
		}
		if (!this.compiled)
		{
			this.compileDisplayList(f);
		}
		if (this.rotateAngleX != 0.0F || this.rotateAngleY != 0.0F || this.rotateAngleZ != 0.0F)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(this.rotationPointX * f, this.rotationPointY * f, this.rotationPointZ * f);
			if (this.rotateAngleZ != 0.0F)
			{
				GL11.glRotatef(this.rotateAngleZ * 57.29578F, 0.0F, 0.0F, 1.0F);
			}
			if (this.rotateAngleY != 0.0F)
			{
				GL11.glRotatef(this.rotateAngleY * 57.29578F, 0.0F, 1.0F, 0.0F);
			}
			if (this.rotateAngleX != 0.0F)
			{
				GL11.glRotatef(this.rotateAngleX * 57.29578F, 1.0F, 0.0F, 0.0F);
			}
			GL11.glCallList(this.displayList);
			GL11.glPopMatrix();
		}
		else if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F)
		{
			GL11.glTranslatef(this.rotationPointX * f, this.rotationPointY * f, this.rotationPointZ * f);
			GL11.glCallList(this.displayList);
			GL11.glTranslatef(-this.rotationPointX * f, -this.rotationPointY * f, -this.rotationPointZ * f);
		}
		else
		{
			GL11.glCallList(this.displayList);
		}
	}

	@Override
	public void renderWithRotation(float scale)
	{
		if (!this.isHidden)
		{
			if (this.showModel)
			{
				if (!this.compiled)
				{
					this.compileDisplayList(scale);
				}

				GL11.glPushMatrix();
				GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);

				if (this.rotateAngleY != 0.0F)
				{
					GL11.glRotatef(this.rotateAngleY * 57.29578F, 0.0F, 1.0F, 0.0F);
				}

				if (this.rotateAngleX != 0.0F)
				{
					GL11.glRotatef(this.rotateAngleX * 57.29578F, 1.0F, 0.0F, 0.0F);
				}

				if (this.rotateAngleZ != 0.0F)
				{
					GL11.glRotatef(this.rotateAngleZ * 57.29578F, 0.0F, 0.0F, 1.0F);
				}

				GL11.glCallList(this.displayList);
				GL11.glPopMatrix();
			}
		}
	}

	@Override
	public void postRender(float scale)
	{
		if (!this.isHidden)
		{
			if (this.showModel)
			{
				if (!this.compiled)
				{
					this.compileDisplayList(scale);
				}

				if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F)
				{
					if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F)
					{
						GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
					}
				}
				else
				{
					GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);

					if (this.rotateAngleZ != 0.0F)
					{
						GL11.glRotatef(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
					}

					if (this.rotateAngleY != 0.0F)
					{
						GL11.glRotatef(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
					}

					if (this.rotateAngleX != 0.0F)
					{
						GL11.glRotatef(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void compileDisplayList(float scale)
	{
		this.displayList = GLAllocation.generateDisplayLists(1);
		GL11.glNewList(this.displayList, GL11.GL_COMPILE);
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();

		for (int i = 0; i < this.faces.length; i++)
		{
			this.faces[i].draw(bufferbuilder, scale);
		}

		GL11.glEndList();
		this.compiled = true;
	}

	public void func_35969_a(ModelRendererPyramid renderer)
	{
		this.rotationPointX = renderer.rotationPointX;
		this.rotationPointY = renderer.rotationPointY;
		this.rotationPointZ = renderer.rotationPointZ;
		this.rotateAngleX = renderer.rotateAngleX;
		this.rotateAngleY = renderer.rotateAngleY;
		this.rotateAngleZ = renderer.rotateAngleZ;
	}

}