package firis.yuzukizuflower.common.tileentity.handler;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * 水管理用FluidHandler
 * @author computer
 *
 */
public class YKWaterFluidHandler extends YKOnlyFluidHandler {
	
	/**
	 * コンストラクタ
	 */
	public YKWaterFluidHandler(TileEntity tile, int maxLiquid) {
		super(FluidRegistry.getFluid("water"), tile, maxLiquid);
	}	
}
