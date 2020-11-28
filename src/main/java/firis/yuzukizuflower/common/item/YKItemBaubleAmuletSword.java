package firis.yuzukizuflower.common.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class YKItemBaubleAmuletSword extends YKItemBaseBaubleAmulet {

	@Override
	protected void getBaubleAttribute(Multimap<String, AttributeModifier> attributes, ItemStack stack) {
		
		UUID uuid = getAmuletUUID(stack);
		
		//ベース攻撃力up
		attributes.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), 
				new AttributeModifier(uuid, "Attack Damage modifier", 3, 0));
		
		//リーチアップ
		attributes.put(EntityPlayer.REACH_DISTANCE.getName(), 
        		new AttributeModifier(uuid, "Reach modifier", 1.0F, 0));
	}
	
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    	tooltip.add(I18n.format("attribute.name.generic.attackDamage") + " +3");
    }
	

}
