package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTilePetalWorkbench;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 花びら作業台
 * @author computer
 *
 */
public class YKBlockPetalWorkbench extends BlockContainer {

	public YKBlockPetalWorkbench() {
		super(Material.ROCK);
		
		this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
		this.setHardness(0.8F);
		this.setResistance(20.0F);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
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
    
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTilePetalWorkbench();
	}
	
	
	/**
     * Called when the block is right clicked by a player.
     */
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		
		//メインのみ
		if (hand == EnumHand.OFF_HAND) {
			return false;
		}
		
    	//右クリックでGUIを開く
		playerIn.openGui(YuzuKizuFlower.INSTANCE, YKGuiHandler.PETAL_WORKBENCH,
				worldIn, pos.getX(), pos.getY(), pos.getZ());
		
    	return true;
    }

}
