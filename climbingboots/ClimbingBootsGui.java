package climbingboots;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.dreambot.api.methods.friend.Friend;

@SuppressWarnings("serial")
public class ClimbingBootsGui extends JFrame{

	private List<String> accountNames;
	private float onlineTime = 5;
	private float offlineTime = 55;

	public ClimbingBootsGui(String label, Friend[] friends, boolean multi){
		super("Script Settings");
		accountNames = new ArrayList<>();
		JPanel panel = new JPanel();
		setSize(400, 600);
		setLocationRelativeTo(null);
		panel.setLayout(new BorderLayout());
		String[] stringFriends = new String[friends.length];
		for(int j = 0; j < friends.length; j++)
			stringFriends[j] = friends[j].getName();

		JList<String> friendsList = new JList<String>(stringFriends);
		JTextPane traderList = new JTextPane();
		traderList.setFont(traderList.getFont().deriveFont(Font.BOLD));
		traderList.setEditable(false);
		friendsList.setSelectionMode(multi ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);
		friendsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e){
				List<String> names = friendsList.getSelectedValuesList();
				if(multi){
					String text = traderList.getText();
					for(String s : names.toArray(new String[names.size()]))
						if(!text.contains(s))
							text = text.concat(s + "\n");
					traderList.setText(text);
				}else traderList.setText(friendsList.getSelectedValue() == null ? "" : friendsList.getSelectedValue());
			}
		});
		JScrollPane flsp = new JScrollPane(friendsList);
		flsp.setPreferredSize(new Dimension(190, 150));
		JScrollPane tlsp = new JScrollPane(traderList);
		tlsp.setPreferredSize(new Dimension(190, 150));
		panel.add(flsp, BorderLayout.WEST);
		panel.add(tlsp, BorderLayout.EAST);
		JPanel bottom = new JPanel(new FlowLayout());
		JPanel top = new JPanel(new BorderLayout());
		top.add(new JLabel("Friends"), BorderLayout.WEST);
		top.add(new JLabel("Selected"), BorderLayout.EAST);
		JTextField txtField = new JTextField(13);
		txtField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				if(e.getSource().equals(txtField)){
					String input = txtField.getText();
					if(input.length() > 0){
						if(multi){
							if(!traderList.getText().contains(input))
								traderList.setText(traderList.getText().concat(input + "\n"));
						}else traderList.setText(input);
						txtField.setText("");
					}
				}
			}
		});
		bottom.add(new JLabel(label));
		bottom.add(txtField);
		bottom.add(new JLabel("Min. Online:"));
		JTextField onTxt = new JTextField("5");
		bottom.add(onTxt);
		bottom.add(new JLabel("Min. Offline:"));
		JTextField offTxt = new JTextField("55");
		bottom.add(offTxt);
		
		JButton button = new JButton("Done");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				if(e.getSource().equals(button)){
					if(traderList.getText() != null)
						accountNames = Arrays.asList(traderList.getText().split("\n"));
					try {
						onlineTime = Float.parseFloat(onTxt.getText());
						offlineTime = Float.parseFloat(offTxt.getText());						
					}catch(Exception ex) {
						ex.printStackTrace();
						onlineTime = 5;
						offlineTime = 55;
					}
					if((multi && accountNames.size() > 0) || !multi)
						dispose();
				}
			}
		});
		bottom.add(button);
		panel.add(top, BorderLayout.PAGE_START);
		panel.add(bottom, BorderLayout.PAGE_END);
		setContentPane(panel);
		setVisible(true);
		pack();
	}

	public List<String> getTradeAccounts(){
		return accountNames;
	}
	
	public float getOnlineTime() {
		return onlineTime;
	}
	
	public float getOfflineTime() {
		return offlineTime;
	}
}
