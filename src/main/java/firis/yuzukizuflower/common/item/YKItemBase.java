package firis.yuzukizuflower.common.item;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.item.Item;

public class YKItemBase extends Item {
	
	/**
	 * コンストラクタ
	 */
	public YKItemBase() {
		
		super();
		
		//初期化
		this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
		
	}

}
