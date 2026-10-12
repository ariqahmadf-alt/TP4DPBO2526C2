public class Person {
    private String id;
    private String nama;
    private int tahunLahir;
    private String kategori;
    // atribut baru (tugas no. 3): golongan darah, dipilih lewat JRadioButton
    private String golonganDarah;

    // Constructor
    public Person() {
    }

    public Person(String id, String nama, int tahunLahir, String kategori, String golonganDarah) {
        this.id = id;
        this.nama = nama;
        this.tahunLahir = tahunLahir;
        this.kategori = kategori;
        this.golonganDarah = golonganDarah;
    }

    // Getter dan Setter untuk id
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    // Getter dan Setter untuk nama
    public String getNama() { return nama; }

    public void setNama(String nama) { this.nama = nama; }

    // Getter dan Setter untuk tahunLahir
    public int getTahunLahir() { return tahunLahir; }

    public void setTahunLahir(int tahunLahir) { this.tahunLahir = tahunLahir; }

    // Getter dan Setter untuk kategori
    public String getKategori() { return kategori; }

    public void setKategori(String kategori) { this.kategori = kategori; }

    // Getter dan Setter untuk golonganDarah
    public String getGolonganDarah() { return golonganDarah; }

    public void setGolonganDarah(String golonganDarah) { this.golonganDarah = golonganDarah; }
}
