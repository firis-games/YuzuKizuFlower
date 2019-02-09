package firis.yuzukizuflower.block;

import java.text.NumberFormat;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.container.YKGuiContainerManaTank;
import firis.yuzukizuflower.gui.YKGuiHandler;
import firis.yuzukizuflower.tileentity.YKTileBoxedEndoflame;
import firis.yuzukizuflower.tileentity.YKTileManaTank;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.handler.HUDHandler;

//public class YKBlockBoxedEndoflame extends BlockContainer implements IWandHUD, IWandable {
public class YKBlockManaTank extends YKBotaniaGenBase
								implements IWandHUD, IWandable {

	public YKBlockManaTank() {
		super(Material.PISTON);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileManaTank();
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
		playerIn.openGui(YuzuKizuFlower.INSTANCE, YKGuiHandler.MANA_TANK, 
				worldIn, pos.getX(), pos.getY(), pos.getZ());
    	return true;
    }
	
	
	@SideOnly(Side.CLIENT)
	@Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }
	
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
    	return true;
    }
    
    
    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        
        if (tileentity instanceof YKTileManaTank)
        {
        	
        	YKTileManaTank tileManaTank = (YKTileManaTank) tileentity;
        	
        	ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
        	
        	if (tileManaTank.getMana() > 0) {
        		NBTTagCompound nbt = new NBTTagCompound();
        		nbt.setInteger("mana", ((YKTileManaTank)tileentity).getMana());
        		stack.setTagInfo("BlockEntityTag", nbt);
        	}
        	//マナ以外は放出する
        	spawnAsEntity(worldIn, pos, stack);
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    //ドロップを抑制する
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        super.addInformation(stack, player, tooltip, advanced);
        
        if(stack.hasTagCompound()) {
        	NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("BlockEntityTag");
        	
        	Integer mana = nbt.getInteger("mana");
        	
        	tooltip.add(NumberFormat.getNumberInstance().format(mana) + " Mana");
        }        
    }
	
	//IWandHUD
	//******************************************************************************************
	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		
		if (!(world.getTileEntity(pos) instanceof YKTileManaTank)) {
        	return;
        }
		YKTileManaTank te = (YKTileManaTank)world.getTileEntity(pos);
		
		//ItemStack pool = new ItemStack(YuzuKizuBlocks.BOXED_ENDOFLAME);
		//String name = I18n.format(pool.getUnlocalizedName().replaceAll("tile.", "tile." + LibResources.PREFIX_MOD) + ".name");
		String name = I18n.format("tile.mana_tank.name");
		int color = 0x4444FF;
		HUDHandler.drawSimpleManaHUD(color, te.getMana(), te.getMaxMana(), name, res);
		
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
	}
	//******************************************************************************************
	
	//IWandable
	//******************************************************************************************
	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
	//******************************************************************************************

	
}
