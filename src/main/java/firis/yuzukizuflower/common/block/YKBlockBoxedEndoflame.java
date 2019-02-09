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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

//public class YKBlockBoxedEndoflame extends BlockContainer implements IWandHUD, IWandable {
public class YKBlockBoxedEndoflame extends YKBotaniaGenBase {

	protected int mode = 0;
	
	public YKBlockBoxedEndoflame() {
		super(Material.GLASS);
	}
	
	public YKBlockBoxedEndoflame(int mode) {
		super(Material.GLASS);
		this.mode = mode;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedEndoflame(mode);
	}

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
        //return BlockRenderLayer.TRANSLUCENT;
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
		
		ItemStack handItem = playerIn.getHeldItem(hand);
		
		//杖系だとなにもしない
		if (handItem.getItem() instanceof ICoordBoundItem ||
				handItem.getItem() instanceof ITileBound) {
			return false;
		}
		
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
	
    /**
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
	

	
}
