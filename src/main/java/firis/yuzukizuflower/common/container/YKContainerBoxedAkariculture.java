package firis.yuzukizuflower.common.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class YKContainerBoxedAkariculture extends YKContainerBaseBoxedFuncFlower {

	/**
	 * コンストラクタ
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKContainerBoxedAkariculture(IInventory iTeInv, IInventory playerInv) {
		super(iTeInv, playerInv);
	}
	 
	@Override
	protected void initTileEntitySlot(IInventory iTeInv) {
		
		//input
		this.addSlotToContainer(new Slot(iTeInv, 0, 80, 35));
		
	}
}
