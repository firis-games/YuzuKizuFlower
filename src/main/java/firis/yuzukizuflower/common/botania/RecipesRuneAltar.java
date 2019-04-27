package firis.yuzukizuflower.common.botania;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;

public class RecipesRuneAltar {
	
	/**
	 * ルーンの祭壇のレシピ
	 * @param stack
	 * @param simulate
	 * @return
	 */
	public ManaRecipe getMatchesRecipe(List<ItemStack> stackList) {
		
		IItemHandler inv = new RecipesInventory(stackList);
		
		ManaRecipe ret = null;
		
		for(RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes) {
			//一致する場合
			if(recipe.matches(inv)) {
				ret = new ManaRecipe(ItemStack.EMPTY.copy(), 
						recipe.getOutput().copy(),
						recipe.getManaUsage(),
						0);
				break;
			}
		}
		
		return ret;
	}
	
	private ResourceLocation rl_stone = new ResourceLocation("botania", "livingrock");
	
	public boolean isStone(ItemStack stack) {
		boolean ret = false;
		
		if (!stack.isEmpty() 
				&& stack.getItem().getRegistryName().equals(rl_stone)
				&& stack.getMetadata() == 0) {
			ret = true;
		}
		
		return ret;
	}
	
	private ResourceLocation rl_rune = new ResourceLocation("botania", "rune");
	
	/**
	 * 消費しないアイテムをチェックする
	 * @param stack
	 * @return
	 */
	public boolean isNoShrink(ItemStack stack) {
		boolean ret = false;
		
		if (!stack.isEmpty() 
				&& stack.getItem().getRegistryName().equals(rl_rune)) {
			ret = true;
		}
		
		return ret;
	}
	
	protected static class RecipesInventory extends ItemStackHandler {
		
		public RecipesInventory(List<ItemStack> stackList) {
			stacks = NonNullList.create();
			
			for (ItemStack stack : stackList) {
				stacks.add(stack);
			}
		}
	}
}
