package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.client.gui.parts.YKGuiScrollBar;
import firis.yuzukizuflower.common.container.YKContainerRemoteChest;
import firis.yuzukizuflower.common.inventory.IScrollInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author computer
 *
 */
public class YKGuiContainerRemoteChest extends YKGuiContainerBaseScrollInventory {

	/**
	 * コンストラクタ
	 * @param iinv
	 * @param playerInv
	 */
	public YKGuiContainerRemoteChest(IScrollInventory iinv, InventoryPlayer playerInv) {
		
		super(new YKContainerRemoteChest(iinv, playerInv), iinv);
		
		// GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/scroll_chest.png");

		String title = "";
		ItemStack stack = playerInv.player.getHeldItemMainhand();
		if(stack.hasTagCompound()) {
        	NBTTagCompound nbt = stack.getTagCompound();
        	
        	String name = nbt.getString("BlockName");
        	Integer posX = nbt.getInteger("BlockPosX");
        	Integer posY = nbt.getInteger("BlockPosY");
        	Integer posZ = nbt.getInteger("BlockPosZ");
        	
        	title = name + "<" + posX.toString() + ", " + posY.toString() + ", " + posZ.toString() + ">";
        }
		
		// GUIタイトル
		this.guiTitle = title;

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
