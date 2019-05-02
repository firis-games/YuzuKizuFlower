package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class YKContainerAutoWorkbench extends YKContainerBaseBoxedFuncFlower {

	/**
	 * コンストラクタ
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKContainerAutoWorkbench(IInventory iTeInv, IInventory playerInv) {
		super(iTeInv, playerInv);
	}
	 
	@Override
	protected void initTileEntitySlot(IInventory iTeInv) {
		
		//基準座標
		int xBasePos = 35;
		int yBasePos = 23;

		int invX = 5;
		int invY = 4;
		int baseSlot = 0;
		
		//inputスロット(20)
		for (int i = 0; i < invY; i++) {
            for (int j = 0; j < invX; j++) {
            	int slotIndex = j + i * invX + baseSlot;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new YKSlotInventory(iTeInv, slotIndex, xPos, yPos));
            }
        }
		
		//inputスロット(機械と設計図配置用)
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 20, 131, 27) {
			@Override
			public int getSlotStackLimit() {
		        return 1;
		    }
		});
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 21, 158, 27) {
			@Override
			public int getSlotStackLimit() {
		        return 1;
		    }
		});
		
		//outputスロット
		this.addSlotToContainer(new YKSlotInventory(iTeInv, 22, 157, 73));
		
	}
	
	/**
	 * プレイヤースロットの初期設定を行う
	 */
	@Override
	protected void initPlayerSlot(IInventory playerInv) {
		
		//プレイヤースロットの開始位置を設定
		this.startIndexPlayerSlot = this.inventorySlots.size();

		//基準座標
		int xBasePos = 0;
		int yBasePos = 0;

		//playerインベントリ基準座標設定
		xBasePos = 16;
		yBasePos = 100;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
            	int slotIndex = j + i * 9 + 9; //index 9 からスタート
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new Slot(playerInv, slotIndex, xPos, yPos));
            }
        }
        //playerホットバー
        xBasePos = 16;
		yBasePos = 158;
		for (int i = 0; i < 9; i++) {
			int slotIndex = i; //index 0 からスタート
        	int xPos = xBasePos + i * 18;
        	int yPos = yBasePos;
            this.addSlotToContainer(new Slot(playerInv, slotIndex, xPos, yPos));
		}
	}
}
