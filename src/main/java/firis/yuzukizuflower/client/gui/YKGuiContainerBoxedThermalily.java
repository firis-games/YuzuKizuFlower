package firis.yuzukizuflower.client.gui;

import java.text.NumberFormat;

import firis.yuzukizuflower.common.container.YKContainerBoxedThermalily;
import firis.yuzukizuflower.common.inventory.BoxedFieldConst;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedThermalily extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerBoxedThermalily(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedThermalily(iTeInv, playerInv));
		
		this.tileEntity = iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_thermalily.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_thermalily.name";
		
		//GUI矢印
		this.guiVisibleArrow = false;
		
		//GUI炎マーク
		this.guiVisibleFire = true;
		this.guiFireX = 80;
		this.guiFireY = 58;
	}
	

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		// 画面へバインド（かまどのGUIサイズ）
		int xSize = this.guiWidth;
		int ySize = this.guiHeight;

		// 描画位置を計算
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		
		//溶岩ゲージを描画する
        this.drawLavaGage(x + 100, y + 23);
        
        //テクスチャを戻す
        this.mc.getTextureManager().bindTexture(guiTextures);
        
        //メモリの描画
        this.drawTexturedModalRect(x + 100, y + 22, guiManaGageLayerX, guiManaGageLayerY, 10, 50);
		
	}
	
	/**
	 * テクスチャ
	 */
	protected TextureAtlasSprite lavaTextures = null;
	/**
	 * 溶岩を描画する
	 */
	public void drawLavaGage(int x, int y) {
	
		//テクスチャを設定
		if (lavaTextures == null) {
			Fluid fluid = FluidRegistry.getFluid("lava");
			ResourceLocation fluidLocation = fluid.getStill();
			
			this.lavaTextures = Minecraft.getMinecraft().getTextureMapBlocks()
					.getTextureExtry(fluidLocation.toString());
		}
		
        //ゲージを描く
        int mana = this.tileEntity.getField(BoxedFieldConst.FLUID);
        int maxMana = this.tileEntity.getField(BoxedFieldConst.MAX_FLUID);

		
		//テクスチャバインド
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
      		this.drawTexturedModalRect(x, y + y_start, this.lavaTextures, 16, y_length);
      	}		
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
		
		RenderHelper.disableStandardItemLighting();
		
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		//液体ゲージtooltip
        this.drawLavaTooltip(mouseX, mouseY);
        
        RenderHelper.enableGUIStandardItemLighting();
        
    }
	/**
	 * 液体ゲージのtooltip表示
	 * @param mouseX
	 * @param mouseY
	 */
	protected void drawLavaTooltip(int mouseX, int mouseY) {
    	//基準点
        int xSize = this.guiWidth;
        int ySize = this.guiHeight;
        
        //描画位置を計算
        int tip_x = (this.width - xSize) / 2;
        int tip_y = (this.height - ySize) / 2;
        
        //ゲージの位置を計算
        tip_x += 100;
        tip_y += 22;
        
        //drawGuiContainerForegroundLayerの場合はGUI上にないとだめのよう
        //72 * 26
        if (tip_x <= mouseX && mouseX <= tip_x + 16
        		&& tip_y <= mouseY && mouseY <= tip_y + 50) {
        	Integer mana = this.tileEntity.getField(BoxedFieldConst.FLUID);
            
        	//GUIの左上からの位置
            int xAxis = (mouseX - (width - xSize) / 2);
    		int yAxis = (mouseY - (height - ySize) / 2);

        	this.drawHoveringText(NumberFormat.getNumberInstance().format(mana) + "mb", xAxis, yAxis);
        }
	}
}