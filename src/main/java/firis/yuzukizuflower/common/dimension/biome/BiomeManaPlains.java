/**
 * 
 */
package firis.yuzukizuflower.common.dimension.biome;

import java.util.Random;

import firis.yuzukizuflower.YuzuKizuFlower;
import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuBlocks;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import vazkii.botania.common.block.ModBlocks;

/**
 * @author computer
 *
 */
public class BiomeManaPlains extends Biome {

	protected final IBlockState cstmSTONE = ModBlocks.livingrock.getDefaultState();
	protected final IBlockState cstmWATER = YuzuKizuBlocks.LIQUID_MANA.getDefaultState();
	
	@SuppressWarnings("deprecation")
	public BiomeManaPlains() {
		
		super(getBiomeProperties());
		this.setRegistryName(new ResourceLocation(YuzuKizuFlower.MODID, "mana_plains"));
		
		//decoratorの設定
        this.decorator.treesPerChunk = 0;
        this.decorator.extraTreeChance = 0.1F;
        this.decorator.flowersPerChunk = 8;
        this.decorator.grassPerChunk = 1;
        
		this.topBlock = ModBlocks.altGrass.getStateFromMeta(4);
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        
        //スポーン対象再設定
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();

        //動物のみスポーンさせる
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntitySheep.class, 8, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPig.class, 6, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityChicken.class, 6, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityCow.class, 6, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityOcelot.class, 4, 1, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityParrot.class, 4, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityWolf.class, 4, 1, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityHorse.class, 5, 2, 6));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityDonkey.class, 1, 1, 3));
	}
	/**
	 * Biome初期設定
	 * @return
	 */
	public static BiomeProperties getBiomeProperties() {
		BiomeProperties properties = new BiomeProperties("Mana Plains");
		return properties;
	}
	
	
	/**
	 * バイオーム構造設定
	 * @param worldIn
	 * @param rand
	 * @param chunkPrimerIn
	 * @param x
	 * @param z
	 * @param noiseVal
	 */
	@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
		int i = worldIn.getSeaLevel();
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate1 = this.fillerBlock;
        int j = -1;
        int k = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = x & 15;
        int i1 = z & 15;

        for (int j1 = 255; j1 >= 0; --j1)
        {
            if (j1 <= rand.nextInt(5))
            {
                chunkPrimerIn.setBlockState(i1, j1, l, BEDROCK);
            }
            else
            {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

                if (iblockstate2.getMaterial() == Material.AIR)
                {
                    j = -1;
                }
                else if (iblockstate2.getBlock() == cstmSTONE.getBlock())
                {
                    if (j == -1)
                    {
                        if (k <= 0)
                        {
                            iblockstate = AIR;
                            iblockstate1 = cstmSTONE;
                        }
                        else if (j1 >= i - 4 && j1 <= i + 1)
                        {
                            iblockstate = this.topBlock;
                            iblockstate1 = this.fillerBlock;
                        }

                        if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR))
                        {
                        	iblockstate = cstmWATER;
                        }

                        j = k;

                        if (j1 >= i - 1)
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
                        }
                        else if (j1 < i - 7 - k)
                        {
                            iblockstate = AIR;
                            iblockstate1 = cstmSTONE;
                            chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL);
                        }
                        else
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                        }
                    }
                    else if (j > 0)
                    {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

                        if (j == 0 && iblockstate1.getBlock() == Blocks.SAND && k > 1)
                        {
                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
                            iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
                        }
                    }
                }
            }
        }
    }
}
