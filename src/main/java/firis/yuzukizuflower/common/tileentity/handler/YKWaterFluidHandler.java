package firis.yuzukizuflower.common.tileentity.handler;

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
	public YKWaterFluidHandler(int maxLiquid) {
		super(FluidRegistry.getFluid("water"), maxLiquid);
	}	
}
