package firis.yuzukizuflower.client.layer;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import firis.yuzukizuflower.common.item.YKItemBackpackChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.item.IBaubleRender.Helper;

/**
 * BackPack描画用
 * @author computer
 *
 */
public final class YKBackPackLayer implements LayerRenderer<EntityPlayer> {

	@Override
	public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		if (!this.isRender(player)) {
			return;
		}
		
		GlStateManager.pushMatrix();
		//ブロックを描画
		//========================================
		//スニーク位置調整
		Helper.rotateIfSneaking(player);
		//Helper.translateToChest();
		//Helper.defaultTransforms();
		
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		GlStateManager.rotate(270, 0, 1, 0);
		GlStateManager.rotate(180, 1, 0, 0);
		GlStateManager.translate(0.2F, -0.9F, 0.5F);
		
		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.CHEST.getDefaultState(), 1.0F);
		//========================================
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		
		//付属アイテム描画
		//========================================
		//スニーク位置調整
		Helper.rotateIfSneaking(player);
				
		GlStateManager.scale(1.0F, 1.0F, 1.0F);
		
		//Xが横軸
		//Zが縦軸
		//180度回転		
		//腰の位置に来るように調整
		GlStateManager.translate(0.0F, 0.0F, 0.2F);
		GlStateManager.rotate(180, 1, 0, 0);
		GlStateManager.translate(0.0F, -1.0F, 0.0F);
		//位置の微調整
		GlStateManager.translate(-0.3F, 0.15F, 0.0F);
		//アイテムが真横になるように傾ける
		GlStateManager.rotate(-45, 0, 0, 1);

		EntityItem item = new EntityItem(player.getEntityWorld());
		item.setItem(new ItemStack(Items.IRON_SHOVEL));
		item.hoverStart = 0.0F;
		
		//debug表示
		//Minecraft.getMinecraft().getRenderManager().setDebugBoundingBox(true);
		Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0.0D, 0.0D, 0.0D, 0.0F, 0, false);
		
		//========================================
		GlStateManager.popMatrix();
	}
	
	/**
	 * 描画するかの判断を行う
	 * @param player
	 * @return
	 */
	private boolean isRender(EntityPlayer player) {
		
		boolean ret = false;
		
		//BODY枠
		IBaublesItemHandler baublesHandler = BaublesApi.getBaublesHandler(player);
		for (int i = 0; i < BaubleType.BODY.getValidSlots().length; i++) {
			int slot = BaubleType.BODY.getValidSlots()[i];
			
			ItemStack work = baublesHandler.getStackInSlot(slot);
			
			if (work.getItem() instanceof YKItemBackpackChest) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
