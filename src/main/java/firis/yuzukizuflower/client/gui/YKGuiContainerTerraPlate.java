package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerTerraPlate;
import firis.yuzukizuflower.common.tileentity.IYKTileGuiBoxedFlower;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerTerraPlate extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerTerraPlate(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerTerraPlate(iTeInv, playerInv));
		
		this.tileEntity = (IYKTileGuiBoxedFlower) iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/terra_plate.png");
		
		//GUIタイトル
		this.guiTitle = "gui.terra_plate.name";
		
		//GUI矢印
		this.guiArrowX = 83;
		this.guiArrowY = 41;
		
	}
	
}
