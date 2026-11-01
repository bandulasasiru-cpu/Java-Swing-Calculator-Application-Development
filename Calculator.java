import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new HomePageFrame().setVisible(true);
        });
    }
}

class HomePageFrame extends JFrame {
    public HomePageFrame() {
        setTitle("Calculator - Welcome");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(15, 23, 42); 
                Color color2 = new Color(51, 65, 85); 
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Welcome to Calculator", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("Perform your calculations quickly and easily.", JLabel.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        descLabel.setForeground(new Color(203, 213, 225));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(139, 92, 246)); 
                g2.fillRoundRect(35, 10, 80, 100, 16, 16);
                
                // Screen
                g2.setColor(Color.WHITE);
                g2.fillRect(45, 20, 60, 25);
                g2.setColor(new Color(15, 23, 42));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                g2.drawString("1234", 55, 38);

                g2.setColor(new Color(56, 189, 248));
                g2.fillRoundRect(45, 55, 15, 12, 4, 4);
                g2.fillRoundRect(67, 55, 15, 12, 4, 4);
                g2.fillRoundRect(89, 55, 15, 12, 4, 4);

                g2.fillRoundRect(45, 73, 15, 12, 4, 4);
                g2.fillRoundRect(67, 73, 15, 12, 4, 4);
                g2.fillRoundRect(89, 73, 15, 12, 4, 4);
            }
        };
        iconPanel.setPreferredSize(new Dimension(150, 120));
        iconPanel.setMaximumSize(new Dimension(150, 120));
        iconPanel.setOpaque(false);
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton useCalcButton = new JButton("Use Calculator") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(new Color(109, 40, 217));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(147, 51, 234));
                } else {
                    g2.setColor(new Color(124, 58, 237)); 
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        useCalcButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        useCalcButton.setForeground(Color.WHITE);
        useCalcButton.setFocusPainted(false);
        useCalcButton.setBorderPainted(false);
        useCalcButton.setContentAreaFilled(false);
        useCalcButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        useCalcButton.setPreferredSize(new Dimension(200, 45));
        useCalcButton.setMaximumSize(new Dimension(200, 45));
        useCalcButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        useCalcButton.addActionListener(e -> {
            dispose();
            new CalculatorFrame().setVisible(true);
        });

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(descLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(iconPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(useCalcButton);

        add(mainPanel);
    }
}

class CalculatorFrame extends JFrame {
    private JTextField displayField;
    private DefaultListModel<String> historyModel;
    
    private double firstOperand = 0;
    private String operator = "";
    private boolean startNew = true;
    private boolean hasError = false;

    public CalculatorFrame() {
        setTitle("Modern Calculator");
        setSize(780, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(15, 23, 42));

        JPanel calcSection = new JPanel(new BorderLayout(10, 10));
        calcSection.setOpaque(false);
        
        displayField = new JTextField("0");
        displayField.setFont(new Font("Segoe UI", Font.BOLD, 30));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        displayField.setBackground(new Color(30, 41, 59));
        displayField.setForeground(Color.WHITE);
        displayField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51, 65, 85), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        displayField.setPreferredSize(new Dimension(380, 75));
        calcSection.add(displayField, BorderLayout.NORTH);

        JPanel keypadPanel = new JPanel(new GridLayout(5, 4, 8, 8));
        keypadPanel.setOpaque(false);

        Color numColor = new Color(51, 65, 85);     
        Color numHover = new Color(71, 85, 105);
        Color opColor = new Color(124, 58, 237);      
        Color opHover = new Color(139, 92, 246);
        Color clearColor = new Color(225, 29, 72);    
        Color clearHover = new Color(244, 63, 94);
        Color equalsColor = new Color(16, 185, 129);  
        Color equalsHover = new Color(52, 211, 153);

        String[] buttons = {
            "C", "DEL", "/", "*",
            "7", "8", "9", "-",
            "4", "5", "6", "+",
            "1", "2", "3", "=",
            "0", ".", "", ""
        };

        ButtonClickListener clickListener = new ButtonClickListener();

        for (String text : buttons) {
            if (text.isEmpty()) {
                JPanel empty = new JPanel();
                empty.setOpaque(false);
                keypadPanel.add(empty);
                continue;
            }

            Color bg, hover;
            if (text.equals("C") || text.equals("DEL")) {
                bg = clearColor;
                hover = clearHover;
            } else if (text.equals("=")) {
                bg = equalsColor;
                hover = equalsHover;
            } else if (text.equals("+") || text.equals("-") || text.equals("*") || text.equals("/")) {
                bg = opColor;
                hover = opHover;
            } else {
                bg = numColor;
                hover = numHover;
            }

            JButton btn = createRoundedButton(text, bg, hover);
            btn.addActionListener(clickListener);
            keypadPanel.add(btn);
        }

        calcSection.add(keypadPanel, BorderLayout.CENTER);

        JPanel historySection = new JPanel(new BorderLayout(10, 10));
        historySection.setPreferredSize(new Dimension(300, 0));
        historySection.setOpaque(false);
        historySection.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51, 65, 85), 2),
            new EmptyBorder(12, 12, 12, 12)
        ));
        historySection.setBackground(new Color(30, 41, 59));

        JLabel historyTitle = new JLabel("Calculation History", JLabel.CENTER);
        historyTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        historyTitle.setForeground(Color.WHITE);
        historySection.add(historyTitle, BorderLayout.NORTH);

        historyModel = new DefaultListModel<>();
        JList<String> historyList = new JList<>(historyModel);
        historyList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyList.setBackground(new Color(15, 23, 42));
        historyList.setForeground(new Color(226, 232, 240));
        historyList.setSelectionBackground(new Color(51, 65, 85));
        historyList.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        historySection.add(scrollPane, BorderLayout.CENTER);

        JButton clearHistoryBtn = createRoundedButton("Clear History", new Color(71, 85, 105), new Color(100, 116, 139));
        clearHistoryBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clearHistoryBtn.addActionListener(e -> historyModel.clear());
        historySection.add(clearHistoryBtn, BorderLayout.SOUTH);

        // Add sections to main layout
        mainPanel.add(calcSection, BorderLayout.CENTER);
        mainPanel.add(historySection, BorderLayout.EAST);

        add(mainPanel);
    }

    private JButton createRoundedButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bg.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hover);
                } else {
                    g2.setColor(bg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = ((JButton) e.getSource()).getText();
            if (cmd.isEmpty()) return;

            if (hasError) {
                displayField.setText("0");
                hasError = false;
                firstOperand = 0;
                operator = "";
                startNew = true;
            }

            if (cmd.matches("[0-9]")) {
                if (startNew) {
                    displayField.setText(cmd);
                    startNew = false;
                } else {
                    if (displayField.getText().equals("0")) {
                        displayField.setText(cmd);
                    } else {
                        displayField.setText(displayField.getText() + cmd);
                    }
                }
            } else if (cmd.equals(".")) {
                if (startNew) {
                    displayField.setText("0.");
                    startNew = false;
                } else if (!displayField.getText().contains(".")) {
                    displayField.setText(displayField.getText() + ".");
                }
            } else if (cmd.equals("C")) {
                displayField.setText("0");
                firstOperand = 0;
                operator = "";
                startNew = true;
            } else if (cmd.equals("DEL")) {
                String current = displayField.getText();
                if (current.length() > 1 && !startNew) {
                    displayField.setText(current.substring(0, current.length() - 1));
                } else {
                    displayField.setText("0");
                    startNew = true;
                }
            } else if (cmd.equals("+") || cmd.equals("-") || cmd.equals("*") || cmd.equals("/")) {
                try {
                    firstOperand = Double.parseDouble(displayField.getText());
                } catch (NumberFormatException ex) {
                    firstOperand = 0;
                }
                operator = cmd;
                startNew = true;
            } else if (cmd.equals("=")) {
                if (operator.isEmpty()) return;
                double secondOperand;
                try {
                    secondOperand = Double.parseDouble(displayField.getText());
                } catch (NumberFormatException ex) {
                    secondOperand = 0;
                }

                double result = 0;
                boolean errorOccurred = false;

                switch (operator) {
                    case "+":
                        result = firstOperand + secondOperand;
                        break;
                    case "-":
                        result = firstOperand - secondOperand;
                        break;
                    case "*":
                        result = firstOperand * secondOperand;
                        break;
                    case "/":
                        if (secondOperand == 0) {
                            errorOccurred = true;
                        } else {
                            result = firstOperand / secondOperand;
                        }
                        break;
                }

                if (errorOccurred) {
                    displayField.setText("Error: Div by zero");
                    hasError = true;
                    operator = "";
                    startNew = true;
                } else {
                    String resultStr = (result == (long) result) ? String.format("%d", (long) result) : String.valueOf(result);
                    String op1Str = (firstOperand == (long) firstOperand) ? String.valueOf((long) firstOperand) : String.valueOf(firstOperand);
                    String op2Str = (secondOperand == (long) secondOperand) ? String.valueOf((long) secondOperand) : String.valueOf(secondOperand);

                    String historyEntry = op1Str + " " + operator + " " + op2Str + " = " + resultStr;
                    historyModel.addElement(historyEntry);

                    displayField.setText(resultStr);
                    firstOperand = result;
                    operator = "";
                    startNew = true;
                }
            }
        }
    }
}
