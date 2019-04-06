package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import firis.yuzukizuflower.common.inventory.IInventoryMultiItemHandler;
import firis.yuzukizuflower.common.inventory.YKDummyItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.corporea.ICorporeaSpark;
import vazkii.botania.api.corporea.InvWithLocation;

/**
 * コーポリアチェスト
 * @author computer
 *
 */
public class YKTileCorporeaChest extends TileEntity implements ITickable {

	public YKTileCorporeaChest() {
	}
	
	/**
	 * コーポリアネットワークのインベントリをもつBlockのPosListを取得
	 * @return
	 */
	public List<BlockPos> getCorporeaBlockPosList() {
		return this.corporeaBlockPosList;
	}
	
	/**
	 * コーポリアネットワークからIInventoryを取得する
	 */
	public IInventoryMultiItemHandler getIInventoryFromCorporeaNetwork() {
		return new IInventoryMultiItemHandler(this);
	}
	
	/********************************************************************************/
	/**
	 * コーポリアネットワークのインベントリBlockPosを保持する
	 */
	protected List<BlockPos> corporeaBlockPosList = new ArrayList<BlockPos>();

	/**
	 * コーポリアネットワークのインベントリBlockPosが更新されたらtrueへ変更して同期する
	 */	
	protected boolean updateCorporeaBlockPosList = false;
	public boolean getUpdateCorporeaBlockPosList() {
		boolean ret = this.updateCorporeaBlockPosList;
		this.updateCorporeaBlockPosList = false;
		return ret;
	}
	
	/********************************************************************************/
	
	public int startOpenAnimation = 0;
	//アニメーション用
	public void setStartOpenAnimation() {
		this.startOpenAnimation = 1;
	}
	
	
	/********************************************************************************/
	

	private int tick = 0;
	/**
	 * @Intarface ITickable
	 */
	@Override
	public void update() {
		tick += 1;
		if (tick % 20 == 0) {
			this.onUpdate();
			tick = 0;
		}
		
		//アニメーション制御
		this.animationController.update();
	}
	
	/**
	 * 一定Tickごとにコーポリアネットワークの更新を行う
	 */
	public void onUpdate() {
		
		//サーバーサイドのみ処理を行う
		if (this.getWorld().isRemote) return;

		this.updateCorporeaBlockPosList = false;
		
		List<BlockPos> workPosList = new ArrayList<BlockPos>();
		ICorporeaSpark spark = CorporeaHelper.getSparkForBlock(this.getWorld(), this.getPos());
		
		//スパークがある場合はリスト生成
		if (spark != null) {
			List<InvWithLocation> inventories = CorporeaHelper.getInventoriesOnNetwork(spark);

			for (InvWithLocation invloc : inventories) {
				workPosList.add(new BlockPos(invloc.pos));
			}
		}
		
		//変更された場合は更新
		if (!workPosList.equals(this.corporeaBlockPosList)) {
			this.corporeaBlockPosList = workPosList;
			this.updateCorporeaBlockPosList = true;
			this.playerServerSendPacket();
		}
	}
	
	/********************************************************************************/
	IItemHandler itemHandler = new YKDummyItemHandler();

	@Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
    }

	@Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
    	if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
		}
    	return super.getCapability(capability, facing);
    
    }
	/********************************************************************************/
	
	
	/**
	 * サーバー->クライアントのデータ同期用
	 * サーバーからクライアントへデータを送信する
	 */
	protected void playerServerSendPacket() {
		//Server Side
		if (!this.getWorld().isRemote) {
			
			this.markDirty();
			
			List<EntityPlayer> list = world.playerEntities;
			Packet<?> pkt = this.getUpdatePacket();
			if (pkt != null) {
				for (EntityPlayer player : list) {
					EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
					mpPlayer.connection.sendPacket(pkt);
				}
			}
		}
	}
	
	/********************************************************************************/
	/** NBT保存関連 */
	/********************************************************************************/
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
		//BlockPosListをロード
		NBTTagList nbttaglist = compound.getTagList("CoporeaPosList", 10);

		this.corporeaBlockPosList = new ArrayList<BlockPos>(nbttaglist.tagCount());

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound posNbt = nbttaglist.getCompoundTagAt(i);
            int idx = posNbt.getByte("idx") & 255;
            
            int x = posNbt.getInteger("posX");
            int y = posNbt.getInteger("posY");
            int z = posNbt.getInteger("posZ");

            corporeaBlockPosList.add(idx, new BlockPos(x, y, z));            	
        }
        
        //更新フラグ
        this.updateCorporeaBlockPosList = compound.getBoolean("updateCorporeaBlockPosList");
    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        //BlockPosListをセーブ
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.corporeaBlockPosList.size(); i++) {
        	
        	BlockPos pos = this.corporeaBlockPosList.get(i);
        	
        	NBTTagCompound posNbt = new NBTTagCompound();
        	posNbt.setByte("idx", (byte) i);
        	posNbt.setInteger("posX", pos.getX());
        	posNbt.setInteger("posY", pos.getY());
        	posNbt.setInteger("posZ", pos.getZ());
        	
        	nbttaglist.appendTag(posNbt);
        }
        if (!nbttaglist.hasNoTags()) {
        	compound.setTag("CoporeaPosList", nbttaglist);
        }
        
        //更新フラグ
        compound.setBoolean("updateCorporeaBlockPosList", this.updateCorporeaBlockPosList);

        return compound;
    }
	
	@Override
    public NBTTagCompound getUpdateTag()
    {
		return this.writeToNBT(new NBTTagCompound());
    }
	
	@Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }
	
	@Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
		return new SPacketUpdateTileEntity(this.pos, 0, this.writeToNBT(new NBTTagCompound()));
    }

	@Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
    {
		this.readFromNBT(pkt.getNbtCompound());
    }
	
	/**
	 * チェストアニメーション管理用クラス
	 * @author computer
	 *
	 */
	public YKChestAnimationController animationController = new YKChestAnimationController(this);
	public static class YKChestAnimationController {
		
		/**
		 * コンストラクタ
		 */
		public YKChestAnimationController(TileEntity tile) {
			this.tile = tile;
		}
		
		TileEntity tile;
		
		public int numPlayersUsing;
		
		public void openInventory(EntityPlayer player)
	    {
	        if (!player.isSpectator())
	        {
	            if (this.numPlayersUsing < 0)
	            {
	                this.numPlayersUsing = 0;
	            }

	            ++this.numPlayersUsing;
	        }
	    }

	    public void closeInventory(EntityPlayer player)
	    {
	        if (!player.isSpectator())
	        {
	            --this.numPlayersUsing;
	        }
	    }
	    
	    private int ticksSinceSync = 0;
	    
	    /** The current angle of the lid (between 0 and 1) */
	    public float lidAngle = 0.0F;
	    
	    /** The angle of the lid last tick */
	    public float prevLidAngle = 0.0F;
	    
	    public void update()
	    {
	        int i = this.tile.getPos().getX();
	        int j = this.tile.getPos().getY();
	        int k = this.tile.getPos().getZ();
	        ++this.ticksSinceSync;
	        
	        //numPlayersUsing
	        //1がopen
	        //-1がclose
	        //0が通常状態
	        
	        if (!this.tile.getWorld().isRemote 
	        		&& this.numPlayersUsing != 0 
	        		&& (this.ticksSinceSync + i + j + k) % 200 == 0)
	        {
	            this.numPlayersUsing = 0;
	        }

	        this.prevLidAngle = this.lidAngle;

	        //開く音を鳴らす
	        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
	        {
	            double d1 = (double)i + 0.5D;
	            double d2 = (double)k + 0.5D;

	            this.tile.getWorld().playSound((EntityPlayer)null, d1, (double)j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, tile.getWorld().rand.nextFloat() * 0.1F + 0.9F);
	        }

	        //動作中の場合
	        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F 
	        		|| this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
	        {
	            float f2 = this.lidAngle;

	            if (this.numPlayersUsing > 0)
	            {
	                this.lidAngle += 0.1F;
	            }
	            else
	            {
	                this.lidAngle -= 0.1F;
	            }

	            if (this.lidAngle > 1.0F)
	            {
	                this.lidAngle = 1.0F;
	            }

	            //閉じる音
	            if (this.lidAngle < 0.5F && f2 >= 0.5F)
	            {
	                double d3 = (double)i + 0.5D;
	                double d0 = (double)k + 0.5D;

	                this.tile.getWorld().playSound(
	                		(EntityPlayer)null, d3, (double)j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, tile.getWorld().rand.nextFloat() * 0.1F + 0.9F);
	            }

	            if (this.lidAngle < 0.0F)
	            {
	                this.lidAngle = 0.0F;
	            }
	        }
	    }
	}
}
