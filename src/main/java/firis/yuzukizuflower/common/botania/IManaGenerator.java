package firis.yuzukizuflower.common.botania;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

public interface IManaGenerator {
	
	public ManaGenerator getMatchesRecipe(@Nonnull ItemStack stack);

}
