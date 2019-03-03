package firis.yuzukizuflower.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import firis.yuzukizuflower.common.container.YKContainerBoxedYuquarry;
import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.network.PacketTileBoxedFlower;
import firis.yuzukizuflower.common.tileentity.IYKTileGuiBoxedFlower;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedYuquarry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedYuquarry extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerBoxedYuquarry(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedYuquarry(iTeInv, playerInv));
		
		this.tileEntity = (IYKTileGuiBoxedFlower) iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_yuquarry.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_yuquarry.name";
		
		//GUI矢印と炎
		this.guiVisibleArrow = false;
		this.guiVisibleFire = false;
		
	}
	
	/**
	 * ボタンを追加する
	 */
	@Override
	public void initGui()
    {
        super.initGui();
        
        int x = (this.width - this.guiWidth) / 2;
        int y = (this.height - this.guiHeight) / 2;
        
        //ボタンの定義
        this.buttonList.add(new YKGuiItemIconButton(0, x + 142 , y + 20, "minecraft:blocks/flower_rose"));
        
        //シルクタッチボタン
        this.buttonList.add(new YKGuiItemIconButton(1, x + 142 , y + 42, "minecraft:items/diamond_pickaxe"));
        YKTileBoxedYuquarry tile = (YKTileBoxedYuquarry)this.tileEntity;
		((YKGuiItemIconButton)this.buttonList.get(1)).setSelected(tile.getSilkTouch());
    }	
	
    /**
     * ボタン表示処理を追加する
     */
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
		//ベースの処理
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		RenderHelper.disableStandardItemLighting();
		
        //ボタン表示用処理
        for (GuiButton guibutton : this.buttonList)
        {
            if (guibutton.isMouseOver())
            {
            	//ボタンの上にのってたら表示処理を行う
            	if (guibutton.id == 0) {
            		int xAxis = (mouseX - (width - this.guiWidth) / 2);
            		int yAxis = (mouseY - (height - this.guiHeight) / 2);
            		YKTileBoxedYuquarry tile = ((YKTileBoxedYuquarry)this.tileEntity);
            		
            		String modeName = tile.getFlowerMode().getName();
            		Integer width = tile.getFlowerMode().getRange();
            		
            		List<String> message = new ArrayList<String>();
            		message.add(modeName + "" + I18n.format("gui.boxed_yuquarry.mode.name"));
            		message.add(I18n.format("gui.boxed_yuquarry.range.name") + "：" + width.toString());
            		
            		this.drawHoveringText(message, xAxis, yAxis);
                    break;
            	} else if (guibutton.id == 1) {
            		int xAxis = (mouseX - (width - this.guiWidth) / 2);
            		int yAxis = (mouseY - (height - this.guiHeight) / 2);
            		YKTileBoxedYuquarry tile = ((YKTileBoxedYuquarry)this.tileEntity);
            		
            		boolean silkTouch = tile.getSilkTouch();
            		
            		List<String> message = new ArrayList<String>();
            		message.add(I18n.format("gui.boxed_yuquarry.silk_touch.name") 
            				+ (silkTouch ? "ON" : "OFF"));
            		this.drawHoveringText(message, xAxis, yAxis);
                    break;
            	}
            }
        }
        RenderHelper.enableGUIStandardItemLighting();
    }
	
	/**
     * ボタンクリック時の制御
     */
	@Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
    	super.actionPerformed(button);
    	
    	BlockPos pos = ((TileEntity)tileEntity).getPos();
    	
    	if (button.id == 0) {
    		//FlowerModeのトグル
    		NetworkHandler.network.sendToServer(
    				new PacketTileBoxedFlower.MessageTileBoxedFlower(pos, 0));
        }else if (button.id == 1) {
    		//シルクタッチモードのトグル
    		NetworkHandler.network.sendToServer(
    				new PacketTileBoxedFlower.MessageTileBoxedFlower(pos, 1));        	
        }
    }
	
	/**
	 * ボタンの描画制御を行う
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
		
		//ボタン制御
		for (GuiButton guibutton : this.buttonList)
        {
			if (guibutton.id == 1) {
				//シルクタッチボタン
				YKTileBoxedYuquarry tile = (YKTileBoxedYuquarry)this.tileEntity;
				((YKGuiItemIconButton)guibutton).setSelected(tile.getSilkTouch());
			}
        }
	}
}