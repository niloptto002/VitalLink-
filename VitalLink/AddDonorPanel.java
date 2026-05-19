import javax.swing.*;
import java.awt.*;

public class AddDonorPanel extends JPanel {
    private JTextField idField, nameField, dobField, lastDonationField, addressField, mobileField, emailField;
    private JComboBox<String> bloodCombo;
    private JButton addButton;
    private AllDonorsPanel allDonorsPanel;
    private DonorManager manager;

    public AddDonorPanel(AllDonorsPanel allPanel) {
        this.allDonorsPanel = allPanel;
        manager = DonorManager.getInstance();
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6); 
        c.anchor = GridBagConstraints.WEST;

        Dimension fieldSize = new Dimension(350, 35); 
        idField = new JTextField(); idField.setPreferredSize(fieldSize);
        nameField = new JTextField(); nameField.setPreferredSize(fieldSize);
        bloodCombo = new JComboBox<>(new String[]{"","A+","A-","B+","B-","AB+","AB-","O+","O-"}); bloodCombo.setPreferredSize(fieldSize);
        dobField = new JTextField(); dobField.setPreferredSize(fieldSize);
        lastDonationField = new JTextField(); lastDonationField.setPreferredSize(fieldSize);
        addressField = new JTextField(); addressField.setPreferredSize(fieldSize);
        mobileField = new JTextField(); mobileField.setPreferredSize(fieldSize);
        emailField = new JTextField(); emailField.setPreferredSize(fieldSize);

        addButton = new JButton("Add Donor");
        Font fieldFont = new Font("Arial", Font.PLAIN, 16); 
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        JTextField[] textFields = {idField, nameField, dobField, lastDonationField, addressField, mobileField, emailField};
        for(JTextField tf : textFields) tf.setFont(fieldFont);
        bloodCombo.setFont(fieldFont);

        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setBackground(new Color(229, 57, 53)); 
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setOpaque(true);
        addButton.setPreferredSize(new Dimension(160, 40)); 

        String[] labels = {"Id:", "Name:", "Blood Group:", "Date of Birth:", "Last Donation:", "Address:", "Mobile:", "Email:"};
        JComponent[] fields = {idField, nameField, bloodCombo, dobField, lastDonationField, addressField, mobileField, emailField};
        for(int i=0; i<labels.length; i++){
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(labelFont);
            c.gridx = 0; c.gridy = i; add(lbl, c);
            c.gridx = 1; add(fields[i], c);
        }

        c.gridx = 1; c.gridy = labels.length; add(addButton, c);

        addButton.addActionListener(e -> {
            if(idField.getText().isEmpty() || nameField.getText().isEmpty() || bloodCombo.getSelectedItem().toString().isEmpty() ||
               dobField.getText().isEmpty() || lastDonationField.getText().isEmpty() || addressField.getText().isEmpty() || mobileField.getText().isEmpty()){
                JOptionPane.showMessageDialog(this,"All details are required","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            Donor d = DonorFactory.createDonor(
                    idField.getText(), nameField.getText(), bloodCombo.getSelectedItem().toString(),
                    dobField.getText(), lastDonationField.getText(), addressField.getText(),
                    mobileField.getText(), emailField.getText()
            );

            manager.addDonor(d);
            allDonorsPanel.refreshTable();
            JOptionPane.showMessageDialog(this,"Donor added successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        });
    }

    private void clearFields(){
        idField.setText(""); nameField.setText(""); bloodCombo.setSelectedIndex(0);
        dobField.setText(""); lastDonationField.setText(""); addressField.setText("");
        mobileField.setText(""); emailField.setText("");
    }

    private static class DonorFactory {
        public static Donor createDonor(String id, String name, String blood, String dob, String lastDonation,
                                        String address, String mobile, String email){
            return new Donor(id, name, blood, dob, lastDonation, address, mobile, email);
        }
    }
}