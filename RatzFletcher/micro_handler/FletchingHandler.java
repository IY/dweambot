package micro_handler;

import node_structure.BranchNode;
import node_structure.LeafNode;
import node_structure.branch_nodes.*;
import node_structure.leaf_nodes.*;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.container.impl.Inventory;

import java.util.Random;

public class FletchingHandler {
    private final BranchNode root;
    private final Random random;
    private String taskDescription;
    private AntiBanHandler antiBanHandler;
    private MethodContext context;


    public FletchingHandler(final MethodContext context) {
        this.root = buildTree(context);
        random = new Random();
        antiBanHandler = new AntiBanHandler(context);
        this.context = context;
    }

    /**
     * Building the connection of the nodes in the structure of a tree
     *
     * @param context
     * @return
     */
    private BranchNode buildTree(final MethodContext context) {
        final BranchNode root = new BusyFletching(context);
        BranchNode failureBranch = root;
        failureBranch.setSuccess(new IdleWhileFletching(context, failureBranch, true));
        failureBranch = failureBranch.setFailure(new ContainsItems(context));
        BranchNode successBranch = failureBranch.setSuccess(new BankOpen(context));
        failureBranch = failureBranch.setFailure(new BankOpen(context));
        failureBranch.setFailure(new OpenBank(context, failureBranch));
        failureBranch = failureBranch.setSuccess(new InventoryFull(context));
        failureBranch.setFailure(new WithdrawItems(context, failureBranch));
        failureBranch.setSuccess(new DepositItems(context, failureBranch));
        successBranch.setSuccess(new CloseBank(context, successBranch));
        failureBranch = successBranch.setFailure(new FletchingInterfaceOpen(context));
        failureBranch.setFailure(new Combination(context, failureBranch));
        successBranch = failureBranch.setSuccess(new FletchingSetToAll(context));
        successBranch.setSuccess(new SelectFletchingOption(context, successBranch));
        successBranch.setFailure(new SetFletchingToAll(context, successBranch));
        return root;
    }

    public int verify() {
        BranchNode pointer = root;
        while (!(pointer instanceof LeafNode))
            pointer = pointer.isValid() ? pointer.getSuccess() : pointer.getFailure();
        final LeafNode toExecute = ((LeafNode) pointer);
        taskDescription = toExecute.getTaskDescription();
        if (toExecute.isAntiBanActive()) antiBanHandler.verify();
        final Inventory inventory = context.getInventory();
        if (inventory.isItemSelected()) inventory.deselect();
        return toExecute.execute() ? -1 : random.nextInt(25);
    }

    public String getTaskDescription() {
        return taskDescription;
    }
}
