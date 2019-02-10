package firis.yuzukizuflower.client.gui;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import firis.yuzukizuflower.common.container.YKContainerBoxedRannucarpus;
import firis.yuzukizuflower.common.network.MessageRannucarpus;
import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBaseManaPool;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedRannucarpus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedRannucarpus extends GuiContainer{

protected IInventory invTileEntity = null;
	
	public YKGuiContainerBoxedRannucarpus(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedRannucarpus(iTeInv, playerInv));
		
		invTileEntity = iTeInv;
		
		//GUIのサイズを設定
		//this.xSize = 176;
		//this.xSize = 166;
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		//テクスチャロード
		ResourceLocation GuiTextures = 
				new ResourceLocation("yuzukizuflower", "textures/gui/boxed_rannucarpus.png");
        this.mc.getTextureManager().bindTexture(GuiTextures);
        
        //画面へバインド（かまどのGUIサイズ）
        int xSize = 176;
        int ySize = 166;
        //描画位置を計算
        int x = (this.width - xSize) / 2;
        int y = (this.height - ySize) / 2;

        //画面へ描画
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        
        /*
        //炎の描画
        int k = this.getBurnLeftScaled(13);
        if (k != 0) {
        	this.drawTexturedModalRect(x + 80, y + 58 + 13 - k, 200, 12 - k, 14, k + 1);
        }
        */
        
        //マナゲージを描く
        int mana = ((YKTileBaseManaPool)invTileEntity).getMana();
        int maxMana = ((YKTileBaseManaPool)invTileEntity).getMaxMana();
        
        //マナゲージを描画する
        this.drawManaGage(x + 16, y + 23, mana, maxMana);
        
        //テクスチャを戻す
        this.mc.getTextureManager().bindTexture(GuiTextures);
        
        //メモリの描画
        this.drawTexturedModalRect(x + 16, y + 22, 176, 0, 10, 50);
        
        
        
        //this.drawTexturedModalRect(x + 79, y + 34, 176, 14, l + 1, 16);
	}

	protected TextureAtlasSprite manaWaterTexture = null;
	
	/**
	 * マナゲージを描画する
	 */
	public void drawManaGage(int x, int y, int mana, int maxMana) {
		
		//マナのテクスチャ取得
		if (manaWaterTexture == null) {
			manaWaterTexture = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("botania:blocks/mana_water");
		}
		
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
      		this.drawTexturedModalRect(x, y + y_start, manaWaterTexture, 16, y_length);
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
				new TextComponentTranslation("gui.boxed_rannucarpus.name", new Object[0]);
        String text = langtext.getFormattedText();
        
        int x = this.xSize / 2 - this.fontRenderer.getStringWidth(text) / 2;
        int y = 6;
        
        //タイトル文字
        this.fontRenderer.drawString(text, x, y, 4210752);
        
        //ボタン表示用処理
        for (GuiButton guibutton : this.buttonList)
        {
            if (guibutton.isMouseOver())
            {
            	//ボタンの上にのってたら表示処理を行う
            	if (guibutton.id == 0) {
                    //guibutton.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
            		//上のは使えないから使わない
            		int xAxis = (mouseX - (width - xSize) / 2);
            		int yAxis = (mouseY - (height - ySize) / 2);
            		YKTileBoxedRannucarpus tile = ((YKTileBoxedRannucarpus)invTileEntity);
            		
            		String modeName = tile.getModeName();
            		Integer width = tile.getAreaWidth();
            		Integer height = tile.getAreaHeight();
            		
            		List<String> message = new ArrayList<String>();
            		message.add(modeName + "モード");
            		message.add("範囲：" + width.toString());
            		message.add("高さ：" + height.toString());
            		
            		this.drawHoveringText(message, xAxis, yAxis);
                    break;
            	}
            }
        }
        
        //ツールチップはこっちにする？
        //********************************************************************************
        //独自ツールチップを表示したい
        //画面へバインド（かまどのGUIサイズ）
        int xSize = 176;
        int ySize = 166;
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
        	Integer mana = ((YKTileBaseManaPool)invTileEntity).getMana();

        	//GUIの左上からの位置
            int xAxis = (mouseX - (width - xSize) / 2);
    		int yAxis = (mouseY - (height - ySize) / 2);
    		
    		/*
    		//ゲージ位置を調整してるからだめなのか
            int xAxis1 = mouseX - tip_x;
    		int yAxis1 = mouseY - tip_y;
    		*/
    		
        	this.drawHoveringText(NumberFormat.getNumberInstance().format(mana) + " Mana", xAxis, yAxis);
        }
        //********************************************************************************

        RenderHelper.enableGUIStandardItemLighting();
    }
	
	
	/**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
	
	@Override
	public void updateScreen()
	{
		//なんに使うかはあとで調べる
		//メカニズムではボタンの表示表示の描画制御をやってるよう
		super.updateScreen();
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
        
        //ツールチップはdrawGuiContainerForegroundLayerへ移動
        /*
        //独自ツールチップを表示したい
        //画面へバインド（かまどのGUIサイズ）
        int xSize = 176;
        int ySize = 166;
        //描画位置を計算
        int x = (this.width - xSize) / 2;
        int y = (this.height - ySize) / 2;
        
        //ゲージの位置を計算
        x += 16;
        y += 23;
        
        //72 * 26
        if (x <= mouseX && mouseX <= x + 16
        		&& y <= mouseY && mouseY <= y + 50) {
        	Integer mana = ((YKTileBaseManaPool)invTileEntity).getMana();
        	this.drawHoveringText(NumberFormat.getNumberInstance().format(mana) + " Mana", mouseX, mouseY);
        }
        */
    }
	
	//****************************************************************************************************
	
	//ここからボタン系の処理を追加する
	
	protected GuiButton buttonMode;
	
	@Override
	public void initGui()
    {
        super.initGui();
        
        int x = (this.width - xSize) / 2;
        int y = (this.height - ySize) / 2;
        
        //ボタンの定義
        this.buttonMode = new YKGuiButton(0, x + 135 , y + 20, "モード");
        
        this.buttonList.add(buttonMode);
        
        
    }
	
	
	/**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
	@Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
    	super.actionPerformed(button);
    	
    	
    	if (button.id == 0)
        {
    		/*
    		YKTileBoxedRannucarpus tile = ((YKTileBoxedRannucarpus)invTileEntity);
    		tile.changeMode();
    		//Packet<?> pkt = tile.getUpdatePacket();
    		
    		Packet<?> pkt =  new CPacketClickWindow();
    		
    		
    		
    		this.mc.player.connection.sendPacket(pkt);
    		*/
    		
    		//ネットワーク新方式
    		NetworkHandler.network.sendToServer(new MessageRannucarpus(((YKTileBaseManaPool)invTileEntity).getWorld(), 
    				((YKTileBaseManaPool)invTileEntity).getPos()));
    		
        }
    	
    }
	
}
