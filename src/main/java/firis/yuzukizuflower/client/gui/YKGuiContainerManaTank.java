package firis.yuzukizuflower.client.gui;

import java.text.NumberFormat;

import firis.yuzukizuflower.common.container.YKContainerManaTank;
import firis.yuzukizuflower.common.inventory.BoxedFieldConst;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerManaTank extends GuiContainer {

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
	 * GUIタイトル
	 */
	protected String guiTitle = "";
	
	/**
	 * YKTileBaseManaPool
	 * @param inventorySlotsIn
	 */
	protected IInventory tileEntity = null;

	/**
	 * コンストラクタ
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerManaTank(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerManaTank(iTeInv, playerInv));
		
		tileEntity = iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/mana_tank.png");
		
		Integer metadata = tileEntity.getField(BoxedFieldConst.TIER);
		
		//GUIタイトル
		this.guiTitle = "gui.mana_tank_" + metadata.toString() +  ".name";
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
        
	    //マナゲージの描画
        this.drawManaGage(x + 72, y + 26);
        
        //テクスチャを戻す
        this.mc.getTextureManager().bindTexture(guiTextures);

        //メモリの描画
        this.drawTexturedModalRect(x + 72, y + 25, 176, 0, 10, 50);        
	}
	
	/**
	 * マナゲージを描画する
	 */
	public void drawManaGage(int x, int y) {
		
		//マナゲージを描く
        int mana = this.tileEntity.getField(BoxedFieldConst.MANA);
        int maxMana = this.tileEntity.getField(BoxedFieldConst.MAX_MANA);
        
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
      		
      		//左と右の2回描画する(32ドット分)
      		this.drawTexturedModalRect(x, y + y_start, manaWaterTextures, 16, y_length);
      		this.drawTexturedModalRect(x + 16, y + y_start, manaWaterTextures, 16, y_length);
      	}
      	
	}
	
	
    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
		//xx_xx.langから取得
		TextComponentTranslation langtext = 
				new TextComponentTranslation(this.guiTitle, new Object[0]);
        String text = langtext.getFormattedText();
        
        int x = this.xSize / 2 - this.fontRenderer.getStringWidth(text) / 2;
        int y = 6;
        
        //タイトル文字
        this.fontRenderer.drawString(text, x, y, 4210752);
        
        
        //基準点
        int xSize = this.guiWidth;
        int ySize = this.guiHeight;
        
        //描画位置を計算
        int tip_x = (this.width - xSize) / 2;
        int tip_y = (this.height - ySize) / 2;
        
        //ゲージの位置を計算
        tip_x += 72;
        tip_y += 26;
        
        //drawGuiContainerForegroundLayerの場合はGUI上にないとだめのよう
        //72 * 26
        if (tip_x <= mouseX && mouseX <= tip_x + 32
        		&& tip_y <= mouseY && mouseY <= tip_y + 50) {
        	Integer mana = this.tileEntity.getField(BoxedFieldConst.MANA);

        	//GUIの左上からの位置
            int xAxis = (mouseX - (width - xSize) / 2);
    		int yAxis = (mouseY - (height - ySize) / 2);

        	this.drawHoveringText(NumberFormat.getNumberInstance().format(mana) + " Mana", xAxis, yAxis);
        }
    }
	
	
	/**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
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
