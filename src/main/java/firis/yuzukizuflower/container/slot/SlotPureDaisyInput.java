package firis.yuzukizuflower.container.slot;

import firis.yuzukizuflower.tileentity.YKTileBoxedPureDaisy;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPureDaisyInput extends Slot {

	public SlotPureDaisyInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
    /**
     * セットできるアイテムの判定
     */
    public boolean isItemValid(ItemStack stack)
    {
    	//ピュアデイジーレシピチェック
    	return YKTileBoxedPureDaisy.isRecipe(stack);
    }
    
}
