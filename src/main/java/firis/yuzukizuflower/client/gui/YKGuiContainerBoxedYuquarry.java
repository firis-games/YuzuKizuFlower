package firis.yuzukizuflower.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import firis.yuzukizuflower.client.gui.parts.YKGuiItemIconButton;
import firis.yuzukizuflower.common.container.YKContainerBoxedYuquarry;
import firis.yuzukizuflower.common.inventory.BoxedFieldConst;
import firis.yuzukizuflower.common.inventory.ClientInventory;
import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.network.PacketTileBoxedFlower;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedYuquarry.FlowerMode;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
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
		
		this.tileEntity = iTeInv;
		
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
        boolean isSilk = this.tileEntity.getField(BoxedFieldConst.SILK_TOUCH) == 1 ? true : false;
		((YKGuiItemIconButton)this.buttonList.get(1)).setSelected(isSilk);
    }	
	
    /**
     * ボタン表示処理を追加する
     */
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
		//名称変更
		boolean isFlatMode = this.tileEntity.getField(BoxedFieldConst.FLAT_MODE) == 1 ? true : false;
		if (!isFlatMode) {
			this.guiTitle = "gui.boxed_yuquarry.name";			
		} else {
			this.guiTitle = "gui.boxed_yuquarry.flat.name";
		}
		
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
            		
            		FlowerMode flowerMode = FlowerMode.getById(this.tileEntity.getField(BoxedFieldConst.MODE));
            		
            		String modeName = flowerMode.getName();
            		Integer width = flowerMode.getRange();
            		
            		List<String> message = new ArrayList<String>();
            		message.add(modeName + "" + I18n.format("gui.boxed_yuquarry.mode.name"));
            		message.add(I18n.format("gui.boxed_yuquarry.range.name") + "：" + width.toString());
            		
            		this.drawHoveringText(message, xAxis, yAxis);
                    break;
            	} else if (guibutton.id == 1) {
            		int xAxis = (mouseX - (width - this.guiWidth) / 2);
            		int yAxis = (mouseY - (height - this.guiHeight) / 2);
            		
            		boolean silkTouch = this.tileEntity.getField(BoxedFieldConst.SILK_TOUCH) == 1 ? true : false;
            		
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
    	
    	BlockPos pos = ((ClientInventory)tileEntity).getPos();
    	
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
				boolean isSilk = this.tileEntity.getField(BoxedFieldConst.SILK_TOUCH) == 1 ? true : false;
				((YKGuiItemIconButton)guibutton).setSelected(isSilk);
			}
        }
	}
}