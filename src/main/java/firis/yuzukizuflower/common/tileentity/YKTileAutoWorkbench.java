package firis.yuzukizuflower.common.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import firis.yuzukizuflower.common.botania.BotaniaHelper;
import firis.yuzukizuflower.common.botania.ManaRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

/**
 * 自動作業台
 * @author computer
 *
 */
public class YKTileAutoWorkbench extends YKTileBaseBoxedProcFlower {
	
	//アイテム確認用ID
	ResourceLocation rlPetalWorkbench = new ResourceLocation("yuzukizuflower", "petal_workbench");
	ResourceLocation rlRuneWorkbench = new ResourceLocation("yuzukizuflower", "rune_workbench");
	ResourceLocation rlBlueprint = new ResourceLocation("yuzukizuflower", "blueprint");
	ResourceLocation rlRune = new ResourceLocation("botania", "rune");
	
	//作業台と設計図のスロット番号
	protected final int workbench_slot = 20;
	protected final int blueprint_slot = 21;
	
	public YKTileAutoWorkbench() {
		
		this.maxMana = 100000;
		
		//inputスロット
		this.inputSlotIndex = IntStream.range(0, 22).boxed().collect(Collectors.toList());

		//outputスロット
		this.outputSlotIndex = IntStream.range(22, 23).boxed().collect(Collectors.toList());
				
		//tick周期
		this.setCycleTick(10);
		
	}
	
	@Override
	public void updateProccessing() {
		
		//レッドストーン信号を受けているときは処理を一切行わない
		if(this.isRedStonePower()) return;
		
		if (this.world.isRemote) return;
		
		int mode = isActiveMode();
		
		switch (mode) {
		case 1:
			updateProccessingPetal();
			break;
			
		case 2:
			updateProccessingRune();
			break;
		
		}
	}
	
	/**
	 * 
	 * @param stack1
	 * @param stack2
	 * @return
	 */
	public boolean isEqualItemStack(ItemStack stack1, ItemStack stack2) {
		ItemStack stackA = stack1.copy();
		ItemStack stackB = stack2.copy();
		stackA.setCount(1);
		stackB.setCount(1);
		return ItemStack.areItemStacksEqualUsingNBTShareTag(stackA, stackB);
	}
	
	/**
	 * リストの中身がinventoryに含まれているかをチェックする
	 * @param stackList
	 * @return
	 */
	public boolean isInventoryItemStackList(List<ItemStack> stackList) {

		boolean ret = true;
		//内部インベントリに含まれるか確認する
		for (int idx = 0; idx < stackList.size(); idx++) {
			ItemStack check = stackList.get(idx).copy();
			for (int i = 0; i < 20; i++) {
				ItemStack invStack = this.getStackInSlot(i).copy();
				if (invStack.isEmpty()) continue;
				//一致するか
				if (isEqualItemStack(check, invStack)) {
					check.shrink(invStack.getCount());
				}
				if (check.isEmpty()) {
					break;
				}
			}
			if (!check.isEmpty()) {
				ret = false;
				break;
			}
		}
		return ret;
	}
	
	/**
	 * リストの中身をインベントリから減らす
	 * @param stackList
	 * @return
	 */
	public void shrinkInventoryItemStackList(List<ItemStack> stackList) {

		//内部インベントリに含まれるか確認する
		for (int idx = 0; idx < stackList.size(); idx++) {
			ItemStack check = stackList.get(idx).copy();
			for (int i = 0; i < 20; i++) {
				ItemStack invStack = this.getStackInSlot(i);
				if (invStack.isEmpty()) continue;
				//一致するか
				if (isEqualItemStack(check, invStack)) {
					
					if (invStack.getCount() < check.getCount()) {
						//inventoryを空にする
						check.shrink(invStack.getCount());
						invStack.shrink(invStack.getCount());
					} else {
						//checkを空にする
						invStack.shrink(check.getCount());
						check.shrink(check.getCount());
					}
						
				}
				if (check.isEmpty()) {
					break;
				}
			}
		}
	}
	
	/**
	 * 花びら作業台のレシピチェック処理
	 */
	protected void updateProccessingPetal() {
		
		//マナの残量を確認（水のかわりに100manaを使う）
		int useMana = 100;
		if (this.getMana() < useMana) return;
		
		//設計図からレシピ情報を取得する
		List<ItemStack> stackList;
		stackList = getBlueprintRecipe(this.getStackInSlot(blueprint_slot), false);
		if (stackList.size() == 0) return;
		
		//レシピの整合性をチェックする
		ItemStack resultStack = BotaniaHelper.recipesPetal.getMatchesRecipe(stackList);
		if (resultStack.isEmpty()) return;
		
		//outputスロットに出力できるかを確認する
		if (this.isFillOutputSlotStack(resultStack)) return;
		
		//すべて含まれている場合は調合を行う(触媒込みで判断)
		stackList = getBlueprintRecipe(this.getStackInSlot(blueprint_slot), true);
		if (isInventoryItemStackList(stackList)) {
			
			//inventoryからアイテムを減らす
			this.shrinkInventoryItemStackList(stackList);
			
			//マナを消費
			this.recieveMana(-useMana);
			
			//結果をセット
			this.insertOutputSlotItemStack(resultStack.copy());
		}
		
	}
	
	/**
	 * ルーンの作業台のレシピチェック処理
	 */
	protected void updateProccessingRune() {
		
		//設計図からレシピ情報を取得する
		List<ItemStack> stackList;
		stackList = getBlueprintRecipe(this.getStackInSlot(blueprint_slot), false);
		if (stackList.size() == 0) return;
		
		//レシピの整合性をチェックする
		ManaRecipe resultRecipe = BotaniaHelper.recipesRuneAltar.getMatchesRecipe(stackList);
		if (resultRecipe == null) return;
		
		//outputスロットに出力できるかを確認する
		if (this.isFillOutputSlotStack(resultRecipe.getOutputItemStack())) return;
		
		//マナが足りているか確認する
		int useMana = resultRecipe.getMana();
		if (this.getMana() < useMana) return;
		
		//すべて含まれている場合は調合を行う(触媒込みで判断)
		stackList = getBlueprintRecipe(this.getStackInSlot(blueprint_slot), true);
		
		//stackListからルーンアイテムを除外
		List<ItemStack> workList = new ArrayList<ItemStack>();
		for (ItemStack work : stackList) {
			if (!work.getItem().getRegistryName().equals(rlRune)) {
				workList.add(work);
			}
		}
		stackList = workList;
		
		if (isInventoryItemStackList(stackList)) {
			
			
			//inventoryからアイテムを減らす
			this.shrinkInventoryItemStackList(stackList);
			
			//マナを消費
			this.recieveMana(-useMana);
			
			//結果をセット
			this.insertOutputSlotItemStack(resultRecipe.getOutputItemStack().copy());
		}
	}
	
	/**
	 * 自動処理が稼動可能かを判断する
	 * 1:花びら作業台
	 * 2:ルーンの作業台
	 * @return
	 */
	public int isActiveMode() {
		
		int procMode = 0;
		
		//設計図を取得
		ItemStack workbench = this.getStackInSlot(20);
		ItemStack blueprint = this.getStackInSlot(21);
		String blueprintMode = "";
		if (blueprint.hasTagCompound()) {
			blueprintMode = blueprint.getTagCompound().getString("recipetype");
		}
		
		if ("petal".equals(blueprintMode)) {
			if (!workbench.isEmpty() 
					&& workbench.getItem().getRegistryName().equals(rlPetalWorkbench)) {
				procMode = 1;
			}
		}
		
		if ("rune".equals(blueprintMode)) {
			if (!workbench.isEmpty() 
					&& workbench.getItem().getRegistryName().equals(rlRuneWorkbench)) {
				procMode = 2;
			}
		}
		
		return procMode;
	}

	/**
	 * 設計図に登録されたレシピを取得する
	 * 触媒を含めての場合は同じアイテムを重ねる
	 * @return
	 */
	public List<ItemStack> getBlueprintRecipe(ItemStack stack, boolean catalyst) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		
		if (!stack.hasTagCompound()) {
			return stackList;
		}
		
		//レシピアイテムの取得
		NBTTagList recipeNbtTagList = (NBTTagList) stack.getTagCompound().getTag("recipe");
		for (int i = 0; i < recipeNbtTagList.tagCount(); i++) {
			NBTTagCompound itemTag = (NBTTagCompound) recipeNbtTagList.get(i);
			stackList.add(new ItemStack(itemTag));
		}
				
		//触媒アイテムを取得
		if (catalyst) {
			NBTTagCompound itemTag = (NBTTagCompound) stack.getTagCompound().getTag("catalyst");
			stackList.add(new ItemStack(itemTag));
		
			//重ねる
			List<ItemStack> workList = new ArrayList<ItemStack>();
			for (ItemStack work : stackList) {
				for (int i = 0; i < workList.size(); i++) {
					
					//workとworkリストの中身を重ねる
					if (!work.isEmpty() && isEqualItemStack(work, workList.get(i))) {
						//一致してる場合
						workList.get(i).setCount(workList.get(i).getCount() + work.getCount());
						work.shrink(work.getCount());
					}
					
				}
				if (!work.isEmpty()) {
					workList.add(work.copy());
				}
				
			}
			stackList = workList;
		}
		//変換結果を返却する
		return stackList;
	}

	@Override
	public int getFlowerRange() {
		return 0;
	}

	@Override
	public int getSizeInventory() {
		return 23;
	}
	
	
	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
    }
	
	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        return compound;
    }
	
	
	/**
	 * 対象スロットの使用許可
	 * @Intarface IInventory
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		
		boolean ret = super.isItemValidForSlot(index, stack);
		
		//inputの設計図と機械は別制御
		//機械の判断
		if (index == workbench_slot) {
			ret = false;
			if (!stack.isEmpty() 
					&& (stack.getItem().getRegistryName().equals(rlPetalWorkbench) 
							|| stack.getItem().getRegistryName().equals(rlRuneWorkbench))) {
				ret = true;
			}
		} else if (index == blueprint_slot) {
			ret = false;
			if (!stack.isEmpty() && stack.getItem().getRegistryName()
					.equals(rlBlueprint)){
				ret = true;
			}
		}
		
		return ret;
	}
	
}
