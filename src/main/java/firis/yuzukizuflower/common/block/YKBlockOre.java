package firis.yuzukizuflower.common.block;

import java.util.Random;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class YKBlockOre extends Block {

	/**
	 * 鉱石タイプ
	 */
	public static enum OreType {
		
		PIXIE(1, "botania:manaresource", 8, 3, 3, 0, 2),
		ELVEN(1, "botania:quartz", 5, 2, 5, 1, 3),
		ELEMENTIUM(2, "", 0, 1, 0, 3, 7),
		DRAGON(3, "botania:manaresource", 9, 1, 1, 3, 7),
		GAIA(3, "botania:manaresource", 5, 1, 1, 5, 10)
		;
		
		private OreType(int harvestLevel, String item, int meta, int drop, int randomDrop, int expMin, int expMax) {
			this.harvestLevel = harvestLevel;
			this.item = item;
			this.meta = meta;
			this.drop = drop;
			this.randomDrop = randomDrop;
			this.expMin = expMin;
			this.expMax = expMax;
		}
		private int harvestLevel = 0;
		public int getHarvestLevel() {
			return this.harvestLevel;
		}
		private String item = "";
		public Item getItem() {
			Item item = Item.getByNameOrId(this.item);
			return item;
		}
		private int meta = 0;
		public int getMeta() {
			return this.meta;
		}
		private int drop = 0;
		public int getDrop() {
			return this.drop;
		}
		private int randomDrop = 0;
		public int getRandomDrop() {
			return this.randomDrop;
		}
		private int expMin = 0;
		public int getExpMin() {
			return this.expMin;
		}
		private int expMax = 0;
		public int getExpMax() {
			return this.expMax;
		}
	}
	
	public OreType oreType;
	
	public YKBlockOre(OreType oreType) {
		this(oreType, Material.ROCK.getMaterialMapColor());
	}

	/**
	 * コンストラクタ
	 * @param color
	 */
	public YKBlockOre(OreType oreType, MapColor color) {
        super(Material.ROCK, color);
        
        this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
        
        this.oreType = oreType;
        
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.STONE);
        
        this.setHarvestLevel("pickaxe", oreType.getHarvestLevel());
        
    }
	
	/**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	Item item = oreType.getItem();
    	if (item == null) {
    		return Item.getItemFromBlock(this);
    	}
    	return item;
    }
    
    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random)
    {
        return oreType.getDrop() + (oreType.getRandomDrop() == 0 ? 0 : random.nextInt(oreType.getRandomDrop()));
    }
    
    /**
     * Get the quantity dropped based on the given fortune level
     */
    @Override
    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        if (fortune > 0 && 
        		Item.getItemFromBlock(this) != this.getItemDropped((IBlockState)this.getBlockState().getValidStates().iterator().next(), random, fortune)){
            int i = random.nextInt(fortune + 2) - 1;

            if (i < 0)
            {
                i = 0;
            }

            return this.quantityDropped(random) * (i + 1);
        }
        else
        {
            return this.quantityDropped(random);
        }
    }
    
    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
        {
            int i = 0;
            i = MathHelper.getInt(rand, oreType.getExpMin(), oreType.getExpMax());
            return i;
        }
        return 0;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this);
    }
    
    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    @Override
    public int damageDropped(IBlockState state)
    {
        return oreType.getMeta();
    }
	
}
