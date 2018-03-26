package Slay;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JEditorPane;

import slayerMonsters.Loot;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import Slay.MonsterVars;

@SuppressWarnings("serial")
public class MonsterGUI extends JFrame {
	
	private JPanel inputPane;

	public MonsterGUI(final MonsterVars monsterVars) {
		setTitle("Kill Anything");
		setName("Monster GUI");
		setAlwaysOnTop(true);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(250, 250, 300, 337);
		inputPane = new JPanel();
		inputPane.setBackground(Color.CYAN);
		inputPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(inputPane);
		
		final JTextField input = new JTextField("Monster Name");
		input.setPreferredSize(new Dimension(50, 16));
		input.setHorizontalAlignment(SwingConstants.CENTER);
		inputPane.add(input);
		
		final JTextField loot = new JTextField("Loot");
		loot.setPreferredSize(new Dimension(50, 16));
		loot.setHorizontalAlignment(SwingConstants.CENTER);
		inputPane.add(loot);
		
		JEditorPane display = new JEditorPane();
		display.setEditable(false);
		display.setText("Monster: " + "\n"
				);
		inputPane.add(display);
		
		
		
		JCheckBox usePrayer = new JCheckBox("usePrayer");
		usePrayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				monsterVars.usePrayer = true;
			}
			
		});
		inputPane.add(usePrayer);
		

		JEditorPane display2 = new JEditorPane();
		display2.setEditable(false);
		display2.setText("Loot: " + " \n -");
		inputPane.add(display2);
		
		JButton nameFoodSetter = new JButton();
		nameFoodSetter.setText("Set Monster Name");
		nameFoodSetter.setPreferredSize(new Dimension(100, 30));
		nameFoodSetter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < monsterVars.slayerTasks.length; i++) {
					if(input.getText().equals(monsterVars.slayerTasks[i].getName())) {
						monsterVars.currentTask =   monsterVars.slayerTasks[i];
						break;
					}
				}
				display.setText("Monster: " + input.getText());
				monsterVars.loots = monsterVars.currentTask.getLoots();
				String loots = "";
				for(int i = 0; i < monsterVars.loots.size(); i++) {
					loots += monsterVars.loots.get(i).getName() + " ";
				}
				display2.setText(display2.getText() + loots + " \n -");
			}
		});
		inputPane.add(nameFoodSetter);
		
		JButton lootSetter = new JButton();
		lootSetter.setText("Set Loot");
		lootSetter.setPreferredSize(new Dimension(100, 30));
		lootSetter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!loot.getText().equals("Loot") && !loot.getText().equals("")) {
					monsterVars.loots.add(new Loot(loot.getText()));
				}
					String loots = "";
					for(int i = 0; i < monsterVars.loots.size(); i++) {
						loots += monsterVars.loots.get(i) + " ";
					}
					display2.setText(display2.getText() + loots + " \n -");
			}
		});
		inputPane.add(lootSetter);
		
		inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.PAGE_AXIS));
	}
}
