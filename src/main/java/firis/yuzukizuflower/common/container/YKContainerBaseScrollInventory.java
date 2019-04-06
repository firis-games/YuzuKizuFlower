package firis.yuzukizuflower.common.container;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import firis.yuzukizuflower.client.gui.parts.YKGuiScrollBar.IYKGuiScrollBarChanged;
import firis.yuzukizuflower.common.container.slot.YKSlotInventory;
import firis.yuzukizuflower.common.inventory.IScrollInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class YKContainerBaseScrollInventory extends Container implements IYKGuiScrollBarChanged {
	
	/**
	 * コンストラクタ
	 * @param iinv
	 * @param playerInv
	 */
	public YKContainerBaseScrollInventory(IScrollInventory iinv, InventoryPlayer playerInv) {
				
		this.iTeInv = iinv;
		this.iTeInv.openInventory(playerInv.player);
				
		//基準座標
		int xBasePos = 8;
		int yBasePos = 18;

		//outputスロット
		int invX = 9;
		int invY = 6;
		for (int i = 0; i < invY; i++) {
            for (int j = 0; j < invX; j++) {
            	int slotIndex = j + i * invX;
            	int xPos = xBasePos + j * 18;
            	int yPos = yBasePos + i * 18;
            	this.addSlotToContainer(new YKSlotInventory(iTeInv, slotIndex, xPos, yPos));
            }
        }
		
		
		this.initPlayerSlot(playerInv);
	}
	
	public IScrollInventory iTeInv;
	
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
	
    /**
     * Called when the container is closed.
     */
	@Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        this.iTeInv.closeInventory(playerIn);
    }
    
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	/**
	 * @Intarface IYKGuiScrollBarChanged
	 * @param page
	 */
	@Override
	public void onScrollChanged(int page) {
		
		int nowPage = iTeInv.getScrollPage();
		
		iTeInv.setScrollPage(page);
		
		//ページ切り替えが発生していない場合は更新しない
		if (nowPage == page) return;
		
		int pageRowCount = iTeInv.getScrollSlotRowCount();
		int pageCount = iTeInv.getScrollSlotPageCount();
		
		int rowCount = pageCount / pageRowCount;
		
		//1ページ次へ
		List<Integer> slotList;
		if (nowPage + 1 == page) {
			slotList = IntStream.range(pageRowCount * (rowCount - 1), pageCount).boxed().collect(Collectors.toList());			
		}
		//1ページ前へ
		else if (nowPage - 1 == page) {
			slotList = IntStream.range(0, pageRowCount).boxed().collect(Collectors.toList());
		//全ページ更新
		} else {
			slotList = IntStream.range(0, pageCount).boxed().collect(Collectors.toList());
		}
		
		//最終行のみ更新
		for (Integer idx : slotList) {
			ItemStack itemstack1 = this.inventoryItemStacks.get(idx);
			for (int j = 0; j < this.listeners.size(); ++j)
	        {
				((IContainerListener)this.listeners.get(j)).sendSlotContents(this, idx, itemstack1);
				
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

}
