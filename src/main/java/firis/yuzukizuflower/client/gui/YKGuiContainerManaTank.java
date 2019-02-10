package firis.yuzukizuflower.client.gui;

import java.text.NumberFormat;

import firis.yuzukizuflower.common.container.YKContainerManaTank;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedEndoflame;
import firis.yuzukizuflower.common.tileentity.YKTileManaTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerManaTank extends GuiContainer{

	protected IInventory invTileEntity = null;
	
	public YKGuiContainerManaTank(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerManaTank(iTeInv, playerInv));
		
		invTileEntity = iTeInv;
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		//テクスチャロード
		ResourceLocation GuiTextures = 
				new ResourceLocation("yuzukizuflower", "textures/gui/mana_tank.png");
        this.mc.getTextureManager().bindTexture(GuiTextures);
        
        //画面へバインド（かまどのGUIサイズ）
        int xSize = 176;
        int ySize = 166;
        //描画位置を計算
        int x = (this.width - xSize) / 2;
        int y = (this.height - ySize) / 2;

        //画面へ描画
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
                
        int mana = ((YKTileManaTank)invTileEntity).getMana();
        int maxMana = ((YKTileManaTank)invTileEntity).getMaxMana();
        
        //マナゲージを描画する
        this.drawManaGage(x + 72, y + 26, mana, maxMana);
        
        //テクスチャを戻す
        this.mc.getTextureManager().bindTexture(GuiTextures);

        //メモリの描画
        this.drawTexturedModalRect(x + 72, y + 25, 176, 0, 10, 50);
        
        /*
        //炎の描画
        int k = this.getBurnLeftScaled(13);
        if (k != 0) {
        	this.drawTexturedModalRect(x + 80, y + 58 + 13 - k, 176, 12 - k, 14, k + 1);
        }
        */
        
        //this.drawTexturedModalRect(x + 79, y + 34, 176, 14, l + 1, 16);
	}
	
	/**
	 * マナウォーターのテクスチャ情報
	 */
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
      		
      		//左と右の2回描画する(32ドット分)
      		this.drawTexturedModalRect(x, y + y_start, manaWaterTexture, 16, y_length);
      		this.drawTexturedModalRect(x + 16, y + y_start, manaWaterTexture, 16, y_length);
      	}
      	
      	
      	/*
      	//this.drawTexturedModalRect(x + 76, y + 19, texture, 24, 16);
        //this.drawTexturedModalRect(x + 76, y + 19, texture, 16, 8);
        this.drawTexturedModalRect(x + 72, y + 26 + 8, manaWaterTexture, 16, 8);
        this.drawTexturedModalRect(x + 72, y + 26 + 16, manaWaterTexture, 16, 16);
        this.drawTexturedModalRect(x + 72, y + 26 + 16*2, manaWaterTexture, 16, 16);
        this.drawTexturedModalRect(x + 72, y + 26 + 16*3, manaWaterTexture, 16, 2);

        //this.drawTexturedModalRect(x + 76+16, y + 19, texture, 8, 8);
        this.drawTexturedModalRect(x + 72+16, y + 26 + 8, manaWaterTexture, 16, 8);
        this.drawTexturedModalRect(x + 72+16, y + 26 + 16, manaWaterTexture, 16, 16);
        this.drawTexturedModalRect(x + 72+16, y + 26 + 16*2, manaWaterTexture, 16, 16);
        this.drawTexturedModalRect(x + 72+16, y + 26 + 16*3, manaWaterTexture, 16, 2);
        */
		
	}
	
	public enum FluidType
	{
		STILL,
		FLOWING
	}
	
	public TextureAtlasSprite getFluidTexture() 
	//public TextureAtlasSprite getFluidTexture(Fluid fluid, FluidType type) 
	{
		
		Fluid fluid = FluidRegistry.getFluid("lava");
		FluidType type = FluidType.STILL;
		
		if(fluid == null || type == null)
		{
			//return missingIcon;
		}
		
		ResourceLocation spriteLocation;
		if (type == FluidType.STILL){
			spriteLocation = fluid.getStill();
		} else {
			spriteLocation = fluid.getFlowing();
		}
		
		String manaWater = "botania:blocks/mana_water";
		//manaWater = spriteLocation.toString();
		
		//TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(spriteLocation.toString());
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(manaWater);

//		TextureAtlasSprite sprite = texMap.getTextureExtry(spriteLocation.toString());
		
		//return sprite != null ? sprite : missingIcon;
		
		return sprite;
	}
	
    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
		//xx_xx.langから取得
		TextComponentTranslation langtext = 
				new TextComponentTranslation("gui.mana_tank.name", new Object[0]);
        String text = langtext.getFormattedText();
        
        int x = this.xSize / 2 - this.fontRenderer.getStringWidth(text) / 2;
        int y = 6;
        
        //タイトル文字
        this.fontRenderer.drawString(text, x, y, 4210752);
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
        
        //独自ツールチップを表示したい
        //画面へバインド（かまどのGUIサイズ）
        int xSize = 176;
        int ySize = 166;
        //描画位置を計算
        int x = (this.width - xSize) / 2;
        int y = (this.height - ySize) / 2;
        
        //ゲージの位置を計算
        x += 72;
        y += 26;
        
        //72 * 26
        if (x <= mouseX && mouseX <= x + 32
        		&& y <= mouseY && mouseY <= y + 50) {
        	Integer mana = ((YKTileManaTank)invTileEntity).getMana();
        	this.drawHoveringText(NumberFormat.getNumberInstance().format(mana) + " Mana", mouseX, mouseY);
        }
        
    }
}
