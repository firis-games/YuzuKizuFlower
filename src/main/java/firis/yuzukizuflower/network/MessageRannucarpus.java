package firis.yuzukizuflower.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageRannucarpus implements IMessage {
	
	protected int mode = 0;
	
	//ディメンションIDは使わない
	public int dimensionId;
	public BlockPos blockPos;
	
	public MessageRannucarpus() {
	}
	
	/*
	 * 
	 */
	public MessageRannucarpus(World world, BlockPos pos) {
		this.dimensionId = world.provider.getDimension();
		this.blockPos = pos;
	}
	
	/**
	 * byteからの復元
	 * @param buf
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		
		//書き込んだ順番で読み込めばいける？
		this.dimensionId = buf.readInt();
		
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		
		this.blockPos = new BlockPos(x, y, z);
	}

	/**
	 * byteへ変換
	 * @param buf
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		
		//書き込んだ順番で読み込めばいける？
		
		buf.writeInt(this.dimensionId);

		//intを書き込み
		buf.writeInt(this.blockPos.getX());
		buf.writeInt(this.blockPos.getY());
		buf.writeInt(this.blockPos.getZ());
		
		
		
	}

}
