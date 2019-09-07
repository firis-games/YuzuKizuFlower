package firis.yuzukizuflower.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 箱入り醸造台
 * @author computer
 *
 */
public class YKTileBoxedBrewery extends YKTileBaseBoxedProcFlower {
	
	public ItemStackHandler inventory;
	
	public YKTileBoxedBrewery() {
		
		//Inventory初期化
		this.inventory = new ItemStackHandler(18);
		
		this.maxMana = 500000;
		
		//tick周期
		this.setCycleTick(1);
		
	}

	@Override
	public void updateProccessing() {
		
	}

	@Override
	public int getFlowerRange() {
		return 0;
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}
	
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		
		//ItemHandler
		inventory.deserializeNBT(compound);
		
    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        
        //ItemHandler
        compound.merge(inventory.serializeNBT());

        return compound;
    }
	
}
