// Á¤ÀÇÅÃ 201258141
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Test3 extends JFrame implements ActionListener{
	private JButton button3,button1, button2;
	private JPanel panel,panel2,panel3;
	private JTextField t1,t2,t3;
	private JLabel l1;
	
	public Test3(){
		this.setBounds(100, 100, 510, 130);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("3");

		panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1, 0, 0));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel2 = new JPanel();
		panel3 = new JPanel();
		t1 = new JTextField(10);
		t2 = new JTextField(10);
		t3 = new JTextField(10);
		l1 = new JLabel("+");
		button3 =  new JButton("=");
		button1 = new JButton("»¡°­");
		button2 = new JButton("³ë¶û");
		button1.addActionListener(this); 
		button2.addActionListener(this);
		button3.addActionListener(this);

		panel.add(panel2);
		panel.add(panel3);

		panel2.add(t1);
		panel2.add(l1);
		panel2.add(t2);
		panel2.add(button3);
		panel2.add(t3);

		panel3.add(button1);
		panel3.add(button2);

		this.add(panel);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == button1){
			panel2.setBackground(Color.RED);
		}
		if(e.getSource() == button2){
			panel2.setBackground(Color.YELLOW);
		}
		if(e.getSource() == button3){
			float n1 = Float.parseFloat(t1.getText());
			float n2 = Float.parseFloat(t2.getText());

			float result = n1 + n2;
			t3.setText(" "+ result);
		}
	}

	public static void main(String[] args) {
		Test3 t3 = new Test3();
	}	
}
