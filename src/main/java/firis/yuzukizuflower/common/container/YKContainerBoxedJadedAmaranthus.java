package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.container.slot.SlotPureDaisyOutput;
import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class YKContainerBoxedJadedAmaranthus extends YKContainerBaseBoxedFuncFlower {

	/**
	 * コンストラクタ
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKContainerBoxedJadedAmaranthus(IInventory iTeInv, IInventory playerInv) {
		super(iTeInv, playerInv);
	}
	 
	@Override
	protected void initTileEntitySlot(IInventory iTeInv) {
		
		//基準座標
		int xBasePos = 72;
		int yBasePos = 22;
				
		//outputスロット
		for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            	int slotIndex = j + i * 3;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new YKSlotInventory(iTeInv, slotIndex, xPos, yPos));
            }
        }
		
		//upgradeスロット
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 9, 144, 23));
	}

}
