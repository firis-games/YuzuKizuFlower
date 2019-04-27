package firis.yuzukizuflower.common.botania;

import java.util.List;
import java.util.regex.Pattern;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;

public class RecipesPetal {

	/**
	 * 花びら調合所のレシピ
	 * @param stack
	 * @param simulate
	 * @return
	 */
	public ItemStack getMatchesRecipe(List<ItemStack> stackList) {
		
		ItemStack stack = ItemStack.EMPTY.copy();
		IItemHandler inv = new RecipesPetalInventory(stackList);
		
		for(RecipePetals recipe : BotaniaAPI.petalRecipes) {
			//一致する場合
			if(recipe.matches(inv)) {
				stack = recipe.getOutput().copy();
				break;
			}
		}
		return stack;
	}
	
	
	private static final Pattern SEED_PATTERN = Pattern.compile("(?:(?:(?:[A-Z-_.:]|^)seed)|(?:(?:[a-z-_.:]|^)Seed))(?:[sA-Z-_.:]|$)");
	
	/**
	 * 種かどうかを判断する
	 * @param stack
	 * @return
	 */
	public boolean isSeed(ItemStack stack) {
		boolean ret = false;
		
		if(SEED_PATTERN.matcher(stack.getUnlocalizedName()).find()) {
			ret = true;
		}
		
		return ret;
	}
	
	protected static class RecipesPetalInventory extends ItemStackHandler {
		
		public RecipesPetalInventory(List<ItemStack> stackList) {
			stacks = NonNullList.create();
			
			for (ItemStack stack : stackList) {
				stacks.add(stack);
			}
		}
	}
}
