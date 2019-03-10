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
import net.minecraft.client.resources.I18n;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.common.Botania;

/**
 * ユクァーリーの処理
 * @author computer
 *
 */
public class YKTileBoxedYuquarry extends YKTileBaseBoxedProcFlower implements IYKNetworkTileBoxedFlower {
	
	/**
	 * お花モードの定義
	 * @author computer
	 *
	 */
	public static enum FlowerMode {
		
		MODE1(1, "mode1", 2),
		MODE2(2, "mode2", 4),
		MODE3(3, "mode3", 8),
		MODE4(4, "mode4", 16);
		
		private int id;
		private String name;
		private int range;
		
		private FlowerMode(final int id, final String name, final int range) {
			this.id = id;
			this.name = name;
			this.range = range;
		}
		public int getId() {
			return this.id;
		}
		public String getName() {
	        return I18n.format("gui.boxed_yuquarry."
	        		+ this.name + ".name");
		}
		public int getRange() {
			return this.range;
		}
		public static FlowerMode getById(int id) {
			for(FlowerMode mode : FlowerMode.values()) {
				if(mode.getId() == id) {
					return mode;
				}
			}
			return null;
		}
		public static FlowerMode nextMode(FlowerMode mode) {
			
			FlowerMode nextMode = null;
			for (int i = 0 ; i < FlowerMode.values().length; i++ ) {
				if(mode == FlowerMode.values()[i]) {
					if (i == FlowerMode.values().length - 1) {
						nextMode = FlowerMode.values()[0];
					} else {
						nextMode = FlowerMode.values()[i+1];						
					}
					break;
				}
			}
			return nextMode;
		}
	}
	
	/**
	 * お花のモード
	 */
	protected FlowerMode flowerMode;
	public FlowerMode getFlowerMode() {
		return this.flowerMode;
	}
	
	/**
	 * シルクタッチモード
	 */
	protected boolean silkTouch = false;
	public boolean getSilkTouch() {
		return this.silkTouch;
	}
	
	
	/**
	 * コンストラクタ
	 */
	public YKTileBoxedYuquarry() {
		
		this.maxMana = 10000;
		
		//初期mode
		this.flowerMode = FlowerMode.MODE1;
		this.silkTouch = false;
		
		//outputスロット
		this.outputSlotIndex = IntStream.range(0, 15).boxed().collect(Collectors.toList());

		//tick周期(2秒で5回)
		this.setCycleTick(8);
	}
	
	@Override
	public int getSizeInventory() {
		return 15;
	}
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
        this.workY = compound.getInteger("workY");
        this.flowerMode = FlowerMode.getById(compound.getInteger("flowerMode"));
        this.silkTouch = compound.getBoolean("silkTouch");

    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        compound.setInteger("workY", this.workY);
        compound.setInteger("flowerMode", this.flowerMode.getId());
        compound.setBoolean("silkTouch", this.silkTouch);
        
        return compound;
    }
	
	@Override
	public void update() {
		super.update();
		
		if (this.world.isRemote) {
			//パーティクル判定
			if(!isRedStonePower()
					&& !(this.mana < this.procMana)
					&& 0 < this.workY) {
				clientSpawnParticle();
			}
			return;
		}
		
		//インベントリ操作
		if (!this.getWorld().isRemote) {
			this.autoOutputInventory();			
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
		
		//高さを設定
		if (workY <= -1) {
			workY = this.getPos().down().getY();
			startY = true;
		}
		
		//処理終了
		if (workY == 0) {
			return;
		}
		
		//範囲を取得
		int range = this.flowerMode.getRange();
		BlockPos startBasePos = this.getPos().add(-range, 0, -range);
		BlockPos endBasePos = this.getPos().add(range, 0, range);
		
		//ブロックをチャンク範囲で取得してみる
		boolean flg = false;
		for (int posY = workY; 0 < posY; posY--) {
			
			BlockPos posStart = new BlockPos(
					startBasePos.getX(),
					posY,
					startBasePos.getZ()
					);
			
			BlockPos posEnd = new BlockPos(
					endBasePos.getX(),
					posY,
					endBasePos.getZ()
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
				
				//ブロック破壊してドロップを取得
				NonNullList<ItemStack> drops = YKBlockHelper.destroyBlock(world, pos, this.silkTouch, fortune);
				
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
			this.playerServerSendPacket();
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
					this.playerServerSendPacket();
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
	
	/****************************************************
	 * モード切替処理
	 ***************************************************/
	/**
	 * ClientからPacketを受け取った際に呼び出される
	 * @intarface IYKNetworkTileBoxedFlower
	 */
	@Override
	public void receiveFromClientMessage(int mode) {
		
		if (mode == 0) {
			//モード切替を行う
			changeFlowerMode();			
		} else if(mode == 1) {
			this.silkTouch = !this.silkTouch;
			this.playerServerSendPacket();
		}
	}
	
	/**
	 * モード切替を行う
	 */
	public void changeFlowerMode() {
		
		this.flowerMode = FlowerMode.nextMode(flowerMode);
		
		//モード変更された場合最初からやり直す
		this.workY = this.getPos().down().getY();
		
		this.playerServerSendPacket();
	}
	
	//******************************************************************************************
	// SubTile設定
	//******************************************************************************************
	@Override
	public int getFlowerRange() {
		return this.flowerMode.getRange();
	}
	
}
