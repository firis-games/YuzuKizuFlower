package firis.yuzukizuflower.common.container;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import firis.yuzukizuflower.common.inventory.IScrollInventory;
import firis.yuzukizuflower.common.inventory.ITextScrollInventoryItemHandler;
import invtweaks.api.container.ChestContainer;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ChestContainer(showButtons = false)
public class YKContainerScrollChest extends YKContainerBaseScrollInventory {

	@Optional.Method(modid = "inventorytweaks")
	@ChestContainer.RowSizeCallback
	public int getRowCount() {
		return 9;
	}
	
	@Optional.Method(modid = "inventorytweaks")
	@ContainerSectionCallback
	public Map<ContainerSection, List<Slot>> ContainerSectionCallback() {
		Map<ContainerSection, List<Slot>> map = Maps.newHashMap();
		map.put(ContainerSection.CHEST, inventorySlots.subList(0, 53));
		map.put(ContainerSection.INVENTORY, inventorySlots.subList(54, 89));
		map.put(ContainerSection.INVENTORY_NOT_HOTBAR, inventorySlots.subList(54, 80));
		map.put(ContainerSection.INVENTORY_HOTBAR, inventorySlots.subList(81, 89));
		return map;
	}
	
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
	
	/**
	 * パケットから呼ばれる用
	 * @param search
	 */
	public void onTextChange(String search) {
		this.iTeInv.setTextChanged(search);
		//Clientと同期する
		this.allSendChanges();
	}
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
    {
		ItemStack stack = super.slotClick(slotId, dragType, clickTypeIn, player);
		
		//Clientと同期する
		this.detectAndSendChanges();
		if (player instanceof EntityPlayerMP) {
			EntityPlayerMP playermp = (EntityPlayerMP) player;
			playermp.sendContainerToPlayer(this);
			
			ITextScrollInventoryItemHandler tile = ((ITextScrollInventoryItemHandler) this.iTeInv);
			if(!"".equals(tile.getTextSearch())) {
				((ITextScrollInventoryItemHandler) this.iTeInv).setFilerList();			
			}
			
			this.allSendChanges();
		}
		
		
		return stack;
    }
	
	/**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void allSendChanges()
    {
        for (int i = 0; i < this.iTeInv.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.iTeInv.getStackInSlot(i);
            ItemStack itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
            
            for (int j = 0; j < this.listeners.size(); ++j)
            {
                ((IContainerListener)this.listeners.get(j)).sendSlotContents(this, i, itemstack1);
            }
        }
        
        for (int j = 0; j < this.listeners.size(); ++j)
        {
			this.listeners.get(j).sendAllWindowProperties(this, this.iTeInv);
        }
    }
}

