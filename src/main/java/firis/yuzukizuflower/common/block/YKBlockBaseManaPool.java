package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.tileentity.YKTileBaseManaPool;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
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
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.handler.HUDHandler;

/**
 * 箱入りお花のベースクラス
 * @author computer
 *
 */
public abstract class YKBlockBaseManaPool extends BlockContainer implements IWandHUD, IWandable {

	/**
	 * ブロックMaterial設定
	 */
	public static final Material BOXED_FLOWER = new Material(MapColor.FOLIAGE) {
		{
			this.setImmovableMobility();
		}
		@Override
		public boolean isOpaque()
	    {
	        return false;
	    }
	};
	
	/**
	 * GUI_ID
	 */
	protected int GUI_ID;

	/**
	 * コンストラクタ
	 * @param materialIn
	 */
	protected YKBlockBaseManaPool() {
		
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
	
	/**
	 * ブロック破壊の際に内部インベントリのアイテムをばら撒く
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    /**
     * レッドストーン信号の間接信号を通常のブロックと同じように伝達する
     */
	@Override
    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
		//return state.isNormalCube();
		return true;
    }
    
    /**
     * TileEntityを設定する
     */
	@Override
	public abstract TileEntity createNewTileEntity(World worldIn, int meta);
	
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
		
		ItemStack handItem = playerIn.getHeldItem(hand);
		//杖系だとなにもしない
		if (handItem.getItem() instanceof ICoordBoundItem) {
			//森の杖の場合
			return onBlockActivatedICoordBoundItem(worldIn, pos, state, playerIn, hand, facing, hitZ, hitZ, hitZ);
		}
		
    	//右クリックでGUIを開く
		playerIn.openGui(YuzuKizuFlower.INSTANCE, GUI_ID,
				worldIn, pos.getX(), pos.getY(), pos.getZ());
		
    	return true;
    }
	
	/**
	 * メインハンドが森の杖の場合の処理
	 * @return
	 */
	protected boolean onBlockActivatedICoordBoundItem(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return false;
	}
	
	/**
	 * ****************************************************************************************************
	 * IWandHUD
	 * ****************************************************************************************************
	 */
	/**
	 * @interface IWandHUD
	 * Wandを持っている際の表示制御
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {

		//マナプール機能有無
		if (!(world.getTileEntity(pos) instanceof YKTileBaseManaPool)) {
        	return;
        }
		
		YKTileBaseManaPool tileEntity = (YKTileBaseManaPool)world.getTileEntity(pos);
		
		
		//ブロック名
		//String name = I18n.format("tile." + this.getRegistryName() + ".name");
		String name = this.getLocalizedName();
		int color = 0x4444FF;
		HUDHandler.drawSimpleManaHUD(color, tileEntity.getMana(), tileEntity.getMaxMana(), name, res);
		
	}
	
	/**
	 * ****************************************************************************************************
	 * IWandable
	 * ****************************************************************************************************
	 */
	/**
	 * @interface IWandable
	 * Wandを使用した際の制御 何もしない
	 */
	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		return false;
	}
}
