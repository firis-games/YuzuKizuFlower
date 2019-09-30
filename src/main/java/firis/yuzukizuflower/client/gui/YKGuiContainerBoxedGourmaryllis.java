package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerBoxedGourmaryllis;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedGourmaryllis extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerBoxedGourmaryllis(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedGourmaryllis(iTeInv, playerInv));
		
		this.tileEntity = iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_gourmaryllis.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_gourmaryllis.name";
		
		//GUI矢印
		this.guiVisibleArrow = false;
		
		//GUI炎マーク
		this.guiVisibleFire = true;
		this.guiFireX = 80;
		this.guiFireY = 58;
	}
}