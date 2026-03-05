import javax.swing.*;

public class BMICalc {
    private JPanel background;

    public static void main(String[] args) {
        JFrame frame = new JFrame("BMICalc");
        frame.setContentPane(new BMICalc().background);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }
}
