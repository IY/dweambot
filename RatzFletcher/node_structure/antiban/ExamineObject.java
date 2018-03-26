package node_structure.antiban;

import node_structure.AntiBanNode;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.wrappers.interactive.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExamineObject extends AntiBanNode {
    public ExamineObject(final MethodContext context) {
        super(context);
    }

    @Override
    public boolean execute() {
        final MethodContext context = getContext();
        final List<Entity> interactables = new ArrayList<>();
        interactables.addAll(context.getGameObjects().all(entity -> entity != null && entity.isOnScreen() && entity.hasAction("Examine")));
        interactables.addAll(context.getNpcs().all(entity -> entity != null && entity.isOnScreen() && entity.hasAction("Examine")));
        interactables.addAll(context.getGroundItems().all(entity -> entity != null && entity.isOnScreen() && entity.hasAction("Examine")));
        final Optional<Entity> examinableEntity = interactables.stream().findAny();
        return examinableEntity.isPresent() && examinableEntity.get().interact("Examine");
    }
}
