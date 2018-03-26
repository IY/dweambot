package node_structure;

import org.dreambot.api.methods.MethodContext;

public class LeafNode extends BranchNode implements TaskEvent {
    protected static final boolean TASK_SUCCESS = false, TASK_FAILURE = true;
    private BranchNode parent;
    private final boolean antiBanActive;

    public LeafNode(final MethodContext context, final BranchNode parent) {
        this(context, parent, false);
    }

    public LeafNode(final MethodContext context, final BranchNode parent, final boolean antiBanActive) {
        super(context);
        this.parent = parent;
        this.antiBanActive = antiBanActive;
    }

    @Override
    public boolean execute() {
        return false;
    }

    /**
     * A quick means of checking whether the leaf node has successfully executed
     * Used solely within the context of a #sleepUntil within the #execute method
     *
     * @return
     */
    protected boolean isParentConditionValid() {
        return parent.isValid();
    }

    public String getTaskDescription() {
        return "No current task";
    }

    public boolean isAntiBanActive() {
        return antiBanActive;
    }
}
