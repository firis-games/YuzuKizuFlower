package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerBoxedAocean;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedAocean extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerBoxedAocean(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedAocean(iTeInv, playerInv));
		
		this.tileEntity = iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_aocean.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_aocean.name";
		
		//GUI矢印
		this.guiArrowX = 40;
		this.guiArrowY = 41;
		
	}
	
}
