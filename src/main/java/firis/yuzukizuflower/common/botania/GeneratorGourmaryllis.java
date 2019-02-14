package firis.yuzukizuflower.common.botania;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class GeneratorGourmaryllis implements IManaGenerator {
	
	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaGenerator getMatchesRecipe(@Nonnull ItemStack stack) {
		
		ManaGenerator generator = null;
		
		//食べ物の判断
		if (stack.isEmpty()
				|| !(stack.getItem() instanceof ItemFood)) {
			return generator;
		}
		
		//食事量からのマナ係数計算
		int val = Math.min(12, ((ItemFood) stack.getItem()).getHealAmount(stack));
		
		//オリジナルの計算式
		//ここから重複分を減らしていく
		//マナを計算
		int mana = val * val * 70;
		//tick数
		int time = val * 10;
		
		
		//係数を減らして5とする
		mana = val * 5;
		time = val * 10;
		
		//1tick1cycleとする

		
		ItemStack fuel = stack.copy();
		fuel.setCount(1);
		
		//レシピへ変換
		generator = new ManaGenerator(
				fuel,
				mana,
				time,
				1);
		
		return generator;
	}
	
	
}
