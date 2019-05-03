package firis.yuzukizuflower.common.item;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.dimension.DimensionHandler;
import firis.yuzukizuflower.common.dimension.TeleporterAlfheim;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class YKItemDimensionKey extends Item {

	/**
	 * コンストラクタ
	 */
	public YKItemDimensionKey() {

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
        return EnumActionResult.SUCCESS;
    }
    
	/**
     * Called when the equipped item is right clicked.
     */
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if(worldIn.isRemote) return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
		
		playerIn.setPosition(0, 128, 0);
		
		WorldServer server;
		if (playerIn.isSneaking()) {
			server = playerIn.getServer().getWorld(DimensionType.OVERWORLD.getId());
			playerIn.changeDimension(DimensionType.OVERWORLD.getId(), new TeleporterAlfheim(server));	
		} else {
			server = playerIn.getServer().getWorld(DimensionHandler.dimensionAlfheim.getId());
			playerIn.changeDimension(DimensionHandler.dimensionAlfheim.getId(), new TeleporterAlfheim(server));
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
		
    }
}
