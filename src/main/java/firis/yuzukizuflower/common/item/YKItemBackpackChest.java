package firis.yuzukizuflower.common.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class YKItemBackpackChest extends Item implements IBauble {

	public YKItemBackpackChest() {
		super();
		//init
		this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
		this.setMaxStackSize(1);
	}
	
    /**
     * @interface IBauble
     */
	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.BODY;
	}

}
