package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedThermalily;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockBoxedThermalily extends YKBlockBaseManaPool {

	/**
	 * コンストラクタ
	 */
	public YKBlockBoxedThermalily() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_THERMALILY;

	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedThermalily();
	}
}
