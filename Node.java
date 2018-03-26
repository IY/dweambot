package node;


import org.dreambot.api.script.AbstractScript;

public abstract class Node {

	protected AbstractScript as;
	 
	public Node(AbstractScript as) {
		this.as = as;
	}
	
	public AbstractScript getAs() {
		return this.as;
	}
	
	public abstract int execute() throws InterruptedException;
	public abstract boolean activate() throws InterruptedException;
	
}
