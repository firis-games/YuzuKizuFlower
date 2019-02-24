package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerBoxedAkariculture;
import firis.yuzukizuflower.common.tileentity.IYKTileGuiBoxedFlower;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedAkariculture extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerBoxedAkariculture(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedAkariculture(iTeInv, playerInv));
		
		this.tileEntity = (IYKTileGuiBoxedFlower) iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_akariculture.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_akariculture.name";
		
		//GUI矢印と炎
		this.guiVisibleArrow = false;
		this.guiVisibleFire = false;
		
	}
}