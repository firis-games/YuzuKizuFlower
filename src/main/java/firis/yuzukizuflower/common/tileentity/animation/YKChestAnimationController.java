package firis.yuzukizuflower.common.tileentity.animation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

public class YKChestAnimationController {
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
            
            for (EntityPlayer entityplayer : tile.getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double)((float)i - 5.0F), (double)((float)j - 5.0F), (double)((float)k - 5.0F), (double)((float)(i + 1) + 5.0F), (double)((float)(j + 1) + 5.0F), (double)((float)(k + 1) + 5.0F))))
            {
            	//本来は自身のContainerかの判断を行う
                if (entityplayer.openContainer != null)
                {
                	++this.numPlayersUsing;
                }
            }
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
