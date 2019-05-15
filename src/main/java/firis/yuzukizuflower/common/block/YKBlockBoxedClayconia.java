package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedClayconia;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * 箱入りクレイコニア
 * @author computer
 *
 */
public class YKBlockBoxedClayconia extends YKBlockBaseManaPool {

	public YKBlockBoxedClayconia() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_CLAYCONIA;
		
	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedClayconia();
	}
	
}
