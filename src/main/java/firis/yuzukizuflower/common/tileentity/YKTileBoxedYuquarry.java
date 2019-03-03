package firis.yuzukizuflower.common.tileentity;

import java.awt.Color;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import firis.yuzukizuflower.common.helpler.YKBlockHelper;
import firis.yuzukizuflower.common.network.NetworkHandler;
import firis.yuzukizuflower.common.network.PacketTileParticle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.common.Botania;

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

		//tick周期(2秒で5回)
		this.setCycleTick(8);
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
					&& !(this.mana < this.procMana)) {
				clientSpawnParticle();
			}
			return;
		}
	}
	
	/**
	 * 1回の動作に必要なマナ
	 */
	protected final int procMana = 100;
	
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
		
		//レッドストーン入力がある場合に動作を停止する
		if(isRedStonePower()) {
			return;
		}
		
		//最低マナより少ない場合は処理を行わない
		if (this.mana < this.procMana) {
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
					this.replaceLiquidBlock(
							posStart.north().west().up(), 
							posEnd.south().east());
					//マナを消費して処理を終了
					this.recieveMana(-this.procMana);
					return;
				}
				
				//空気の場合はスルー
				if (state.getBlock().isAir(state, this.getWorld(), pos)) {
					continue;
				}
				//液体の場合はスルー
				else if (state.getMaterial().isLiquid()) {
					continue;
				}
				//非破壊ブロック(岩盤を除く)
				else if (state.getBlockHardness(this.getWorld(), pos) == -1.0
						&& !Blocks.BEDROCK.equals(state.getBlock())) {
					continue;
				}
				//Mobスポナーは破壊しない
				else if (Blocks.MOB_SPAWNER.equals(state.getBlock())) {
					continue;
				}
				//TileEntityもちは破壊しない
				else if (this.getWorld().getTileEntity(pos) != null) {
					continue;
				}
				
				
				//幸運とシルクタッチ
				int fortune = 0;
				boolean silkTouch = true;
				
				//ブロック破壊してドロップを取得
				NonNullList<ItemStack> drops = YKBlockHelper.destroyBlock(world, pos, silkTouch, fortune);
				
				//岩盤はアイテムドロップさせない
				if (Blocks.BEDROCK.equals(state.getBlock())) {
					drops.clear();
				}
				
				//アイテムの挿入orワールドへドロップ
				for (ItemStack drop : drops) {
					if(!this.insertOutputSlotItemStack(drop)) {
						//ワールドへドロップ
						Block.spawnAsEntity(this.getWorld(), this.getPos().up(), drop);
					}
				}
				this.recieveMana(-this.procMana);
				serverSpawnParticle(pos);
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
	 * 液体を固体ブロックへ変換する処理
	 */
	private void replaceLiquidBlock(BlockPos posStart, BlockPos posEnd) {
		//一段上と2段分の液体を検索して変換する
		for(BlockPos liquidPos : BlockPos.getAllInBox(posStart, posEnd)) {
			
			IBlockState liquidState = this.getWorld().getBlockState(liquidPos);
			SoundEvent soundEvent = SoundEvents.BLOCK_LAVA_EXTINGUISH;
			//液体かどうか
			if (liquidState.getMaterial().isLiquid()) {
				//デフォルト土
				IBlockState replaceState = Blocks.DIRT.getDefaultState();
				//水 -> 氷塊
				if (liquidState.getMaterial().equals(Material.WATER)) {
					replaceState = Blocks.PACKED_ICE.getDefaultState();
					//氷塊の破壊音
					soundEvent = SoundEvents.BLOCK_GLASS_BREAK;
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
				//音
				this.getWorld().playSound(null, liquidPos, 
						soundEvent, 
						SoundCategory.BLOCKS, 
						0.5F, 
						2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
				continue;
			}
			
		}
	}
	
	/**
	 * 内部インベントリを隣接チェストへ自動搬出する
	 */
	public void autoOutputInventory() {
		
		//outputインベントリ操作
		if(!this.isEmpty()) {
						
			//チェスト単位で処理を行う
			//inventoryが空でない場合
			for(EnumFacing facing : EnumFacing.VALUES) {
				//tileEntity
				TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(facing));
				if (tile == null) {
					continue;
				}
				//箱入り系のブロックは許可しないようにする
				if (tile instanceof YKTileBaseManaPool) {
					continue;
				}
				//方角を反転して取得
				//Capability取得
				IItemHandler capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
				if (capability == null) {
					continue;
				}
				
				//空ではないスロットがある場合に処理を行う
				boolean flg = false;
				for(int slot : this.outputSlotIndex) {
					
					//inventoryのitemstackのコピー
					ItemStack simInsStack = this.getStackInSlot(slot).copy();
					
					if (simInsStack.isEmpty()) {
						continue;
					}
					
					//最大8個アイテムを移動する
					int simInsStackCount = Math.min(8, simInsStack.getCount());
					simInsStack.setCount(simInsStackCount);
					
					//移動のシミュレート
					for (int cabSlot = 0; cabSlot < capability.getSlots(); cabSlot++) {
						//insert
						simInsStack = capability.insertItem(cabSlot, simInsStack, true);
						if (simInsStack.isEmpty()) {
							break;
						}
					}
					//移動の結果
					if (simInsStackCount == simInsStack.getCount()) {
						//移動できていないので対象外
						continue;
					}
					
					//実際のインベントリを操作してアイテムを移動させる
					int insStackCount = simInsStackCount - simInsStack.getCount();
					ItemStack insItemStack = this.decrStackSize(slot, insStackCount);
					for (int cabSlot = 0; cabSlot < capability.getSlots(); cabSlot++) {
						//insert
						insItemStack = capability.insertItem(cabSlot, insItemStack, false);
						if (insItemStack.isEmpty()) {
							break;
						}
					}
					flg = true;
					break;
				}
				
				//移動済みの場合は処理終了
				if (flg) {
					break;
				}
			}
		}
				
	}
	
	/**
	 * ランダムでパーティクルを表示する
	 */
	@Override
	public void clientSpawnParticle() {
		
		//クライアントの場合
		if(this.getWorld().isRemote) {
			
			double particleChance = 0.75F;
			
			//紫色
			Color color = new Color(167, 87, 168);
			
			if(Math.random() > particleChance) {
				//マナプールと同じパーティクル
				Botania.proxy.wispFX(
						pos.getX() + 0.3 + Math.random() * 0.5, 
						pos.getY() + 0.6 + Math.random() * 0.25, 
						pos.getZ() + Math.random(), 
						color.getRed() / 255F, 
						color.getGreen() / 255F, 
						color.getBlue() / 255F, 
						(float) Math.random() / 10F, 
						(float) -Math.random() / 100F, 
						1.5F);
			}
		}
	}
	
	
	/**
	 * パーティクルを表示する
	 */
	public void serverSpawnParticle(BlockPos pos) {
		
		//クライアントへ送信
		NetworkHandler.network.sendToAll(
				new PacketTileParticle.MessageTileParticle(pos));
	}
	
}
