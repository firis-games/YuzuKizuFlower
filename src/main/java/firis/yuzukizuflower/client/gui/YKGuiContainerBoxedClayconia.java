package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerBoxedClayconia;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedClayconia extends YKGuiContainerBaseBoxedFuncFlower {

	public YKGuiContainerBoxedClayconia(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedClayconia(iTeInv, playerInv));
		
		this.tileEntity = iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_clayconia.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_clayconia.name";
		
		//GUI矢印
		this.guiArrowX = 73;
		this.guiArrowY = 41;
		
	}
}
