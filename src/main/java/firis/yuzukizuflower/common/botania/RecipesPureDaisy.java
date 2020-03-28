package firis.yuzukizuflower.common.botania;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
		
		//対象がEMPTYの場合は無条件でスキップ
		if (stack.isEmpty()) return null;
		
		ManaRecipe recipe = null;
		
		RecipePureDaisy result = null;
		for (RecipePureDaisy pdRrecipe : BotaniaAPI.pureDaisyRecipes) {
			boolean ret = false;
			Object input = pdRrecipe.getInput();
			
			//Blockで比較
			if(input instanceof Block) {
				ItemStack inputStack = new ItemStack((Block) input);
				//メタデータは無視
				ret = stack.getItem() == inputStack.getItem();
				
			//IBlockStateで比較
			} else if (input instanceof IBlockState) {
				IBlockState state = (IBlockState) input;
				ItemStack inputStack = new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
				ret = ItemStack.areItemsEqual(stack, inputStack);
				
			//鉱石辞書で比較
			} else if (input instanceof String) {
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
