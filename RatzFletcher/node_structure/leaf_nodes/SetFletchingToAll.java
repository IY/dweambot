package node_structure.leaf_nodes;

import node_structure.BranchNode;
import node_structure.LeafNode;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class SetFletchingToAll extends LeafNode {
    public SetFletchingToAll(final MethodContext context, final BranchNode parent) {
        super(context, parent);
    }

    @Override
    public boolean execute() {
        final WidgetChild allButton = getContext().getWidgets().getWidgetChild(270, 12);
        if(allButton == null)   return TASK_SUCCESS;
        allButton.interact();
        MethodContext.sleepUntil(this::isParentConditionValid, 1000);
        return TASK_SUCCESS;
    }

    @Override
    public String getTaskDescription() {
        return "Selecting \"all\"";
    }
}
