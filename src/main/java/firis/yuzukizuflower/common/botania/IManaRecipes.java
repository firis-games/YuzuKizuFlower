package firis.yuzukizuflower.common.botania;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

public interface IManaRecipes {
	
	public ManaRecipe getMatchesRecipe(@Nonnull ItemStack stack);

}
