package firis.yuzukizuflower.common.item;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.world.generator.WorldGenHouse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class YKItemInstantHouse extends Item {

	/**
	 * コンストラクタ
	 */
	public YKItemInstantHouse() {

		super();
		//初期化
		this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
		this.setMaxStackSize(64);
	}
	
	/**
     * Called when a Block is right-clicked with this Item
     */
	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		boolean ret = createInstantHouse(player, hand, pos);
		if (!ret) {
			return EnumActionResult.PASS;
    	}
		return EnumActionResult.SUCCESS;
    }
	
	/**
	 * インスタントハウス生成処理
	 * 常に生成する
	 * @param player
	 * @param hand
	 * @return
	 */
	protected boolean createInstantHouse(EntityPlayer player, EnumHand hand, BlockPos pos) {
		
		World world = player.getEntityWorld();
		
		//アイテムを消費
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getItem() != this) return false;
		
		stack.shrink(1);
		
		if (world.isRemote) return true;

		//Playerの向いてる方向
		EnumFacing playerFacing = player.getHorizontalFacing();
		
		WorldGenHouse gen = new WorldGenHouse(playerFacing);
		gen.generate(world, world.rand, pos);

        return true;
	}
}
