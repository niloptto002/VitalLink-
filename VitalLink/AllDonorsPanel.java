import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

public class AllDonorsPanel extends JPanel {
    private JTable table; 
    private DefaultTableModel model;
    private JButton deleteBtn, editBtn;

    public AllDonorsPanel(){
        setLayout(new BorderLayout()); 
        setBackground(new Color(255,245,245));

        String[] cols={"ID","Name","Blood Group","DOB","Last Donation","Address","Mobile","Email"};
        model=new DefaultTableModel(cols,0){
            @Override
            public boolean isCellEditable(int r,int c){ return false; }
        };
        table=new JTable(model); 
        table.setFont(new Font("Arial",Font.BOLD,12));
        table.getTableHeader().setFont(new Font("Arial",Font.BOLD,13));
        table.setRowHeight(25); 
        centerTableCells();
        add(new JScrollPane(table),BorderLayout.CENTER);

        deleteBtn = createFlatButton("    Delete    ");
        editBtn   = createFlatButton("    Edit    ");

        JPanel bottom = new JPanel(); 
        bottom.setBackground(getBackground());
        bottom.add(editBtn); 
        bottom.add(deleteBtn); 
        add(bottom,BorderLayout.SOUTH);

        DonorFacade facade = new DonorFacade();

        deleteBtn.addActionListener(e -> facade.deleteDonor());
        editBtn.addActionListener(e -> facade.editDonor());

        refreshTable();
    }

    public void refreshTable(){
        model.setRowCount(0);
        List<Donor> all = DonorManager.getInstance().getAllDonors();
        all.sort((d1,d2) -> d1.getId().compareTo(d2.getId()));
        for(Donor d: all){
            model.addRow(new Object[]{d.getId(), d.getName(), d.getBlood(), d.getDob(),
                    d.getLastDonation(), d.getAddress(), d.getMobile(), d.getEmail()});
        }
    }

    private void deleteSelected(){
        int sel = table.getSelectedRow();
        if(sel >= 0){
            String id = (String) model.getValueAt(sel,0);
            Donor target = new Donor(id,"","","","","","","");
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this donor?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if(confirm != JOptionPane.YES_OPTION) return;

            DonorManager.getInstance().deleteDonor(target);
            refreshTable();
            JOptionPane.showMessageDialog(this,"Donor deleted!");
        } else JOptionPane.showMessageDialog(this,"Select a donor to delete!");
    }

    private void editSelected(){
        int sel = table.getSelectedRow();
        if(sel < 0){ 
            JOptionPane.showMessageDialog(this,"Select a donor to edit!"); 
            return; 
        }

        String id = (String) model.getValueAt(sel,0);
        Donor old = null;
        for(Donor d : DonorManager.getInstance().getAllDonors()){ 
            if(d.getId().equals(id)){ old = d; break; } 
        }
        if(old == null) return;

        JTextField nameF = new JTextField(old.getName());
        JComboBox<String> bloodF = new JComboBox<>(new String[]{"","A+","A-","B+","B-","AB+","AB-","O+","O-"}); 
        bloodF.setSelectedItem(old.getBlood());
        JTextField dobF = new JTextField(old.getDob()), lastF = new JTextField(old.getLastDonation()), addressF = new JTextField(old.getAddress());
        JTextField mobileF = new JTextField(old.getMobile()), emailF = new JTextField(old.getEmail());

        JPanel panel = new JPanel(new GridLayout(8,2,5,5));
        panel.add(new JLabel("ID:")); panel.add(new JLabel(old.getId()));
        panel.add(new JLabel("Name:")); panel.add(nameF);
        panel.add(new JLabel("Blood Group:")); panel.add(bloodF);
        panel.add(new JLabel("DOB:")); panel.add(dobF);
        panel.add(new JLabel("Last Donation:")); panel.add(lastF);
        panel.add(new JLabel("Address:")); panel.add(addressF);
        panel.add(new JLabel("Mobile:")); panel.add(mobileF);
        panel.add(new JLabel("Email:")); panel.add(emailF);

        int res = JOptionPane.showConfirmDialog(this,panel,"Edit Donor",
                JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(res == JOptionPane.OK_OPTION){
            Donor updated = new Donor(old.getId(), nameF.getText(), (String) bloodF.getSelectedItem(),
                    dobF.getText(), lastF.getText(), addressF.getText(), mobileF.getText(), emailF.getText());
            DonorManager.getInstance().updateDonor(old, updated);
            refreshTable(); 
            JOptionPane.showMessageDialog(this,"Donor updated!");
        }
    }

    private JButton createFlatButton(String text){
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(229,57,53));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        return btn;
    }

    private void centerTableCells(){
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    private class DonorFacade {
        public void deleteDonor() {
            deleteSelected();
        }
        
        public void editDonor() {
            editSelected();
        }
    }
}