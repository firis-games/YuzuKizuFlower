package firis.yuzukizuflower.common.world.generator;

import java.util.Random;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

/**
 * アルフヘイムポータル設置
 * @author computer
 *
 */
public class WorldGenAlfheimPortal extends WorldGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		
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

}
