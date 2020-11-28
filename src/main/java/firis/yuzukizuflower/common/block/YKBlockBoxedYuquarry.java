package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.botania.BotaniaHelper;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedYuquarry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class YKBlockBoxedYuquarry extends YKBlockBaseManaPool {

	public YKBlockBoxedYuquarry() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_YUQUARRY;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedYuquarry();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		//ショベル系アイテムの場合
		ItemStack handItem = playerIn.getHeldItem(hand);
		if (!handItem.isEmpty() && handItem.getItem().getRegistryName().toString().indexOf("shovel") > -1) {
			YKTileBoxedYuquarry tile = (YKTileBoxedYuquarry)worldIn.getTileEntity(pos);
			tile.changeFlatMode();
			return true;
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

	
	/**
	 * メインハンドが森の杖の場合の処理
	 * @return
	 */
	@Override
	protected boolean onBlockActivatedICoordBoundItem(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		ItemStack handItem = playerIn.getHeldItem(hand);
		
		//機能モード以外
		if (BotaniaHelper.getTwingWandMode(handItem) != BotaniaHelper.TwingWandMode.FUNC) {
			//機能モード以外は何もしない
			return false;
		}
		
		YKTileBoxedYuquarry tile = (YKTileBoxedYuquarry)worldIn.getTileEntity(pos);
		
		
		//杖で叩いた面の方角を処理対象とする
		tile.setFacing(facing);
		
		return true;
	}
}
