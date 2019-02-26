package firis.yuzukizuflower.common.tileentity;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

/**
 * ユクァーリーの処理
 * @author computer
 *
 */
public class YKTileBoxedYuquarry extends YKTileBaseBoxedProcFlower {
	
	/**
	 * コンストラクタ
	 */
	public YKTileBoxedYuquarry() {
		
		this.maxMana = 10000;
		
		//inputスロット

		//outputスロット
		this.outputSlotIndex = IntStream.range(0, 21).boxed().collect(Collectors.toList());

		//tick周期
		this.setCycleTick(1);
	}
	
	@Override
	public int getSizeInventory() {
		return 21;
	}
	
	@Override
	public void update() {
		super.update();
		
		if (this.world.isRemote) {
			//パーティクル判定
			if(!isRedStonePower()
					&& !this.getStackInputSlotFirst().isEmpty()) {
				clientSpawnParticle();
			}
			return;
		}
	}
	
	/**
	 * 指定tickごとに処理を行う
	 * @interface YKTileBaseBoxedProcFlower
	 */
	@Override
	public void updateProccessing() {
		
		if (this.world.isRemote) {
			return;
		}
		
		//レッドストーン入力がある場合に動作する
		if(!isRedStonePower()) {
			return;
		}
		
		//ブロック設置処理
		procYuquarry();
	}
	
	
	protected Integer workY = -1;
	
	/**
	 * ユクァーリーの処理
	 */
	private void procYuquarry() {
		
		//高さを設定
		if (workY <= 0) {
			workY = this.getPos().down().getY();
		}
		
		Chunk chunk = this.getWorld().getChunkFromBlockCoords(this.getPos());
		
		//ブロックをチャンク範囲で取得してみる
		boolean flg = false;
		for (int posY = workY; 0 < posY; posY--) {
			
			BlockPos posStart = new BlockPos(
					chunk.getPos().getXStart(),
					posY,
					chunk.getPos().getZStart()
					);
			
			BlockPos posEnd = new BlockPos(
					chunk.getPos().getXEnd(),
					posY,
					chunk.getPos().getZEnd()
					);
			
			for(BlockPos pos : BlockPos.getAllInBox(posStart, posEnd)) {
				
				IBlockState state = this.getWorld().getBlockState(pos);
				
				if (state.getBlock().isAir(state, this.getWorld(), pos)) {
					continue;
				}
				
				//それ以外の場合はブロックを破壊して終了
				//ただのブロック破壊
				this.getWorld().destroyBlock(pos, true);
				//空気への入れ替え
				this.getWorld().notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 3);
				flg = true;
				break;
			}
			if (flg) {
				break;
			}
			//基準点を一段下げる
			workY = workY--;
		}

	}
}
