package firis.yuzukizuflower.common.tileentity;

/**
 * 一定周期ごとに処理を行う処理系の箱入りお花ベースクラス
 * @author computer
 *
 */
public abstract class YKTileBaseBoxedProcFlower extends YKTileBaseManaPool {
	
	/**
	 * Tickカウンタ
	 */
	private int counterTick = 0;
	
	/**
	 * 設定された周期ごとに処理を行う
	 * 最低1tick以上設定する
	 */
	private int cycleTick = 1;
	public void setCycleTick(int tick) {
		cycleTick = Math.max(tick, 1);
	}
	
	/**
	 * @interface ITickable
	 */
	@Override
	public void update() {
		
		//tickをカウントする
		counterTick = counterTick < Integer.MAX_VALUE ? counterTick + 1 : 0;
		
		//一定周期ごとに処理を行う
		if (counterTick % cycleTick == 0) {
			this.updateProccessing();
		}
	}
	
	/**
	 * 一定周期ごとに処理を行う
	 */
	public abstract void updateProccessing();
}