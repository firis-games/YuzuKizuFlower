package firis.yuzukizuflower.common.event;

import firis.yuzukizuflower.common.world.dimension.DimensionHandler;
import firis.yuzukizuflower.common.world.dimension.TeleporterAlfheim;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.block.ModBlocks;

@EventBusSubscriber
public class PlayerInteractEventHandler {

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void onPlayerInteractEvent(PlayerInteractEvent.RightClickBlock event) {
		
		if (event.getWorld().isRemote) return;

		//アルフヘイムのみ動作する
		if (event.getWorld().provider.getDimension() != DimensionHandler.dimensionAlfheim.getId()) return;
		
		//座標 0, y, 0 のみ動作する
		if (event.getPos().getX() != 0 || event.getPos().getZ() != 0) return;
		
		IBlockState state = event.getWorld().getBlockState(event.getPos());
		
		//パイロン系のブロックであること
		if (state.getBlock() != ModBlocks.pylon) return;
		
		
		EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
		
		//オーバーワールドへ戻る
		WorldServer wolrd = player.getServer().getWorld(DimensionType.OVERWORLD.getId());
		
		//移動する
		player.changeDimension(DimensionType.OVERWORLD.getId(), new TeleporterAlfheim(wolrd));
		
		//移動後に座標移動
		BlockPos movePos = player.getBedLocation(DimensionType.OVERWORLD.getId());
		if (movePos == null) {
			movePos = wolrd.provider.getRandomizedSpawnPoint();
		}
		
		//位置調整
		player.connection.setPlayerLocation(movePos.getX(), movePos.getY(), movePos.getZ(), player.rotationYaw, player.rotationPitch);
		
	}
	
}
