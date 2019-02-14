package firis.yuzukizuflower.common.botania;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

public interface IManaGenerator {
	
	public ManaGenerator getMatchesRecipe(@Nonnull List<ItemStack> stackList);
	
	public default ManaGenerator getMatchesRecipe(@Nonnull ItemStack stack) {
		return getMatchesRecipe(new ArrayList<ItemStack>(Arrays.asList(stack)));
	}
	
	public boolean isMatchesItemStackSlot(@Nonnull ItemStack stack);

}
