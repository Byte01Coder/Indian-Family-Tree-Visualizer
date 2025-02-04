import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

class FamilyTreeGUI {
    public FamilyTree familyTree;

    public FamilyTreeGUI() {
        familyTree = new FamilyTree();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Family Tree Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel addPersonPanel = createAddPersonPanel();
        JPanel findRelationPanel = createFindRelationPanel();

        tabbedPane.addTab("Add Person", addPersonPanel);
        tabbedPane.setBackgroundAt(0, new Color(102, 205, 170)); // Medium aquamarine
        tabbedPane.addTab("Find Relation", findRelationPanel);
        tabbedPane.setBackgroundAt(1, new Color(255, 160, 122)); // Light salmon

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JPanel createAddPersonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2, 10, 10));
        panel.setBackground(new Color(224, 255, 255)); // Light cyan

        JTextField nameField = new JTextField();
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"male", "female"});
        JTextField parentField = new JTextField();
        JTextField spouseField = new JTextField();
        JTextField siblingField = new JTextField();
        JTextField grandparentField = new JTextField();
        JTextField greatGrandparentField = new JTextField();
        JTextField uncleAuntField = new JTextField();
        JTextField nieceNephewField = new JTextField();

        JButton addButton = new JButton("Add Person");
        addButton.setBackground(new Color(50, 205, 50)); // Lime green
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setFocusPainted(false);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderBox);
        panel.add(new JLabel("Parents (comma-separated):"));
        panel.add(parentField);
        panel.add(new JLabel("Spouses (comma-separated):"));
        panel.add(spouseField);
        panel.add(new JLabel("Siblings (comma-separated):"));
        panel.add(siblingField);
        panel.add(new JLabel("Grandparents (comma-separated):"));
        panel.add(grandparentField);
        panel.add(new JLabel("Great-Grandparents (comma-separated):"));
        panel.add(greatGrandparentField);
        panel.add(new JLabel("Uncle/Aunts (comma-separated):"));
        panel.add(uncleAuntField);
        panel.add(new JLabel("Niece/Nephews (comma-separated):"));
        panel.add(nieceNephewField);

        panel.add(new JLabel()); 
        panel.add(addButton);

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String gender = (String) genderBox.getSelectedItem();
            List<String> parents = parseInput(parentField.getText());
            List<String> spouses = parseInput(spouseField.getText());
            List<String> siblings = parseInput(siblingField.getText());
            List<String> grandparents = parseInput(grandparentField.getText());
            List<String> greatGrandparents = parseInput(greatGrandparentField.getText());
            List<String> uncleAunts = parseInput(uncleAuntField.getText());
            List<String> nieceNephews = parseInput(nieceNephewField.getText());

            familyTree.addPerson(name, gender, parents, spouses, siblings, grandparents, greatGrandparents, uncleAunts, nieceNephews);
            JOptionPane.showMessageDialog(panel, "Person added successfully!");

            nameField.setText("");
            parentField.setText("");
            spouseField.setText("");
            siblingField.setText("");
            grandparentField.setText("");
            greatGrandparentField.setText("");
            uncleAuntField.setText("");
            nieceNephewField.setText("");
        });

        return panel;
    }

    private JPanel createFindRelationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBackground(new Color(255, 228, 225)); // Misty rose

        JTextField person1Field = new JTextField();
        JTextField person2Field = new JTextField();
        JLabel resultLabel = new JLabel(" ", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setForeground(Color.BLUE);

        JButton findButton = new JButton("Find Relation");
        findButton.setBackground(new Color(70, 130, 180)); // Steel blue
        findButton.setForeground(Color.BLACK);
        findButton.setFont(new Font("Arial", Font.BOLD, 14));
        findButton.setFocusPainted(false);

        panel.add(new JLabel("Person 1:"));
        panel.add(person1Field);
        panel.add(new JLabel("Person 2:"));
        panel.add(person2Field);
        panel.add(new JLabel("Relation:"));
        panel.add(resultLabel);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(findButton);

        findButton.addActionListener(e -> {
            String person1 = person1Field.getText().trim();
            String person2 = person2Field.getText().trim();

            String relation = familyTree.findRelation(person1, person2);
            if (relation.equals("One or both persons not found in the family tree.")) 
            { resultLabel.setText(relation); } 
            else 
            { resultLabel.setText(person1 + " is " + relation + " of " + person2); }
        });

        return panel;
    }

    private List<String> parseInput(String input) {
        if (input.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(input.split(","));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FamilyTreeGUI::new);
    }
}
