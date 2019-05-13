package firis.yuzukizuflower.common.botania;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GeneratorKekimurus implements IManaGenerator {
	
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
		
		//ケーキの場合は発電する
		//ケキムラスと同じ効率にする
		//合計7きれ
		//1きれあたり4s 1800mana 
		
		//28秒
		int time = 28 * 20;
		
		//80tick
		int cycle = 80;
		//4sに1回マナを生産する
		int mana = 1800;
		
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
		if (stack.getItem() != Items.CAKE) {
			return false;
		}
		return true;
	}
	
	
}
