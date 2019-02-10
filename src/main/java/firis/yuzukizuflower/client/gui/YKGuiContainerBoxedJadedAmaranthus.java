package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerBoxedJadedAmaranthus;
import firis.yuzukizuflower.common.tileentity.IYKTileGuiBoxedFlower;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedJadedAmaranthus extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerBoxedJadedAmaranthus(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedJadedAmaranthus(iTeInv, playerInv));
		
		this.tileEntity = (IYKTileGuiBoxedFlower) iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_jaded_amaranthus.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_jaded_amaranthus.name";
		
		//GUI矢印
		this.guiArrowX = 40;
		this.guiArrowY = 41;
		
	}
	
}
