package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import net.minecraft.inventory.IInventory;

public class YKContainerBoxedPureDaisy  extends YKContainerBaseBoxedFuncFlower {

	/**
	 * コンストラクタ
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKContainerBoxedPureDaisy(IInventory iTeInv, IInventory playerInv) {
		super(iTeInv, playerInv);
	}
	 
	@Override
	protected void initTileEntitySlot(IInventory iTeInv) {
		
		//input
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 0, 51, 35));
		
		//output
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 1, 116, 35));
		
	}
}
