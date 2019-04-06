package firis.yuzukizuflower.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * DummyItemHandler
 * @author computer
 *
 */
public class YKDummyItemHandler implements IItemHandler {
	@Override
	public int getSlots() {
		return 0;
	}
	@Override
	public ItemStack getStackInSlot(int slot) {
		return null;
	}
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return null;
	}
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return null;
	}
	@Override
	public int getSlotLimit(int slot) {
		return 0;
	}
}
