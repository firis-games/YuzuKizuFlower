package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.client.gui.parts.YKGuiScrollBar;
import firis.yuzukizuflower.common.container.YKContainerRemoteChest;
import firis.yuzukizuflower.common.inventory.IScrollInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerScrollChest extends YKGuiContainerBaseScrollInventory {

	/**
	 * コンストラクタ
	 * @param iinv
	 * @param playerInv
	 */
	public YKGuiContainerScrollChest(IScrollInventory iinv, InventoryPlayer playerInv) {
		
		super(new YKContainerRemoteChest(iinv, playerInv), iinv);
		
		// GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/scroll_chest.png");

		// GUIタイトル
		this.guiTitle = "gui.scroll_chest.name";

		// GUIサイズ
		this.guiWidth = 193;
		this.guiHeight = 222;

		this.xSize = this.guiWidth;
		this.ySize = this.guiHeight;
		
		int maxPage = iinv.getScrollMaxPage();
		
		// スクロールバー
		// x座標 y座標 スクロールの高さ スクロールのページ数
		scrollBar = new YKGuiScrollBar(this, 174, 18, 106, maxPage);
	}
}