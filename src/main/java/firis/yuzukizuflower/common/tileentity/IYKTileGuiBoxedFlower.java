package firis.yuzukizuflower.common.tileentity;

public interface IYKTileGuiBoxedFlower {

	/**
	 * マナの最大容量取得
	 * @return
	 */
	public int getMaxMana();
	
	/**
	 * マナの最大容量取得
	 * @return
	 */
	public int getMana();
	
	/**
	 * 処理終了
	 * @return
	 */
	public int getMaxTimer();
	
	/**
	 * 処理時間
	 * @return
	 */
	public int getTimer();
	
}
