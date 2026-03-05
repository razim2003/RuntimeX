import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyBMICalc {
    private JLabel BodyCalculator;
    private JTextField inputWeight;
    private JTextField inputHeight;
    private JButton calculateButton;
    private JComboBox comboBox1;
    private JComboBox comboBox2;

    public MyBMICalc() {
        inputHeight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String heightString = inputHeight.getText();
                double height = Double.parseDouble(heightString);
                System.out.println("height: " + height);
            }
        });
        inputWeight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String weightString = inputWeight.getText();
                double weight = Double.parseDouble(weightString);
                System.out.println("Weight: " + weight);
            }
        });
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dropResults = comboBox1.getSelectedItem();
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
