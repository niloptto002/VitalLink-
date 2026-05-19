import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

interface DonorIterator {
    boolean hasNext();
    Donor next();
}

class DonorCollection {
    private List<Donor> donors;

    public DonorCollection(List<Donor> donors) {
        this.donors = donors;
    }

    public DonorIterator createIterator() {
        return new DonorListIterator();
    }

    private class DonorListIterator implements DonorIterator {
        private int index = 0;

        public boolean hasNext() {
            return index < donors.size();
        }

        public Donor next() {
            return donors.get(index++);
        }
    }
}

public class VitalLinkFrame extends JPanel {
    private JTabbedPane tabs;
    private AllDonorsPanel allDonorsPanel;
    private AddDonorPanel addDonorPanel;
    private SearchDonorPanel searchPanel;

    public VitalLinkFrame() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(220, 20, 60));

        JLabel lbl = new JLabel("VitalLink");
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
        lbl.setForeground(Color.WHITE);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(lbl, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(new Font("Arial", Font.BOLD, 14));

        allDonorsPanel = new AllDonorsPanel();
        addDonorPanel = new AddDonorPanel(allDonorsPanel);
        searchPanel = new SearchDonorPanel();

        tabs.addTab("Add Donor", addDonorPanel);
        tabs.addTab("Search Donor", searchPanel);
        tabs.addTab("All Donors", allDonorsPanel);

        add(tabs, BorderLayout.CENTER);

        DonorCollection collection =
                new DonorCollection(DonorManager.getInstance().getAllDonors());

        DonorIterator iterator = collection.createIterator();
        while (iterator.hasNext()) {
            Donor d = iterator.next();
        }
    }

    public JPanel getMainPanel() {
        return this;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("VitalLink - Blood Bank Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(950, 650);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new VitalLinkFrame().getMainPanel());
            frame.setVisible(true);
        });
    }
}