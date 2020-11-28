package firis.yuzukizuflower.common.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import firis.yuzukizuflower.YuzuKizuFlower;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class YKItemBaseBaubleAmulet extends Item implements IBauble {
	
	private static UUID uuid1 = UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6C");
	private static UUID uuid2 = UUID.fromString("bb099c30-6420-4a1b-9fc8-6d8c412c73d5");
	
	/**
	 * コンストラクタ
	 */
	public YKItemBaseBaubleAmulet() {
		super();
		this.setCreativeTab(YuzuKizuFlower.YuzuKizuCreativeTab);
		this.setMaxStackSize(1);
	}
	
	/**
	 * 装備時Multimap設定追加
	 */
	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		if(!player.world.isRemote) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			getBaubleAttribute(attributes, itemstack);
			player.getAttributeMap().applyAttributeModifiers(attributes);
		}
	}

	/**
	 * 装備解除時Multimap設定削除
	 */
	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		if(!player.world.isRemote) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			getBaubleAttribute(attributes, itemstack);
			player.getAttributeMap().removeAttributeModifiers(attributes);
		}
	}
	
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		
		//playerのhashcodeは毎回変更されるようなので制御が必要
		//player.hashCode()
		if(!player.world.isRemote) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			getBaubleAttribute(attributes, itemstack);
			player.getAttributeMap().applyAttributeModifiers(attributes);
		}
	}
	
	/**
	 * BaublesのMultimapを設定する
	 * @param attributes
	 * @param stack
	 */
	protected abstract void getBaubleAttribute(Multimap<String, AttributeModifier> attributes, ItemStack stack);
	
	
	@Override
	public abstract void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn);
	
	@Override
	public BaubleType getBaubleType(ItemStack paramItemStack) {
		if (paramItemStack.hasTagCompound() && paramItemStack.getTagCompound().hasKey("amulet")) {
			if (!paramItemStack.getTagCompound().getBoolean("amulet")) {
				return BaubleType.RING;
			}
		}
		return BaubleType.AMULET;
	}
	
	/**
	 * UUIDをアクセサリーのタイプによって変更する
	 * @param stack
	 * @return
	 */
	protected UUID getAmuletUUID(ItemStack stack) {
		if (getBaubleType(stack) == BaubleType.RING) {
			return uuid2;
		}
		return uuid1;
	}
	
}
