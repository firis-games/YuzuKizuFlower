package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import net.minecraft.inventory.IInventory;

public class YKContainerTerraPlate extends YKContainerBaseBoxedFuncFlower {

	/**
	 * コンストラクタ
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKContainerTerraPlate(IInventory iTeInv, IInventory playerInv) {
		super(iTeInv, playerInv);
	}
	 
	@Override
	protected void initTileEntitySlot(IInventory iTeInv) {
		
		//基準座標
		int xBasePos = 52;
		int yBasePos = 22;

		int invX = 1;
		int invY = 3;
		int baseSlot = 0;
		
		//inputスロット
		for (int i = 0; i < invY; i++) {
            for (int j = 0; j < invX; j++) {
            	int slotIndex = j + i * invX + baseSlot;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new YKSlotInventory(iTeInv, slotIndex, xPos, yPos));
            }
        }
		
		xBasePos = 131;
		yBasePos = 40;

		//outputスロット
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 3, xBasePos, yBasePos));
	}

}
