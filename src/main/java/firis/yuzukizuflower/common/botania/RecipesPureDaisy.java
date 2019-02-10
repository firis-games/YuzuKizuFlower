package firis.yuzukizuflower.common.botania;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePureDaisy;

public class RecipesPureDaisy implements IManaRecipes{

	/**
	 * 一致するレシピを検索する
	 * 一致しない場合はnullを返却する
	 * @return
	 */
	public ManaRecipe getMatchesRecipe(@Nonnull ItemStack stack, boolean simulate) {
		
		ManaRecipe recipe = null;
		
		RecipePureDaisy result = null;
		for (RecipePureDaisy pdRrecipe : BotaniaAPI.pureDaisyRecipes) {
			boolean ret = false;
			Object input = pdRrecipe.getInput();
			
			//文字列で格納されている
			if (input instanceof String) {
				String oredict = (String) input;
				//recipe.isOreDict
				ret = RecipesPureDaisy.isOreDict(stack, oredict);
			}
			
			if (ret) {
				result = pdRrecipe;
				break;
			}
		}
		
		if (result != null) {
			
			ItemStack inputStack =  stack.copy();
			inputStack.setCount(1);
			
			//マナレシピへ変換
			recipe = new ManaRecipe(
					inputStack,
					new ItemStack(result.getOutputState().getBlock()),
					0,
					150);
		}
		return recipe;
	}
	
	private static boolean isOreDict(ItemStack stack, String entry) {
		if(stack.isEmpty())
			return false;

		for(ItemStack ostack : OreDictionary.getOres(entry, false)) {
			if(OreDictionary.itemMatches(ostack, stack, false)) {
				return true;
			}
		}

		return false;
	}	
}
