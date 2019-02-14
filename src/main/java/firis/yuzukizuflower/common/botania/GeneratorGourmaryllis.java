package firis.yuzukizuflower.common.botania;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class GeneratorGourmaryllis implements IManaGenerator {
	
	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaGenerator getMatchesRecipe(@Nonnull List<ItemStack> stackList) {
		
		ManaGenerator generator = null;
		
		if (stackList.size() != 2) {
			return generator;
		}
		
		ItemStack stack1 = stackList.get(0);
		ItemStack stack2 = stackList.get(1);
		
		//食べ物の判断
		if (!isMatchesItemStackSlot(stack1)
				|| !isMatchesItemStackSlot(stack2)) {
			return generator;
		}
		//同じ食べ物ではない判断
		if (ItemStack.areItemsEqual(stack1, stack2)) {
			return generator;
		}
		
		//食事量からのマナ係数計算
		int val1 = Math.min(12, ((ItemFood) stack1.getItem()).getHealAmount(stack1));
		int val2 = Math.min(12, ((ItemFood) stack2.getItem()).getHealAmount(stack2));
		
		//オリジナルの計算式
		//ここから重複分を減らしていく
		//マナを計算
		int mana1 = val1 * val1 * 70;
		//tick数
		int time1 = val1 * 10;
		
		int mana2 = val2 * val2 * 70;
		int time2 = val2 * 10;
		
		//生成合計マナ
		int mana = mana1 + mana2;
		//cooldown
		int time = time1 + time2;
		
		//1tick1cycleとする
		//1tickあたりのマナ
		//mana = mana / time;
		ItemStack fuel = stack1.copy();
		fuel.setCount(1);
		
		//レシピへ変換
		generator = new ManaGenerator(
				fuel,
				mana,
				time,
				time);
		
		return generator;
	}
	
	/**
	 * スロットのチェック処理
	 */
	public boolean isMatchesItemStackSlot(@Nonnull ItemStack stack) {
		if(stack.isEmpty()
				|| !(stack.getItem() instanceof ItemFood)) {
			return false;
		}
		return true;
	}
	
}
