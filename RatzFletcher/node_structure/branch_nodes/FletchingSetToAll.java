package node_structure.branch_nodes;

import node_structure.BranchNode;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class FletchingSetToAll extends BranchNode{
    public FletchingSetToAll(final MethodContext context) {
        super(context);
    }

    /**
     * If All is selected, there are no widget child actions
     * If All is NOT selected, a single option "All" will appear in the array of actions
     * This helps us distinguish between a selected & unselected All button
     * @return
     */
    @Override
    public boolean isValid() {
        final WidgetChild widgetChild = getContext().getWidgets().getWidgetChild(270, 12);
        if(widgetChild == null) return false;
        final String[] actions = widgetChild.getActions();
        return actions == null;
    }
}
