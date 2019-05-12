package firis.yuzukizuflower.common.event;

import firis.yuzukizuflower.YuzuKizuFlower.YuzuKizuBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 
 * @author computer
 *
 */
@EventBusSubscriber
public class LivingEventHandler {
	
	/**
	 * EntityUpdateイベント
	 * @param event
	 */
	@SubscribeEvent
	public static void onLivingUpdateEvent(LivingUpdateEvent event) {
	
		EntityLivingBase entity = event.getEntityLiving();
		if (entity == null || !(entity instanceof EntityPlayer)) return;

		EntityPlayer player = (EntityPlayer) entity;
		
		//2sに1回
		if (player.ticksExisted % 40 != 0) return;
		
		//現在HPを確認
		if (player.getMaxHealth() <= player.getHealth()) return;
		
		//液体マナの中か判断する
		if (isManaWarter(player)) {
			player.heal(0.5F);
		}
		
	}
	
	/**
	 * 参考にしたの溶岩の当たり判定チェック
	 * World.isFlammableWithin
	 * @return
	 */
	protected static boolean isManaWarter(EntityLivingBase living) {
		
		AxisAlignedBB bb = living.getEntityBoundingBox().shrink(0.001D);
		World world = living.getEntityWorld();
		
		int xMin = MathHelper.floor(bb.minX);
        int xMax = MathHelper.ceil(bb.maxX);
        int yMin = MathHelper.floor(bb.minY);
        int yMax = MathHelper.ceil(bb.maxY);
        int zMin = MathHelper.floor(bb.minZ);
        int zMax = MathHelper.ceil(bb.maxZ);
        
        BlockPos posMin = new BlockPos(xMin, yMin, zMin);
        BlockPos posMax = new BlockPos(xMax, yMax, zMax);

        if (world.isAreaLoaded(posMin, posMax, true))
        {
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

            for (int l3 = xMin; l3 < xMax; ++l3)
            {
                for (int i4 = yMin; i4 < yMax; ++i4)
                {
                    for (int j4 = zMin; j4 < zMax; ++j4)
                    {
                        Block block = world.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4)).getBlock();

                        //液体マナ判定
                        if (block == YuzuKizuBlocks.LIQUID_MANA)
                        {
                            blockpos$pooledmutableblockpos.release();
                            return true;
                        }
                    }
                }
            }

            blockpos$pooledmutableblockpos.release();
        }
        return false;
	}
	
}
