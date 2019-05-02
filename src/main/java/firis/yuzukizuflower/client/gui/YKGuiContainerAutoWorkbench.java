package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerAutoWorkbench;
import firis.yuzukizuflower.common.tileentity.IYKTileGuiBoxedFlower;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerAutoWorkbench extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerAutoWorkbench(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerAutoWorkbench(iTeInv, playerInv));
		
		this.tileEntity = (IYKTileGuiBoxedFlower) iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/auto_workbench.png");
		
		//GUIタイトル
		this.guiTitle = "gui.auto_workbench.name";
		
		// GUIサイズ
		this.guiWidth = 193;
		this.guiHeight = 180;

		this.xSize = this.guiWidth;
		this.ySize = this.guiHeight;
		
		//マナゲージレイヤー位置
		this.guiManaGageLayerX = 193;
		this.guiManaGageLayerY = 0;
		
		//GUI矢印と炎
		this.guiVisibleArrow = false;
		this.guiVisibleFire = false;
		
	}
}