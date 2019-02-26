package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedYuquarry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockBoxedYuquarry extends YKBlockBaseManaPool {

	public YKBlockBoxedYuquarry() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_YUQUARRY;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedYuquarry();
	}
}
