package firis.yuzukizuflower.common.container;

import firis.yuzukizuflower.common.inventory.IScrollInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKContainerCorporeaChest extends YKContainerBaseScrollInventory {

	public YKContainerCorporeaChest(IScrollInventory iinv, InventoryPlayer playerInv) {
		super(iinv, playerInv);
	}

	/**
	 * 外部のIItemHandlerを使う場合は制御方法を考える必要がある
	 * 一旦処理を強制停止
	 */
	@Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
		return ItemStack.EMPTY;
    }
	
	@Override
	public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.iTeInv);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
		this.iTeInv.setField(id, data);
    }
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
    {
		ItemStack retStack = super.slotClick(slotId, dragType, clickTypeIn, player);
		
		//Clientと同期する
		this.detectAndSendChanges();
		if (player instanceof EntityPlayerMP) {
			EntityPlayerMP playermp = (EntityPlayerMP) player;
			playermp.sendContainerToPlayer(this);
		}
				
		return retStack;
    }
}
