package firis.yuzukizuflower.block;

import java.util.Random;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.gui.YKGuiHandler;
import firis.yuzukizuflower.tileentity.YKTileBoxedEndoflame;
import firis.yuzukizuflower.tileentity.YKTileBoxedPureDaisy;
import firis.yuzukizuflower.tileentity.YKTileBoxedRannucarpus;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.ITileBound;

public class YKBlockBoxedRannucarpus extends YKBotaniaGenBase {

	public YKBlockBoxedRannucarpus() {
		super(Material.GLASS);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedRannucarpus();
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
		//if (handItem.getItem() instanceof ICoordBoundItem ||
		//		handItem.getItem() instanceof ITileBound) {
		if (handItem.getItem() instanceof ICoordBoundItem) {
						
			//NBTから杖モードを取得
			if(handItem.hasTagCompound()) {
				boolean flg = handItem.getTagCompound().getBoolean("bindMode");
				//機能モードの場合
				if (!flg) {
					TileEntity tile = worldIn.getTileEntity(pos);
					if (tile instanceof YKTileBoxedRannucarpus) {
						YKTileBoxedRannucarpus tileFlower = (YKTileBoxedRannucarpus)tile;
						tileFlower.changeMode();
						if (worldIn.isRemote) {
							//クライアントだけメッセージをだす
							//TextComponentTranslation langtext = 
							//		new TextComponentTranslation("tool.kjizuna.message", new Object[0]);
							
							String message = tileFlower.getModeName();
							/*
							if (tileFlower.getMode()==1) {
								message = "小さなお花";
							} else if (tileFlower.getMode()==2) {
								message = "小さなマナのお花";
							} else if (tileFlower.getMode()==3) {
								message = "お花";
							} else if (tileFlower.getMode()==4) {
								message = "マナのお花";
							}
							*/
							
							TextComponentTranslation langtext = 
									new TextComponentTranslation(message + "モードに変更しました", new Object[0]);
							
							playerIn.sendMessage(langtext);
							return true;
						}
					}
				}
			}
			
			return false;
		}
		
    	//右クリックでGUIを開く
		playerIn.openGui(YuzuKizuFlower.INSTANCE, YKGuiHandler.YK_BOXED_RANNUCARPUS, 
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
