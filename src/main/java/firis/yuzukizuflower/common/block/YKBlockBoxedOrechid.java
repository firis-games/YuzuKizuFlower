package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedOrechid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * 箱入りオアキド
 * @author computer
 *
 */
public class YKBlockBoxedOrechid extends YKBlockBaseManaPool {

	public YKBlockBoxedOrechid() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.YK_BOXED_ORECHID;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedOrechid();
	}
	
}
