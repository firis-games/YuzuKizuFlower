package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import firis.yuzukizuflower.common.botania.ManaRecipe;
import net.minecraft.item.ItemStack;

/**
 * マナプール系処理
 * @author computer
 *
 */
public class YKTileBoxedAocean extends YKTileBaseBoxedFuncFlower {

	public YKTileBoxedAocean() {

		this.maxMana = 10000;
		
		this.funcFlowerRecipes = BotaniaHelper.recipesAocean;
		
		//inputスロット
		this.inputSlotIndex = new ArrayList<Integer>();
		
		//outputスロット
		this.outputSlotIndex = IntStream.rangeClosed(0, 14).boxed().collect(Collectors.toList());
		
		//workスロット
		this.workSlotIndex = new ArrayList<Integer>();
		
	}
	
	@Override
	public int getSizeInventory() {
		return 15;
	}
	
	/**
	 * レシピの出力結果を取得する
	 */
	@Override
	public ItemStack getRecipesResult() {
		
		ItemStack stack = this.getStackInputSlotFirst();
		
		ManaRecipe recipe = funcFlowerRecipes.getMatchesRecipe(stack, false);
		if (recipe == null) {
			//例外
			clearRecipeWork();
			return null;
		}
		
		return recipe.getOutputItemStack();		
	}
}