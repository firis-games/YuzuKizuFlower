package firis.yuzukizuflower.common.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.YKGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class YKItemBackpackChest extends Item implements IBauble {

	public YKItemBackpackChest() {
		super();
		//init
		this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
		this.setMaxStackSize(1);
	}
	
    /**
     * @interface IBauble
     */
	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.BODY;
	}
	
    /**
     * Called when a Block is right-clicked with this Item
     */
	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		//GUIを開く
		ItemStack stack = player.getHeldItem(hand);
		if (openGui(stack, player, hand)) {
	        return EnumActionResult.SUCCESS;
		}
        return EnumActionResult.PASS;
    }
	
	/**
     * Called when the equipped item is right clicked.
     */
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		//GUIを開く
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (openGui(stack, playerIn, handIn)) {
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
    
	/**
	 * ItemHandler用のCapability
	 */
	@Override
    @Nullable
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new InventoryProvider();
    }
	
	/**
	 * Guiを開く
	 * @param stack
	 * @param player
	 * @return
	 */
	public static boolean openGui(ItemStack stack, EntityPlayer player, EnumHand hand) {
		boolean ret = false;
		
		if (!stack.isEmpty()) {
			ret = true;
			
			int gui_id = YKGuiHandler.BACKPACK_CHEST;
			
			int handMode = hand == EnumHand.MAIN_HAND ? 1 : 2;
			
			int lockSlot = -1;
			if (EnumHand.MAIN_HAND == hand) {
				//HotBarのみ取得する
				for (int i = 0; i < 9; i++) {
					//完全一致のみ取得する
					if (player.inventory.getStackInSlot(i) == stack) {
						lockSlot = i;
						break;
					}
				}
			}
			player.openGui(YuzuKizuFlower.INSTANCE, gui_id,
					player.getEntityWorld(), handMode, lockSlot, 0);
		}
		return ret;
	}
	
	/**
	 * Guiを開く
	 * @param stack
	 * @param player
	 * @return
	 */
	public static boolean openGui(ItemStack stack, EntityPlayer player) {
		boolean ret = false;
		
		if (!stack.isEmpty()) {
			ret = true;
			
			int gui_id = YKGuiHandler.BACKPACK_CHEST_KEY;
			player.openGui(YuzuKizuFlower.INSTANCE, gui_id,
					player.getEntityWorld(), 0, -1, 0);
		}
		return ret;
	}
	
	/**
	 * プレイヤーのインベントリから背負いチェストを取得する
	 * @param player
	 * @return
	 */
	public static ItemStack getBackpackChest(EntityPlayer player) {
		
		ItemStack ret = ItemStack.EMPTY.copy();
		
		//BODY枠
		IBaublesItemHandler baublesHandler = BaublesApi.getBaublesHandler(player);
		for (int i = 0; i < BaubleType.BODY.getValidSlots().length; i++) {
			int slot = BaubleType.BODY.getValidSlots()[i];
			
			ItemStack work = baublesHandler.getStackInSlot(slot);
			
			if (work.getItem() instanceof YKItemBackpackChest) {
				ret = work;
				break;
			}
		}
		
		return ret;
	}
	
	private static class InventoryProvider implements ICapabilitySerializable<NBTBase> {

		private final IItemHandler inv = new ItemStackHandler(27);

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inv);
			else return null;
		}

		@Override
		public NBTBase serializeNBT() {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inv, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt) {
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inv, null, nbt);
		}
	}
	
}
