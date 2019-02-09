package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.container.slot.SlotPureDaisyOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class YKContainerBoxedOrechid extends Container {

	public YKContainerBoxedOrechid(IInventory iTeInv, IInventory playerInv) {
	
		//基準座標
		int xBasePos = 0;
		int yBasePos = 0;

		//input
		this.addSlotToContainer(new Slot(iTeInv, 0, 46, 40){
			@Override
			public boolean isItemValid(ItemStack stack)
		    {
				if (!stack.isEmpty() 
						&& stack.getItem().getRegistryName().equals(new ResourceLocation("minecraft:stone"))
						&& stack.getMetadata() == 0
						) {
					return true;
				}else if (!stack.isEmpty() 
						&& stack.getItem().getRegistryName().equals(new ResourceLocation("minecraft:netherrack"))) {
					return true;
				}
		        return false;
		    }
			
		});
		
		xBasePos = 108;
		yBasePos = 22;
		
		//output
		for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            	int slotIndex = j + i * 3 + 1;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new SlotPureDaisyOutput(iTeInv, slotIndex, xPos, yPos));
            }
        }
		
		
		
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
        int playerInventoryIndex = 10;
        
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
