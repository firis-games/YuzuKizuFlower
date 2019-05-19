package firis.yuzukizuflower.common.block;

import java.text.NumberFormat;
import java.util.List;

import javax.annotation.Nullable;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileManaTank;
import firis.yuzukizuflower.common.tileentity.YKTileManaTankExtends;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKBlockManaTank extends YKBlockBaseManaPool {

	public static final PropertyInteger TIER = PropertyInteger.create("tier", 0, 3);
	
	/**
	 * コンストラクタ
	 */
	public YKBlockManaTank() {
		super();
		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.MANA_TANK;
		
		this.setDefaultState(this.blockState.getBaseState()
        		.withProperty(TIER, Integer.valueOf(0)));
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileManaTankExtends(meta);
	}
	
    /**
     * マナタンクのゲージ保存処理
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    	TileEntity tileentity = worldIn.getTileEntity(pos);
        
    	if (tileentity instanceof YKTileManaTank)
        {
    		YKTileManaTank tileManaTank = (YKTileManaTank) tileentity;
        	int meta = tileManaTank.getMetadata();
        	ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1, meta);
        	
        	if (tileManaTank.getMana() > 0) {
        		NBTTagCompound nbt = new NBTTagCompound();
        		nbt.setInteger("mana", ((YKTileManaTank)tileentity).getMana());
        		stack.setTagInfo("BlockEntityTag", nbt);
        	}
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
    
    /**
     * アイテム状態でインフォメーションを表示する
     */
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
    
    /** メタデータ対応 */
    /************************************************************************/
    @Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {TIER});
    }
    
    /**
     * Convert the given metadata into a BlockState for this Block
     */
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TIER, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
	@Override
    public int getMetaFromState(IBlockState state)
    {
		return state.getValue(TIER);
    }
	
	@Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
		for (int i = 0; i < TIER.getAllowedValues().size(); i++) {
			items.add(new ItemStack(this, 1, i));
		}
    }
	
	/**
	 * ドロップ制御
	 */
	@Override
    public int damageDropped(IBlockState state)
    {
		return getMetaFromState(state);
    }
}