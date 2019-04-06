package firis.yuzukizuflower.client.tesr;

import firis.yuzukizuflower.common.tileentity.YKTileScrollChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKTileScrollChestSpRenderer extends TileEntitySpecialRenderer<YKTileScrollChest>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("yuzukizuflower:textures/entity/scroll_chest.png");
    
    private final ModelChest simpleChest = new ModelChest();
    
    public YKTileScrollChestSpRenderer() {
    }

    @Override
    public void render(YKTileScrollChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        
        //描画準備
        int meta = 0;
        
        //チェストの方向を取得
        if (te.hasWorld()) {
            Block block = te.getBlockType();
            meta = te.getBlockMetadata();

            if (block instanceof BlockChest && meta == 0)
            {
                ((BlockChest)block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                meta = te.getBlockMetadata();
            }
        }
        
        //モデルの設定
        ModelChest modelchest = simpleChest;
        
        //ブロック破壊のエフェクト？
        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
        	this.bindTexture(TEXTURE);
        }
        
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        if (destroyStage < 0)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        }

        //描画
        GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        
        //向きを制御
        int rotate = 0;
        switch (meta) {
        	case 2:
        		rotate = 180;
        		break;
        	case 3:
        		rotate = 0;
        		break;
        	case 4:
        		rotate = 90;
        		break;
        	case 5:
        		rotate = -90;
        		break;
        }
        GlStateManager.rotate((float)rotate, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        
        
        //
        float f = te.animationController.prevLidAngle 
        		+ (te.animationController.lidAngle - te.animationController.prevLidAngle) 
        		* partialTicks;
        
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        modelchest.chestLid.rotateAngleX = -(f * ((float)Math.PI / 2F));
        modelchest.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
        
    }
}
