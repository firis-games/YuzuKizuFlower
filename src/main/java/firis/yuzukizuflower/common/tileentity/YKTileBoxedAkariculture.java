package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import firis.yuzukizuflower.common.entity.YKFakePlayer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.FakePlayer;

/**
 * アカリカルチャー
 * @author computer
 *
 */
public class YKTileBoxedAkariculture extends YKTileBaseBoxedProcFlower {
	
	/**
	 * コンストラクタ
	 */
	public YKTileBoxedAkariculture() {
		
		this.maxMana = 1000;
		
		//inputスロット
		this.inputSlotIndex = new ArrayList<Integer>(
				Arrays.asList(0));
		
		//tick周期
		this.setCycleTick(20);
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
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
		
		//レッドストーン入力がある場合は停止する
		if(isRedStonePower()) {
			return;
		}
		
		//ブロック設置処理
		procAkariculture(8, 2);
	}
	
	/**
	 * アカリカルチャーの設置処理
	 */
	private void procAkariculture(int range, int height) {
		
		ItemStack stack = this.getStackInputSlotFirst();
		
		//稼動1回 10mana
		if (this.mana <= 10) {
			return;
		}
		
		//スロットのアイテムが種系アイテムか確認する
		if (!(stack.getItem() instanceof net.minecraftforge.common.IPlantable)
				&& !(stack.getItem() instanceof ItemBlock 
						&& ((ItemBlock)stack.getItem()).getBlock() instanceof net.minecraftforge.common.IPlantable)) {
			//何もしない
			return;
		}
		
		
		List<BlockPos> farmlandList = new ArrayList<BlockPos>();
		//範囲のブロックを取得
		for(BlockPos pos : BlockPos.getAllInBox(pos.add(-range, -height, -range), pos.add(range, height, range))) {
			
			IBlockState state = this.getWorld().getBlockState(pos);
			
			//耕地判断
			if (!Block.isEqualTo(state.getBlock(), Blocks.FARMLAND)) {
				continue;
			}
			//ひとつ上のブロックが空気ブロック
			IBlockState stateUp = this.getWorld().getBlockState(pos.up());
			if (!stateUp.getBlock().isAir(stateUp, this.getWorld(), pos.up())) {
				continue;				
			}
			
			//ブロックリスト
			farmlandList.add(pos);
		}
		
		if (farmlandList.size() <= 0) {
			return;
		}

		//種を設置していく
		FakePlayer fPlayer = new YKFakePlayer(this.getWorld());
		fPlayer.setHeldItem(EnumHand.MAIN_HAND, stack);
		
		for (BlockPos pos : farmlandList) {
			
			if (this.mana < 10 || stack.isEmpty()) {
				break;
			}
			EnumActionResult action = stack.onItemUse(fPlayer, this.getWorld(), pos, EnumHand.MAIN_HAND, EnumFacing.UP, pos.getX(), pos.getY(), pos.getZ());
			if (EnumActionResult.SUCCESS == action) {
				//成功したら音を鳴らす
				this.getWorld().playEvent(2001, pos, Block.getStateId(Blocks.TALLGRASS.getDefaultState()));
				this.recieveMana(-10);
			}				
		}	
	}
	
	//******************************************************************************************
	// SubTile設定
	//******************************************************************************************
	@Override
	public int getFlowerRange() {
		return 8;
	}
}
