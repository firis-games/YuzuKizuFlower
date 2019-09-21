package firis.yuzukizuflower.api.client.jei.transfer;

import mezz.jei.transfer.BasicRecipeTransferInfo;
import net.minecraft.inventory.Container;

/**
 * Botania作業台レシピ系の汎用転送処理
 * @author computer
 *
 * @param <C>
 */
public class BotaniaWorkbenchRecipeTransferInfo<C extends Container> extends BasicRecipeTransferInfo<C> {

	/**
	 * コンストラクタ
	 */
	public BotaniaWorkbenchRecipeTransferInfo(Class<C> containerClass, String recipeCategoryUid) {
		super(containerClass, 
				recipeCategoryUid,
				0, 16, 18, 36);
	}
}
