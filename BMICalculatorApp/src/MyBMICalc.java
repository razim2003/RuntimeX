import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyBMICalc {
    public JPanel mainPanel;
    private JTextField inputWeight;
    private JTextField inputHeight;
    private JButton calculateButton;
    private JComboBox<String> comboBox1; // weight units
    private JComboBox<String> comboBox2; // height units
    private JLabel valueLabel;
    private JLabel statusLabel;
    private JLabel bodyCalculator;
    private JLabel heightLabel;
    private JLabel weightLabel;
    private JLabel outputLabel;
    private JLabel outputStatusLabel;

    public MyBMICalc() {
        // Fill combo boxes
        comboBox1.addItem("kg");
        comboBox1.addItem("lb");
        comboBox2.addItem("m");
        comboBox2.addItem("cm");

        calculateButton.addActionListener(e -> calculateBMI());
    }

    private void calculateBMI() {
        double weight, height;

        try {
            weight = Double.parseDouble(inputWeight.getText());
            height = Double.parseDouble(inputHeight.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Enter valid numbers!");
            return;
        }

        // Convert units if needed
        if ("lb".equals(comboBox1.getSelectedItem())) {
            weight *= 0.453592; // lb to kg
        }

        if ("cm".equals(comboBox2.getSelectedItem())) {
            height /= 100; // cm to meters
        }

        if (height <= 0) {
            JOptionPane.showMessageDialog(null, "Height must be positive!");
            return;
        }

        double bmi = weight / (height * height);
        outputLabel.setText(String.format("%.2f", bmi));
        outputStatusLabel.setText(getStatus(bmi));
    }

    private String getStatus(double bmi) {
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 25) return "Normal";
        else if (bmi < 30) return "Overweight";
        else return "Obese";
    }
}