package firis.yuzukizuflower.common.block;

import java.util.Random;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuBlocks;
import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedEndoflame;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedPureDaisy;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

//public class YKBlockBoxedEndoflame extends BlockContainer implements IWandHUD, IWandable {
public class YKBlockBoxedEndoflameBK extends BlockContainer {

	public YKBlockBoxedEndoflameBK(int mode) {
		super(Material.GLASS);
	}
	
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

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedEndoflame(1);
	}

	
	/**
     * Called when the block is right clicked by a player.
     */
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    	//右クリックでGUIを開く
		playerIn.openGui(YuzuKizuFlower.INSTANCE, YKGuiHandler.YK_BOXED_ENDOFLAME, 
				worldIn, pos.getX(), pos.getY(), pos.getZ());
    	return true;
    }
	
	
	
	
	
	@SideOnly(Side.CLIENT)
	@Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
        
        if (!(worldIn.getTileEntity(pos) instanceof YKTileBoxedEndoflame)) {
        	return;
        }
        YKTileBoxedEndoflame te = (YKTileBoxedEndoflame)worldIn.getTileEntity(pos);

        //処理してないときはエフェクトださない
        if (te.timer == 0) {
        	return;
        }
        
        //worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX()+ 0.5, pos.getY() + 0.5, pos.getZ()+ 0.5, 0.0D, 0.0D, 0.0D);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, 
        		pos.getX() + 0.5, 
        		pos.getY() + 0.7, 
        		pos.getZ() + 0.5,
        		0.0D, 0.0D, 0.0D);
    }

	//IWandHUD
	//******************************************************************************************
	@SideOnly(Side.CLIENT)
	//@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		
		if (!(world.getTileEntity(pos) instanceof YKTileBoxedEndoflame)) {
        	return;
        }
        YKTileBoxedEndoflame te = (YKTileBoxedEndoflame)world.getTileEntity(pos);
        
		//SubTileGeneratingでやっていることを実装
		String name = I18n.format("tile.botania:flower." + getUnlocalizedName() + ".name");
		//int color = getColor();
		int color = 0x785000;
		BotaniaAPI.internalHandler.drawComplexManaHUD(color, 
				te.getMana(), 
				//getMaxMana(), 
				te.getMaxMana(),
				name, 
				res, 
				//BotaniaAPI.internalHandler.getBindDisplayForFlowerType(this),
				new ItemStack(YuzuKizuBlocks.BOXED_ENDOFLAME),
				//isValidBinding());
				true);
	}
	//******************************************************************************************
	
	//IWandable
	//******************************************************************************************
	//@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
	//******************************************************************************************


}
