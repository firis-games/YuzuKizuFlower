package firis.yuzukizuflower.client.gui;

import java.io.IOException;

import firis.yuzukizuflower.client.gui.parts.YKGuiScrollBar;
import firis.yuzukizuflower.client.gui.parts.YKGuiScrollBar.IYKGuiScrollBarChanged;
import firis.yuzukizuflower.common.inventory.IScrollInventory;
import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.network.PacketGuiScroll;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class YKGuiContainerBaseScrollInventory extends GuiContainer implements IYKGuiScrollBarChanged {

	/**
	 * GUIテクスチャ
	 */
	protected ResourceLocation guiTextures = null;

	/**
	 * GUIタイトル
	 */
	protected String guiTitle = "";

	/**
	 * GUIサイズ
	 */
	protected int guiWidth = 176;
	protected int guiHeight = 166;

	/**
	 * Guiスクロールバー
	 */
	protected YKGuiScrollBar scrollBar;
	
	protected IScrollInventory iinventory;

	public YKGuiContainerBaseScrollInventory(Container container, IScrollInventory inventory) {
		
		super(container);

		this.iinventory = inventory;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		// テクスチャロード
		this.mc.getTextureManager().bindTexture(guiTextures);

		// 画面へバインド（かまどのGUIサイズ）
		int xSize = this.guiWidth;
		int ySize = this.guiHeight;

		// 描画位置を計算
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;

		// 画面へ描画
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		// スクロールバーの描画
		scrollBar.setScrollMaxPage(iinventory.getScrollMaxPage());
		scrollBar.drawScrollBar();

	}

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
		RenderHelper.disableStandardItemLighting();
		
		//xx_xx.langから取得
		TextComponentTranslation langtext = 
				new TextComponentTranslation(this.guiTitle, new Object[0]);
        String text = langtext.getFormattedText();

        //左寄せ
        int x = 8;
        int y = 6;
        
        //タイトル文字
        this.fontRenderer.drawString(text, x, y, 4210752);
        
        RenderHelper.enableGUIStandardItemLighting();
    }
	
	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Draws the screen and all the components in it. 背景色黒とアイテムのツールチップを表示するために必要
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		this.drawLockedSlot();
		
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	
	/**
	 * ロックスロットの色を変える
	 */
	protected void drawLockedSlot() {
		
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		
		for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
            Slot slot = this.inventorySlots.inventorySlots.get(i1);
            
            //符号あり4byte
            //AA RR GG BB の16進数で色を表している
            //16進数と10進数を変換するツールを使えば簡単
            //-2130706433 = 80FFFFFF
            //1073741824 = 40 00 00 00 黒で透過率25%を設定
            Integer color_code = 1073741824;
            
            //テクスチャを描画
            if (!slot.isEnabled()) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                int j1 = slot.xPos + x;
                int k1 = slot.yPos + y;
                GlStateManager.colorMask(true, true, true, false);
                this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, color_code, color_code);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
	}
	

	/**
	 * マウス移動等の入力イベント Handles mouse input.
	 */
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();

		// スクロール制御
		this.scrollBar.handleMouseInput();
	}

	/**
	 * マウスクリックイベント 
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		// スクロール制御
		this.scrollBar.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * マウスリリースイベント 
	 * Called when a mouse button is released.
	 */
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);

		// スクロール制御
		this.scrollBar.mouseReleased(mouseX, mouseY, state);
	}

	/**
	 * マウスムーブイベント 
	 * Called when a mouse button is pressed and the mouse is moved
	 * around. Parameters are : mouseX, mouseY, lastButtonClicked &
	 * timeSinceMouseClick.
	 */
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

		// スクロール制御
		this.scrollBar.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	/**
	 * スクロール変更時の処理
	 */
	@Override
	public void onScrollChanged(int page) {
				
		//ページ設定
		iinventory.setScrollPage(page);
		
		//Serverへパケット送信
		NetworkHandler.network.sendToServer(
				new PacketGuiScroll.MessageGuiScroll(page, ""));
	}
}
