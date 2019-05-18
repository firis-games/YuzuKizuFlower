package firis.yuzukizuflower.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import firis.yuzukizuflower.client.gui.parts.YKGuiScrollBar;
import firis.yuzukizuflower.common.container.YKContainerScrollChest;
import firis.yuzukizuflower.common.inventory.IScrollInventory;
import firis.yuzukizuflower.common.inventory.IScrollInventoryClientItemHandler;
import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.network.PacketGuiScroll;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerScrollChest extends YKGuiContainerBaseScrollInventory {

	private GuiTextField textField;
	
	/**
	 * コンストラクタ
	 * @param iinv
	 * @param playerInv
	 */
	public YKGuiContainerScrollChest(IScrollInventory iinv, InventoryPlayer playerInv) {
		
		super(new YKContainerScrollChest(iinv, playerInv), iinv);
		
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
	
	//==========
	/**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
		if (this.textField.textboxKeyTyped(typedChar, keyCode)) {
			//キー入力成功時
			this.onTextChanged();
		} else {
			//通常時
			super.keyTyped(typedChar, keyCode);			
		}
		
    }
	
	/**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
	@Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        
        //テキストフィールドクリックイベント
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);
        
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        
        this.textField.drawTextBox();
    }
	
	
	@Override
	public void initGui() {
		
		super.initGui();
		
		//キーボードの押しっぱなしで繰り返しを有効化
		Keyboard.enableRepeatEvents(true);
		
		//テキストフィールド初期化
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		
		this.textField = new GuiTextField(0, this.fontRenderer, x + 128, y + 5, 55, 12);
        this.textField.setMaxStringLength(50);
        this.textField.setEnableBackgroundDrawing(false);
        this.textField.setVisible(true);
        this.textField.setText("");
        this.textField.setTextColor(16777215);
        
        //inventoryのフィルタを再設定
        //this.textField.setText(((ITextScrollInventoryItemHandler) this.iinventory).getTextSearch());
        this.textField.setText(((IScrollInventoryClientItemHandler) this.iinventory).getTextSearch());
    }
	
	
	/**
	 * スクロール変更時の処理を追加、テキストも同時に送信する
	 */
	/*
	@Override
	public void onScrollChanged(int page) {
				
		//ページ設定
		iinventory.setScrollPage(page);
		
		//Serverへパケット送信
		NetworkHandler.network.sendToServer(
				new PacketGuiScroll.MessageGuiScroll(page, ""));
	}
	*/
	
	/**
	 * テキスト情報を含む情報を送信
	 */
	public void onTextChanged() {
		
		iinventory.setTextChanged(this.textField.getText());
		
		this.scrollBar.setScrollMaxPage(iinventory.getScrollMaxPage());
		this.scrollBar.resetScrollPage();
		
		//Serverへパケット送信
		NetworkHandler.network.sendToServer(
				new PacketGuiScroll.MessageGuiScroll(-1, this.textField.getText()));
	}
	
}