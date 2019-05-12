package firis.yuzukizuflower.common.world.generator;

import java.util.Random;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AltGrassVariant;
import vazkii.botania.common.block.ModBlocks;

/**
 * アルフヘイムチェスト設置
 * @author computer
 *
 */
public class WorldGenAlfheimTreasureChest extends WorldGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		
		//基準座標を計算
		position = this.getBaseBlockPos(world, position);
		
		//土台部分を生成
		this.generateBase(world, rand, position);
		
		//assetsからテンプレートを取得
		WorldServer worldserver = (WorldServer)world;
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        Template template = templatemanager.getTemplate(null,
        		new ResourceLocation(YuzuKizuFlower.MODID, "alfheim/alfheim_treasure_chest"));
		
        PlacementSettings placementsettings =  new PlacementSettings();
        
        BlockPos pos = position;
        //位置調整
        pos = position.up().north(1).west(1);
        //構造体設置
        template.addBlocksToWorldChunk(world, pos, placementsettings);
        
        TileEntity tile;
        
        //スポナーを設置
        pos = position.up(1);
        world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
        tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityMobSpawner) {
        	((TileEntityMobSpawner)tile).getSpawnerBaseLogic()
        	.setEntityId(
        			net.minecraftforge.common.DungeonHooks.getRandomDungeonMob(rand));       	
        }
        
        //シェルカーボックスを設置
        pos = position.up(3);
        world.setBlockState(pos, Blocks.PURPLE_SHULKER_BOX.getDefaultState(), 2);
        //ルートテーブルを設定する
        tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityLockableLoot) {
        	TileEntityLockableLoot tileLoot = (TileEntityLockableLoot) tile;
        	tileLoot.setLootTable(
        			//LootTableList.CHESTS_SIMPLE_DUNGEON,
        			new ResourceLocation(YuzuKizuFlower.MODID, "chests/alfheim_chest"),
        			rand.nextLong());
        }
        
		return true;
	}
	
	/**
	 * 基準座標を計算
	 * @param basePos
	 * @return
	 */
	public BlockPos getBaseBlockPos(World world, BlockPos basePos) {
	
		//上のブロックが液体の場合は処理しない(高さ128まで計算)
		basePos = basePos.up();
		while (basePos.getY() < 128) {
			IBlockState state = world.getBlockState(basePos);
			if(!state.getMaterial().isLiquid()) {
				break;
			}
			basePos = basePos.up();
		}
		basePos = basePos.down();
		
		return basePos;
	}
	
	/**
	 * 構造物の土台を生成する
	 * @param world
	 * @param rand
	 * @param basePos
	 */
	public void generateBase(World world, Random rand, BlockPos basePos) {
		
		//一定空間を空にする
		int range = 3;
		for (BlockPos pos : BlockPos.getAllInBox(
				basePos.north(range).west(range).up(10),
				basePos.south(range).east(range).up())) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
		}
		
		IBlockState baseState;
		
		//土を変更
		baseState = ModBlocks.altGrass.getDefaultState().withProperty(BotaniaStateProps.ALTGRASS_VARIANT, AltGrassVariant.INFUSED);
		for (BlockPos pos : BlockPos.getAllInBox(
				basePos.north(range).west(range).down(1),
				basePos.south(range).east(range))) {
			if (world.getBlockState(pos).getBlock() == Blocks.DIRT) {
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			}
		}
				
		//一定空間に土台をつくる
		range = 2;
		baseState = ModBlocks.enchantedSoil.getDefaultState();
		for (BlockPos pos : BlockPos.getAllInBox(
				basePos.north(range).west(range),
				basePos.south(range).east(range))) {
			world.setBlockState(pos, baseState, 2);
		}
		//土
		baseState = Blocks.DIRT.getDefaultState();
		for (BlockPos pos : BlockPos.getAllInBox(
				basePos.north(range).west(range).down(1),
				basePos.south(range).east(range).down(3))) {
			world.setBlockState(pos, baseState, 2);
		}
		//リビングロック
		baseState = ModBlocks.livingrock.getDefaultState();
		for (BlockPos pos : BlockPos.getAllInBox(
				basePos.north(range).west(range).down(4),
				basePos.south(range).east(range).down(7))) {
			world.setBlockState(pos, baseState, 2);
		}
	}
}
