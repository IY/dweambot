package badmanting;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.clan.Clan;
import org.dreambot.api.methods.friend.Friend;
import org.dreambot.api.methods.friend.Friends;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.input.mouse.MouseSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.methods.worldhopper.WorldHopper;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.AdvancedMessageListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.widgets.Menu;
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.api.wrappers.widgets.message.Message;
import org.dreambot.core.Instance;

@ScriptManifest(author="skengrat", name="Spammer", version=2.2, description="Spams and pm's", category=Category.MISC)
public class MassPMer
extends AbstractScript
implements AdvancedMessageListener {
    Timer timer = new Timer();
    String status = "";
    Point[] lastPositions = new Point[15];
    boolean isRunning = false;
    boolean spamming = false;
    boolean isMember = false;
    boolean clansAdded = false;
    boolean loggedIn = false;
    boolean worldHopping = false;
    boolean clanSpam = false;
    long lastSpam = 0;
    String smartReply = "";
    int modToHop = 0;
    int smartReplys = 0;
    int amountLogin = 0;
    String playerUsername = "";
    String playerPassword = "";
    String user = Client.getForumUser().toString();
    ArrayList<String> clans = new ArrayList();
    ArrayList<String> playersToMessage = new ArrayList();
    ArrayList<String> blacklist2 = new ArrayList();
    ArrayList<String> blacklist = new ArrayList();
    int playersMessaged = 0;
    int worldsHopped = 0;
    static String allClans = "";
    static String userToMessage = "";
    static String spam = "";
    static String reply1 = "";
    static String reply2 = "";
    int spamCount = 0;
    private String userEnabled;

    public void onStart() {
        Friend[] f;
        URL oracle = null;
        try {
            oracle = new URL("http://oppblock.eu/scripts/enabled.txt");
        }
        catch (MalformedURLException e2) {
            e2.printStackTrace();
        }
        BufferedReader in = null;
        try {
            assert (oracle != null);
            in = new BufferedReader(new InputStreamReader(oracle.openStream()));
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            String inputLine;
            assert (in != null);
            while ((inputLine = in.readLine()) != null) {
                this.userEnabled = inputLine;
            }
            MassPMer.log((String)(String.valueOf(this.userEnabled) + " " + this.user));
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.timer.setRunTime(10000000);
        this.createGUI();
        if (this.getClan().inChat()) {
            this.leaveClan();
        }
        if (this.checkAuth()) {
            MassPMer.log((String)("Welcome " + this.user));
        }
        if (this.getClient().isLoggedIn()) {
            MassPMer.log((String)"User is logged in");
            this.loggedIn = true;
        }
        List member = this.getWorlds().members();
        if (this.getClient().getMembershipLeft() > 0 || this.getClient().isMembers() || member.contains(this.getClient().getCurrentWorld())) {
            MassPMer.log((String)"user is a member");
            this.isMember = true;
        }
        if (this.loggedIn && (f = this.getFriends().getFriends()).length > 0) {
            this.status = "Clearing friends list";
            MassPMer.log((String)this.status);
            for (int i = 0; i < f.length; ++i) {
                if (f[i].getName().contains("#")) continue;
                this.deleteFriend(f[i].getName());
            }
            MassPMer.log((String)"List cleared");
        }
        this.playerUsername = this.getClient().getUsername();
        this.playerPassword = this.getClient().getPassword();
        while (!this.isRunning) {
            MassPMer.sleep((int)200);
        }
        this.addClans();
        this.leaveClan();
    }

    public void onExit() {
        MassPMer.log((String)"*********************************");
        if (this.checkAuth()) {
            MassPMer.log((String)("Good bye" + this.user));
        }
        MassPMer.log((String)("isMember: " + this.isMember));
        MassPMer.log((String)("Username: " + this.playerUsername));
        MassPMer.log((String)("Ran for: " + this.timer.formatTime()));
        MassPMer.log((String)("Spammed: " + this.spamCount));
        MassPMer.log((String)("Re-Logged: " + this.amountLogin));
        MassPMer.log((String)("Players Messenged: " + this.playersMessaged));
        MassPMer.log((String)("Worlds Hopped: " + this.worldsHopped));
    }

    public boolean checkAuth() {
        return this.user.contains(this.userEnabled);
    }

    public void addClans() {
        if (!this.clansAdded && allClans.length() != 0) {
            try {
                String e = "";
                for (int i = 0; i < allClans.length(); ++i) {
                    String c = allClans.substring(i, i + 1);
                    if (!c.equals(",")) {
                        e = String.valueOf(e) + c;
                        continue;
                    }
                    this.clans.add(e);
                    e = "";
                }
                this.clans.add(e);
                MassPMer.log((String)("Clans added: " + this.clans.size()));
                this.clansAdded = true;
            }
            catch (Exception var4) {
                MassPMer.log((String)("ERORR addClans: " + var4.toString()));
            }
        } else {
            MassPMer.log((String)"No clans added");
            this.clansAdded = true;
        }
    }

    public void waitFor(boolean x, String y, int z) {
        int count;
        for (count = 0; x && count <= z; ++count) {
            this.status = String.valueOf(y) + "..." + count + "/" + z;
            if (y.toLowerCase().contains("logging in")) {
                if (this.getClient().getLoginIndex() == 3) {
                    return;
                }
                if (this.getClient().isLoggedIn()) {
                    return;
                }
            }
            MassPMer.sleep((int)1000);
        }
        if (count == z) {
            MassPMer.log((String)("Time out: " + y + ".... moving on"));
        }
    }

    public void deleteFriend(String x) {
        try {
            this.status = "Removing Friend: " + x;
            MassPMer.log((String)this.status);
            this.getTabs().open(Tab.FRIENDS);
            MassPMer.sleep((int)250);
            this.getMouse().move(new Point(Calculations.random((int)655, (int)700), Calculations.random((int)435, (int)458)));
            MassPMer.sleep((int)250);
            this.getMouse().click();
            MassPMer.sleep((int)250);
            this.type(x);
            this.waitFor(this.getFriends().haveFriend(x), "Waiting to delete friend", 8);
        }
        catch (Exception var3) {
            MassPMer.log((String)("ERROR deleteFriend: " + var3.toString()));
        }
    }

    public void addFriend(String x) {
        try {
            if (this.getFriends().toString().toLowerCase().contains(x.toLowerCase())) {
                MassPMer.log((String)("Friend is already on list: " + x));
                return;
            }
            this.status = "Adding Friend: " + x;
            MassPMer.log((String)this.status);
            this.getTabs().open(Tab.FRIENDS);
            MassPMer.sleep((int)250);
            this.getMouse().move(new Point(Calculations.random((int)566, (int)630), Calculations.random((int)430, (int)453)));
            MassPMer.sleep((int)250);
            this.getMouse().click();
            MassPMer.sleep((int)1200);
            this.type(x);
            for (int e = 0; !this.getFriends().haveFriend(x) && e < 5; ++e) {
                this.status = "Waiting to add friend";
                MassPMer.sleep((int)1000);
            }
            this.status = Arrays.toString((Object[])this.getFriends().getFriends()).length() > 0 ? "Friend Added" : "Friend Add Failed";
            MassPMer.sleep((int)1500);
        }
        catch (Exception var3) {
            MassPMer.log((String)("addfriend: " + var3.toString()));
        }
    }

    private void type(String message) {
        this.getKeyboard().type((Object)message);
        Canvas canvas = this.getClient().getInstance().getCanvas();
        for (char c : message.toCharArray()) {
            canvas.dispatchEvent(new KeyEvent(canvas, 400, System.currentTimeMillis(), 0, 0, c));
        }
        canvas.dispatchEvent(new KeyEvent(canvas, 401, System.currentTimeMillis(), 0, 10, '\uffff'));
        canvas.dispatchEvent(new KeyEvent(canvas, 402, System.currentTimeMillis(), 0, 10, '\uffff'));
        MassPMer.sleep((int)350);
    }

    public void sendPM2() {
        try {
            this.addFriend(this.playersToMessage.get(0));
            MassPMer.sleep((int)250);
            Friend[] e = this.getFriends().getFriends();
            MassPMer.log((String)("Messaging: " + e[0].getName()));
            while (this.getFriends().getFriends().length > 0) {
                if (!this.getTabs().isOpen(Tab.FRIENDS)) {
                    this.getTabs().open(Tab.FRIENDS);
                }
                String player = e[0].getName();
                if (!this.getFriends().getFriend(player).isOnline()) {
                    MassPMer.log((String)"player is offline");
                    this.deleteFriend(player);
                    this.blacklist2.add(player);
                    this.blacklist.remove(player);
                    this.playersToMessage.remove(player);
                    return;
                }
                this.status = "Messaging: " + player;
                this.getMouse().move(new Point(578, 250));
                MassPMer.sleep((int)150);
                this.getMouse().click(true);
                MassPMer.sleep((int)150);
                if (!this.getClient().getMenu().contains("Message")) {
                    return;
                }
                if (this.playersToMessage.contains(player) && !this.blacklist.contains(player)) {
                    if (!this.getClient().getMenu().clickAction("Message")) {
                        return;
                    }
                    MassPMer.log((String)"r1");
                    MassPMer.sleep((int)500);
                    this.type(reply1);
                    MassPMer.sleep((int)2000);
                    this.getMouse().move(new Point(578, 250));
                    MassPMer.sleep((int)150);
                    this.getMouse().click(true);
                    MassPMer.sleep((int)150);
                    if (!this.getClient().getMenu().clickAction("Message")) {
                        return;
                    }
                    MassPMer.log((String)"r2");
                    MassPMer.sleep((int)500);
                    this.type(reply2);
                    this.status = "Message sent to: " + player;
                    MassPMer.log((String)this.status);
                    this.blacklist.add(player);
                    this.playersToMessage.remove(player);
                    ++this.playersMessaged;
                } else if (this.checkAuth() && this.blacklist2.contains(player) && this.blacklist.contains(player)) {
                    this.status = "Sending smart message: " + player;
                    if (this.getClient().getMenu().clickAction("Message")) {
                        MassPMer.log((String)"Smart reply");
                        MassPMer.sleep((int)500);
                        this.type(this.smartReply);
                        ++this.smartReplys;
                        this.blacklist.remove(player);
                        this.playersToMessage.remove(player);
                    }
                    MassPMer.sleep((int)2000);
                }
                MassPMer.sleep((int)100);
                this.deleteFriend(player);
            }
        }
        catch (Exception var3) {
            MassPMer.log((String)("ERROR sendPM2: " + var3.toString()));
        }
    }

    public void sendPM(String player) {
        try {
            boolean e = false;
            if (this.blacklist2.contains(player) && !this.blacklist.contains(player)) {
                MassPMer.log((String)("Blacklist 2 contains and blacklist doesnt: " + player));
                return;
            }
            this.addFriend(player);
            WidgetChild[] w = this.getWidgets().getWidget(429).getChild(8).getChildren();
            for (int x = 0; x < w.length; ++x) {
                if (w[x].getText().trim().contains(player.trim()) && this.getFriends().getFriend(player).isOnline() && w[x].getText().length() != 0) {
                    MassPMer.log((String)this.status);
                    if (this.playersToMessage.contains(player) && !this.blacklist.contains(player)) {
                        this.status = "Sending message: " + w[x].getText();
                        if (w[x].interact("Message")) {
                            MassPMer.log((String)"r1");
                            MassPMer.sleep((int)500);
                            this.type(reply1);
                        }
                        MassPMer.sleep((int)2000);
                        if (w[x].interact("Message")) {
                            MassPMer.log((String)"r2");
                            MassPMer.sleep((int)500);
                            this.type(reply2);
                        }
                        e = true;
                    } else if (this.blacklist2.contains(player) && this.blacklist.contains(player)) {
                        this.status = "Sending smart message: " + w[x].getText();
                        if (w[x].interact("Message")) {
                            MassPMer.log((String)"Smart reply");
                            MassPMer.sleep((int)500);
                            this.type(this.smartReply);
                            ++this.smartReplys;
                            this.blacklist.remove(player);
                        }
                        MassPMer.sleep((int)2000);
                    }
                    w[x].interact("Delete");
                    this.waitFor(this.getFriends().haveFriend(w[x].getText()), "Waiting to delete friend", 8);
                    this.playersToMessage.remove(player);
                    break;
                }
                if (!w[x].getText().contains(player) || w[x].getText().length() == 0 || this.getFriends().getFriend(player).isOnline()) continue;
                this.status = "Deleting offline player";
                MassPMer.log((String)this.status);
                w[x].interact("Delete");
                this.blacklist2.add(player);
                for (int count = 0; this.getFriends().haveFriend(w[x].getText()) && count <= 8; ++count) {
                    this.status = "Waiting to add friend";
                    MassPMer.sleep((int)1000);
                }
                break;
            }
            if (e) {
                this.blacklist.add(player);
                MassPMer.log((String)("Message sent to: " + player));
            }
            MassPMer.log((String)("Players To Message left: " + this.playersToMessage.size()));
            for (String var8 : this.playersToMessage) {
                MassPMer.log((String)var8);
            }
            this.playersToMessage.remove(player);
            ++this.playersMessaged;
        }
        catch (Exception var7) {
            MassPMer.log((String)("ERROR sendPM: " + var7.toString()));
        }
    }

    public void spam() {
        short interval = (short)Calculations.random((int)3100, (int)5000);
        this.spamming = true;
        this.getMouse().getMouseSettings().setWordsPerMinute(250.0);
        if (this.timer.elapsed() - this.lastSpam < (long)interval && this.spamCount != 0 && this.spamCount % this.modToHop != 0) {
            this.spamming = false;
            this.status = "Waiting for a PM...";
        } else {
            this.getKeyboard().typeSpecialKey(10);
            this.status = "Spamming Public";
            MassPMer.log((String)this.status);
            this.type(spam);
            this.lastSpam = this.timer.elapsed();
            this.spamming = false;
            ++this.spamCount;
        }
        MassPMer.sleep((int)5000, (int)6000);
    }

    public void joinClan() {
        if (!this.clans.isEmpty()) {
            String clanToJoin = this.clans.get(0);
            this.status = "Joining clan: " + clanToJoin;
            MassPMer.log((String)this.status);
            if (!this.getTabs().isOpen(Tab.CLAN)) {
                this.getTabs().open(Tab.CLAN);
            }
            this.getMouse().move(new Point(Calculations.random((int)566, (int)630), Calculations.random((int)431, (int)462)));
            MassPMer.sleep((int)1000);
            this.getMouse().click();
            MassPMer.sleep((int)150);
            this.type(clanToJoin);
            for (int x = 0; !this.getClan().inChat() && x < 8; ++x) {
                MassPMer.log((String)"Waiting to join clan..");
                MassPMer.sleep((int)500);
            }
            this.clans.remove(0);
            MassPMer.log((String)("Joined, clans left: " + this.clans.size()));
        }
    }

    public void leaveClan() {
        if (this.getClan().inChat()) {
            this.status = "Leaving chat";
            MassPMer.log((String)this.status);
            if (!this.getTabs().isOpen(Tab.CLAN)) {
                this.getTabs().open(Tab.CLAN);
            }
            this.getMouse().move(new Point(Calculations.random((int)566, (int)630), Calculations.random((int)430, (int)453)));
            MassPMer.sleep((int)1000);
            this.getMouse().click();
            for (int x = 0; this.getClan().inChat() && x < 8; ++x) {
                MassPMer.sleep((int)500);
            }
        }
        if (this.clans.isEmpty()) {
            MassPMer.log((String)"No more clans to join");
        }
    }

    public int onLoop() {
        if (this.playersToMessage.size() > 0) {
            this.isRunning = true;
            this.loggedIn = true;
        } else if (this.getClient().isLoggedIn()) {
            this.loggedIn = true;
        }
        try {
            if (this.isRunning && this.loggedIn && reply1.length() > 0) {
                if (!this.getClient().isLoggedIn()) {
                    this.loggedIn = false;
                    return 1000;
                }
                if (this.playersToMessage.size() > 0) {
                    this.sendPM2();
                }
                if (this.checkAuth() && this.blacklist2.size() > 0 && this.blacklist.contains(this.blacklist2.get(0))) {
                    this.sendPM2();
                    return 800;
                }
                if (!this.getClan().inChat() && !this.clans.isEmpty()) {
                    this.joinClan();
                    return 800;
                }
                if (this.clanSpam) {
                    this.type("/" + spam);
                    ++this.spamCount;
                    this.leaveClan();
                    this.clanSpam = false;
                    MassPMer.sleep((int)400);
                }
                this.spam();
                return 800;
            }
            if (this.loggedIn) {
                return 800;
            }
            this.isRunning = false;
            this.reLogin();
            return 800;
        }
        catch (Exception var2) {
            MassPMer.log((String)("ERROR onloop: " + var2.toString()));
            return 800;
        }
    }

    public void reLogin() {
        try {
            if (this.getClient().isLoggedIn()) {
                this.loggedIn = true;
                return;
            }
            this.status = "Logging in";
            MassPMer.log((String)"Logging in");
            this.getMouse().move(new Point(467, 297));
            MassPMer.sleep((int)1000);
            this.getMouse().click();
            MassPMer.sleep((int)500);
            this.getKeyboard().type((Object)this.playerUsername, false);
            MassPMer.sleep((int)500);
            this.getKeyboard().typeSpecialKey(9);
            MassPMer.sleep((int)500);
            this.getKeyboard().type((Object)this.playerPassword);
            for (int e = 0; !this.getClient().isLoggedIn() && e < 8; ++e) {
                this.status = "Waiting to log in.." + e;
                MassPMer.log((String)this.status);
                MassPMer.sleep((int)1000);
            }
            if (this.getClient().isLoggedIn()) {
                MassPMer.log((String)"Player logged in");
                this.isRunning = true;
                this.loggedIn = true;
                ++this.amountLogin;
            } else {
                MassPMer.log((String)"Player couldnt log back in");
                this.loggedIn = false;
                this.stop();
            }
        }
        catch (Exception var2) {
            MassPMer.log((String)("ERROR reLogin: " + var2.toString()));
        }
    }

    public void worldHop() {
        if (this.checkAuth()) {
            this.worldHopping = false;
            if (this.playersToMessage.size() <= 0) {
                List count;
                if (!this.isMember && !this.getWorlds().members().contains(this.getClient().getCurrentWorld())) {
                    this.status = "World hop: Free";
                    MassPMer.log((String)this.status);
                    count = this.getWorlds().f2p();
                    this.getWorldHopper().hopWorld((World)count.get(Calculations.random((int)0, (int)count.size())));
                    this.worldHopping = true;
                } else {
                    this.status = "World hop: Members";
                    MassPMer.log((String)this.status);
                    count = this.getWorlds().members();
                    for (int i = 0; i < count.size(); ++i) {
                        if (!((World)count.get(i)).isPVP() && !((World)count.get(i)).isDeadmanMode() && !((World)count.get(i)).isHighRisk() && ((World)count.get(i)).getMinimumLevel() < 500) continue;
                        count.remove(i);
                        --i;
                    }
                    this.getWorldHopper().hopWorld((World)count.get(Calculations.random((int)0, (int)count.size())));
                    this.worldHopping = true;
                    this.isMember = true;
                }
                for (int var3 = 0; this.worldHopping && var3 < 8; ++var3) {
                    this.status = "Waiting to hop worlds.." + var3;
                    MassPMer.sleep((int)500);
                }
                this.worldHopping = false;
            }
        }
    }

    public void randomMouseMove() {
        if (Calculations.random((int)0, (int)10) >= 8) {
            this.status = "Random mouse";
            this.getMouse().move(new Point(this.getMouse().getX() + Calculations.random((int)-200, (int)200), this.getMouse().getY() + Calculations.random((int)-200, (int)200)));
            MassPMer.sleep((int)Calculations.random((int)50, (int)500));
        } else if (Calculations.random((int)0, (int)10) == 1) {
            if (Calculations.random((int)0, (int)10) >= 8) {
                this.status = "Random tabs";
                this.getTabs().openWithMouse(Tab.STATS);
                MassPMer.sleep((int)Calculations.random((int)800, (int)2000));
            }
            if (Calculations.random((int)0, (int)10) >= 9) {
                this.getTabs().openWithMouse(Tab.COMBAT);
                MassPMer.sleep((int)Calculations.random((int)800, (int)2000));
            }
            if (!this.getTabs().isOpen(Tab.INVENTORY)) {
                this.getTabs().open(Tab.INVENTORY);
                MassPMer.sleep((int)Calculations.random((int)250, (int)2000));
                if (Calculations.random((int)0, (int)5) >= 2) {
                    this.getMouse().move(new Point(this.getMouse().getX() + Calculations.random((int)-200, (int)200), this.getMouse().getY() + Calculations.random((int)-200, (int)200)));
                }
            }
        }
    }

    private void createGUI() {
        JFrame frame = new JFrame();
        frame.setTitle("Phisher Gui");
        frame.setDefaultCloseOperation(2);
        frame.setLocationRelativeTo(this.getClient().getInstance().getCanvas());
        frame.getContentPane().setLayout(new BorderLayout());
        frame.pack();
        frame.setVisible(true);
        JPanel settingPanel = new JPanel();
        settingPanel.setLayout(new GridLayout(5, 1));
        JLabel spamLabel = new JLabel();
        spamLabel.setText("Spam content:");
        JTextField spamContent = new JTextField();
        settingPanel.add(spamContent);
        settingPanel.add(spamLabel);
        JLabel reply1Label = new JLabel();
        reply1Label.setText("Reply Text 1");
        JTextField reply1Content = new JTextField();
        settingPanel.add(reply1Content);
        settingPanel.add(reply1Label);
        JLabel reply2Label = new JLabel();
        reply2Label.setText("Reply Text 2");
        JTextField reply2Content = new JTextField();
        settingPanel.add(reply2Content);
        settingPanel.add(reply2Label);
        JLabel smartLabel = new JLabel();
        smartLabel.setText("Smart Reply");
        JTextField smartReplyContent = new JTextField();
        if (this.checkAuth()) {
            settingPanel.add(smartReplyContent);
            settingPanel.add(smartLabel);
        }
        JLabel clanLabel = new JLabel();
        clanLabel.setText("Clan chat");
        settingPanel.add(clanLabel);
        JTextField clanContent = new JTextField();
        clanContent.setPreferredSize(new Dimension(100, 12));
        settingPanel.add(clanContent);
        settingPanel.add(clanLabel);
        frame.getContentPane().add((Component)settingPanel, "East");
        JPanel settingPanel2 = new JPanel();
        settingPanel2.setLayout(new GridLayout(2, 1));
        JLabel hopLabel = new JLabel();
        hopLabel.setText("Hop after: (not used)");
        settingPanel2.add(hopLabel);
        JComboBox<Integer> hopComboBox = new JComboBox<Integer>(new Integer[]{0, 1, 2, 3, 4, 5});
        hopComboBox.setSelectedIndex(2);
        settingPanel2.add(hopComboBox);
        JCheckBox memberCheckBox = new JCheckBox();
        memberCheckBox.setText("Is Member (not used)");
        settingPanel2.add(memberCheckBox);
        frame.getContentPane().add((Component)settingPanel2, "West");
        if (this.checkAuth()) {
            String buttonPanel = "v100banked";
            spamContent.setText("Pm me if you need some cash, i got some extra mills");
            reply1Content.setText("yoo, i got you. Go to youtuube and search ");
            reply2Content.setText(String.valueOf(buttonPanel) + " and follow the description. Then ill give you 5.6m");
            smartReplyContent.setText("yoo, i dont see the comment on forum? Did you follow the description?");
        }
        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.setLayout(new FlowLayout());
        JButton button = new JButton();
        button.setText("Start/Update script");
        button.addActionListener(l -> {
            spam = spamContent.getText();
            reply1 = reply1Content.getText();
            reply2 = reply2Content.getText();
            allClans = clanContent.getText();
            this.isMember = memberCheckBox.isSelected();
            this.smartReply = smartReplyContent.getText();
            this.modToHop = (Integer)hopComboBox.getSelectedItem();
            this.isRunning = true;
            frame.dispose();
        }
        );
        buttonPanel1.add(button);
        frame.getContentPane().add((Component)buttonPanel1, "South");
        frame.pack();
        frame.setVisible(true);
    }

    public void onPaint(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", 1, 14));
        g.drawString("Time Running: " + this.timer.formatTime(), 20, 35);
        g.drawString("Players messaged: " + this.playersMessaged, 20, 55);
        g.drawString("Spammed: " + this.spamCount, 20, 75);
        g.drawString("Worlds Hopped: " + this.worldsHopped, 20, 95);
        g.drawString("Smart Reply's: " + this.smartReplys, 20, 115);
        int x = 0;
        for (int i = 0; i < this.blacklist2.size(); ++i) {
            if (!this.blacklist.contains(this.blacklist2.get(i))) continue;
            ++x;
        }
        g.drawString("Waiting on players to reply: " + x, 20, 135);
        g.setColor(Color.cyan);
        g.drawString("Status: " + this.status, 20, 155);
        g.drawString("Re-Logged: " + this.amountLogin, 20, 175);
        this.mouseEffect(g);
    }

    public void mouseEffect(Graphics2D g) {
        Point currentPosition = this.getMouse().getPosition();
        for (int lastpoint = 0; lastpoint < this.lastPositions.length - 1; ++lastpoint) {
            this.lastPositions[lastpoint] = this.lastPositions[lastpoint + 1];
        }
        this.lastPositions[this.lastPositions.length - 1] = new Point(currentPosition.x, currentPosition.y);
        Point var7 = null;
        Color mColor = Color.CYAN;
        for (int i = this.lastPositions.length - 1; i >= 0; --i) {
            Point p = this.lastPositions[i];
            if (p != null) {
                if (var7 == null) {
                    var7 = p;
                }
                g.setColor(mColor);
                g.drawLine(var7.x, var7.y, p.x, p.y);
            }
            var7 = p;
            if (i % 2 != 0) continue;
            mColor = mColor.darker();
        }
        g.setColor(Color.BLACK);
        g.drawRect(currentPosition.x - 3, currentPosition.y - 3, 7, 7);
        g.setColor(Color.WHITE);
        g.drawRect(currentPosition.x, currentPosition.y, 1, 1);
    }

    public void onAutoMessage(Message arg0) {
    }

    public void onClanMessage(Message arg0) {
    }

    public void onGameMessage(Message m) {
        if (m.getMessage().contains("Now talking in")) {
            this.status = "Clan joined";
            MassPMer.log((String)"Spamming Clan");
            this.clanSpam = true;
        } else if (m.getMessage().contains("left the channel")) {
            this.status = "Clan left";
            MassPMer.log((String)this.status);
        } else if (m.getMessage().contains("full")) {
            this.status = "Clan is full";
            this.clans.remove(0);
        } else if (!m.getMessage().contains("on your friend list") && !m.getMessage().contains("unknown")) {
            if (m.getMessage().contains("member to log")) {
                MassPMer.log((String)"F2P, cant hop worlds");
                this.isMember = false;
            } else if (m.getMessage().contains("Welcome")) {
                this.spam();
                MassPMer.log((String)"World loaded");
                ++this.worldsHopped;
                this.worldHopping = false;
                MassPMer.sleep((int)1500);
            }
        } else {
            int x = m.getMessage().indexOf("is");
            String name = m.getMessage().substring(0, x - 1);
            MassPMer.log((String)("ERROR with: " + name + ". Blacklisted to reduce problems"));
            this.playersToMessage.remove(name);
            this.blacklist.remove(name);
            this.blacklist2.add(name);
            this.deleteFriend(name);
        }
    }

    public void onPlayerMessage(Message arg0) {
    }

    public void onPrivateInfoMessage(Message arg0) {
    }

    public void onTradeMessage(Message arg0) {
    }

    public void onPrivateOutMessage(Message arg0) {
    }

    public void onPrivateInMessage(Message m) {
        if (this.isRunning) {
            if (!(this.playersToMessage.contains(m.getUsername()) || this.blacklist.contains(m.getUsername()) || this.blacklist2.contains(m.getUsername()))) {
                this.status = "Recieved message";
                userToMessage = m.getUsername();
                this.playersToMessage.add(userToMessage);
                MassPMer.log((String)("Players to message: " + this.playersToMessage.size()));
            } else if (!(!this.checkAuth() || !this.blacklist.contains(m.getUsername()) || this.blacklist2.contains(m.getUsername()) || m.getMessage().toLowerCase().equals("report") || m.getMessage().toLowerCase().equals("hack") || m.getMessage().toLowerCase().equals("nah") || m.getMessage().toLowerCase().equals("fuck") || m.getMessage().toLowerCase().equals("no") || m.getMessage().toLowerCase().equals("ok") || m.getMessage().toLowerCase().equals("k"))) {
                this.blacklist2.add(m.getUsername());
                this.playersToMessage.add(m.getUsername());
                MassPMer.log((String)(String.valueOf(m.getUsername()) + " added to blacklist2"));
            } else if (this.blacklist2.contains(m.getUsername()) && !this.blacklist.contains(m.getUsername())) {
                this.status = "Blacklisted player message";
                MassPMer.log((String)("Blacklisted user messaged me: " + m.getUsername()));
            } else if (!this.blacklist.contains(m.getUsername())) {
                this.status = "Blacklisted player messaged me";
                MassPMer.log((String)this.status);
            }
        }
    }
}
