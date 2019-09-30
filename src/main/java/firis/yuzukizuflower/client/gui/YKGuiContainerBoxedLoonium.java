package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerBoxedLoonium;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedLoonium extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerBoxedLoonium(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedLoonium(iTeInv, playerInv));
		
		this.tileEntity = iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_loonium.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_loonium.name";
		
		//GUI矢印
		this.guiArrowX = 40;
		this.guiArrowY = 41;
		
	}
	
}
