package firis.yuzukizuflower.common.botania;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GeneratorEntropinnyum implements IManaGenerator {
	
	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaGenerator getMatchesRecipe(@Nonnull List<ItemStack> stackList) {
		
		ManaGenerator generator = null;
		
		if (stackList.size() <= 0) {
			return generator;
		}
		
		ItemStack stack = stackList.get(0);
		
		if (!this.isMatchesItemStackSlot(stack)) {
			return generator;
		}
		
		//エントロピウムは1回6500マナ
		//本来はクールタイムなし

		//1秒に1回650マナ生産する(合計10回)
		
		//5秒
		int time = 20 * 10;
		
		int cycle = time / 10;
		
		int mana = 6500 / 10;
		
		ItemStack fuel = stack.copy();
		fuel.setCount(1);
		
		//レシピへ変換
		generator = new ManaGenerator(
				fuel,
				mana,
				time,
				cycle);
		
		return generator;
	}
	
	/**
	 * スロットのチェック処理
	 */
	public boolean isMatchesItemStackSlot(@Nonnull ItemStack stack) {
		if (stack.getItem() != Item.getItemFromBlock(Blocks.TNT)) {
			return false;
		}
		return true;
	}
	
	
}
