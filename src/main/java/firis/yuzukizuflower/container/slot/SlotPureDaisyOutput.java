package firis.yuzukizuflower.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPureDaisyOutput extends Slot {

	public SlotPureDaisyOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
    /**
     * アイテムを直接スロットへ配置できないように設定
     */
	@Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

}
