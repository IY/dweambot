package node_structure;

import org.dreambot.api.methods.MethodContext;

public class AntiBanNode implements TaskEvent {
    private final MethodContext context;
    public AntiBanNode(final MethodContext context) {
        this.context = context;
    }

    public MethodContext getContext() {
        return context;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
