package firis.yuzukizuflower.common.container.slot;

import firis.yuzukizuflower.common.inventory.IScrollInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKSlotInventory extends Slot{

	/**
	 * コンストラクタ
	 * @param inventoryIn
	 * @param index
	 * @param xPosition
	 * @param yPosition
	 */
	public YKSlotInventory(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	/**
     * IInventoryのスロット制御を利用する
     */
	@Override
    public boolean isItemValid(ItemStack stack)
    {
        return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
    }
	
    @SideOnly(Side.CLIENT)
    public boolean isEnabled()
    {
    	return !this.isScrollInventoryLocked();
    }
    
    /**
     * スクロールバーのスロット制御用
     * @return
     */
    protected boolean isScrollInventoryLocked() {
    	if (!(this.inventory instanceof IScrollInventory)) {
    		return true;
    	}
    	IScrollInventory inv = (IScrollInventory) this.inventory;
        return inv.isLockedScrollSlot(this.getSlotIndex());
    }
}
