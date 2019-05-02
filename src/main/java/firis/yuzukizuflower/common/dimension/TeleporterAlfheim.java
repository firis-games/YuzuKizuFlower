package firis.yuzukizuflower.common.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterAlfheim extends Teleporter {

	public TeleporterAlfheim(WorldServer worldIn) {
		super(worldIn);
	}

	@Override
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
    {
		return true;
    }
}
