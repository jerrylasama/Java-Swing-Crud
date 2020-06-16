package ui;
import javax.swing.*;
import mdlaf.*;
import db.DBConnection;
import mdlaf.animation.*;
import mdlaf.utils.MaterialColors;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class TableFrame extends JFrame{
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public TableFrame() {
        super("Modul 5 - Swing dan JDBC");
        this.con = DBConnection.getConnection();
        setMinimumSize(new Dimension (600, 400));
        initGUI();
    } 

    public void onSelectedTableRow()
    {
        int idx = this.table.getSelectedRow();
        if (idx == -1) return;
        idUser = (String) tableModel.getValueAt(idx, 0);
        nameField.setText((String) tableModel.getValueAt(idx, 1));
        addressField.setText((String) tableModel.getValueAt(idx, 2));
        phoneField.setText((String) tableModel.getValueAt(idx, 3));
    }

    public void getTableData()
    {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            stmt = con.createStatement();
            String sql = "SELECT * FROM anggota";
            rs = stmt.executeQuery(sql);

            while(rs.next())
            {
                Object[] o = new Object[4];
                o[0] = rs.getString("id");
                o[1] = rs.getString("nama");
                o[2] = rs.getString("alamat");
                o[3] = rs.getString("telp");
                tableModel.addRow(o);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initGUI()
    {

        formPanel.add(titleLabel, "center, wrap, span");
        // Pembuatan Panel form input data user
        formPanel.add(nameLabel, "gapy 20");
        formPanel.add(nameField, "wrap, push, span");
        formPanel.add(addressLabel);
        formPanel.add(addressField, "wrap, push, span");
        formPanel.add(phoneLabel);
        formPanel.add(phoneField, "wrap, push, span");

        // Deklarasi model table dengan 4 kolom
        tableModel.addColumn("id");
        tableModel.addColumn("Nama");
        tableModel.addColumn("Alamat");
        tableModel.addColumn("Telepon");
        
        formPanel.add(new JScrollPane(table), "wrap, width 100%, growx, push, span, gapy 20");


        // Deklarasi buttton-button dan panel untuk button
        JPanel buttonPanel = new JPanel();

        addButton.setMaximumSize (new Dimension (200, 200));
        addButton.setBackground(MaterialColors.LIGHT_BLUE_400);
        addButton.setForeground(MaterialColors.WHITE);
        addButton.setOpaque(true);
        addButton.addMouseListener(MaterialUIMovement.getMovement(addButton, MaterialColors.LIGHT_BLUE_600));

        editButton.setMaximumSize (new Dimension (200, 200));
        editButton.setBackground(MaterialColors.AMBER_400);
        editButton.setForeground(MaterialColors.WHITE);
        editButton.setOpaque(true);
        editButton.addMouseListener(MaterialUIMovement.getMovement(editButton, MaterialColors.AMBER_600));
        
        deleteButton.setMaximumSize (new Dimension (200, 200));
        deleteButton.setBackground(MaterialColors.RED_400);
        deleteButton.setForeground(MaterialColors.WHITE);
        deleteButton.setOpaque(true);
        deleteButton.addMouseListener(MaterialUIMovement.getMovement(deleteButton, MaterialColors.RED_600));

        refreshButton.setMaximumSize (new Dimension (200, 200));
        
        //tambahkan button pada panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        formPanel.add(buttonPanel, "gapy 10, center, span");

        add(formPanel);
        

		// JPanel content = new JPanel ();
        // content.add (button);
		// frame.getContentPane().add(content, BorderLayout.CENTER);


		pack();
        setVisible (true);
        
        // Implements ActionListener on UI-elements
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                onSelectedTableRow();
            }
          });
        
        addButton.addActionListener(new ActionListener() 
        {
           @Override
           public void actionPerformed(ActionEvent e)
           {
                if("Add".equals(addButton.getText()))
                {
                    addButton.setText("Save");
                    editButton.setText("Cancel");
                    deleteButton.setEnabled(false);
                    refreshButton.setEnabled(false);
                    idUser = "";
                    nameField.setText("");
                    addressField.setText("");
                    phoneField.setText("");
                }
                else
                {
                    String sql = "INSERT INTO anggota (nama, alamat, telp) values(?,?,?)";
                    try {
                        PreparedStatement p2 = con.prepareStatement(sql);
                        p2.setString(1, nameField.getText());
                        p2.setString(2, addressField.getText());
                        p2.setString(3, phoneField.getText());
                        p2.executeUpdate();
                        p2.close();
                        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Terjadi Kesalahan " + ex.getMessage());
                    }
                    addButton.setText("Add");
                    editButton.setText("Edit");
                    deleteButton.setEnabled(true);
                    refreshButton.setEnabled(true);
                    getTableData();
                }
           } 
        });

        editButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if("Edit".equals(editButton.getText()))
                {
                    addButton.setText("Update");
                    editButton.setText("Cancel");
                    deleteButton.setEnabled(false);
                    refreshButton.setEnabled(false);
                }
                else{
                    addButton.setText("Add");
                    editButton.setText("Edit");
                    deleteButton.setEnabled(true);
                    refreshButton.setEnabled(true);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "DELETE FROM anggota WHERE id = ?";
                try {
                    PreparedStatement p2 = con.prepareStatement(sql);
                    p2.setString(1, idUser);
                    p2.executeUpdate();
                    p2.close();
                    getTableData();
                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Terjadi Kesalahan " + ex.getMessage());
                }
            }
        });

        refreshButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                getTableData();
                idUser = "";
                nameField.setText("");
                addressField.setText("");
                phoneField.setText("");  
            }

        });
    }

    // Class-wide ui-elements declaration
    private String idUser;
    private JPanel formPanel = new JPanel(new MigLayout());
    private JLabel titleLabel = new JLabel("CRUD dengan Java Swing dan MYSQL");
    private JLabel nameLabel = new JLabel("Nama");
    private JTextField nameField = new JTextField(50);
    private JLabel addressLabel = new JLabel("Alamat");
    private JTextField addressField = new JTextField(50);
    private JLabel phoneLabel = new JLabel("Telepon");
    private JTextField phoneField = new JTextField(50);
    private JButton addButton = new JButton("Add");
    private JButton editButton = new JButton("Edit");
    private JButton deleteButton = new JButton("Delete");
    private JButton refreshButton = new JButton("Refresh");
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable table = new JTable(tableModel);
    // end of declaration
}