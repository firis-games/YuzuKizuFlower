package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import net.minecraft.inventory.IInventory;

public class YKContainerBoxedEndoflame extends YKContainerBaseBoxedFuncFlower {

	/**
	 * コンストラクタ
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKContainerBoxedEndoflame(IInventory iTeInv, IInventory playerInv) {
		super(iTeInv, playerInv);
	}
	 
	@Override
	protected void initTileEntitySlot(IInventory iTeInv) {
		
		//inputスロット
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 0, 80, 35));
		
		//upgradeスロット
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 1, 144, 23) {
			@Override
			public int getSlotStackLimit()
		    {
		        return 8;
		    }			
		});
	}
}
