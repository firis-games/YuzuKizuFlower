package firis.yuzukizuflower.common.inventory;

import net.minecraft.inventory.IInventory;

public interface IScrollInventory extends IInventory {
	
	/**
	 * スクロールの最大ページを取得
	 * @return
	 */
	public int getScrollMaxPage();
	
	/**
	 * スクロールの現在ページを取得
	 * @return
	 */
	public int getScrollPage();
	
	
	/**
	 * スクロールページを設定
	 * @param page
	 */
	public void setScrollPage(int page);

	/**
	 * 対象スロットの使用可否
	 * @return
	 */
	public boolean isLockedScrollSlot(int index);
	
	/**
	 * 1行分のスロット数を取得
	 * @return
	 */
	public int getScrollSlotRowCount();
	
	/**
	 * 1ページ分のスロット数を取得する
	 * @return
	 */
	public int getScrollSlotPageCount();

}
