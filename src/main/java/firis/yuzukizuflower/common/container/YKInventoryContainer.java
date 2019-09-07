package firis.yuzukizuflower.common.container;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import invtweaks.api.container.ChestContainer;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@ChestContainer
public class YKInventoryContainer extends Container {

	@Optional.Method(modid = "inventorytweaks")
	@ContainerSectionCallback
	public Map<ContainerSection, List<Slot>> ContainerSectionCallback() {
		Map<ContainerSection, List<Slot>> map = Maps.newHashMap();
		int invSize = 27;
		map.put(ContainerSection.CHEST, inventorySlots.subList(0, invSize));
		map.put(ContainerSection.INVENTORY_NOT_HOTBAR, inventorySlots.subList(invSize, invSize + 27));
		map.put(ContainerSection.INVENTORY_HOTBAR, inventorySlots.subList(invSize + 27, invSize + 27 + 9));
		map.put(ContainerSection.INVENTORY, inventorySlots.subList(invSize, invSize + 27 + 9));
		
		/*
		//ロックスロットを除外する
		int lockSlotIdx = this.lockSlotIndex;
		List<Slot> lockList; 
		lockList = map.get(ContainerSection.INVENTORY_HOTBAR);
		lockList.remove(lockSlotIdx);
		map.put(ContainerSection.INVENTORY_HOTBAR, lockList);
		
		lockSlotIdx = this.lockSlotIndex + invSize;
		lockList = map.get(ContainerSection.INVENTORY);
		lockList.remove(lockSlotIdx);
		map.put(ContainerSection.INVENTORY, lockList);
		*/
		
		return map;
	}
	
	protected final IInventory inventory;

	protected final int startIndexPlayerSlot;
	
	private int lockSlotIndex = -1;
	/**
	 * コンストラクタ
	 * @param iinv
	 * @param playerInv
	 */
	public YKInventoryContainer(IInventory inventory, InventoryPlayer playerInv, int lockSlot) {
		
		this.inventory = inventory;
		
		this.lockSlotIndex = lockSlot;
		
		//Inventoryスロット初期化
		this.initInventorySlot(inventory);
		
		//プレイヤースロットの開始位置を設定
		this.startIndexPlayerSlot = this.inventorySlots.size();
		
		//Playerスロットの初期化
		this.initPlayerSlot(playerInv);
	}
	
	/**
	 * Inventoryスロット初期化
	 * @param inventory
	 */
	protected void initInventorySlot(IInventory inventory) {
		//基準座標
		int xBasePos = 8;
		int yBasePos = 18;

		int invX = 9;
		int invY = 3;
		int baseSlot = 0;
		
		//outputスロット
		for (int i = 0; i < invY; i++) {
            for (int j = 0; j < invX; j++) {
            	int slotIndex = j + i * invX + baseSlot;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new YKSlotInventory(inventory, slotIndex, xPos, yPos));
            }
        }
	}
	
	
	/**
	 * プレイヤースロットの初期
	 */
	protected void initPlayerSlot(InventoryPlayer playerInv) {
		
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

        	if (i == this.lockSlotIndex) {
        		this.addSlotToContainer(new Slot(playerInv, slotIndex, xPos, yPos) {
            		@Override
            		public boolean isItemValid(ItemStack stack)
            	    {
            	        return false;
            	    }
            		@Override
            		public boolean canTakeStack(EntityPlayer playerIn)
            	    {
            	        return false;
            	    }
            		@Override
            		public boolean getHasStack() {
            			return false;
            		}
            	});
        	} else {
        		this.addSlotToContainer(new Slot(playerInv, slotIndex, xPos, yPos));
        	}
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
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
