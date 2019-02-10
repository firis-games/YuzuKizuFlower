package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class YKContainerManaTank extends YKContainerBaseBoxedFuncFlower {

	/**
	 * コンストラクタ
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKContainerManaTank(IInventory iTeInv, IInventory playerInv) {
		super(iTeInv, playerInv);
	}
	 
	@Override
	protected void initTileEntitySlot(IInventory iTeInv) {
		
		//inputスロット
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 0, 131, 26));
		
		//outputスロット
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 1, 131, 60));
		
		//chargeスロット
		this.addSlotToContainer(new Slot(iTeInv, 2, 19, 42));
		this.addSlotToContainer(new Slot(iTeInv, 3, 37, 42));
		this.addSlotToContainer(new Slot(iTeInv, 4, 19, 60));
		this.addSlotToContainer(new Slot(iTeInv, 5, 37, 60));

		//catalystスロット
		this.addSlotToContainer(new Slot(iTeInv, 6, 153, 43) {
			@Override
			public int getSlotStackLimit()
		    {
		        return 1;
		    }
		});
	}
}
