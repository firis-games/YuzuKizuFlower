package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerBoxedEndoflame;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedEndoflame extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerBoxedEndoflame(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedEndoflame(iTeInv, playerInv));
		
		this.tileEntity = iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_endoflame.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_endoflame.name";
		
		//GUI矢印
		this.guiVisibleArrow = false;
		
		//GUI炎マーク
		this.guiVisibleFire = true;
		this.guiFireX = 80;
		this.guiFireY = 58;
	}
}