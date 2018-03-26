package main;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.PaintListener;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;

@ScriptManifest(author = "skengrat", category = Category.SMITHING, name = "CannonBallSmither", version = 1.0)
public class CannonBallSmither extends AbstractScript implements PaintListener {
    private final Area Edgeville_BankArea = new Area(3091,3488,3098,3499,0);
    private final Tile Edgeville_BankTile = new Tile(3096,3494,0);
    private final Area Furnace_Area = new Area(3105,3497,3110,3501,0);
    private final Tile Furnace_Tile = new Tile(3109,3499,0);
    private int beginning;
    private int smithing_level;
    private long timeBegan;
    private BufferedWriter writer;

    @Override
    public void onStart(){
        try {
            writer = new BufferedWriter(new FileWriter("error_report.txt"));
        }catch(Exception e){
            e.printStackTrace();
        }
        print("Started CannonBallSmither 1.00!");
        beginning = getSkills().getExperience(Skill.SMITHING);
        timeBegan = System.currentTimeMillis();
        smithing_level = getSkills().getRealLevel(Skill.SMITHING);
    }

    @Override
    public int onLoop(){
        print("Loop");
        if ((getInventory().contains(i -> i.getName().contains("Steel bar")))&&(getInventory().contains(i -> i.getName().contains("Ammo mould")))){
            smelt();
        }else{
            bank();
        }
        return 100;
    }

    @Override
    public void onExit(){
        try {
            writer.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void bank(){
        print("Trying to bank");
        if (Edgeville_BankArea.contains(getLocalPlayer())) { //if at bank
            print("At the bank");
            if (getBank().isOpen()) {
                if ((!getInventory().onlyContains("Ammo mould"))&&(!getInventory().isEmpty())) {
                    sleepUntil(() -> getBank().depositAllExcept("Ammo mould"), 1000);
                } else if (!getInventory().contains("Ammo mould")){
                    if (getBank().contains("Ammo mould")) {
                        getBank().withdraw("Ammo mould", 1);
                        sleepUntil(() -> !getInventory().contains(i -> i.getName().contains("Ammo mould")),1000);
                    }else{
                        print("No ammo mould. Exiting script.");
                        stop();
                    }
                }  else if (!getInventory().contains("Steel bar")){
                    System.out.println("don't have steel bars in inventory");
                    if (getBank().contains("Steel bar")) {
                        getBank().withdrawAll("Steel bar");
                        sleepUntil(() -> !getInventory().contains(i -> i.getName().contains("Steel bar")),1000);
                    }else{
                        print("Out of steel bars. Exiting script.");
                        stop();
                    }
                }
                if (sleepUntil(() -> ((getInventory().contains(i -> i.getName().contains("Steel bar")))&&(getInventory().contains(i -> i.getName().contains("Ammo mould")))), 1000)){
                    if (getBank().close()){
                        sleepUntil(() -> !getBank().isOpen(),3000);
                    }
                }
            } else{
                open_bank(Edgeville_BankTile);
            }
        }else if (Furnace_Area.contains(getLocalPlayer())){
            print("At the furnace");
            walk(Edgeville_BankTile);
        }else {
            print("Somewhere else");
            walk(Edgeville_BankTile);
        }
    }

    private void smelt(){
        print("Trying to smelt");
        smithing_level = getSkills().getRealLevel(Skill.SMITHING);
        if (Edgeville_BankArea.contains(getLocalPlayer())) { //if at bank
            print("At the bank");
            walk(Furnace_Tile);
        }else if (Furnace_Area.contains(getLocalPlayer())) { //if near stairs when upstairs
            print("At the furnace");
            if (getInventory().isItemSelected()){
                print(getInventory().getSelectedItemName());
            }
            if ((getInventory().isItemSelected()) && (getInventory().getSelectedItemName().contains("Steel bar"))){
                print("selected steel bar");
                getGameObjects().closest("Furnace").interact();
                sleepUntil(()-> (getWidgets().getWidgetChild(270,14) != null && getWidgets().getWidgetChild(270,14).isVisible()), 1000);
                WidgetChild makeAllWidg = getWidgets().getWidgetChild(270,14);
                if (makeAllWidg != null && makeAllWidg.isVisible()){
                    print("Widget visible");
                    if (makeAllWidg.interact()){
                        sleepUntil(()->(!getInventory().contains("Steel bar") || (getSkills().getRealLevel(Skill.SMITHING) != smithing_level)),161000);
                    }
                }else {
                    random_camera();
                    getWalking().walk(Furnace_Tile);
                }
            } else if (getInventory().isItemSelected()){
                print("Selected something else.");
                getInventory().deselect();
                sleepUntil(() ->!getInventory().isItemSelected(),1000);
            }else{
                print("Selecting the steel bar");
                getInventory().interact("Steel bar","Use");
                sleepUntil(() -> (getInventory().isItemSelected() && getInventory().getSelectedItemName().contains("Steel bar")),1000);
            }
        }else {
            print("Somewhere else");
            walk(Furnace_Tile);
        }
    }

    private void random_camera(){
        getCamera().rotateTo(Calculations.random(0,2048), Calculations.random(getClient().getLowestPitch(), 383));
    }

    //open the bank if you are near it at the specified tile
    private void open_bank(Tile tile){
        getBank().open(BankLocation.EDGEVILLE);
        if (!sleepUntil(() -> getBank().isOpen(), 2000)) {
            getWalking().walk(tile);
            sleep(Calculations.random(900,1500));
        }
    }


    //walk to tile
    private void walk(Tile tile){
        if (getWalking().walk(tile)) {
            sleep(Calculations.random(800, 1000));
            sleepUntil( () -> !getLocalPlayer().isMoving() || getLocalPlayer().distance(
                    getClient().getDestination()) < Calculations.random(
                    1, 5) , Calculations.random(3000, 5000));
        }
    }

    private void print(String message){
        log(message);
        try {
            writer.write(message +"\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPaint(Graphics g) {
        //g.drawString("isHealthBarVisible: " + String.valueOf(getLocalPlayer().isHealthBarVisible()), 20, 110);
        //g.drawString("State: " + String.valueOf(state), 20, 130);
        // g.drawString("Health timer: " + String.valueOf(System.currentTimeMillis() - health_timer), 20, 150);
        g.drawString("Smithing XP gained: " + String.valueOf(getSkills().getExperience(Skill.SMITHING) - beginning), 20, 40);
    }
}
