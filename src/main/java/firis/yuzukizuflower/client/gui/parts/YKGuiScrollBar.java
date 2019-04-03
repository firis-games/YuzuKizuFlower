package firis.yuzukizuflower.client.gui.parts;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * スクロールバー制御
 */
@SideOnly(Side.CLIENT)
public class YKGuiScrollBar {

	protected GuiContainer guiContainer;
	protected static final ResourceLocation GUI_PARTS_TEXTURES = new ResourceLocation(YuzuKizuFlower.MODID,
			"textures/gui/gui_parts.png");

	// スクロールバーのテクスチャ座標
	protected final int scroll_texture_on_x = 0;
	protected final int scroll_texture_on_y = 20;

	// スクロールバーのサイズ
	protected final int scroll_size_width = 12;
	protected final int scroll_size_height = 15;

	// スクロールバー基準点
	protected int scroll_base_x;
	protected int scroll_base_y;

	// スクロールバーの座標
	protected int scroll_height;
	
	// スクロールバー座標位置
	protected float scroll_coord_y_rate;

	// スクロールバーのページ数
	protected int scroll_max_page;
	
	// スクロールバーの現在ページ数
	protected int scroll_page_idx;

	/**
	 * 
	 * @param guiContainer
	 *            描画するGUI
	 * @param x
	 *            GUI上の描画位置
	 * @param y
	 *            GUI上の描画位置
	 * @param height
	 *            GUI上の高さ
	 */
	public YKGuiScrollBar(GuiContainer guiContainer, int x, int y, int height, int page) {
		this.guiContainer = guiContainer;
		this.scroll_base_x = x;
		this.scroll_base_y = y;

		// ページを計算する
		this.scroll_height = height - this.scroll_size_height;
		this.scroll_coord_y_rate = 0;
		this.scroll_max_page = page;
		this.scroll_page_idx = 0;
	}

	/**
	 * スクロールバーの描画
	 */
	public void drawScrollBar() {

		// テクスチャバインド
		this.guiContainer.mc.getTextureManager().bindTexture(GUI_PARTS_TEXTURES);

		// 描画位置を計算
		float x = guiContainer.getGuiLeft() + this.scroll_base_x;
		float y = guiContainer.getGuiTop() + this.scroll_base_y;
		
		//スクロールバーの位置を計算
		y += this.scroll_coord_y_rate * (float)this.scroll_height;

		// スクロールバーの描画
		this.guiContainer.drawTexturedModalRect(x, y, scroll_texture_on_x, scroll_texture_on_y, scroll_size_width,
				scroll_size_height);

	}

	/**
	 * マウス移動イベント
	 */
	public void handleMouseInput() {

		int dwheel = Mouse.getEventDWheel();
		if (dwheel != 0) {
			
			boolean nextFlg = false;
			if (0 < dwheel) {
				// 上方向へ
				nextFlg = false;
			} else {
				// 下方向へ
				nextFlg = true;
			}
			this.setScrollTo(this.scroll_coord_y_rate, nextFlg);

			/*
			// 1スクロールあたりのサイズ
			int scrollPageY = (int) Math.floor((double) (this.scroll_height) / (double) this.page);

			// スクロールの移動座標リストを作成
			List<Integer> scrollPageYList = new ArrayList<Integer>();
			for (int i = 0; i <= this.page; i++) {
				int page = scrollPageY * i;
				if (this.page == i) {
					page = this.scroll_height;
				}
				scrollPageYList.add(page);
			}

			int scroll_idx = scrollPageYList.indexOf(this.scroll_y);
			scroll_idx = scroll_idx == -1 ? 0 : scroll_idx;

			if (0 < dwheel) {
				// 上方向へ
				scroll_idx = Math.max(0, scroll_idx - 1);
				this.scroll_y = scrollPageYList.get(scroll_idx);
			} else {
				// 下方向へ
				scroll_idx = Math.min(scrollPageYList.size() - 1, scroll_idx + 1);
				this.scroll_y = scrollPageYList.get(scroll_idx);
			}
			*/
		}
	}

	/**
	 * スクロール用マウス判定
	 */
	protected boolean leftMouceClicked = false;

	/**
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @param mouseButton
	 */
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		// 左クリックの場合のみ実施
		if (mouseButton != 0)
			return;

		int x_str = guiContainer.getGuiLeft() + this.scroll_base_x;
		int y_str = guiContainer.getGuiTop() + this.scroll_base_y;
		int x_end = guiContainer.getGuiLeft() + this.scroll_base_x + this.scroll_size_width;
		int y_end = guiContainer.getGuiTop() + this.scroll_base_y + this.scroll_size_height + this.scroll_height;

		// スクロールバーの範囲内
		if (x_str <= mouseX && mouseX <= x_end && y_str <= mouseY && mouseY <= y_end) {
			ScrollToMouse(mouseY - y_str);

			// マウスクリック状態
			leftMouceClicked = true;
		}
	}

	/**
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @param state
	 */
	public void mouseReleased(int mouseX, int mouseY, int state) {

		// 左クリックの場合のみ実施
		if (state != 0)
			return;

		// マウスクリック状態
		leftMouceClicked = false;
	}

	/**
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @param clickedMouseButton
	 * @param timeSinceLastClick
	 */
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

		// 左クリックの場合のみ実施
		if (clickedMouseButton != 0 || !leftMouceClicked)
			return;
		
		//スクロールの位置からRateを計算
		int scrollY = guiContainer.getGuiTop() + this.scroll_base_y;
		scrollY = Math.max(0, mouseY - scrollY);
		scrollY = Math.min(this.scroll_height + this.scroll_size_height, scrollY);
		ScrollToMouse(scrollY);
	}

	/**
	 * スクロールバーをYの位置へ移動する
	 * 
	 * @param scrollY
	 */
	protected void ScrollToMouse(int scrollY) {

		// スクロールの位置 0.0F-0.1F
		float scrollRateY = 0.0F;
		scrollRateY = (float) scrollY / (float) this.scroll_height;
		scrollRateY = (float) (Math.floor(scrollRateY * 1000F) / 1000F);
		
		//スクロール位置を設定する
		this.setScrollTo(scrollRateY, false);
	}
	
	
	/**
	 * スクロールの位置を設定する
	 * @param work
	 * @param next
	 */
	protected void setScrollTo(float work, boolean next) {
		
		//スクロールレートを設定
		this.scroll_coord_y_rate = this.calScrollRate(work, next);
		
		if (this.guiContainer instanceof IYKGuiScrollBarChanged) {
			IYKGuiScrollBarChanged inf = (IYKGuiScrollBarChanged) this.guiContainer;
			
			inf.onScrollChanged(this.scroll_page_idx);
		}
		
	}
	
	/**
	 * ScrollRateを計算する
	 * @param rate
	 * @param next workの次の値か前の値かを判断
	 * @return
	 */
	private float calScrollRate(float work, boolean next) {
		
		if (!next && work <= 0.0F) {
			return 0.0F;
		}
		if (next && 1.0F <= work) {
			return 1.0F;
		}
		
		//位置のRate計算
		float scrollPageRateY = Math.round((1F / (float)this.scroll_max_page) * 1000F) / 1000F;
		List<Float> scrollRateYList = new ArrayList<Float>();
		for (float i = 0; i < this.scroll_max_page; i++) {
			scrollRateYList.add(Math.round(scrollPageRateY * i * 1000F) / 1000F);
		}
		scrollRateYList.add(1F);
		
		//逆順で検索
		int scroll_idx = 0;
		if (next) {
			//work値より大きく一番近い値を取得
			for (int i = 0; 0 < scrollRateYList.size(); i++) {
				double rate = scrollRateYList.get(i);
				if (work < rate) {
					scroll_idx = i;
					break;
				}
			}
		} else {
			//work値より小さく一番近い値を取得
			for (int i = scrollRateYList.size() - 1; 0 <= i; i--) {
				double rate = scrollRateYList.get(i);
				if (rate < work) {
					scroll_idx = i;
					break;
				}
			}
		}
		this.scroll_page_idx = scroll_idx;
		return scrollRateYList.get(scroll_idx);
	}
	
	/**
	 * スクロールが変更された場合に呼び出される
	 * @author computer
	 *
	 */
	public interface IYKGuiScrollBarChanged {
		/**
		 * スクロールが変更された場合に呼び出される
		 * @param page
		 */
		public void onScrollChanged(int page);
		
	}

}
