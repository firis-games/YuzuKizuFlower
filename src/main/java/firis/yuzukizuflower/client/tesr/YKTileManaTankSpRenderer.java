package firis.yuzukizuflower.client.tesr;

import org.lwjgl.opengl.GL11;

import firis.yuzukizuflower.common.tileentity.YKTileManaTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

/**
 * マナタンクtesr
 * @author computer
 *
 */
public class YKTileManaTankSpRenderer extends TileEntitySpecialRenderer<YKTileManaTank> {
	
	//マナテクスチャ
	protected TextureAtlasSprite spriteMana = null;
	
	/**
	 * Renderer
	 */
	@Override
	public void render(YKTileManaTank te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        renderFluid(te, x, y, z);
        GlStateManager.popMatrix();
		
	}
		
	/**
	 * 液体部分を描画する
	 */
	protected void renderFluid(YKTileManaTank te, double x, double y, double z) {
		
		if (spriteMana == null) {
			spriteMana = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("botania:blocks/mana_water");
		}
		
		//Manaがない場合は描画しない
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
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        //jsonと同じように16x16で位置を設定調整する
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
        
        //最低値
        vertY = Math.max(0.001D, vertY);
        
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
}
