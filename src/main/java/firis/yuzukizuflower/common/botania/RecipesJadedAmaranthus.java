package firis.yuzukizuflower.common.botania;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

/**
 * オアキド/オアキド・イグネムのレシピ
 * @author computer
 *
 */
public class RecipesJadedAmaranthus implements IManaRecipes{

	private static Random rand = new Random();
	
	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaRecipe getMatchesRecipe(@Nonnull ItemStack stack, boolean simulate) {
		
		ManaRecipe recipe = null;
		
		//stackを無視してレシピを返却する
		ItemStack output = null;
		
		//花の色
		EnumDyeColor color = EnumDyeColor.byMetadata(RecipesJadedAmaranthus.rand.nextInt(16));
		
		if (stack.isEmpty()) {
			//神秘の花
			output = new ItemStack(ModBlocks.flower, 1, color.getDyeDamage());
		} else if (stack.getItem() instanceof ItemShears) {
			//神秘の花びら
			output = new ItemStack(ModItems.petal, 2, color.getDyeDamage());
		}
		
		if (output != null) {
			//レシピを生成
			recipe = new ManaRecipe(
					ItemStack.EMPTY.copy(),
					output,
					600,
					150);
		}
		return recipe;
	}
}
