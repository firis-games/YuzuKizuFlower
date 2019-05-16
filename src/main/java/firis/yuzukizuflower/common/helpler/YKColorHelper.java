package firis.yuzukizuflower.common.helpler;

public class YKColorHelper {


	/**
	 * RGBを符号あり4byteへ変換する
	 * AA RR GG BB の16進数で色を表している
	 * 
	 * -2130706433 = 80FFFFFF
	 * 
	 * 1073741824 = 40 00 00 00 黒で透過率25%を設定
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 * @return
	 */
	public static int getColorInt(int red, int green, int blue, float alpha) {
		
		int iRed = Math.min(255, red) << 16;
		int iGreen = Math.min(255, green) << 8;
		int iBlue = Math.min(255, blue);
		int iAlpha = Math.min(255, (int) (255f * alpha)) << 24;
		
		return iRed + iGreen + iBlue + iAlpha;
	}
	
}
