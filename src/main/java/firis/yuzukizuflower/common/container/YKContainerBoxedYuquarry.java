package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import net.minecraft.inventory.IInventory;

public class YKContainerBoxedYuquarry extends YKContainerBaseBoxedFuncFlower {

	/**
	 * コンストラクタ
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKContainerBoxedYuquarry(IInventory iTeInv, IInventory playerInv) {
		super(iTeInv, playerInv);
	}
	 
	@Override
	protected void initTileEntitySlot(IInventory iTeInv) {
		
		//基準座標
		int xBasePos = 44;
		int yBasePos = 22;
				
		//outputスロット
		for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
            	int slotIndex = j + i * 3;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new YKSlotInventory(iTeInv, slotIndex, xPos, yPos));
            }
        }
		
	}
}
