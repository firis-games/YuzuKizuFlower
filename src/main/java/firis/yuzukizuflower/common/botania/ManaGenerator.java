package firis.yuzukizuflower.common.botania;

import net.minecraft.item.ItemStack;

/**
 * botaniaのマナを利用した発電系お花のレシピで共通化して扱うためのクラス
 * @author computer
 *
 */
public class ManaGenerator {

	/**
	 * コンストラクタ
	 */
	public ManaGenerator(ItemStack fuel, int mana, int time, int cycle) {
		this.fuelItemStack = fuel;
		this.mana = mana;
		this.time = time;
		this.cycle = cycle;
	}
	
	/**
	 * 1回あたりの発生マナ
	 */
	protected int mana = 0;
	
	/**
	 * 燃焼時間
	 */
	protected int time = 0;
	
	/**
	 * 1回あたりの単位（tick）
	 */
	protected int cycle = 0;
	
	/**
	 * 燃料アイテム
	 */
	protected ItemStack fuelItemStack = null;
	
	public int getMana() {
		return this.mana;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public int getCycle() {
		return this.cycle;
	}
	
	public ItemStack getFuelItemStack() {
		return this.fuelItemStack.copy();
	}
	
}
