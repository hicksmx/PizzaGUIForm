import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class PizzaGUIFrame extends JFrame implements ActionListener {
    private JRadioButton thinCrust, regularCrust, deepDishCrust;
    private JComboBox<String> sizeComboBox;
    private JCheckBox[] toppings;
    private JTextArea orderTextArea;
    private JButton orderButton, clearButton, quitButton;

    private final String[] SIZES = {"Small", "Medium", "Large", "Super"};
    private final double[] SIZE_PRICES = {8.00, 12.00, 16.00, 20.00};
    private final String[] TOPPING_NAMES = {"Cheese", "Pepperoni", "Mushrooms", "Olives", "Bacon", "Pineapple"};
    private final double TOPPING_PRICE = 1.00;
    private final double TAX_RATE = 0.07;

    public PizzaGUIFrame() {
        setTitle("Pizza Order System");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crust Panel
        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(BorderFactory.createTitledBorder("Crust Type"));
        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDishCrust = new JRadioButton("Deep-dish");
        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDishCrust);
        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepDishCrust);

        // Size Panel
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createTitledBorder("Size"));
        sizeComboBox = new JComboBox<>(SIZES);
        sizePanel.add(sizeComboBox);

        // Toppings Panel
        JPanel toppingsPanel = new JPanel(new GridLayout(3, 2));
        toppingsPanel.setBorder(BorderFactory.createTitledBorder("Toppings"));
        toppings = new JCheckBox[TOPPING_NAMES.length];
        for (int i = 0; i < TOPPING_NAMES.length; i++) {
            toppings[i] = new JCheckBox(TOPPING_NAMES[i]);
            toppingsPanel.add(toppings[i]);
        }

        // Order Display Panel
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        orderTextArea = new JTextArea(10, 30);
        orderTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderTextArea);
        orderPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");
        orderButton.addActionListener(this);
        clearButton.addActionListener(this);
        quitButton.addActionListener(this);
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        // Main layout
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);
        topPanel.add(toppingsPanel);

        add(topPanel, BorderLayout.NORTH);
        add(orderPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == orderButton) {
            placeOrder();
        } else if (e.getSource() == clearButton) {
            clearForm();
        } else if (e.getSource() == quitButton) {
            quit();
        }
    }

    private void placeOrder() {
        StringBuilder order = new StringBuilder();
        double total = 0;

        // Get crust type
        String crustType = thinCrust.isSelected() ? "Thin" :
                regularCrust.isSelected() ? "Regular" :
                        deepDishCrust.isSelected() ? "Deep-dish" : "";

        if (crustType.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a crust type.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get size and price
        int sizeIndex = sizeComboBox.getSelectedIndex();
        String size = SIZES[sizeIndex];
        double sizePrice = SIZE_PRICES[sizeIndex];
        total += sizePrice;

        order.append("=========================================\n");
        order.append(String.format("%-30s $%.2f\n", crustType + " Crust, " + size, sizePrice));

        // Get toppings
        boolean hasToppings = false;
        for (int i = 0; i < toppings.length; i++) {
            if (toppings[i].isSelected()) {
                hasToppings = true;
                total += TOPPING_PRICE;
                order.append(String.format("%-30s $%.2f\n", TOPPING_NAMES[i], TOPPING_PRICE));
            }
        }

        if (!hasToppings) {
            JOptionPane.showMessageDialog(this, "Please select at least one topping.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double subtotal = total;
        double tax = subtotal * TAX_RATE;
        total += tax;

        DecimalFormat df = new DecimalFormat("#.##");
        order.append("\nSub-total:                     $").append(df.format(subtotal)).append("\n");
        order.append("Tax:                           $").append(df.format(tax)).append("\n");
        order.append("---------------------------------------------\n");
        order.append("Total:                         $").append(df.format(total)).append("\n");
        order.append("=========================================\n");

        orderTextArea.setText(order.toString());
    }

    private void clearForm() {
        thinCrust.setSelected(false);
        regularCrust.setSelected(false);
        deepDishCrust.setSelected(false);
        sizeComboBox.setSelectedIndex(0);
        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        orderTextArea.setText("");
    }

    private void quit() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to quit?", "Confirm Quit",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}