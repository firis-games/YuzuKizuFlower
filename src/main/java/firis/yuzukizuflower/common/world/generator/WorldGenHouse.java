package firis.yuzukizuflower.common.world.generator;

import java.util.Random;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class WorldGenHouse extends WorldGenerator {

	protected EnumFacing facing;
	
	public WorldGenHouse() {
		this.facing = EnumFacing.NORTH;
	}
	
	public WorldGenHouse(EnumFacing facing) {
		this.facing = facing;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		
		//assetsからテンプレートを取得
		WorldServer worldserver = (WorldServer)world;
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        Template template = templatemanager.getTemplate(null,
        		new ResourceLocation(YuzuKizuFlower.MODID, "house/house"));
		
        PlacementSettings placementsettings =  new PlacementSettings();
        
        BlockPos pos = position;
        
        //位置調整
        //北（標準）
        switch (this.facing) {
        case NORTH:
            pos = position.up().north(9).west(4);
        	break;
        case SOUTH:
        	placementsettings.setRotation(Rotation.CLOCKWISE_180);
            pos = position.up().south(9).east(4);
        	break;
        case EAST:
        	placementsettings.setRotation(Rotation.CLOCKWISE_90);
            pos = position.up().north(4).east(9);
        	break;
        case WEST:
        	placementsettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
            pos = position.up().south(4).west(9);
        	break;
       	default:
        }
        
        
        //構造体設置
        template.addBlocksToWorldChunk(world, pos, placementsettings);
        
		return true;
	}

}
