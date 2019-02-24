package firis.yuzukizuflower.common.entity;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class YKFakePlayer extends FakePlayer {
	
	//UUID.randomUUID()で生成したランダムUUID
	private static final GameProfile DUMMY_PROFILE = new GameProfile(
			UUID.fromString("1492d68a-a353-430d-8ce2-f3d9faf049dc"), 
			"[YKFakePlayer]");
	
	public YKFakePlayer(World world) {
		
		super(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(world.provider.getDimension()), 
				DUMMY_PROFILE);
		
	}

}
