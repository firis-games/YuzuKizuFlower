package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import firis.yuzukizuflower.common.inventory.BoxedFieldConst;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemPiston;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import vazkii.botania.api.item.IFlowerPlaceable;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

/**
 * ラナンカーパス
 * @author computer
 *
 */
public class YKTileBoxedRannuncarpus extends YKTileBaseBoxedProcFlower implements IYKNetworkTileBoxedFlower{
	
	/**
	 * お花モードの定義
	 * @author computer
	 *
	 */
	public static enum FlowerMode {
		
		MODE1(1, "mode1", 2, 2),
		MODE2(2, "mode2", 3, 2),
		MODE3(3, "mode3", 6, 6),
		MODE4(4, "mode4", 8, 6);
		
		private int id;
		private String name;
		private int range;
		private int height;
		
		private FlowerMode(final int id, final String name, final int range, final int height) {
			this.id = id;
			this.name = name;
			this.range = range;
			this.height = height;
		}
		public int getId() {
			return this.id;
		}
		public String getName() {
	        return I18n.format("gui.boxed_rannuncarpus."
	        		+ this.name + ".name");
		}
		public int getRange() {
			return this.range;
		}
		public int getHeight() {
			return this.height;
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
	 * コンストラクタ
	 */
	public YKTileBoxedRannuncarpus() {
		this.maxMana = 0;
		
		//初期mode
		this.flowerMode = FlowerMode.MODE1;
		
		//inputスロット
		this.inputSlotIndex = new ArrayList<Integer>(
				Arrays.asList(0));
		
		//tick周期
		this.setCycleTick(10);
	}
	
	/**
	 * お花のモード
	 */
	protected FlowerMode flowerMode;
	public FlowerMode getFlowerMode() {
		return this.flowerMode;
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
	}
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
        this.flowerMode = FlowerMode.getById(compound.getInteger("flowerMode"));
    }
	
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        compound.setInteger("flowerMode", this.flowerMode.getId());
        return compound;
    }
	
	@Override
	public void update() {
		super.update();
		
		//パーティクルスポーン判定
		checkSpawnParticle();
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
		procRannuncarpus(flowerMode.getRange(), flowerMode.getHeight());
	}
	
	/**
	 * ラナンカーパスのブロック設置処理
	 * @param range
	 * @param height
	 */
	@SuppressWarnings("deprecation")
	private void procRannuncarpus(int range, int height) {
		//下を取得
		IBlockState filter = this.getWorld().getBlockState(this.getPos().down());
		
		List<BlockPos> validPositions = new ArrayList<>();
		
		//マナを使い場合の範囲
		int rangePlace = range;
		int rangePlaceY = height;
		
		ItemStack stack = this.getStackInputSlotFirst();
		Item stackItem = stack.getItem();
		
		for(BlockPos pos_ : BlockPos.getAllInBox(pos.add(-rangePlace, -rangePlaceY, -rangePlace), pos.add(rangePlace, rangePlaceY, rangePlace))) {
			IBlockState stateAbove = this.getWorld().getBlockState(pos_.up());
			Block blockAbove = stateAbove.getBlock();
			BlockPos up = pos_.up();
			if(filter == this.getWorld().getBlockState(pos_)
					&& (blockAbove.isAir(stateAbove, this.getWorld(), up)
							|| blockAbove.isReplaceable(this.getWorld(), up)))
				validPositions.add(up);
		}
		
		if(!validPositions.isEmpty()) {
			BlockPos coords = validPositions.get(this.getWorld().rand.nextInt(validPositions.size()));

			IBlockState stateToPlace = null;
			if(stackItem instanceof IFlowerPlaceable)
				stateToPlace = ((IFlowerPlaceable) stackItem).getBlockToPlaceByFlower(stack, null, coords);
			if(stackItem instanceof ItemBlock) {
				int blockMeta = stackItem.getMetadata(stack.getItemDamage());

				if(stackItem instanceof ItemPiston) // Workaround because the blockMeta ItemPiston gives crashes getStateFromMeta
					blockMeta = 0;

				stateToPlace = ((ItemBlock) stackItem).getBlock().getStateFromMeta(blockMeta);
			}
			else if(stackItem instanceof ItemBlockSpecial)
				stateToPlace = ((ItemBlockSpecial) stackItem).getBlock().getDefaultState();
			else if(stackItem instanceof ItemRedstone)
				stateToPlace = Blocks.REDSTONE_WIRE.getDefaultState();

			if(stateToPlace != null) {
				if(stateToPlace.getBlock().canPlaceBlockAt(this.getWorld(), coords)) {
					this.getWorld().setBlockState(coords, stateToPlace, 1 | 2);
					if(ConfigHandler.blockBreakParticles)
						this.getWorld().playEvent(2001, coords, Block.getStateId(stateToPlace));
					validPositions.remove(coords);
					ItemBlock.setTileEntityNBT(this.getWorld(), null, coords, stack);

					TileEntity tile = this.getWorld().getTileEntity(coords);
					if(tile != null && tile instanceof ISubTileContainer) {
						ISubTileContainer container = (ISubTileContainer) tile;
						String subtileName = ItemBlockSpecialFlower.getType(stack);
						container.setSubTile(subtileName);
						SubTileEntity subtile = container.getSubTile();
						subtile.onBlockPlacedBy(this.getWorld(), coords, this.getWorld().getBlockState(coords), null, stack);
					}

					if(stackItem instanceof IFlowerPlaceable)
						((IFlowerPlaceable) stackItem).onBlockPlacedByFlower(stack, null, coords);

					stack.shrink(1);

					//if(mana > 1)
					//	mana--;
					
					return;
				}
			}
		}
	}


	/**
	 * ClientからPacketを受け取った際に呼び出される
	 * @intarface IYKNetworkTileBoxedFlower
	 */
	@Override
	public void receiveFromClientMessage(int mode) {
		//モード切替を行う
		changeFlowerMode();
	}
	
	/**
	 * モード切替を行う
	 */
	public void changeFlowerMode() {
		this.flowerMode = FlowerMode.nextMode(flowerMode);
	}
	
	/**
	 * モードチェンジメッセージを取得する
	 */
	public TextComponentTranslation getMessageChangeFlowerMode() {
		
		String name = this.flowerMode.getName();
		String text = I18n.format("gui.boxed_rannuncarpus.mode.message.name", name);
		
		return new TextComponentTranslation(text);
	}
	
	//******************************************************************************************
	// SubTile設定
	//******************************************************************************************
	@Override
	public int getFlowerRange() {
		return this.flowerMode.getRange();
	}
	
	/**
	 * GUIパラメータ同期用
	 */
	@Override
	public int getField(int id) {
		
		if (id == BoxedFieldConst.MODE) {
			return this.getFlowerMode().getId();
		} else {
			return super.getField(id);
		}

	}

	/**
	 * GUIパラメータ同期用
	 */
	@Override
	public int getFieldCount() {
		return 9;
	}
}
