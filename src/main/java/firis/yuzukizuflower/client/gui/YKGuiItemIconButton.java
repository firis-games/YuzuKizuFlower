package firis.yuzukizuflower.client.gui;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class YKGuiItemIconButton extends GuiButton {

    protected static final ResourceLocation GUI_PARTS_TEXTURES = new ResourceLocation(YuzuKizuFlower.MODID, "textures/gui/gui_parts.png");
    
    protected String iconTexture = "";
    
    /**
     * コンストラクタ
     * @param buttonId
     * @param x
     * @param y
     * @param iconTexture
     */
    public YKGuiItemIconButton(int buttonId, int x, int y, String iconTexture)
    {
    	super(buttonId, x, y, 20, 20, "");
    	this.iconTexture = iconTexture;
    }
    protected YKGuiItemIconButton(int buttonId, int x, int y, int widthIn, int heightIn, String iconTexture) {
    	super(buttonId, x, y, 20, 20, "");
       	this.iconTexture = iconTexture;
    }
    
    /**
     * ボタンを描画
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
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
            //今回は決めうちでやるので独自実装
            this.drawTexturedModalRect(this.x, this.y, btn_textures_x, btn_textures_y, this.width, this.height);
            
            //icon表示
            TextureAtlasSprite textureSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(this.iconTexture);
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.drawTexturedModalRect(this.x + 2, this.y + 2, textureSprite, 16, 16);
            
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}
