package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerBoxedOrechid;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedOrechid extends YKGuiContainerBaseBoxedFuncFlower {

	public YKGuiContainerBoxedOrechid(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedOrechid(iTeInv, playerInv));
		
		this.tileEntity = iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_orechid.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_orechid.name";
		
		//GUI矢印
		this.guiArrowX = 73;
		this.guiArrowY = 41;
		
	}
}
