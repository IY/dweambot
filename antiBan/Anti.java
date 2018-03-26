package antiBan;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.AbstractScript;
import org.omg.PortableServer.POAManagerPackage.State;

public class Anti {

	//between 500-1500
	public int seed;
	//single digit
	public int smallDeviation;
	//two digits
	public int medDeviation;
	public int waitTime;
	public Statex state;
	
	public long time = 0;
	public long start = 0;
	
	public Anti(String name) {
		seededRandom(name);
	}
	
	public enum Statex {
		ACTIVE, INACTIVE;
	}
	
	public void setState() {
		if((int)(Math.random() * seed) > (seed * (smallDeviation * 0.1))) {
			activeState();
			state = Statex.ACTIVE;
		} else {
			inactiveState();
			state = Statex.INACTIVE;
		}
	}
	
	public void activeState() {
		if(((int)Math.random() * 2) == 0) {
			waitTime = (seed + (int)(Math.random() * 2723));
		} else {
			waitTime = (seed - (int)(Math.random() * 234));
		}
	}
	
	public void inactiveState() {
		if(((int)Math.random() * 2) == 0) {
			waitTime = (seed + ((int)(Math.random() * 5723) + (int)(Math.random() * 5001) + (seed + 5000)));
		} else {
			waitTime = (seed + ((int)(Math.random() * 5723) + (int)(Math.random() * 5001) + (seed + 8000)));
		}
	}
	
	public void switchState() {
		time = System.currentTimeMillis() - start;
		if(state == Statex.ACTIVE) {
			//switch states after 51 secs + (0-4) mins
			if((time > (51000 + (int)(Math.random()) * 237135))) {
				time = 0;
				start = System.currentTimeMillis();
				setState();
			}
		} else {
			//switches states after 8 minutes + (0-23) mins
			if((time > (471321 + (int)(Math.random()) * 1413789))) {
				time = 0;
				start = System.currentTimeMillis();
				setState();
			}
		}
	}
	
	public int stateWaitTime() {
		if((((int)Math.random()) * 2) == 0) {
			return waitTime + Calculations.random(seed);
		} else {
			return waitTime - Calculations.random(seed);
		}
	}
	public int seededRandom(String name) {
		
		int total = 0;
		
		if(name.length() % 2 == 0) {
			for(int i = 0; i < name.length(); i++) {
				total += name.charAt(i) * (Math.random() * 100.0);
			}
		} else {
			for(int i = 0; i < name.length(); i++) {
				total += name.charAt(i) * (Math.random() * 100.0);
			}
		}
		
		while(total > 1500) {
			total %= 1500;
		}
		
		while(total < 500) {
			total += (int)(Math.random() * 200);
		}
		
		//copy variables before messing around
		seed = total;
		medDeviation = seed;
		smallDeviation = seed;
		
		while((smallDeviation / 10) > 0) {
			smallDeviation = smallDeviation % 10;
			System.out.println("Loop");
		}
		
		while(medDeviation > 100) {
			medDeviation = medDeviation / 10;
		}
		
		while(medDeviation < 50) {
			medDeviation += smallDeviation;
		}
		return total;
	}
	
	public void forceStateActive(boolean x) {
		if(x == true) {
			this.state = Statex.ACTIVE;
			activeState();
		} else {
			this.state = Statex.INACTIVE;
			inactiveState();
		}
	}
}
