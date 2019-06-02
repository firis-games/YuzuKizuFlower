package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import net.minecraft.inventory.IInventory;

public class YKContainerBoxedThermalily extends YKContainerBaseBoxedFuncFlower {

	/**
	 * コンストラクタ
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKContainerBoxedThermalily(IInventory iTeInv, IInventory playerInv) {
		super(iTeInv, playerInv);
	}
	 
	@Override
	protected void initTileEntitySlot(IInventory iTeInv) {
		
		//inputスロット
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 0, 59, 23));
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 1, 59, 57));
		
		//upgradeスロット
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 2, 144, 23) {
			@Override
			public int getSlotStackLimit()
		    {
		        return 8;
		    }			
		});
	}
}
