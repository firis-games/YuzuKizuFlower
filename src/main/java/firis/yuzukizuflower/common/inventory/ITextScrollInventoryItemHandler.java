package firis.yuzukizuflower.common.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

public class ITextScrollInventoryItemHandler extends IScrollInventoryItemHandler {

	
	protected String textSearch;
	
	protected List<Integer> capabilityIndexList;
	
	public ITextScrollInventoryItemHandler(TileEntity tile, boolean animation) {
		super(tile, animation);
		
		//Indexを初期化
		this.textSearch = "";
		this.capabilityIndexList = null;
		this.setFilerList();
	}
	
	public void setFilerList() {
		
		if (textSearch.equals("")) {
			//デフォルト
			capabilityIndexList = IntStream.range(0, this.getItemHandler().getSlots()).boxed().collect(Collectors.toList());
			
		} else {
			capabilityIndexList = new ArrayList<Integer>();
			//フィルタあり
			for (int i = 0; i < this.getItemHandler().getSlots(); i++) {
				
				ItemStack stack = this.getItemHandler().getStackInSlot(i);
				if (!stack.isEmpty()) {
					
					//検索
					if (stack.getDisplayName().indexOf(textSearch) != -1
							|| stack.getItem().getRegistryName().toString().indexOf(textSearch) != -1 ) {
						capabilityIndexList.add(i);
					}
				}
			}
		}
		
		//ページを初期化
		this.page = 0;
		//MaxPageを計算する
		float calPage = capabilityIndexList.size() - this.inventoryCount;
		if (calPage > 0) {
			this.maxPage = (int) Math.ceil(calPage / (float)this.inventoryRowCount);
		} else {
			this.maxPage = 0;
		}
		
	}
	
	@Override
	public int getCapabilityIndex(int index) {
		int defIndex = index + this.page * this.inventoryRowCount;
		if (capabilityIndexList.size() <= index) return index; 
		return capabilityIndexList.get(defIndex);
	}
	
	/**
	 * @Intarface IScrollInventory
	 */
	@Override
	public boolean isLockedScrollSlot(int index) {
		IItemHandler handler = getItemHandler();
		if (handler == null || capabilityIndexList.size() <= index) return true;
		
		return false;
	}

	//テキスト制御用
	@Override
	public void setTextChanged(String text) {
		
		this.textSearch = text;
		setFilerList();
		
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		super.setInventorySlotContents(index, stack);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack = super.decrStackSize(index, count);
		return stack;
	}
}
