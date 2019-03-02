package firis.yuzukizuflower.common.tileentity;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import firis.yuzukizuflower.common.entity.YKFakePlayer;
import firis.yuzukizuflower.common.helpler.YKReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

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
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
        this.workY = compound.getInteger("workY");

    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        compound.setInteger("workY", this.workY);
        
        return compound;
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
		
		//インベントリ操作
		this.autoOutputInventory();
		
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
		
		boolean startY = false;
		
		//処理終了
		if (workY == 0) {
			return;
		}
		
		//高さを設定
		if (workY <= -1) {
			workY = this.getPos().down().getY();
			startY = true;
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
				
				//段を下げた一回目だけ処理をやりたい
				//液体変換処理
				if (startY) {
					
					//north z軸マイナス
					//south z軸プラス
					//west  x軸マイナス
					//east  x軸プラス
					
					//一段上と2段分の液体を検索して変換する
					for(BlockPos liquidPos : BlockPos.getAllInBox(posStart.north().west().up(), posEnd.south().east())) {
						
						IBlockState liquidState = this.getWorld().getBlockState(liquidPos);
						
						//液体かどうか
						if (liquidState.getMaterial().isLiquid()) {
							
							//デフォルト土
							IBlockState replaceState = Blocks.DIRT.getDefaultState();
							
							//水 -> 氷塊
							if (liquidState.getMaterial().equals(Material.WATER)) {
								replaceState = Blocks.PACKED_ICE.getDefaultState();
							}
							//溶岩源 -> 黒曜石
							else if (liquidState.getMaterial().equals(Material.LAVA)
									&& liquidState.getValue(BlockLiquid.LEVEL).intValue() == 0) {
								replaceState = Blocks.OBSIDIAN.getDefaultState();
							}
							//溶岩流 -> 丸石
							else if (liquidState.getMaterial().equals(Material.LAVA)
									&& liquidState.getValue(BlockLiquid.LEVEL).intValue() > 0) {
								replaceState = Blocks.COBBLESTONE.getDefaultState();
							}
							
							//液体を置換する
							this.getWorld().setBlockState(liquidPos, replaceState, 3);
							
							continue;
						}
						
					}
					startY = false;
					
				}
				
				//空気の場合はスルー
				if (state.getBlock().isAir(state, this.getWorld(), pos)) {
					continue;
				}
				//液体の場合はスルー
				if (state.getMaterial().isLiquid()) {
					continue;
				}

				
				//ドロップを手動で行う
				//幸運
				int fortune = 0;
				//ドロップリストを取得
				NonNullList<ItemStack> drops = NonNullList.create();
				
				
				//シルクタッチ
				if(state.getBlock().canSilkHarvest(world, pos, state, new YKFakePlayer(world))) {
					
					//シルクタッチ
					ItemStack silk = ItemStack.EMPTY.copy();
					Method method = YKReflectionHelper.findMethod(state.getBlock().getClass(), "getSilkTouchDrop", "func_180643_i", IBlockState.class);
					try {
						silk = (ItemStack) method.invoke(state.getBlock(), state);
					} catch (Exception e) {
					}
					
					drops.add(silk);
				} else {
					//ノーマル採掘
					state.getBlock().getDrops(drops, this.getWorld(), pos, state, fortune);
				}
				
				//
				NonNullList<ItemStack> dropsList = NonNullList.create();
				for (ItemStack drop : drops) {
					if(!this.insertOutputSlotItemStack(drop)) {
						dropsList.add(drop);
					}
				}
				
				for (ItemStack drop : dropsList) {
					Block.spawnAsEntity(this.getWorld(), this.getPos().up(), drop);
				}
				
				
				//それ以外の場合はブロックを破壊して終了
				//ただのブロック破壊
				this.getWorld().destroyBlock(pos, false);
				
				//空気への入れ替え
				this.getWorld().notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 3);
				flg = true;
				break;
			}
			if (flg) {
				break;
			}
			//基準点を一段下げる
			workY = workY - 1;
			startY = true;
		}
	}
	
	/**
	 * 内部インベントリを隣接チェストへ自動搬出する
	 */
	public void autoOutputInventory() {
		
		//outputインベントリ操作
		if(!this.isEmpty()) {
			
			//outputから移動用のアイテムを選別
			int moveStackIdx = -1;
			for (int slot : this.outputSlotIndex) {
				if (!this.getStackInSlot(slot).isEmpty()) {
					moveStackIdx = slot;
					break;
				}
			}
			
			//inventoryが空でない場合
			for(EnumFacing facing : EnumFacing.VALUES) {
				//tileEntity
				TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(facing));
				if (tile == null) {
					continue;
				}
				//方角を反転して取得
				//Capability取得
				IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
				if (capability == null) {
					continue;
				}
				//最大8つだけ移動
				ItemStack insertStack = this.getStackInSlot(moveStackIdx).copy();
				int insertStackCount = Math.min(8, insertStack.getCount());
				insertStack.setCount(insertStackCount);
				
				//シミュレート
				ItemStack result = insertStack.copy();
				for (int slot = 0; slot < capability.getSlots(); slot++) {
					//insert
					result = capability.insertItem(slot, result, true);
					if (result.isEmpty()) {
						break;
					}
				}
				//問題なければ移動する
				insertStackCount = insertStack.getCount() - result.getCount();
				insertStack = this.decrStackSize(moveStackIdx, insertStackCount);
				
				if (insertStack.getCount() > 0) {
					for (int slot = 0; slot < capability.getSlots(); slot++) {
						//insert
						insertStack = capability.insertItem(slot, insertStack, false);
						if (insertStack.isEmpty()) {
							break;
						}
					}
				}
			}
		}
				
	}
	
}
