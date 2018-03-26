package climbingboots;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.randoms.RandomSolver;

public class TradeSolver extends RandomSolver{

	ClimbingBootsTraderScript script;
	private long lastTime = 0L;
	private float threshold;
	private final float THRESH_BASE = 0.3f;
//	private final float ONLINE_A = 2f;
//	private final float ONLINE_B = 5f;
//	private final float OFFLINE_A = 20;
//	private final float OFFLINE_B = 30;

	public TradeSolver(RandomEvent re, ClimbingBootsTraderScript cbs){
		super(re, cbs);
		this.script = cbs;
		this.threshold = (float) (Calculations.getRandom().nextFloat() * (1f - THRESH_BASE) + THRESH_BASE);
	}

	@Override
	public int onLoop(){
		return 2000;
	}

	@Override
	public boolean shouldExecute(){
		if(!script.getClient().isLoggedIn()){
			long delta = script.getTimer().elapsed() - lastTime;
			float fDeltaMinutes = ((float) delta) / 60000.0f;
			if(Math.exp((fDeltaMinutes - script.getOfflineTime())* 2 / script.getOfflineTime()) > threshold){
				lastTime = script.getTimer().elapsed();
				threshold = Calculations.getRandom().nextFloat() * (1f - THRESH_BASE) + THRESH_BASE;
				script.getRandomManager().enableSolver(RandomEvent.LOGIN);
				return true;
			}
		}else{
			long delta = script.getTimer().elapsed() - lastTime;
			float fDeltaMinutes = ((float) delta) / 60000.0f;
			if(Math.exp((fDeltaMinutes - script.getOnlineTime()) * 2 / script.getOnlineTime()) > threshold){
				lastTime = script.getTimer().elapsed();
				threshold = Calculations.getRandom().nextFloat() * (1f - THRESH_BASE) + THRESH_BASE;
				script.startLogout();
				return true;
			}
		}

		return false;
	}

}
