package firis.yuzukizuflower.container;

import firis.yuzukizuflower.container.slot.SlotPureDaisyOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class YKContainerManaTank extends Container {

	public YKContainerManaTank(IInventory iTeInv, IInventory playerInv) {
	
		//GUI変換部分
		this.addSlotToContainer(new Slot(iTeInv, 0, 131, 26));
		this.addSlotToContainer(new SlotPureDaisyOutput(iTeInv, 1, 131, 60));
		
		//普通のスロット
		this.addSlotToContainer(new Slot(iTeInv, 2, 19, 42));
		this.addSlotToContainer(new Slot(iTeInv, 3, 37, 42));
		this.addSlotToContainer(new Slot(iTeInv, 4, 19, 60));
		this.addSlotToContainer(new Slot(iTeInv, 5, 37, 60));

		//変換
		this.addSlotToContainer(new Slot(iTeInv, 6, 153, 43) {
			@Override
			public int getSlotStackLimit()
		    {
		        return 1;
		    }
		});

		//基準座標
		int xBasePos = 0;
		int yBasePos = 0;
		
        //playerインベントリ基準座標設定
		xBasePos = 8;
		yBasePos = 84;
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
		yBasePos = 142;
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
	
	/* これをいれないとエラーになる
	@Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        return ItemStack.EMPTY;
    }
	*/
    /**
     * Shift-click時の挙動を制御している
     * なんとか汎用化したいかな
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
	@Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
		
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        //6からプレイヤーインベントリ
        int playerInventoryIndex = 7;
        
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (index < playerInventoryIndex) {
            	//コンテナーインベントリ -> プレイヤーインベントリ
            	if (!this.mergeItemStack(itemstack1, playerInventoryIndex, this.inventorySlots.size(), false))
                {
                    return ItemStack.EMPTY;
                }
            	
            } else {
            	//プレイヤーインベントリ -> コンテナーインベントリ
            	if (!this.mergeItemStack(itemstack1, 0, playerInventoryIndex, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

}
