package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKBlockBaseBoxed extends Block {

	public YKBlockBaseBoxed() {
		
		super(YKBlockBaseManaPool.BOXED_FLOWER);
		
		//共通設定
        this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab)
	        .setHardness(0.8F)
	        .setResistance(20.0F);
	}
	
	/**
	 * キューブ型
	 */
	@Override
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
    /**
     * 重ねた際の描画OFF or ON
     */
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	/**
	 * ブロックが隣接した際の描画
	 * 常に描画する設定
	 */
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
    	return true;
    }
	
	/**
	 * テクスチャ重ねを有効化
	 * TRANSLUCENTの場合はアイテム状態のモデルがおかしくなる（モデル全体が透過してる？）
	 * CUTOUT_MIPPEDの場合は同じサイズのモデルの重ねの場合は正常に表示される（草ブロックと同じ）
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
