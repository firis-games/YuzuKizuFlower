package firis.yuzukizuflower.common;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.Type;

@Config(modid = YuzuKizuFlower.MODID, type = Type.INSTANCE, name = YuzuKizuFlower.MODID)
public class YKConfig {

	/**
	 * マナ溜り生成率
	 * default:1%
	 */
	@Comment({"World Generation Rate Mana Pool", "default:10"})
	@RangeInt(min = 0, max = 1000)
	public static int GEN_RATE_MANA_POOL = 10;
	
	/**
	 * マナの池生成率
	 * default:1%
	 */
	@Comment({"World Generation Rate Mana Lake", "default:10"})
	@RangeInt(min = 0, max = 1000)
	public static int GEN_RATE_MANA_LAKE = 10;
	
}
