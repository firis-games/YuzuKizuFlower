package firis.yuzukizuflower.common.botania;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.api.recipe.RecipeBrew;

public class RecipesBrewery {
	
	/**
	 * 醸造台のレシピ
	 * @param stack
	 * @param simulate
	 * @return
	 */
	public ManaRecipe getMatchesRecipe(List<ItemStack> stackList, ItemStack catalystStack) {
		
		IItemHandler inv = new RecipesInventory(stackList);
		
		ManaRecipe ret = null;
		
		for(RecipeBrew recipe : BotaniaAPI.brewRecipes) {
			//一致する場合
			if(recipe.matches(inv)) {
				
				int manaCost = 1000;
				
				if (!catalystStack.isEmpty() && catalystStack.getItem() instanceof IBrewContainer) {
					IBrewContainer container = (IBrewContainer) catalystStack.getItem();
					manaCost = container.getManaCost(recipe.getBrew(), catalystStack);
				}
				
				ret = new ManaRecipe(ItemStack.EMPTY.copy(), 
						recipe.getOutput(catalystStack).copy(),
						manaCost,
						0);
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * 触媒のチェック処理
	 * @param stack
	 * @return
	 */
	public boolean isCatalyst(ItemStack stack) {
		
		boolean ret = false;
		
		if (stack.isEmpty()) {
			return false;
		}
		//アイテムが空 or IBrewItemでない
		if (!(stack.getItem() instanceof IBrewItem) && !(stack.getItem() instanceof IBrewContainer)) {
			return ret;
		}
		
		if (stack.getItem() instanceof IBrewContainer) {
			ret = true;
			return ret;
		}
		
		IBrewItem brewItem = (IBrewItem) stack.getItem();
		Brew brew = brewItem.getBrew(stack);

		if (brew != null
				&& brew != BotaniaAPI.fallbackBrew) {
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
