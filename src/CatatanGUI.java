import javax.swing.*; // Import komponen GUI Swing
import java.awt.*; // Import pengaturan layout dan warna
import java.awt.event.ActionEvent; // Import untuk event handling
import java.awt.event.ActionListener; // Import untuk event listener
import java.io.File; // Import untuk bekerja dengan file
import java.io.FileWriter; // Import untuk menulis file
import java.io.IOException; // Import untuk menangani kesalahan IO
import java.util.ArrayList; // Import untuk penggunaan ArrayList

public class CatatanGUI extends JFrame {
    private ArrayList<Catatan> daftarCatatanList = new ArrayList<>(); // List untuk menyimpan catatan
    private DefaultListModel<Catatan> modelDaftarCatatan; // Model untuk JList
    private JTextField fieldJudul; // Field untuk memasukkan judul
    private JTextArea areaIsi; // Area untuk memasukkan isi catatan
    private JList<Catatan> daftarCatatan; // Komponen untuk menampilkan daftar catatan

    public CatatanGUI() {
        setTitle("Catatan Harian - 2210010611"); // Set judul jendela
        setSize(700, 500); // Set ukuran jendela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Tutup aplikasi saat jendela ditutup
        setLocationRelativeTo(null); // Pusatkan jendela

        // Font setup
        Font fontBanschrift14 = new Font("Banschrift", Font.BOLD, 14);
        Font fontBanschrift18 = new Font("Banschrift", Font.BOLD, 18);

        // Panel utama dengan BorderLayout
        setLayout(new BorderLayout(10, 10));

        // Panel atas untuk judul aplikasi
        JPanel panelJudul = new JPanel();
        panelJudul.setBackground(Color.DARK_GRAY);
        JLabel judulLabel = new JLabel("Aplikasi Catatan Harian - 2210010611");
        judulLabel.setFont(fontBanschrift18);
        judulLabel.setForeground(Color.LIGHT_GRAY);
        panelJudul.add(judulLabel); // Tambahkan label judul ke panel

        // Panel kiri untuk input judul dan isi catatan
        JPanel panelKiri = new JPanel(new GridBagLayout());
        panelKiri.setBackground(Color.GRAY);
        GridBagConstraints gbc = new GridBagConstraints(); // Layout GridBag
        gbc.insets = new Insets(5, 5, 5, 5); // Margin

        // Label dan field untuk input judul
        JLabel labelJudul = new JLabel("Judul:");
        labelJudul.setFont(fontBanschrift14);
        labelJudul.setForeground(Color.LIGHT_GRAY);
        fieldJudul = new JTextField(20);
        fieldJudul.setFont(fontBanschrift14);
        fieldJudul.setBackground(Color.DARK_GRAY);
        fieldJudul.setForeground(Color.WHITE);

        // Label dan area untuk input isi catatan
        JLabel labelIsi = new JLabel("Isi:");
        labelIsi.setFont(fontBanschrift14);
        labelIsi.setForeground(Color.LIGHT_GRAY);
        areaIsi = new JTextArea(10, 20);
        areaIsi.setFont(fontBanschrift14);
        areaIsi.setLineWrap(true);
        areaIsi.setWrapStyleWord(true);
        areaIsi.setBackground(Color.DARK_GRAY);
        areaIsi.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(areaIsi); // Tambahkan scroll untuk area isi

        // Pengaturan layout komponen pada panel kiri
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

        // Panel bawah untuk tombol-tombol
        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBawah.setBackground(Color.DARK_GRAY);

        // Tombol-tombol
        JButton tombolTambah = new JButton("Tambah");
        JButton tombolEdit = new JButton("Edit");
        JButton tombolHapus = new JButton("Hapus");
        JButton tombolReset = new JButton("Reset");
        JButton tombolSaveAs = new JButton("Save");

        // Styling tombol
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

        // Tambahkan panel-panel ke JFrame
        add(panelJudul, BorderLayout.NORTH);
        add(panelKiri, BorderLayout.CENTER);
        add(scrollDaftarCatatan, BorderLayout.EAST);
        add(panelBawah, BorderLayout.SOUTH);

        // Event Handling untuk tombol
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
            daftarCatatanList.add(catatan); // Tambahkan catatan ke list
            modelDaftarCatatan.addElement(catatan); // Tambahkan ke model daftar
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
            daftarCatatanList.remove(selectedCatatan); // Hapus catatan dari list
            modelDaftarCatatan.removeElement(selectedCatatan); // Hapus dari model
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

        JFileChooser fileChooser = new JFileChooser(); // Dialog pemilihan file
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
        fieldJudul.setText(""); // Kosongkan field judul
        areaIsi.setText(""); // Kosongkan area isi
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
