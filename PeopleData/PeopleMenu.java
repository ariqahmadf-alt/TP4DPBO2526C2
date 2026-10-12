import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Year;
import java.util.ArrayList;

public class PeopleMenu extends JFrame {
    // index baris yang sedang dipilih (-1 = tidak ada yang dipilih)
    private int selectedIndex = -1;
    // list untuk menampung semua data person
    private ArrayList<Person> listPerson;
    // group agar hanya satu radio button golongan darah yang bisa dipilih
    private ButtonGroup golonganDarahGroup;

    // component hasil binding dari PeopleMenu.form
    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;
    private JPanel golonganDarahPanel;
    private JRadioButton radioA;
    private JRadioButton radioB;
    private JRadioButton radioAB;
    private JRadioButton radioO;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable personTable;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel namaLabel;
    private JLabel tahunLahirLabel;
    private JLabel kategoriLabel;
    private JLabel golonganDarahLabel;

    public static void main(String[] args) {
        // buat object window
        PeopleMenu menu = new PeopleMenu();

        // atur ukuran window
        menu.setSize(800, 650);

        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(java.awt.Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // constructor
    public PeopleMenu() {
        // judul window
        setTitle("Data Civitas Akademika");

        // inisialisasi listPerson
        listPerson = new ArrayList<>();

        // isi listPerson
        populateList();

        // isi tabel person
        refreshTable();

        // hanya boleh memilih satu baris
        personTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] kategoriData = { "???", "Mahasiswa", "Dosen", "Tenaga Kependidikan", "Alumni", "Tamu" };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // gabungkan radio button golongan darah ke dalam satu group
        golonganDarahGroup = new ButtonGroup();
        golonganDarahGroup.add(radioA);
        golonganDarahGroup.add(radioB);
        golonganDarahGroup.add(radioAB);
        golonganDarahGroup.add(radioO);

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    return;
                }

                // konfirmasi sebelum menghapus data
                Object[] pilihan = { "Ya", "Tidak" };
                int hasil = JOptionPane.showOptionDialog(
                        PeopleMenu.this,
                        "Apakah Anda yakin ingin menghapus data \""
                                + listPerson.get(selectedIndex).getNama() + "\"?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        pilihan,
                        pilihan[1]);

                // 0 = "Ya"; "Tidak" atau dialog ditutup = batal
                if (hasil == 0) {
                    deleteData();
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // saat salah satu baris tabel ditekan
        personTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // cari baris tabel yang diklik
                int row = personTable.rowAtPoint(e.getPoint());
                if (row == -1) {
                    return;
                }

                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = row;
                Person p = listPerson.get(selectedIndex);

                // ubah isi textfield, combo box, dan radio button
                idField.setText(p.getId());
                namaField.setText(p.getNama());
                tahunLahirField.setText(String.valueOf(p.getTahunLahir()));
                kategoriComboBox.setSelectedItem(p.getKategori());
                setGolonganDarahTerpilih(p.getGolonganDarah());

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    // method untuk mengisi data awal ke listPerson
    public void populateList() {
        listPerson.add(new Person("MHS001", "Budi Santoso", 2004, "Mahasiswa", "A"));
        listPerson.add(new Person("MHS002", "Siti Aminah", 2005, "Mahasiswa", "O"));
        listPerson.add(new Person("MHS003", "Rian Pratama", 2003, "Mahasiswa", "B"));
        listPerson.add(new Person("DSN001", "Dewi Lestari", 1985, "Dosen", "AB"));
        listPerson.add(new Person("DSN002", "Ahmad Fauzi", 1979, "Dosen", "O"));
        listPerson.add(new Person("TKN001", "Nadia Putri", 1992, "Tenaga Kependidikan", "A"));
        listPerson.add(new Person("TKN002", "Rizky Ramadhan", 1990, "Tenaga Kependidikan", "B"));
        listPerson.add(new Person("ALM001", "Maya Indah", 1998, "Alumni", "O"));
        listPerson.add(new Person("ALM002", "Hendra Wijaya", 1995, "Alumni", "AB"));
        listPerson.add(new Person("TMU001", "Clarissa Yunita", 1988, "Tamu", "A"));
    }

    // method untuk membuat model tabel dari listPerson
    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = { "No", "ID", "Nama", "Tahun Lahir", "Kategori", "Golongan Darah" };

        // buat objek tabel dengan kolom yang sudah dibuat (sel tidak bisa diedit langsung)
        DefaultTableModel tmp = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // isi tabel dengan listPerson
        for (int i = 0; i < listPerson.size(); i++) {
            Object[] row = {
                    i + 1,
                    listPerson.get(i).getId(),
                    listPerson.get(i).getNama(),
                    listPerson.get(i).getTahunLahir(),
                    listPerson.get(i).getKategori(),
                    listPerson.get(i).getGolonganDarah()
            };
            tmp.addRow(row);
        }

        return tmp;
    }

    // method untuk memasang model terbaru ke tabel
    private void refreshTable() {
        personTable.setModel(setTable());

        // atur lebar kolom "No" agar tidak terlalu lebar
        personTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        personTable.getColumnModel().getColumn(0).setMaxWidth(60);
    }

    // method untuk validasi isi form; mengembalikan pesan error, atau null jika valid
    // ignoreIndex = index data yang sedang diubah (diabaikan saat cek ID kembar), -1 jika tambah data
    private String validasiForm(int ignoreIndex) {
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        String tahunText = tahunLahirField.getText().trim();

        if (id.isEmpty()) {
            return "ID tidak boleh kosong!";
        }
        if (nama.isEmpty()) {
            return "Nama tidak boleh kosong!";
        }
        if (tahunText.isEmpty()) {
            return "Tahun lahir tidak boleh kosong!";
        }

        int tahun;
        try {
            tahun = Integer.parseInt(tahunText);
        } catch (NumberFormatException ex) {
            return "Tahun lahir harus berupa angka!";
        }
        int tahunSekarang = Year.now().getValue();
        if (tahun < 1900 || tahun > tahunSekarang) {
            return "Tahun lahir harus antara 1900 dan " + tahunSekarang + "!";
        }

        if (kategoriComboBox.getSelectedIndex() <= 0) {
            return "Pilih kategori terlebih dahulu!";
        }
        if (getGolonganDarahTerpilih() == null) {
            return "Pilih golongan darah terlebih dahulu!";
        }

        // ID harus unik
        for (int i = 0; i < listPerson.size(); i++) {
            if (i != ignoreIndex && listPerson.get(i).getId().equalsIgnoreCase(id)) {
                return "ID \"" + id + "\" sudah digunakan!";
            }
        }

        return null;
    }

    // method untuk mengambil golongan darah dari radio button yang dipilih
    private String getGolonganDarahTerpilih() {
        if (radioA.isSelected()) return "A";
        if (radioB.isSelected()) return "B";
        if (radioAB.isSelected()) return "AB";
        if (radioO.isSelected()) return "O";
        return null;
    }

    // method untuk memilih radio button sesuai golongan darah
    private void setGolonganDarahTerpilih(String golongan) {
        golonganDarahGroup.clearSelection();
        if (golongan == null) {
            return;
        }
        switch (golongan) {
            case "A":
                radioA.setSelected(true);
                break;
            case "B":
                radioB.setSelected(true);
                break;
            case "AB":
                radioAB.setSelected(true);
                break;
            case "O":
                radioO.setSelected(true);
                break;
            default:
                break;
        }
    }

    public void insertData() {
        // validasi input
        String error = validasiForm(-1);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ambil value dari textfield, combobox, dan radio button
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        int tahunLahir = Integer.parseInt(tahunLahirField.getText().trim());
        String kategori = kategoriComboBox.getSelectedItem().toString();
        String golonganDarah = getGolonganDarahTerpilih();

        // tambahkan data ke dalam list
        listPerson.add(new Person(id, nama, tahunLahir, kategori, golonganDarah));

        // update tabel
        refreshTable();

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Insert berhasil");
        JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan");
    }

    public void updateData() {
        // validasi input (ID boleh sama dengan ID data yang sedang diubah)
        String error = validasiForm(selectedIndex);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ambil data dari form
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        int tahunLahir = Integer.parseInt(tahunLahirField.getText().trim());
        String kategori = kategoriComboBox.getSelectedItem().toString();
        String golonganDarah = getGolonganDarahTerpilih();

        // ubah data person di list
        listPerson.get(selectedIndex).setId(id);
        listPerson.get(selectedIndex).setNama(nama);
        listPerson.get(selectedIndex).setTahunLahir(tahunLahir);
        listPerson.get(selectedIndex).setKategori(kategori);
        listPerson.get(selectedIndex).setGolonganDarah(golonganDarah);

        // update tabel
        refreshTable();

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Update berhasil");
        JOptionPane.showMessageDialog(this, "Data berhasil diubah");
    }

    public void deleteData() {
        // hapus data dari list
        listPerson.remove(selectedIndex);

        // update tabel
        refreshTable();

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
    }

    public void clearForm() {
        // kosongkan semua textfield, combo box, dan radio button
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        golonganDarahGroup.clearSelection();

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // hilangkan highlight baris pada tabel
        personTable.clearSelection();

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }
}
