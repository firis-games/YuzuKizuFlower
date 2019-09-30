package firis.yuzukizuflower.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import firis.yuzukizuflower.client.gui.parts.YKGuiItemIconButton;
import firis.yuzukizuflower.common.container.YKContainerBoxedRannuncarpus;
import firis.yuzukizuflower.common.inventory.BoxedFieldConst;
import firis.yuzukizuflower.common.inventory.ClientInventory;
import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.network.PacketTileBoxedFlower;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedRannuncarpus.FlowerMode;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class YKGuiContainerBoxedRannuncarpus extends YKGuiContainerBaseBoxedFuncFlower{
	
	/**
	 * 
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKGuiContainerBoxedRannuncarpus(IInventory iTeInv, IInventory playerInv) {
		
		super(new YKContainerBoxedRannuncarpus(iTeInv, playerInv));
		
		this.tileEntity = iTeInv;
		
		//GUIテクスチャ
		this.guiTextures = new ResourceLocation("yuzukizuflower", "textures/gui/boxed_rannuncarpus.png");
		
		//GUIタイトル
		this.guiTitle = "gui.boxed_rannuncarpus.name";
		
		//GUI矢印と炎とマナゲージ
		this.guiVisibleArrow = false;
		this.guiVisibleFire = false;
		this.guiVisibleManaGage = false;
		
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
        this.buttonList.add(new YKGuiItemIconButton(0, x + 135 , y + 20, "minecraft:blocks/flower_rose"));
        
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
            		
            		FlowerMode flowerMode = FlowerMode.getById(this.tileEntity.getField(BoxedFieldConst.MODE));
            		
            		String modeName = flowerMode.getName();
            		Integer width = flowerMode.getRange();
            		Integer height = flowerMode.getHeight();
            		
            		List<String> message = new ArrayList<String>();
            		message.add(modeName 
            				+ I18n.format("gui.boxed_rannuncarpus.mode.name"));
            		message.add(I18n.format("gui.boxed_rannuncarpus.range.name")  
            				+ "：" + width.toString());
            		message.add(I18n.format("gui.boxed_rannuncarpus.height.name")
            				+ "：" + height.toString());
            		
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
    	if (button.id == 0)
        {
    		BlockPos pos = ((ClientInventory)tileEntity).getPos();
    		//ネットワーク新方式
    		NetworkHandler.network.sendToServer(
    				new PacketTileBoxedFlower.MessageTileBoxedFlower(pos, 0));
    		
        }
    	
    }
}