package firis.yuzukizuflower.container;

import firis.yuzukizuflower.tileentity.YKTileBoxedPureDaisy;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedPureDaisy extends GuiContainer{

	protected IInventory invTileEntity = null;
	
	public YKGuiContainerBoxedPureDaisy(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedPureDaisy(iTeInv, playerInv));
		
		invTileEntity = iTeInv;
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		//テクスチャロード
		ResourceLocation GuiTextures = 
				new ResourceLocation("yuzukizuflower", "textures/gui/boxed_pure_daisy.png");
        this.mc.getTextureManager().bindTexture(GuiTextures);
        
        //画面へバインド（かまどのGUIサイズ）
        int xSize = 176;
        int ySize = 166;
        //描画位置を計算
        int x = (this.width - xSize) / 2;
        int y = (this.height - ySize) / 2;

        //画面へ描画
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        //矢印の描画
        int l = this.getProgressScaled(24);
        this.drawTexturedModalRect(x + 79, y + 34, 176, 14, l + 1, 16);
        
	}
	
	/**
	 * 矢印制御用
	 * @param pixels
	 * @return
	 */
	private int getProgressScaled(int pixels)
    {
    	//int i = this.tileFurnace.getField(2);
        //int j = this.tileFurnace.getField(3);
        
        int i = ((YKTileBoxedPureDaisy) this.invTileEntity).timer;
        int j = ((YKTileBoxedPureDaisy) this.invTileEntity).maxTimer;
        
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }
	
    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
		//xx_xx.langから取得
		TextComponentTranslation langtext = 
				new TextComponentTranslation("gui.boxed_pure_daisy.name", new Object[0]);
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
    }
}
