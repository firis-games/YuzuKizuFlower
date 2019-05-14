package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.common.YKGuiHandler;
import firis.yuzukizuflower.common.tileentity.YKTileBoxedEntropinnyum;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class YKBlockBoxedEntropinnyum extends YKBlockBaseManaPool {

	/**
	 * コンストラクタ
	 */
	public YKBlockBoxedEntropinnyum() {

		super();

		//GUIのIDを設定
		this.GUI_ID = YKGuiHandler.BOXED_ENTROPINNYUM;

	}
	
	/**
	 * TileEntity設定
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileBoxedEntropinnyum();
	}
}
