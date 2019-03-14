package firis.yuzukizuflower.client.gui;

import java.text.NumberFormat;

import firis.yuzukizuflower.common.tileentity.IYKTileGuiBoxedFlower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public abstract class YKGuiContainerBaseBoxedFuncFlower extends GuiContainer {
	
	/**
	 * GUIテクスチャ
	 */
	protected ResourceLocation guiTextures = null;
	
	/**
	 * Manaテクスチャ
	 */
	protected TextureAtlasSprite manaWaterTextures = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("yuzukizuflower:blocks/mana_water_still");;
	
	/**
	 * Guiサイズ
	 */
	protected int guiWidth = 176;
	protected int guiHeight = 166;
	
	/**
	 * Gui矢印の位置
	 */
	protected int guiArrowX = 0;
	protected int guiArrowY = 0;
	
	/**
	 * Gui炎マークの位置
	 */
	protected int guiFireX = 0;
	protected int guiFireY = 0;
	
	/**
	 * GUIタイトル
	 */
	protected String guiTitle = "";
	
	
	/**
	 * GUI矢印表示可否設定
	 */
	protected boolean guiVisibleArrow = true;

	/**
	 * GUI炎マーク表示可否設定
	 */
	protected boolean guiVisibleFire = false;

	/**
	 * GUIマナゲージ表示可否設定
	 */
	protected boolean guiVisibleManaGage = true;
	
	/**
	 * IYKTileGuiBoxedFlower
	 * @param inventorySlotsIn
	 */
	protected IYKTileGuiBoxedFlower tileEntity = null;
	
	public YKGuiContainerBaseBoxedFuncFlower(Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		//テクスチャロード
        this.mc.getTextureManager().bindTexture(guiTextures);
        
        //画面へバインド（かまどのGUIサイズ）
        int xSize = this.guiWidth;
        int ySize = this.guiHeight;
        
        //描画位置を計算
        int x = (this.width - xSize) / 2;
        int y = (this.height - ySize) / 2;

        //画面へ描画
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
        //矢印の描画
	    if (guiVisibleArrow) {
	        int l = this.getProgressScaled(24);
	        this.drawTexturedModalRect(x + guiArrowX, y + guiArrowY, 192, 0, l + 1, 16);
        }
        
        //炎の描画
	    if (guiVisibleFire) {
	        int k = this.getBurnLeftScaled(13);
	        if (k != 0) {
	        	this.drawTexturedModalRect(x + guiFireX, y + guiFireY + 13 - k, 200, 12 - k, 14, k + 1);
	        }
	    }
	    
	    //マナゲージの描画
        if (guiVisibleManaGage) {
	        //マナゲージを描画する
	        this.drawManaGage(x + 16, y + 23);
	        
	        //テクスチャを戻す
	        this.mc.getTextureManager().bindTexture(guiTextures);
	        
	        //メモリの描画
	        this.drawTexturedModalRect(x + 16, y + 22, 176, 0, 10, 50);
        }

	}
	
	/**
	 * 矢印制御用
	 * @param pixels
	 * @return
	 */
	private int getProgressScaled(int pixels)
    {

        int i = this.tileEntity.getTimer();
        int j = this.tileEntity.getMaxTimer();
        
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }
	
	/**
	 * 炎マーク用
	 * @param pixels
	 * @return
	 */
	private int getBurnLeftScaled(int pixels)
    {
		int i = this.tileEntity.getTimer();
        int j = this.tileEntity.getMaxTimer();
        
        if (i == 0) {
        	return 0;
        }
        
        int ret = pixels * i / j;

        return pixels - ret;
    }
	
	/**
	 * マナゲージを描画する
	 */
	public void drawManaGage(int x, int y) {
		
        //マナゲージを描く
        int mana = this.tileEntity.getMana();
        int maxMana = this.tileEntity.getMaxMana();
		
		//マナのテクスチャバインド
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        
        //32 x 50
        //ゲージのy軸を計算(最大値50)
      	int gage = (int) Math.floor((double)mana / (double)maxMana * 50);
      	
      	
      	//下から上へ最大4回描画する
      	//　16*3　+　2分のゲージ
      	for (int idx = 0; idx < 4; idx++) {
      		
      		//ゲージの初期位置
      		int y_base = 50 - (16 * (idx + 1));
      		
      		//描画初期位置
      		int y_start = y_base;
      		//ゲージの長さ
      		int y_length = Math.min(16, gage - (16 * idx));
      		
      		//マイナスの場合は処理を終了
      		if (y_length < 0) {
      			break;
      		}
      		//残りゲージによるy軸計算
      		if (y_length < 16) {
      			y_start = y_start + (16 - y_length);
      		}
      		
      		//ノーマルは1回分描画
      		this.drawTexturedModalRect(x, y + y_start, manaWaterTextures, 16, y_length);
      	}		
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
        
        int x = this.xSize / 2 - this.fontRenderer.getStringWidth(text) / 2;
        int y = 6;
        
        //タイトル文字
        this.fontRenderer.drawString(text, x, y, 4210752);
        
        //マナゲージのツールチップ
        //********************************************************************************
        if (guiVisibleManaGage) {
        	//基準点
	        int xSize = this.guiWidth;
	        int ySize = this.guiHeight;
	        
	        //描画位置を計算
	        int tip_x = (this.width - xSize) / 2;
	        int tip_y = (this.height - ySize) / 2;
	        
	        //ゲージの位置を計算
	        tip_x += 16;
	        tip_y += 23;
	        
	        //drawGuiContainerForegroundLayerの場合はGUI上にないとだめのよう
	        //72 * 26
	        if (tip_x <= mouseX && mouseX <= tip_x + 16
	        		&& tip_y <= mouseY && mouseY <= tip_y + 50) {
	        	Integer mana = this.tileEntity.getMana();
	
	        	//GUIの左上からの位置
	            int xAxis = (mouseX - (width - xSize) / 2);
	    		int yAxis = (mouseY - (height - ySize) / 2);
	
	        	this.drawHoveringText(NumberFormat.getNumberInstance().format(mana) + " Mana", xAxis, yAxis);
	        }
        }
        //********************************************************************************

        RenderHelper.enableGUIStandardItemLighting();
    }
	
	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
	
    /**
     * Draws the screen and all the components in it.
     * 背景色黒とアイテムのツールチップを表示するために必要
     */
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }	
}