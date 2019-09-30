package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerBoxedAkanerald;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedAkanerald extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerBoxedAkanerald(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedAkanerald(iTeInv, playerInv));
		
		this.tileEntity = iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_akanerald.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_akanerald.name";
		
		//GUI矢印
		this.guiArrowX = 40;
		this.guiArrowY = 41;
		
	}
	
}
