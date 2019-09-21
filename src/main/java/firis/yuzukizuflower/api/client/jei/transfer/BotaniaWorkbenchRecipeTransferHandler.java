package firis.yuzukizuflower.api.client.jei.transfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mezz.jei.JustEnoughItems;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.config.ServerInfo;
import mezz.jei.network.packets.PacketRecipeTransfer;
import mezz.jei.runtime.JeiHelpers;
import mezz.jei.startup.StackHelper;
import mezz.jei.transfer.BasicRecipeTransferHandler;
import mezz.jei.util.Log;
import mezz.jei.util.Translator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Botania作業台レシピ系の汎用転送処理
 * @author computer
 *
 * @param <C>
 */
public class BotaniaWorkbenchRecipeTransferHandler<C extends Container> extends BasicRecipeTransferHandler<C> {

	private final StackHelper stackHelper;
	private final IRecipeTransferHandlerHelper handlerHelper;
	private final IRecipeTransferInfo<C> transferHelper;
	
	/**
	 * コンストラクタ
	 * @param stackHelper
	 * @param handlerHelper
	 * @param transferHelper
	 */
	public BotaniaWorkbenchRecipeTransferHandler(JeiHelpers jeiHelpers, IRecipeTransferInfo<C> transferInfo) {
		super(jeiHelpers.getStackHelper(), jeiHelpers.recipeTransferHandlerHelper(), transferInfo);
		
		this.stackHelper = jeiHelpers.getStackHelper();
		this.handlerHelper = jeiHelpers.recipeTransferHandlerHelper();
		this.transferHelper = transferInfo;
	}

	@Override
	public IRecipeTransferError transferRecipe(C container, IRecipeLayout recipeLayout, EntityPlayer player, boolean maxTransfer, boolean doTransfer) {

		//先頭の作業用ブロックを除外する
		IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
		Map<Integer, IGuiIngredient<ItemStack>> itemStackGroupIngredients = new HashMap<>();
		int i = 0;
		for (IGuiIngredient<ItemStack> ingredient : itemStackGroup.getGuiIngredients().values()) {
			itemStackGroupIngredients.put(i, ingredient);
			i++;
		}
		itemStackGroupIngredients.remove(0);
		
		return this.transferRecipe(container, itemStackGroupIngredients, player, maxTransfer, doTransfer);
	}
	
	
	/**
	 * @licence MIT
	 * @author mezz
	 * mezz.jei.transfer.BasicRecipeTransferInfo transferRecipeを改変
	 * IRecipeLayout recipeLayoutからレシピチェック用ItemStackList取得部分を
	 * Map<Integer, IGuiIngredient<ItemStack>> itemStackGroupIngredientsへ差し替え
	 * itemStackGroup.getGuiIngredients()とitemStackGroupIngredientsが同じ
	 */
	public IRecipeTransferError transferRecipe(C container, Map<Integer, IGuiIngredient<ItemStack>> itemStackGroupIngredients, EntityPlayer player, boolean maxTransfer, boolean doTransfer) {
		if (!ServerInfo.isJeiOnServer()) {
			String tooltipMessage = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.no.server");
			return handlerHelper.createUserErrorWithTooltip(tooltipMessage);
		}

		if (!transferHelper.canHandle(container)) {
			return handlerHelper.createInternalError();
		}

		Map<Integer, Slot> inventorySlots = new HashMap<>();
		for (Slot slot : transferHelper.getInventorySlots(container)) {
			inventorySlots.put(slot.slotNumber, slot);
		}

		Map<Integer, Slot> craftingSlots = new HashMap<>();
		for (Slot slot : transferHelper.getRecipeSlots(container)) {
			craftingSlots.put(slot.slotNumber, slot);
		}

		int inputCount = 0;
		//IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
		//for (IGuiIngredient<ItemStack> ingredient : itemStackGroup.getGuiIngredients().values()) {
		for (IGuiIngredient<ItemStack> ingredient : itemStackGroupIngredients.values()) {
				if (ingredient.isInput() && !ingredient.getAllIngredients().isEmpty()) {
				inputCount++;
			}
		}

		if (inputCount > craftingSlots.size()) {
			Log.get().error("Recipe Transfer helper {} does not work for container {}", transferHelper.getClass(), container.getClass());
			return handlerHelper.createInternalError();
		}

		Map<Integer, ItemStack> availableItemStacks = new HashMap<>();
		int filledCraftSlotCount = 0;
		int emptySlotCount = 0;

		for (Slot slot : craftingSlots.values()) {
			final ItemStack stack = slot.getStack();
			if (!stack.isEmpty()) {
				if (!slot.canTakeStack(player)) {
					Log.get().error("Recipe Transfer helper {} does not work for container {}. Player can't move item out of Crafting Slot number {}", transferHelper.getClass(), container.getClass(), slot.slotNumber);
					return handlerHelper.createInternalError();
				}
				filledCraftSlotCount++;
				availableItemStacks.put(slot.slotNumber, stack.copy());
			}
		}

		for (Slot slot : inventorySlots.values()) {
			final ItemStack stack = slot.getStack();
			if (!stack.isEmpty()) {
				availableItemStacks.put(slot.slotNumber, stack.copy());
			} else {
				emptySlotCount++;
			}
		}

		// check if we have enough inventory space to shuffle items around to their final locations
		if (filledCraftSlotCount - inputCount > emptySlotCount) {
			String message = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.inventory.full");
			return handlerHelper.createUserErrorWithTooltip(message);
		}
		
		//StackHelper.MatchingItemsResult matchingItemsResult = stackHelper.getMatchingItems(availableItemStacks, itemStackGroup.getGuiIngredients());
		StackHelper.MatchingItemsResult matchingItemsResult = stackHelper.getMatchingItems(availableItemStacks, itemStackGroupIngredients);

		if (matchingItemsResult.missingItems.size() > 0) {
			String message = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.missing");
			return handlerHelper.createUserErrorForSlots(message, matchingItemsResult.missingItems);
		}

		List<Integer> craftingSlotIndexes = new ArrayList<>(craftingSlots.keySet());
		Collections.sort(craftingSlotIndexes);

		List<Integer> inventorySlotIndexes = new ArrayList<>(inventorySlots.keySet());
		Collections.sort(inventorySlotIndexes);

		// check that the slots exist and can be altered
		for (Map.Entry<Integer, Integer> entry : matchingItemsResult.matchingItems.entrySet()) {
			int craftNumber = entry.getKey();
			int slotNumber = craftingSlotIndexes.get(craftNumber);
			if (slotNumber < 0 || slotNumber >= container.inventorySlots.size()) {
				Log.get().error("Recipes Transfer Helper {} references slot {} outside of the inventory's size {}", transferHelper.getClass(), slotNumber, container.inventorySlots.size());
				return handlerHelper.createInternalError();
			}
		}

		if (doTransfer) {
			PacketRecipeTransfer packet = new PacketRecipeTransfer(matchingItemsResult.matchingItems, craftingSlotIndexes, inventorySlotIndexes, maxTransfer, transferHelper.requireCompleteSets());
			JustEnoughItems.getProxy().sendPacketToServer(packet);
		}

		return null;
	}
	
}
