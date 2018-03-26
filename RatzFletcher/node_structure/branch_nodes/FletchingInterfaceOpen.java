package node_structure.branch_nodes;

import node_structure.BranchNode;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.widget.Widget;

public class FletchingInterfaceOpen extends BranchNode{

    public FletchingInterfaceOpen(final MethodContext context) {
        super(context);
    }

    @Override
    public boolean isValid() {
        final Widget widget = getContext().getWidgets().getWidget(270);
        return widget != null && widget.isVisible();
    }
}
