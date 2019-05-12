package firis.yuzukizuflower.common.block;

import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;

public class YKBlockAlfSand extends BlockFalling {

	
	public YKBlockAlfSand() {
		
		this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
		
		this.setHardness(0.5F);
		this.setSoundType(SoundType.SAND);
		
	}
	
}
