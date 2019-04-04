package firis.yuzukizuflower.common.item;

import java.util.List;

import javax.annotation.Nullable;

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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class YKItemRemoteChest extends Item {
	
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
		
		if(stack.hasTagCompound()) {
        	NBTTagCompound nbt = stack.getTagCompound();
        	
        	Integer posX = nbt.getInteger("BlockPosX");
        	Integer posY = nbt.getInteger("BlockPosY");
        	Integer posZ = nbt.getInteger("BlockPosZ");
        	
        	//右クリックでGUIを開く
    		playerIn.openGui(YuzuKizuFlower.INSTANCE, YKGuiHandler.REMOTE_CHEST,
    				worldIn, posX, posY, posZ);
    		
    		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
		
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
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
        	
        	
        	tooltip.add(name + "<" + posX.toString() + ", " + posY.toString() + ", " + posZ.toString() + ">");
        }
    }
	
}
