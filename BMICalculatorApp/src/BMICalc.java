import javax.swing.*;

public class BMICalc {


    public static void main(String[] args) {

        JFrame frame = new JFrame("BMI Calculator");
        //MyBMICalc myBMICalc = new MyBMICalc();
        frame.setContentPane(new MyBMICalc().mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}