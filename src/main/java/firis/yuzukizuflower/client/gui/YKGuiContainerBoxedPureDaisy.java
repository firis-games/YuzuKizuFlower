package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.common.container.YKContainerBoxedPureDaisy;
import firis.yuzukizuflower.common.tileentity.IYKTileGuiBoxedFlower;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedPureDaisy extends YKGuiContainerBaseBoxedFuncFlower {

	public YKGuiContainerBoxedPureDaisy(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedPureDaisy(iTeInv, playerInv));
		
		this.tileEntity = (IYKTileGuiBoxedFlower) iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_pure_daisy.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_pure_daisy.name";
		
		//GUI矢印
		this.guiArrowX = 79;
		this.guiArrowY = 34;
		
		//マナゲージの処理を行わない
		this.guiVisibleManaGage = false;
		
	}
}
