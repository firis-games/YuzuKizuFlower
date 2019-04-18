package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import net.minecraft.inventory.IInventory;

public class YKContainerBoxedOrechid extends YKContainerBaseBoxedFuncFlower {

	/**
	 * コンストラクタ
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKContainerBoxedOrechid(IInventory iTeInv, IInventory playerInv) {
		super(iTeInv, playerInv);
	}
	 
	@Override
	protected void initTileEntitySlot(IInventory iTeInv) {
		
		//基準座標
		int xBasePos = 0;
		int yBasePos = 0;

		//input
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 0, 46, 40));
		
		xBasePos = 108;
		yBasePos = 22;
		
		//outputスロット
		int invX = 3;
		int invY = 3;
		int baseSlot = 1;
		
		for (int i = 0; i < invY; i++) {
            for (int j = 0; j < invX; j++) {
            	int slotIndex = j + i * invX + baseSlot;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new YKSlotInventory(iTeInv, slotIndex, xPos, yPos));
            }
        }
		
		//work
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 10, 0, 0) {
			@Override
			public boolean isEnabled()
		    {
		        return false;
		    }
		});
	}
	
}
