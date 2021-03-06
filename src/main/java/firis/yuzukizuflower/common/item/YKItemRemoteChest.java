package firis.yuzukizuflower.common.item;

import java.util.List;

import javax.annotation.Nullable;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.YKGuiHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@EventBusSubscriber
public class YKItemRemoteChest extends Item implements IBauble {
	
	public YKItemRemoteChest() {
		super();
		
		//初期化
		this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
		this.setMaxStackSize(1);
		
	}
	
	/**
     * Called when a Block is right-clicked with this Item
     */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (EnumHand.MAIN_HAND != hand) return EnumActionResult.PASS;
		
		ItemStack stack = player.getHeldItemMainhand();
		
		if (stack.getItem() instanceof YKItemRemoteChest) {
			
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile != null) {
				IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				if (capability != null) {

					String blockName = worldIn.getBlockState(pos).getBlock().getLocalizedName();
					
					//BlockPosを保存
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("BlockName", blockName);
					nbt.setInteger("BlockPosX", pos.getX());
					nbt.setInteger("BlockPosY", pos.getY());
					nbt.setInteger("BlockPosZ", pos.getZ());
					nbt.setInteger("Dimension", worldIn.provider.getDimension());
					stack.setTagCompound(nbt);
				}
			}
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
		if (EnumHand.MAIN_HAND != handIn) return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
		
		ItemStack stack = playerIn.getHeldItemMainhand();
				
		//条件を満たす場合にGuiを開く
		if(YKItemRemoteChest.openGui(stack, playerIn, false)) {
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
	
	/**
	 * 条件を満たす場合にGuiを表示する
	 * @param stack
	 * @param player
	 * @return
	 */
	public static boolean openGui(ItemStack stack, EntityPlayer player, boolean keyMode) {
		
		if(!stack.isEmpty() && stack.hasTagCompound()) {
        	NBTTagCompound nbt = stack.getTagCompound();
        	
        	Integer posX = nbt.getInteger("BlockPosX");
        	Integer posY = nbt.getInteger("BlockPosY");
        	Integer posZ = nbt.getInteger("BlockPosZ");
        	Integer dim = nbt.getInteger("Dimension");
        	
        	BlockPos pos = new BlockPos(posX, posY, posZ);
        	
    		WorldServer dimWorld = DimensionManager.getWorld(dim, false);
    		if (dimWorld == null) return false;
        	if (!dimWorld.isBlockLoaded(pos)) return false;
    		
        	TileEntity tile = dimWorld.getTileEntity(pos);
        	
        	if (tile != null && !tile.isInvalid()) {
        		IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        		if (capability != null) {
        			int guiid = YKGuiHandler.REMOTE_CHEST;
        			if (keyMode) guiid = YKGuiHandler.REMOTE_CHEST_KEY;
        			
	            	//右クリックでGUIを開く
        			player.openGui(YuzuKizuFlower.INSTANCE, guiid,
        					dimWorld, posX, posY, posZ);
        			return true;
        		}
        	}
		}
		return false;
	}
	
	/**
     * Informationを表示する
     */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        super.addInformation(stack, player, tooltip, advanced);
        
        if(stack.hasTagCompound()) {
        	NBTTagCompound nbt = stack.getTagCompound();
        	
        	String name = nbt.getString("BlockName");
        	Integer posX = nbt.getInteger("BlockPosX");
        	Integer posY = nbt.getInteger("BlockPosY");
        	Integer posZ = nbt.getInteger("BlockPosZ");
        	Integer dim = nbt.getInteger("Dimension");
        	String dimName = DimensionManager.getProviderType(dim).getName();
        	
        	tooltip.add(name); 
        	tooltip.add(dimName);
        	tooltip.add("<" + posX.toString() + ", " + posY.toString() + ", " + posZ.toString() + ">");
        }
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
    	if(stack.hasTagCompound()) {
        	NBTTagCompound nbt = stack.getTagCompound();
        	
        	Integer posX = nbt.getInteger("BlockPosX");
        	Integer posY = nbt.getInteger("BlockPosY");
        	Integer posZ = nbt.getInteger("BlockPosZ");
        	Integer dim = nbt.getInteger("Dimension");
        	
        	BlockPos pos = new BlockPos(posX, posY, posZ);
        	WorldServer dimWorld = DimensionManager.getWorld(dim, false);
    		if (dimWorld == null) return false;
        	if (!dimWorld.isBlockLoaded(pos)) return false;
    		TileEntity tile = dimWorld.getTileEntity(pos);
    		if (tile == null) return false;
        	
    		if (tile != null && !tile.isInvalid()) {
        		IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        		if (capability != null) {
        			return true;
        		}
    		}    		
        }
        return false;
    }

    /******************************************************************************************/
    
    /**
     * NBTからブロックの登録情報を取得する
     * @param stack
     * @return
     */
    private static BlockPos getNbtBlockPos(ItemStack stack) {
    	
    	BlockPos pos = null;
    	if(stack.hasTagCompound()) {
        	NBTTagCompound nbt = stack.getTagCompound();        	
        	Integer posX = nbt.getInteger("BlockPosX");
        	Integer posY = nbt.getInteger("BlockPosY");
        	Integer posZ = nbt.getInteger("BlockPosZ");
        	
        	pos = new BlockPos(posX, posY, posZ);
        }
    	
    	return pos;
    }
    
    
    /******************************************************************************************/

    /**
     * @interface IBauble
     */
	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}

	/******************************************************************************************/
	
	/**
	 * Baublesスロットの縁結びの輪を取得する
	 */
	public static ItemStack getBaublesItemStack(EntityPlayer player) {
		//アミュレット枠
		IBaublesItemHandler baublesHandler = BaublesApi.getBaublesHandler(player);
		
		ItemStack chest = ItemStack.EMPTY.copy();
		for (int i = 0; i < BaubleType.AMULET.getValidSlots().length; i++) {
			int slot = BaubleType.AMULET.getValidSlots()[i];
			
			ItemStack work = baublesHandler.getStackInSlot(slot);
			
			if (work.getItem() instanceof YKItemRemoteChest) {
				//チェストの登録情報をもっていること
				BlockPos pos = getNbtBlockPos(work);
				if(pos != null) {
					chest = work;
					break;
				}
			}
		}
		return chest;
	}
	
	/**
	 * アイテム回収イベント
	 * @param event
	 */
	@SubscribeEvent
	public static void onItemPickup(EntityItemPickupEvent event) {
		
		EntityPlayer player = event.getEntityPlayer();
		if (player == null) return;
		
		//アミュレット枠から縁結びの輪を取得する
		ItemStack chest = YKItemRemoteChest.getBaublesItemStack(player);
		
		if (chest.isEmpty()) return;
		
		BlockPos pos = YKItemRemoteChest.getNbtBlockPos(chest);
		World world = player.getEntityWorld();
		IItemHandler capability = null;
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		}
		
		if (capability == null) return;
		
		ItemStack stack = event.getItem().getItem();
		
		//自動格納を行う
		//移動のシミュレート
		ItemStack simInsStack = stack.copy();
		for (int cabSlot = 0; cabSlot < capability.getSlots(); cabSlot++) {
			//insert
			simInsStack = capability.insertItem(cabSlot, simInsStack, true);
			if (simInsStack.isEmpty()) {
				break;
			}
		}
		
		//移動の結果
		if (stack.getCount() == simInsStack.getCount()) {
			//移動できていないので対象外
			return;
		}
		
		//実際のインベントリを操作してアイテムを移動させる
		int insStackCount = stack.getCount() - simInsStack.getCount();
		
		//チェスト格納用アイテム
		ItemStack insItemStack = stack.copy();
		insItemStack.setCount(insStackCount);

		//アイテムの数を減らす
		stack.setCount(simInsStack.getCount());
		
		for (int cabSlot = 0; cabSlot < capability.getSlots(); cabSlot++) {
			//insert
			insItemStack = capability.insertItem(cabSlot, insItemStack, false);
			if (insItemStack.isEmpty()) {
				break;
			}
		}
		
		event.setResult(Result.ALLOW);
	}
	
}
