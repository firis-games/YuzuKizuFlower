package firis.yuzukizuflower.common.world.dimension;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class DimensionHandler {
	
	public static DimensionType dimensionAlfheim;
	
	/**
	 * dimension初期化
	 */
	public static void init() {
		
		//アルフヘイムDimensionを登録
		dimensionAlfheim = DimensionType.register(
				"Alfheim Dimension", 
				"_alfheim", 
				DimensionManager.getNextFreeDimId(), 
				WorldProviderAlfheim.class, false);
		
		DimensionManager.registerDimension(dimensionAlfheim.getId(), dimensionAlfheim);
	}
}
