package firis.yuzukizuflower.client.tesr;

import org.lwjgl.opengl.GL11;

import firis.yuzukizuflower.common.tileentity.YKTileManaTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class YKTileManaTankSpRenderer  extends TileEntitySpecialRenderer<YKTileManaTank> {
	
	/**
	 * 描画
	 */
	@Override
	public void render(YKTileManaTank te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		
        GlStateManager.pushMatrix();
        //GlStateManager.translate((float)x + 0.5F, (float)y, (float)z + 0.5F);
        //座標を基準位置へ
        GlStateManager.translate((float)x, (float)y, (float)z);
        //renderFluid(te, x, y, z, partialTicks);
        renderFluid(te, x, y, z);
        GlStateManager.popMatrix();
		
	}
	
	//マナテクスチャ
	protected TextureAtlasSprite spriteMana = null;
	
	/**
	 * 液体部分を描画する
	 */
	protected void renderFluid(YKTileManaTank te, double x, double y, double z) {
		
		//テクスチャ設定
		if (spriteMana == null) {
			spriteMana = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("botania:blocks/mana_water");
		}
		
		if (te.getMana() == 0) {
			return;
		}
	
        //テクスチャバインド
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		//GL11初期化
		GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDisable(GL11.GL_CULL_FACE);


        //ライト設定（これをしないと描画したものが暗くなる）
        RenderHelper.disableStandardItemLighting();
        
        //描画のオブジェクトを透過する（液体なので透過する必要あり）
        //GlStateManager.enableBlend();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        
        //位置を微調整する
        GlStateManager.translate(
        		2.0F / 16.0F, 
        		2.0F / 16.0F, 
        		2.0F / 16.0F);

        //スプライトからUVを取得
        float minU = spriteMana.getMinU();
        float maxU = spriteMana.getMaxU();
        float minV = spriteMana.getMinV();
        float maxV = spriteMana.getMaxV();
        
        //基準の高さ
        double vertX = 12.0 / 16.0;
        double vertY = 12.0 / 16.0;
        double vertZ = 12.0 / 16.0;
        
        //高さはさらに計算が必要
        double manaCap = (double)te.getMana() / (double)te.getMaxMana();
        vertY *= manaCap;
        
        //maxU *= manaCap;
        
        //描画処理
        //天板
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(minU, minV);
        GL11.glVertex3d(0, vertY, 0);
        GL11.glTexCoord2d(maxU, minV);
        GL11.glVertex3d(vertX, vertY, 0);
        GL11.glTexCoord2d(maxU, maxV);
        GL11.glVertex3d(vertX, vertY, vertZ);
        GL11.glTexCoord2d(minU, maxV);
        GL11.glVertex3d(0, vertY, vertZ);
        GL11.glEnd();
        
        //裏面描画をしない
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        //前面
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(minU, minV);
        GL11.glVertex3d(0, vertY, 0);
        GL11.glTexCoord2d(maxU, minV);
        GL11.glVertex3d(vertX, vertY, 0);
        GL11.glTexCoord2d(maxU, maxV);
        GL11.glVertex3d(vertX, 0, 0);
        GL11.glTexCoord2d(minU, maxV);
        GL11.glVertex3d(0, 0, 0);
        GL11.glEnd();
        
        //右側
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(minU, minV);
        GL11.glVertex3d(vertX, vertY, 0);
        GL11.glTexCoord2d(maxU, minV);
        GL11.glVertex3d(vertX, vertY, vertZ);
        GL11.glTexCoord2d(maxU, maxV);
        GL11.glVertex3d(vertX, 0, vertZ);
        GL11.glTexCoord2d(minU, maxV);
        GL11.glVertex3d(vertX, 0, 0);
        GL11.glEnd();
        
        //左側
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(minU, minV);
        GL11.glVertex3d(0, 0, 0);
        GL11.glTexCoord2d(maxU, minV);
        GL11.glVertex3d(0, 0, vertZ);
        GL11.glTexCoord2d(maxU, maxV);
        GL11.glVertex3d(0, vertY, vertZ);
        GL11.glTexCoord2d(minU, maxV);
        GL11.glVertex3d(0, vertY, 0);
        GL11.glEnd();
        
        //背面
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(minU, minV);
        GL11.glVertex3d(0, 0, vertZ);
        GL11.glTexCoord2d(maxU, minV);
        GL11.glVertex3d(vertX, 0, vertZ);
        GL11.glTexCoord2d(maxU, maxV);
        GL11.glVertex3d(vertX, vertY, vertZ);
        GL11.glTexCoord2d(minU, maxV);
        GL11.glVertex3d(0, vertY, vertZ);
        GL11.glEnd();
        
        //底板
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(minU, minV);
        GL11.glVertex3d(0, 0, 0);
        GL11.glTexCoord2d(maxU, minV);
        GL11.glVertex3d(vertX, 0, 0);
        GL11.glTexCoord2d(maxU, maxV);
        GL11.glVertex3d(vertX, 0, vertZ);
        GL11.glTexCoord2d(minU, maxV);
        GL11.glVertex3d(0, 0, vertZ);
        GL11.glEnd();
        
        //GL11終了処理
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glFlush();
        
	}
	
	/**
	 * 流体の四角を描画するためのメソッド
	 */
	protected void vertexFluid() {
		
		
	}
	
	
	
	
	
	
	
	
	protected TextureAtlasSprite manaWaterTexture = null;
	
	
	
    /** Takes _RGB (alpha is set to 1) */
    protected void setGLColorFromInt(int color) {
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        GlStateManager.color(red, green, blue);
    }
    
	protected void renderFluidBC(YKTileManaTank te, double x, double y, double z, float partialTicks) {
		
		/*
		FluidStackInterp forRender = tile.getFluidForRender(partialTicks);
        if (forRender == null) {
            return;
        }
        */
        
        Minecraft.getMinecraft().mcProfiler.startSection("bc");
        Minecraft.getMinecraft().mcProfiler.startSection("tank");

        // gl state setup
        RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

        // buffer setup
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        
        //buffer.setTranslation(x, y, z);
        
        //描画方向の判断
        boolean[] sideRender = { true, true, true, true, true, true };
        /* BCでは判断しているけど今回は無視
        boolean connectedUp = isFullyConnected(tile, EnumFacing.UP, partialTicks);
        boolean connectedDown = isFullyConnected(tile, EnumFacing.DOWN, partialTicks);
        sideRender[EnumFacing.DOWN.ordinal()] = !connectedDown;
        sideRender[EnumFacing.UP.ordinal()] = !connectedUp;
        */
        
        /* おそらく液体の光源レベルの制御をやってるっぽい、今回は無視
        Vec3d min = connectedDown ? MIN_CONNECTED : MIN;
        Vec3d max = connectedUp ? MAX_CONNECTED : MAX;
        FluidStack fluid = forRender.fluid;
        int blocklight = fluid.getFluid().getLuminosity(fluid);
        int combinedLight = tile.getWorld().getCombinedLight(tile.getPos(), blocklight);

        FluidRenderer.vertex.lighti(combinedLight);
        */
        
        int amount = 2000;
        int cap = 10000;
        
        //液体の書き出し
        {
        	Minecraft.getMinecraft().mcProfiler.startSection("fluid");
        	
        	
        	double height = MathHelper.clamp(amount / cap, 0, 1);
            final Vec3d realMin, realMax;
            /* ガスかどうかを判断する、今回は不要
            if (fluid.getFluid().isGaseous(fluid)) {
                realMin = VecUtil.replaceValue(min, Axis.Y, MathUtil.interp(1 - height, min.y, max.y));
                realMax = max;
            } else {
                realMin = min;
                realMax = VecUtil.replaceValue(max, Axis.Y, MathUtil.interp(height, min.y, max.y));
            }
            */
            /* これが別の場所で定義されているもの
            private static final Vec3d MIN = new Vec3d(0.13, 0.01, 0.13);
            private static final Vec3d MAX = new Vec3d(0.86, 0.99, 0.86);
            */
            //Vec3d min = new Vec3d(0.13, 0.01, 0.13);
            //Vec3d max = new Vec3d(0.86, 0.99, 0.86);
            Vec3d min = new Vec3d(0,0,0);
            Vec3d max = new Vec3d(1,1,1);
            
            realMin = min;
            //おそらくここでサイズを制御している
            //realMax = VecUtil.replaceValue(max, Axis.Y, MathUtil.interp(height, min.y, max.y));
            realMax = max;
            

            //bufferを代入してるが今回は必要ない
            //bb = bbIn;

            /*
            if (type == null) {
                type = FluidSpriteType.STILL;
            }
            sprite = fluidSprites.get(type).get(fluid.getFluid().getName());
            if (sprite == null) {
                sprite = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
            }
            */
            //スプライトを取得する
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("botania:blocks/mana_water");

            final double xs = realMin.x;
            final double ys = realMin.y;
            final double zs = realMin.z;

            final double xb = realMax.x;
            final double yb = realMax.y;
            final double zb = realMax.z;
            
            /* 流れるほうかSTILLかで分岐している今回だとSTILL固定
            if (type == FluidSpriteType.FROZEN) {
                if (min.x > 1) {
                    xTexDiff = Math.floor(min.x);
                } else if (min.x < 0) {
                    xTexDiff = Math.floor(min.x);
                } else {
                    xTexDiff = 0;
                }
                if (min.y > 1) {
                    yTexDiff = Math.floor(min.y);
                } else if (min.y < 0) {
                    yTexDiff = Math.floor(min.y);
                } else {
                    yTexDiff = 0;
                }
                if (min.z > 1) {
                    zTexDiff = Math.floor(min.z);
                } else if (min.z < 0) {
                    zTexDiff = Math.floor(min.z);
                } else {
                    zTexDiff = 0;
                }
            } else {
                xTexDiff = 0;
                yTexDiff = 0;
                zTexDiff = 0;
            }
            */
            double xTexDiff, yTexDiff, zTexDiff = 0;

            //色の制御を行う
            //vertexはローカルにstaticで定義される
            //vertex.colouri(RenderUtil.swapARGBforABGR(fluid.getFluid().getColor(fluid)));
            
            //ここから手探り
            //**************************************************
            //描画している部分の
            double spriteU = sprite.getInterpolatedU(1 * 16);
            double spriteV = sprite.getInterpolatedV(1 * 16);
            
            //ポジション設定
            //xs, yb, zb
            
            //テクスチャの設定
            //u,vを設定してるだけ
            spriteU = sprite.getInterpolatedU(xs * 16);
            spriteV = sprite.getInterpolatedV(yb * 16);
            
            //bufferに設定する
            buffer.pos(xs, yb, zb);
            //buffer.color(red, green, blue, alpha);
            buffer.tex(spriteU, spriteV);
            //bb.lightmap(light_sky << 4, light_block << 4);
            buffer.endVertex();
            

            spriteU = sprite.getInterpolatedU(xb * 16);
            spriteV = sprite.getInterpolatedV(yb * 16);

            buffer.pos(xb, yb, zb);
            buffer.tex(spriteU, spriteV);
            buffer.endVertex();

            spriteU = sprite.getInterpolatedU(xb * 16);
            spriteV = sprite.getInterpolatedV(yb * 16);

            buffer.pos(xb, yb, zs);
            buffer.tex(spriteU, spriteV);
            buffer.endVertex();
            
            
            spriteU = sprite.getInterpolatedU(xs * 16);
            spriteV = sprite.getInterpolatedV(yb * 16);

            buffer.pos(xs, yb, zs);
            buffer.tex(spriteU, spriteV);
            buffer.endVertex();
            
            
            
            
            
/*
            texmap = TexMap.XZ;
            // TODO: Enable/disable inversion for the correct faces
            invertU = false;
            invertV = false;
            if (sideRender[EnumFacing.UP.ordinal()]) {
                vertex(xs, yb, zb);
                vertex(xb, yb, zb);
                vertex(xb, yb, zs);
                vertex(xs, yb, zs);
            }

            if (sideRender[EnumFacing.DOWN.ordinal()]) {
                vertex(xs, ys, zs);
                vertex(xb, ys, zs);
                vertex(xb, ys, zb);
                vertex(xs, ys, zb);
            }

            texmap = TexMap.ZY;
            if (sideRender[EnumFacing.WEST.ordinal()]) {
                vertex(xs, ys, zs);
                vertex(xs, ys, zb);
                vertex(xs, yb, zb);
                vertex(xs, yb, zs);
            }

            if (sideRender[EnumFacing.EAST.ordinal()]) {
                vertex(xb, yb, zs);
                vertex(xb, yb, zb);
                vertex(xb, ys, zb);
                vertex(xb, ys, zs);
            }

            texmap = TexMap.XY;
            if (sideRender[EnumFacing.NORTH.ordinal()]) {
                vertex(xs, yb, zs);
                vertex(xb, yb, zs);
                vertex(xb, ys, zs);
                vertex(xs, ys, zs);
            }

            if (sideRender[EnumFacing.SOUTH.ordinal()]) {
                vertex(xs, ys, zb);
                vertex(xb, ys, zb);
                vertex(xb, yb, zb);
                vertex(xs, yb, zb);
            }
            sprite = null;
            texmap = null;
            bb = null;
*/
        	Minecraft.getMinecraft().mcProfiler.endSection();
        }
        
        
        // 書き出し
        //buffer.setTranslation(0, 0, 0);
        tessellator.draw();
        
        // gl state finish
        RenderHelper.enableStandardItemLighting();

        Minecraft.getMinecraft().mcProfiler.endSection();
        Minecraft.getMinecraft().mcProfiler.endSection();
        
        
        
        
        
        //初期設定
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        
        RenderHelper.disableStandardItemLighting();
        //GlStateManager.enableBlend();
        //GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);


        
        //ここから
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        
        
        
        
        GlStateManager.translate(0F, 1.5F, 0F);
        
        /* 直接描画？*/
        //GL11.glColor3d(1.0, 0.0, 0.0);
        //裏面描画なしを無効化
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glBegin(GL11.GL_QUADS);
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("botania:blocks/mana_water");
        double spriteU = sprite.getInterpolatedU(1 * 16);
        double spriteV = sprite.getInterpolatedV(1 * 16);
        /*
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMinV());
        GL11.glVertex2d(-0.5, -0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMinV());
        GL11.glVertex2d(-0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMaxV());
        GL11.glVertex2d(0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMaxV());
        GL11.glVertex2d(0.5, -0.5);
        */
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMinV());
        GL11.glVertex3d(-0.5, 0.5, -0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMinV());
        GL11.glVertex3d(-0.5, 0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMaxV());
        GL11.glVertex3d(0.5, 0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMaxV());
        GL11.glVertex3d(0.5, 0.5, -0.5);
        
        //ここから上までで四角を描画してる
        //GL11.glBindTexture 最終的にここまでやってるみたい
        //終了処理
        
        GL11.glEnd();
        
        
        //側面
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMinV());
        GL11.glVertex3d(0.5, -0.5, -0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMinV());
        GL11.glVertex3d(0.5, -0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMaxV());
        GL11.glVertex3d(0.5, 0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMaxV());
        GL11.glVertex3d(0.5, 0.5, -0.5);
        GL11.glEnd();
        
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMinV());
        GL11.glVertex3d(-0.5, -0.5, -0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMinV());
        GL11.glVertex3d(-0.5, -0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMaxV());
        GL11.glVertex3d(-0.5, 0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMaxV());
        GL11.glVertex3d(-0.5, 0.5, -0.5);
        GL11.glEnd();
        
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMinV());
        GL11.glVertex3d(0.5, -0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMinV());
        GL11.glVertex3d(0.5, 0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMaxV());
        GL11.glVertex3d(-0.5, 0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMaxV());
        GL11.glVertex3d(-0.5, -0.5, 0.5);
        GL11.glEnd();
        
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMinV());
        GL11.glVertex3d(0.5, -0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMinV());
        GL11.glVertex3d(0.5, 0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMaxV());
        GL11.glVertex3d(-0.5, 0.5, 0.5);
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMaxV());
        GL11.glVertex3d(-0.5, -0.5, 0.5);
        GL11.glEnd();
        
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMinV());
        GL11.glVertex3d(0.5, -0.5, -0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMinV());
        GL11.glVertex3d(0.5, 0.5, -0.5);
        GL11.glTexCoord2d(sprite.getMaxU(), sprite.getMaxV());
        GL11.glVertex3d(-0.5, 0.5, -0.5);
        GL11.glTexCoord2d(sprite.getMinU(), sprite.getMaxV());
        GL11.glVertex3d(-0.5, -0.5, -0.5);
        GL11.glEnd();
        
        
        
        GL11.glFlush();
        /* 直接描画？*/
        
        //裏面描画なしを有効化
        GL11.glEnable(GL11.GL_CULL_FACE);
	    
	}
	
	
	/**
	 * 液体部分の描画
	 */
	protected void renderFluid(YKTileManaTank te, double x, double y, double z, float partialTicks) {
		
		//どうやってやるか
		
		//マナのテクスチャ取得
		if (manaWaterTexture == null) {
			manaWaterTexture = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("botania:blocks/mana_water");
		}
		
		
		Minecraft.getMinecraft().mcProfiler.startSection("ykflower");
		Minecraft.getMinecraft().mcProfiler.startSection("tank");
		
		//gl state startup
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x + 0.5F, (float)y + 1, (float)z + 0.5F);
		
		
		
		RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        
		float scale = 1.0F;
		GlStateManager.scale(scale, scale, scale);
		//GL11.glColor3d(1.0, 0.0, 0.0);
		GL11.glDisable(GL11.GL_CULL_FACE);
		
		float u0 = manaWaterTexture.getMinU();
		float u1 = manaWaterTexture.getMaxU();
		float v0 = manaWaterTexture.getMinV();
		float v1 = manaWaterTexture.getMaxV();
		
        //描画処理
        
        GL11.glBegin(GL11.GL_QUADS);
        
        //ミッシングのスプライト
        //Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
        GL11.glVertex2d(-0.5, -0.5);
        GL11.glVertex2d(-0.5, 0.5);
        GL11.glVertex2d(0.5, 0.5);
        GL11.glVertex2d(0.5, -0.5);
                
        GL11.glEnd();
        
        // gl state finish
        RenderHelper.enableStandardItemLighting();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        
        Minecraft.getMinecraft().mcProfiler.endSection();
        Minecraft.getMinecraft().mcProfiler.endSection();

		
		
	}
	
	/**
     * Render the mob inside the mob spawner.
     */
    protected void renderItem(YKTileManaTank te, double posX, double posY, double posZ, float partialTicks) {
    	
    	ItemStack renderItemStack = new ItemStack(Items.DIAMOND);
    	EntityItem renderItemEntity = new EntityItem(te.getWorld());
    	renderItemEntity.setItem(renderItemStack);
    	renderItemEntity.hoverStart = 0;
    	
    	//Mobの拡大率
    	float scale = 1.0F;
    	
    	//基本座標をブロック位置の中心部へ変更は上の階層でやってる
    	
    	GlStateManager.scale(scale, scale, scale);
    	
    	//ブロックの上（エンチャントテーブルの高さ）に表示する
        GlStateManager.translate(0.0F, 0.75F - 0.1F, 0.0F);
        
        //ワールドのプレイ時間で回転させる
        float rot = te.getWorld().getTotalWorldTime() % 360;
        rot = rot * 2;
        
        GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);
        
        //これを初期化してないとぷるぷるする
        partialTicks = 0;
        
        //Entityをレンダリング
        Minecraft.getMinecraft().getRenderManager().renderEntity(renderItemEntity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
        
}
    
	
	/*
	public void render(YKTileManaTank te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.5F, (float)y, (float)z + 0.5F);
        //renderMob(te, x, y, z, partialTicks);
        renderMob0(te, x, y, z, partialTicks);
        GlStateManager.popMatrix();

        //実験用
        //renderMob2(te, x, y, z, partialTicks);

	}
	*/
	
}
