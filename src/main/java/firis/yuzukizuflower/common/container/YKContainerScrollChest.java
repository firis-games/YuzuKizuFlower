package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import firis.yuzukizuflower.common.tileentity.YKTileScrollChest;
import firis.yuzukizuflower.common.tileentity.YKTileScrollChest.IScrollInventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

public class YKContainerScrollChest extends Container {

	
	public YKTileScrollChest.IScrollInventoryHandler iTeInv;
	
	public YKContainerScrollChest(TileEntity tile, InventoryPlayer playerInv) {
		
		/* 実験ソース
		TileEntity tile1 = tile.getWorld().getTileEntity(tile.getPos().down());
		IItemHandler capability = tile1.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		IInventory iTeInv = new YKTileScrollChest.IInventoryHandler(capability, tile1);
		*/
		//iTeInv = (IInventory) tile1;
		
		YKTileScrollChest inv = (YKTileScrollChest) tile;
		this.iTeInv = (IScrollInventoryHandler) inv.getIInventory();
		
		//基準座標
		int xBasePos = 8;
		int yBasePos = 18;
		
		int ii = 0;
		
		//outputスロット
		int invX = 9;
		int invY = 6;
		for (int i = 0; i < invY; i++) {
            for (int j = 0; j < invX; j++) {
            	int slotIndex = j + i * invX;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new YKSlotInventory(iTeInv, slotIndex, xPos, yPos));
            	
            	ii += 1;
            	//if (!(ii < capability.getSlots())) {
              	if (!(ii < iTeInv.getSizeInventory())) {
            		break;
            	}
            }
            //if (!(ii < capability.getSlots())) {
            if (!(ii < iTeInv.getSizeInventory())) {
        		break;
        	}
        }
		
		
		this.initPlayerSlot(playerInv);
	}
	
	protected int startIndexPlayerSlot;
	
	/**
	 * プレイヤースロットの初期設定を行う
	 */
	protected void initPlayerSlot(IInventory playerInv) {
		
		//プレイヤースロットの開始位置を設定
		this.startIndexPlayerSlot = this.inventorySlots.size();

		//基準座標
		int xBasePos = 0;
		int yBasePos = 0;

		//playerインベントリ基準座標設定
		xBasePos = 8;
		yBasePos = 140;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
            	int slotIndex = j + i * 9 + 9; //index 9 からスタート
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new Slot(playerInv, slotIndex, xPos, yPos));
            }
        }
        //playerホットバー
        xBasePos = 8;
		yBasePos = 198;
		for (int i = 0; i < 9; i++) {
			int slotIndex = i; //index 0 からスタート
        	int xPos = xBasePos + i * 18;
        	int yPos = yBasePos;
            this.addSlotToContainer(new Slot(playerInv, slotIndex, xPos, yPos));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
