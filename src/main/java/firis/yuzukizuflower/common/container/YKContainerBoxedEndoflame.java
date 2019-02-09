package firis.yuzukizuflower.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.block.ModBlocks;

public class YKContainerBoxedEndoflame extends Container {

	public YKContainerBoxedEndoflame(IInventory iTeInv, IInventory playerInv) {
	
		//独自スロット(かまど部分)
		//this.addSlotToContainer(new SlotFurnaceFuel(iTeInv, 0, 78, 35));
		this.addSlotToContainer(new SlotFurnaceFuel(iTeInv, 0, 80, 35));
		this.addSlotToContainer(new Slot(iTeInv, 1, 144, 23) {
			@Override
			public int getSlotStackLimit()
		    {
		        return 8;
		    }
			
			@Override
			public boolean isItemValid(ItemStack stack)
		    {
				if (!stack.isEmpty() 
						&& stack.getItem().getRegistryName().equals(new ResourceLocation("botania:specialflower"))
						&& stack.getItemDamage() == 0) {
					return true;
				}
		        return false;
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
        int playerInventoryIndex = 2;
        
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
