package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import firis.yuzukizuflower.common.inventory.PetalInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class YKContainerPetalWorkbench extends Container {

	protected PetalInventory inventory; 
	
	/**
	 * コンストラクタ
	 */
	public YKContainerPetalWorkbench(IInventory iinv, InventoryPlayer playerInv) {
		
		inventory = (PetalInventory) iinv;
		
		//基準座標
		int xBasePos = 41;
		int yBasePos = 23;
		int baseSlot = 0;
		
		//inputスロット
		int invX = 4;
		int invY = 4;
		for (int i = 0; i < invY; i++) {
            for (int j = 0; j < invX; j++) {
            	int slotIndex = j + i * invX + baseSlot;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new YKSlotInventory(iinv, slotIndex, xPos, yPos));
            }
        }
		
		//seedスロット
		this.addSlotToContainer(new YKSlotInventory(iinv, 16, 16, 77) {
			@Override
			public boolean isItemValid(ItemStack stack)
		    {
				return BotaniaHelper.recipesPetal.isSeed(stack);
		    }
		});
		
		//outputスロット
		this.addSlotToContainer(new YKSlotInventory(iinv, 17, 156, 50) {
			public boolean isItemValid(ItemStack stack)
		    {
		        return false;
		    }
		});
		
		this.initPlayerSlot(playerInv);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
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
	
	
	/**
     * Shift-click時の挙動を制御している
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
	@Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
		
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        
        //プレイヤーインベントリの開始index
        int playerInventoryIndex = this.startIndexPlayerSlot;
        
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
                if (slot.getSlotIndex() == 17) {
                	//CraftingResult更新
                    inventory.slotChangedCraftingResult();
                } else {
                	//CraftingGrid更新
                	inventory.slotChangedCraftingGrid();
                }
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
}
