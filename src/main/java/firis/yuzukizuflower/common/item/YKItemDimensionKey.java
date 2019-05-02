package firis.yuzukizuflower.common.item;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.dimension.DimensionHandler;
import firis.yuzukizuflower.common.dimension.TeleporterAlfheim;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
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
     * Called when the equipped item is right clicked.
     */
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if(worldIn.isRemote) return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
		
		playerIn.setPosition(0, 128, 0);
		
		WorldServer server = (WorldServer) worldIn;
		playerIn.changeDimension(DimensionHandler.dimensionAlfheim.getId(), new TeleporterAlfheim(server));
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
		
    }
}
