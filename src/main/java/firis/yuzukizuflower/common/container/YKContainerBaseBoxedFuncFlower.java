package firis.yuzukizuflower.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class YKContainerBaseBoxedFuncFlower extends Container {

	protected IInventory iinventory;
	
	/**
	 * コンストラクタ
	 * @param iTeInv
	 * @param playerInv
	 */
	public YKContainerBaseBoxedFuncFlower(IInventory iTeInv, IInventory playerInv) {
		
		//スロットの初期設定
		this.initTileEntitySlot(iTeInv);
		
		this.initPlayerSlot(playerInv);
		
		this.iinventory = iTeInv;
		
		//負荷軽減用
		this.lastFieldList = NonNullList.<Integer>withSize(iTeInv.getFieldCount(), 0);
		
	}
	
	/**
	 * スロットの初期設定を行う
	 */
	protected abstract void initTileEntitySlot(IInventory iTeInv);
	
	/**
	 * 内部プレイヤースロットの開始位置
	 */
	protected int startIndexPlayerSlot = -1;
	
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

	/**
	 * ゲージ系の数値をGUIを開いているときのみ同期する
	 */
	@Override
	public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.iinventory);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
		this.iinventory.setField(id, data);
    }
	
	/**
	 * 負荷軽減用
	 * 最後に送信した数値を保存する
	 */
	protected final NonNullList<Integer> lastFieldList;
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (IContainerListener listener : listeners) {
			//数値系を同期する
			for (int i = 0; i < iinventory.getFieldCount(); i++) {
				//変更された場合のみパケットを送信する
				int newValue = iinventory.getField(i);
				if (newValue != lastFieldList.get(i)) {
					lastFieldList.set(i, newValue);
					listener.sendWindowProperty(this, i, newValue);
				}
			}
		}
	}
}
