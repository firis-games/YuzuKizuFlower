package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.inventory.IScrollInventory;
import firis.yuzukizuflower.common.inventory.ITextScrollInventoryItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class YKContainerScrollChest extends YKContainerBaseScrollInventory {

	public YKContainerScrollChest(IScrollInventory iinv, InventoryPlayer playerInv) {
		super(iinv, playerInv);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
		//Clientと同期する
		ItemStack stack = super.transferStackInSlot(playerIn, index);
		this.detectAndSendChanges();
		if (playerIn instanceof EntityPlayerMP) {
			EntityPlayerMP playermp = (EntityPlayerMP) playerIn;
			playermp.sendContainerToPlayer(this);
		}
		return stack;
    }
	
	/**
	 * パケットから呼ばれる用
	 * @param search
	 */
	public void onTextChange(String search) {
		this.iTeInv.setTextChanged(search);
	}
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
    {
		ItemStack stack = super.slotClick(slotId, dragType, clickTypeIn, player);
		((ITextScrollInventoryItemHandler) this.iTeInv).setFilerList();
		return stack;
    }
}

