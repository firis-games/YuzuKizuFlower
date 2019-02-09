package firis.yuzukizuflower.common.botania;

import net.minecraft.item.ItemStack;

/**
 * botaniaのマナを利用した変換系お花のレシピで共通化して扱うためのクラス
 * @author computer
 *
 */
public class ManaRecipe {
	
	/**
	 * コンストラクタ
	 */
	public ManaRecipe(ItemStack input, ItemStack output, int mana, int time) {
		this.inputItemStack = input;
		this.outputItemStack = output;
		this.mana = mana;
		this.time = time;
	}
	
	/**
	 * 必要なマナの量
	 */
	protected int mana = 0;
	
	/**
	 * 必要な時間
	 */
	protected int time = 0;
	
	/**
	 * 素材アイテム
	 */
	protected ItemStack inputItemStack = null;
	
	/**
	 * 変換アイテム
	 */
	protected ItemStack outputItemStack = null;
	
	
	public int getMana() {
		return this.mana;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public ItemStack getInputItemStack() {
		return this.inputItemStack.copy();
	}
	
	public ItemStack getOutputItemStack() {
		return this.outputItemStack.copy();
	}
	
}
