package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileScrollChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class YKBlockScrollChest extends YKBlockBaseChest {

	public YKBlockScrollChest() {

	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileScrollChest();
	}
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		//メインのみ
		if (hand == EnumHand.OFF_HAND) {
			return false;
		}

    	//右クリックでGUIを開く
		playerIn.openGui(YuzuKizuFlower.INSTANCE, YKGuiHandler.SCROLL_CHEST,
				worldIn, pos.getX(), pos.getY(), pos.getZ());
		
    	return true;
    }
	
	/**
     * インベントリ内のアイテム保持
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    	TileEntity tileentity = worldIn.getTileEntity(pos);
        
    	if (tileentity instanceof YKTileScrollChest)
        {
    		YKTileScrollChest tile = (YKTileScrollChest) tileentity;
        	
        	ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
        	
        	NBTTagCompound nbt = tile.serializeNBT();
        	stack.setTagInfo("BlockEntityTag", nbt);
        	spawnAsEntity(worldIn, pos, stack);
        }
    	
    	//共通側で内部インベントリのドロップ処理を行う
    	super.breakBlock(worldIn, pos, state);
    }
    
    /**
     * breakBlockで手動ドロップ処理を行っているため
     * 標準のドロップ処理は無効化する
     */
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
    }
}
