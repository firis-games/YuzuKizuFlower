package firis.yuzukizuflower.common.tileentity.handler;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * 溶岩管理用FluidHandler
 * @author computer
 *
 */
public class YKLavaFluidHandler extends YKOnlyFluidHandler {
	
	/**
	 * コンストラクタ
	 */
	public YKLavaFluidHandler(TileEntity tile, int maxLiquid) {
		super(FluidRegistry.getFluid("lava"), tile, maxLiquid);
	}	
}
