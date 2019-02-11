package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedRannucarpus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class YKBlockBoxedRannucarpus extends YKBlockBaseManaPool {

	public YKBlockBoxedRannucarpus() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_RANNUCARPUS;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedRannucarpus();
	}
	
	/**
	 * メインハンドが森の杖の場合の処理
	 * @return
	 */
	@Override
	protected boolean onBlockActivatedICoordBoundItem(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		ItemStack handItem = playerIn.getHeldItem(hand);
		
		//NBTから杖モードを取得して機能モードの判断
		if (!handItem.hasTagCompound() 
				|| handItem.getTagCompound().getBoolean("bindMode")) {
			//接続モードの場合は何もしない
			return false;
		}
		
		YKTileBoxedRannucarpus tile = (YKTileBoxedRannucarpus)worldIn.getTileEntity(pos);
		
		//モード切替
		tile.changeFlowerMode();
		
		//変更メッセージを表示する
		if (worldIn.isRemote) {
			playerIn.sendMessage(tile.getMessageChangeFlowerMode());
		}
		
		return true;
	}
}
