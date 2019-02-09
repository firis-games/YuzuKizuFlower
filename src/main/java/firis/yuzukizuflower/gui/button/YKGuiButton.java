package firis.yuzukizuflower.gui.button;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class YKGuiButton extends GuiButton {

    protected static final ResourceLocation GUI_PARTS_TEXTURES = new ResourceLocation(YuzuKizuFlower.MODID, "textures/gui/gui_parts.png");
    
    /**
     * ボタン生成
     * @param buttonId
     * @param x
     * @param y
     * @param buttonText
     */
    public YKGuiButton(int buttonId, int x, int y, String buttonText)
    {
    	super(buttonId, x, y, 20, 20, buttonText);
    }
    
    protected YKGuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
    	super(buttonId, x, y, 20, 20, buttonText);
    }
    
    /**
     * ボタンを描画
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(GUI_PARTS_TEXTURES);
            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            
            //テクスチャの位置を計算
            int btn_textures_x = 0 + i * 20;
            int btn_textures_y = 0;
            
            //左半分と右半分を連結して長い長さも対応してるよう
            //今回は決めうちでやるので独自でやる
            this.drawTexturedModalRect(this.x, this.y, btn_textures_x, btn_textures_y, this.width, this.height);
            
            //レッドローズ
            TextureAtlasSprite textureSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("minecraft:blocks/flower_rose");
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            
            this.drawTexturedModalRect(this.x + 2, this.y + 2, textureSprite, 16, 16);
            
            
            
            /*
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            */
            
            this.mouseDragged(mc, mouseX, mouseY);
            /*
            int j = 14737632;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
            */
        }
    }
}
