
/*
Simply application for computing value added tax.
I used regular expressions to make code more readable.
This project is upgraded version of another project (Kalkulator VAT),
And this time I wanted to utilize my new skills acquired during solving
tasks on codewars.com. Enjoy! :)

Przemysław Poskrobko
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFrame extends JFrame implements ActionListener, FocusListener{
    JButton computeButton, clearButton;
    JLabel netValueLabel, vatValueLabel, grossValueLabel;
    JTextField netValueTextField, vatValueTextField, grossValueTextField;

    public MainFrame(){
        super("Calculator 2.0");
        this.setLayout(null);
        this.getContentPane().setBackground(Color.white);
//          adding components
        netValueLabel = new JLabel("Net Value",null,SwingConstants.RIGHT);
        netValueLabel.setBounds(76,62,75,17);
        this.add(netValueLabel);
        vatValueLabel = new JLabel("VAT Tax",null,SwingConstants.RIGHT);
        vatValueLabel.setBounds(76, 95, 75, 17);
        this.add(vatValueLabel);
        grossValueLabel = new JLabel("Gross Value",null,SwingConstants.RIGHT);
        grossValueLabel.setBounds(76, 127, 75, 17);
        this.add(grossValueLabel);
        netValueTextField = new JTextField();
        netValueTextField.setBounds(160, 58, 150, 25);
        this.add(netValueTextField);
        vatValueTextField = new JTextField();
        vatValueTextField.setBounds(160, 91, 150, 25);
        this.add(vatValueTextField);
        grossValueTextField = new JTextField();
        grossValueTextField.setBounds(160, 123, 150, 25);
        this.add(grossValueTextField);
        computeButton = new JButton("Compute!");
        computeButton.setBounds(116, 210, 90, 25);
        this.add(computeButton);
        clearButton = new JButton("Clear");
        clearButton.setBounds(215, 210, 70, 25);
        this.add(clearButton);
//        add action listeners
        computeButton.addActionListener(this);
        clearButton.addActionListener(this);
//        add lost listener
        netValueTextField.addFocusListener(this);
        vatValueTextField.addFocusListener(this);
        grossValueTextField.addFocusListener(this);

        this.setLayout(new BorderLayout());
        this.setSize(400, 310);
        this.setLocation(300, 310);
        this.setVisible(true);
    }

    private static void checkIfNumbersWereTyped(JTextField textField){
        Pattern appropriateFormatwithDecimal = Pattern.compile("^\\d+[.]?\\d*$");
        Matcher matcher = appropriateFormatwithDecimal.matcher(textField.getText());
        if (!matcher.matches() && !textField.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Napisz, proszę poprawny format liczby, pamiętaj proszę by zamiast przecinków używać kropek");
            textField.requestFocus();
            textField.setSelectionStart(0);
            textField.setSelectionEnd(textField.getText().length());
        }
    }
    public boolean checkIfOneTextFieldIsEmpty(){
        int textFieldsInUse = 0;
        if (!netValueTextField.getText().isEmpty()) textFieldsInUse ++ ;
        if (!vatValueTextField.getText().isEmpty()) textFieldsInUse ++;
        if (!grossValueTextField.getText().isEmpty()) textFieldsInUse ++;
        return (textFieldsInUse == 2) ? true : false;
    }

    public static void organizeAmount (JTextField textField){
//        pattern który będzie obsługiwał 3 cyfry i więcj po przecinku
        Pattern pattern = Pattern.compile("^\\d+[.]?\\d{3,}");
        Matcher matcher = pattern.matcher(textField.getText());
//        pattern który będzie ossługiwał 1 cyfrę po przecinku
        Pattern pattern1 = Pattern.compile("^\\d+[.]\\d$");
        Matcher matcher1 = pattern1.matcher(textField.getText());
//        pattern który będzie obsługiwał liczbę zakończoną przecinkiem lub kropką
        Pattern pattern2 = Pattern.compile("^\\d+[.]$");
        Matcher matcher2 = pattern2.matcher(textField.getText());
//        pattern który obsłuży liczbę int
        Pattern pattern3 = Pattern.compile("^\\d+$");
        Matcher matcher3 = pattern3.matcher(textField.getText());
        if (matcher.matches()){
            double number;
            number = Double.parseDouble(textField.getText());
            double numberRounded;
            numberRounded = (int) Math.round((number * 100));
            textField.setText(String.valueOf(numberRounded/100));
        } else if (matcher1.matches()) {textField.setText(textField.getText() + "0");}
          else if (matcher2.matches()) {textField.setText(textField.getText() + "00");}
          else if (matcher3.matches()) {textField.setText(textField.getText() + ".00");
        } else {};
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == computeButton){
            if(checkIfOneTextFieldIsEmpty()){
                double netValue, vatValue, grossValue;
                if(netValueTextField.getText().isEmpty()){
                    vatValue = Double.parseDouble(vatValueTextField.getText());
                    grossValue = Double.parseDouble(grossValueTextField.getText());
                    netValue = 100 * grossValue / (100 + vatValue);
                    netValueTextField.setText(String.valueOf(netValue));
                    organizeAmount(netValueTextField);

                }
                else if (vatValueTextField.getText().isEmpty()) {
                    netValue = Double.parseDouble(netValueTextField.getText());
                    grossValue = Double.parseDouble(grossValueTextField.getText());
                    vatValue = (grossValue / netValue - 1) * 100;
                    vatValueTextField.setText(String.valueOf(vatValue));
                    organizeAmount(vatValueTextField);

                }
                else{
//                    gross value textField is empty

                    netValue = Double.parseDouble(netValueTextField.getText());
                    vatValue = Double.parseDouble(vatValueTextField.getText());
                    grossValue = netValue + (netValue * vatValue) / 100;
                    grossValueTextField.setText(String.valueOf(grossValue));
                    organizeAmount(grossValueTextField);
                }

            }
            else{
                JOptionPane.showMessageDialog(null, "Jedno pole musi pozostać wolne");
            }
        }
        else{
            netValueTextField.setText("");
            vatValueTextField.setText("");
            grossValueTextField.setText("");
        }
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        checkIfNumbersWereTyped(((JTextField)e.getSource()));
        organizeAmount(((JTextField) e.getSource()));
    }
}
