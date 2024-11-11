import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CatatanGUI extends JFrame {
    private ArrayList<Catatan> daftarCatatanList = new ArrayList<>();
    private DefaultListModel<Catatan> modelDaftarCatatan;
    private JTextField fieldJudul;
    private JTextArea areaIsi;
    private JList<Catatan> daftarCatatan;

    public CatatanGUI() {
        setTitle("Catatan Harian - 2210010611"); 
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Font setup
        Font fontBanschrift14 = new Font("Banschrift", Font.BOLD, 14);
        Font fontBanschrift18 = new Font("Banschrift", Font.BOLD, 18);

        // Panel Utama dan Tata Letak
        setLayout(new BorderLayout(10, 10));

        // Panel Atas untuk Judul
        JPanel panelJudul = new JPanel();
        panelJudul.setBackground(Color.DARK_GRAY);
        JLabel judulLabel = new JLabel("Aplikasi Catatan Harian - 2210010611");
        judulLabel.setFont(fontBanschrift18);
        judulLabel.setForeground(Color.LIGHT_GRAY);
        panelJudul.add(judulLabel);

        // Panel Kiri untuk Input Judul dan Isi
        JPanel panelKiri = new JPanel(new GridBagLayout());
        panelKiri.setBackground(Color.GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Komponen GUI
        JLabel labelJudul = new JLabel("Judul:");
        labelJudul.setFont(fontBanschrift14);
        labelJudul.setForeground(Color.LIGHT_GRAY);
        fieldJudul = new JTextField(20);
        fieldJudul.setFont(fontBanschrift14);
        fieldJudul.setBackground(Color.DARK_GRAY);
        fieldJudul.setForeground(Color.WHITE);

        JLabel labelIsi = new JLabel("Isi:");
        labelIsi.setFont(fontBanschrift14);
        labelIsi.setForeground(Color.LIGHT_GRAY);
        areaIsi = new JTextArea(10, 20);
        areaIsi.setFont(fontBanschrift14);
        areaIsi.setLineWrap(true);
        areaIsi.setWrapStyleWord(true);
        areaIsi.setBackground(Color.DARK_GRAY);
        areaIsi.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(areaIsi);

        // Pengaturan GridBag untuk komponen pada panel kiri
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panelKiri.add(labelJudul, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelKiri.add(fieldJudul, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panelKiri.add(labelIsi, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panelKiri.add(scrollPane, gbc);

        // Panel Bawah untuk Tombol
        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBawah.setBackground(Color.DARK_GRAY);

        // Tombol
        JButton tombolTambah = new JButton("Tambah");
        JButton tombolEdit = new JButton("Edit");
        JButton tombolHapus = new JButton("Hapus");
        JButton tombolReset = new JButton("Reset");
        JButton tombolSaveAs = new JButton("Save");

        // Set tampilan tombol
        JButton[] buttons = {tombolTambah, tombolEdit, tombolHapus, tombolReset, tombolSaveAs};
        for (JButton button : buttons) {
            button.setFont(fontBanschrift14);
            button.setPreferredSize(new Dimension(100, 40));
            button.setBackground(Color.DARK_GRAY);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            panelBawah.add(button);
        }

        // Daftar catatan di panel kanan
        modelDaftarCatatan = new DefaultListModel<>();
        daftarCatatan = new JList<>(modelDaftarCatatan);
        daftarCatatan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        daftarCatatan.setBackground(Color.LIGHT_GRAY);
        daftarCatatan.setFont(fontBanschrift14);
        JScrollPane scrollDaftarCatatan = new JScrollPane(daftarCatatan);
        scrollDaftarCatatan.setPreferredSize(new Dimension(200, 0));

        // Tambah panel ke JFrame
        add(panelJudul, BorderLayout.NORTH);
        add(panelKiri, BorderLayout.CENTER);
        add(scrollDaftarCatatan, BorderLayout.EAST);
        add(panelBawah, BorderLayout.SOUTH);

        // Event Handling
        tombolTambah.addActionListener(e -> tambahCatatan());
        tombolEdit.addActionListener(e -> editCatatan());
        tombolHapus.addActionListener(e -> hapusCatatan());
        tombolReset.addActionListener(e -> resetInput());
        tombolSaveAs.addActionListener(e -> saveAsTxt());

        daftarCatatan.addListSelectionListener(e -> {
            Catatan selectedCatatan = daftarCatatan.getSelectedValue();
            if (selectedCatatan != null) {
                fieldJudul.setText(selectedCatatan.getJudul());
                areaIsi.setText(selectedCatatan.getIsi());
            }
        });
    }

    private void tambahCatatan() {
        String judul = fieldJudul.getText();
        String isi = areaIsi.getText();
        if (!judul.isEmpty() && !isi.isEmpty()) {
            Catatan catatan = new Catatan(judul, isi);
            daftarCatatanList.add(catatan);
            modelDaftarCatatan.addElement(catatan);
            resetInput();
        } else {
            JOptionPane.showMessageDialog(null, "Judul dan Isi tidak boleh kosong!");
        }
    }

    private void editCatatan() {
        Catatan selectedCatatan = daftarCatatan.getSelectedValue();
        if (selectedCatatan != null) {
            selectedCatatan.setJudul(fieldJudul.getText());
            selectedCatatan.setIsi(areaIsi.getText());
            daftarCatatan.repaint();
            resetInput();
        } else {
            JOptionPane.showMessageDialog(null, "Pilih catatan yang ingin diedit!");
        }
    }

    private void hapusCatatan() {
        Catatan selectedCatatan = daftarCatatan.getSelectedValue();
        if (selectedCatatan != null) {
            daftarCatatanList.remove(selectedCatatan);
            modelDaftarCatatan.removeElement(selectedCatatan);
            resetInput();
        } else {
            JOptionPane.showMessageDialog(null, "Pilih catatan yang ingin dihapus!");
        }
    }

   private void saveAsTxt() {
    if (modelDaftarCatatan.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Tidak ada catatan untuk disimpan!");
        return;
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save As");
    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text", "txt"));

    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();

        try {
            saveAllNotesAsTxt(file);
            JOptionPane.showMessageDialog(null, "File berhasil disimpan sebagai TXT");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan file: " + ex.getMessage());
        }
    }
}

private void saveAllNotesAsTxt(File file) throws IOException {
    try (FileWriter writer = new FileWriter(file + ".txt")) {
        for (int i = 0; i < modelDaftarCatatan.size(); i++) {
            Catatan catatan = modelDaftarCatatan.get(i);
            writer.write("Judul: " + catatan.getJudul() + "\n");
            writer.write("Isi:\n" + catatan.getIsi() + "\n\n");
            writer.write("======================================\n\n");  // Separator untuk setiap catatan
        }
    }
}

    private void resetInput() {
        fieldJudul.setText("");
        areaIsi.setText("");
    }
  
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1116, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 373, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CatatanGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CatatanGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CatatanGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CatatanGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

      
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CatatanGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
