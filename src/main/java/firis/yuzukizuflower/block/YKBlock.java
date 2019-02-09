package firis.yuzukizuflower.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKBlock extends Block {

	public YKBlock(Material materialIn) {
		super(materialIn);
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
    {
        return true;
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
	 * テクスチャ重ねを有効化
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
    {
        //return BlockRenderLayer.TRANSLUCENT;
        return BlockRenderLayer.CUTOUT_MIPPED;
    }


}
