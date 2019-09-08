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
	
	/**
	 * 縁結びの輪対象リスト
	 */
	@Comment({"Remote Chest White List"})
	public static String[] REMOTE_CHEST_WHITE_LIST = {
			"minecraft:chest",
			"minecraft:trapped_chest",
			"minecraft:white_shulker_box",
			"minecraft:orange_shulker_box",
			"minecraft:magenta_shulker_box",
			"minecraft:light_blue_shulker_box",
			"minecraft:yellow_shulker_box",
			"minecraft:lime_shulker_box",
			"minecraft:pink_shulker_box",
			"minecraft:gray_shulker_box",
			"minecraft:silver_shulker_box",
			"minecraft:cyan_shulker_box",
			"minecraft:purple_shulker_box",
			"minecraft:blue_shulker_box",
			"minecraft:brown_shulker_box",
			"minecraft:green_shulker_box",
			"minecraft:red_shulker_box",
			"minecraft:black_shulker_box",
			"yuzukizuflower:scroll_chest"
	};
	
	/**
	 * マナのエンチャント台で重複エンチャントを許可する設定
	 */
	@Comment({"Allow duplicate enchantment of ManaEnchanter", "default:false"})
	public static boolean DUPLICATE_ENCHANTMENT = false;
}
