package firis.yuzukizuflower.common.world.generator;

import java.util.Random;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
 * アルフヘイムポータル設置
 * @author computer
 *
 */
public class WorldGenAlfheimPortal extends WorldGenerator {

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
        		new ResourceLocation(YuzuKizuFlower.MODID, "alfheim/alfheim_portal"));
		
        PlacementSettings placementsettings =  new PlacementSettings();
        
        BlockPos pos = position;

        //位置調整
        pos = position.up().north(5).west(5);
        
        //構造体設置
        template.addBlocksToWorldChunk(world, pos, placementsettings);
        
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
		int range = 8;
		for (BlockPos pos : BlockPos.getAllInBox(
				basePos.north(range).west(range),
				basePos.south(range).east(range).up())) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
		}
		range = 11;
		for (BlockPos pos : BlockPos.getAllInBox(
				basePos.north(range).west(range).up(range),
				basePos.south(range).east(range).up(2))) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
		}
		
		IBlockState baseState;
		
		//土を変更マナ土に変更
		range = 15;
		baseState = ModBlocks.altGrass.getDefaultState().withProperty(BotaniaStateProps.ALTGRASS_VARIANT, AltGrassVariant.INFUSED);
		for (BlockPos pos : BlockPos.getAllInBox(
				basePos.north(range).west(range).down(3),
				basePos.south(range).east(range).up(3))) {
			if (world.getBlockState(pos).getBlock() == Blocks.DIRT
					&& world.getBlockState(pos.up()).getBlock() == Blocks.AIR) {
				world.setBlockState(pos, baseState, 2);
			}
		}
		
		//一定空間に土台をつくる
		range = 8;
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
				basePos.south(range).east(range).down(4))) {
			world.setBlockState(pos, baseState, 2);
		}
		//リビングロック
		baseState = ModBlocks.livingrock.getDefaultState();
		for (BlockPos pos : BlockPos.getAllInBox(
				basePos.north(range).west(range).down(5),
				basePos.south(range).east(range).down(15))) {
			world.setBlockState(pos, baseState, 2);
		}
		//土をランダムに設置する
		baseState = Blocks.DIRT.getDefaultState();
		for (BlockPos pos : BlockPos.getAllInBox(
				basePos.north(range).west(range).down(5),
				basePos.south(range).east(range).down(15))) {
			if (rand.nextInt(6) == 0) {
				world.setBlockState(pos, baseState, 2);
			}
		}
	}
}
