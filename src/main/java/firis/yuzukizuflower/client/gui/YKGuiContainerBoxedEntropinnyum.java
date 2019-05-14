package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerBoxedEntropinnyum;
import firis.yuzukizuflower.common.tileentity.IYKTileGuiBoxedFlower;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedEntropinnyum extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerBoxedEntropinnyum(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedEntropinnyum(iTeInv, playerInv));
		
		this.tileEntity = (IYKTileGuiBoxedFlower) iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_entropinnyum.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_entropinnyum.name";
		
		//GUI矢印
		this.guiVisibleArrow = false;
		
		//GUI炎マーク
		this.guiVisibleFire = true;
		this.guiFireX = 80;
		this.guiFireY = 58;
	}
}