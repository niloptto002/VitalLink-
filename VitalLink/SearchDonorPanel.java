import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

interface SearchStrategy {
    List<Donor> filter(List<Donor> donors, String value);
}

class IdSearchStrategy implements SearchStrategy {
    public List<Donor> filter(List<Donor> donors, String value) {
        donors.removeIf(d -> !d.getId().equalsIgnoreCase(value));
        return donors;
    }
}

class NameSearchStrategy implements SearchStrategy {
    public List<Donor> filter(List<Donor> donors, String value) {
        donors.removeIf(d -> !d.getName().toLowerCase().contains(value.toLowerCase()));
        return donors;
    }
}

class AddressSearchStrategy implements SearchStrategy {
    public List<Donor> filter(List<Donor> donors, String value) {
        donors.removeIf(d -> !d.getAddress().toLowerCase().contains(value.toLowerCase()));
        return donors;
    }
}

class BloodSearchStrategy implements SearchStrategy {
    public List<Donor> filter(List<Donor> donors, String value) {
        donors.removeIf(d -> !d.getBlood().equals(value));
        return donors;
    }
}

public class SearchDonorPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField idField, nameField, addressField;
    private JComboBox<String> bloodCombo;

    public SearchDonorPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(255,245,245));

        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0,10,3,10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,5,0,5);
        c.gridy = 0;

        bloodCombo = new JComboBox<>(new String[]{"","A+","A-","B+","B-","AB+","AB-","O+","O-"});
        idField = new JTextField();
        nameField = new JTextField();
        addressField = new JTextField();

        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Arial", Font.BOLD, 14));
        searchBtn.setBackground(new Color(229,57,53));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setOpaque(true);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font("Arial", Font.BOLD, 14));
        clearBtn.setBackground(new Color(229,57,53));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFocusPainted(false);
        clearBtn.setBorderPainted(false);
        clearBtn.setOpaque(true);

        Dimension fieldSize = new Dimension(150, 25);
        bloodCombo.setPreferredSize(fieldSize);
        idField.setPreferredSize(fieldSize);
        nameField.setPreferredSize(fieldSize);
        addressField.setPreferredSize(fieldSize);

        double fieldWeight = 0.20;

        c.gridx = 0; c.weightx = 0; searchPanel.add(new JLabel("Blood Group:"), c);
        c.gridx = 1; c.weightx = fieldWeight; c.fill = GridBagConstraints.HORIZONTAL; searchPanel.add(bloodCombo, c);

        c.gridx = 2; c.weightx = 0; c.fill = GridBagConstraints.NONE; searchPanel.add(new JLabel("ID:"), c);
        c.gridx = 3; c.weightx = fieldWeight; c.fill = GridBagConstraints.HORIZONTAL; searchPanel.add(idField, c);

        c.gridx = 4; c.weightx = 0; c.fill = GridBagConstraints.NONE; searchPanel.add(new JLabel("Name:"), c);
        c.gridx = 5; c.weightx = fieldWeight; c.fill = GridBagConstraints.HORIZONTAL; searchPanel.add(nameField, c);

        c.gridx = 6; c.weightx = 0; c.fill = GridBagConstraints.NONE; searchPanel.add(new JLabel("Address:"), c);
        c.gridx = 7; c.weightx = fieldWeight; c.fill = GridBagConstraints.HORIZONTAL; searchPanel.add(addressField, c);

        c.gridx = 8; c.weightx = 0; c.fill = GridBagConstraints.NONE; searchPanel.add(searchBtn, c);
        c.gridx = 9; searchPanel.add(clearBtn, c);

        add(searchPanel, BorderLayout.NORTH);

        String[] cols={"ID","Name","Blood Group","DOB","Last Donation","Address","Mobile","Email"};
        model = new DefaultTableModel(cols,0){ public boolean isCellEditable(int r,int c){ return false; } };
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setRowHeight(25);
        centerTableCells();
        add(new JScrollPane(table), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String blood = (String) bloodCombo.getSelectedItem();

            List<Donor> results = new ArrayList<>(DonorManager.getInstance().getAllDonors());

            if(!id.isEmpty()) results = new IdSearchStrategy().filter(results, id);
            if(!name.isEmpty()) results = new NameSearchStrategy().filter(results, name);
            if(!address.isEmpty()) results = new AddressSearchStrategy().filter(results, address);
            if(!blood.isEmpty()) results = new BloodSearchStrategy().filter(results, blood);

            results.sort((d1,d2)->d1.getId().compareTo(d2.getId()));
            updateTable(results);
        });

        clearBtn.addActionListener(e -> {
            bloodCombo.setSelectedIndex(0);
            idField.setText("");
            nameField.setText("");
            addressField.setText("");
            model.setRowCount(0);
        });
    }

    private void updateTable(List<Donor> results){
        model.setRowCount(0);
        for(Donor d: results){
            model.addRow(new Object[]{d.getId(), d.getName(), d.getBlood(), d.getDob(),
                    d.getLastDonation(), d.getAddress(), d.getMobile(), d.getEmail()});
        }
    }

    private void centerTableCells(){
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0;i<table.getColumnCount();i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);
    }
}