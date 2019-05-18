package firis.yuzukizuflower.common.container;

import java.util.Arrays;
import java.util.List;

import firis.yuzukizuflower.common.YKConfig;
import firis.yuzukizuflower.common.inventory.IScrollInventory;
import firis.yuzukizuflower.common.item.YKItemRemoteChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKContainerRemoteChest extends YKContainerBaseScrollInventory {

	public YKContainerRemoteChest(IScrollInventory iinv, InventoryPlayer playerInv, boolean keyMode) {
		super(iinv, playerInv);
		
		ItemStack stack = playerInv.player.getHeldItemMainhand();
		if (keyMode) {
			stack = YKItemRemoteChest.getBaublesItemStack(playerInv.player);
		}
		if(stack.hasTagCompound()) {
        	NBTTagCompound nbt = stack.getTagCompound();
        	Integer posX = nbt.getInteger("BlockPosX");
        	Integer posY = nbt.getInteger("BlockPosY");
        	Integer posZ = nbt.getInteger("BlockPosZ");
        	IBlockState state = playerInv.player.getEntityWorld().getBlockState(new BlockPos(posX, posY, posZ));
        	
        	List<String> config = Arrays.asList(YKConfig.REMOTE_CHEST_WHITE_LIST);
        	ResourceLocation remoteBlockId = state.getBlock().getRegistryName();
        	
        	int configIdx = config.indexOf(remoteBlockId.toString());

        	if (configIdx != -1 ) {
        		isTransferStackInSlot = true;
        	}
        	
        }
	}
	
	protected boolean isTransferStackInSlot = false;

	/**
	 * 外部のIItemHandlerを使う場合は制御方法を考える必要がある
	 * 一旦処理を強制停止
	 */
	@Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
		if (this.isTransferStackInSlot) {
			return super.transferStackInSlot(playerIn, index);
		}
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
