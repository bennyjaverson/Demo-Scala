# Sorting Produk Pertanian

Tugas Pemrograman Fungsional Kelompok 7 tentang fungsi rekursif dan sorting. Aplikasi web ini mengurutkan 10 produk pertanian berdasarkan harga menggunakan method bawaan Scala dan Recursive Insertion Sort.

## Fitur

- Empat metode: `sorted`, `sortBy`, `sortWith`, dan Recursive Insertion Sort.
- Pilihan ascending (termurah ke termahal) dan descending (termahal ke termurah).
- Tabel data awal dan hasil pengurutan.
- Perbandingan hasil rekursif dengan ketiga method bawaan.
- Data tetap di backend menggunakan `List[Produk]`, tanpa database.

## Teknologi

| Bagian | Teknologi |
| --- | --- |
| Bahasa backend | Scala 3.3.8 |
| Framework backend | Cask 0.11.3 |
| Frontend | HTML, CSS, dan JavaScript |
| Komunikasi frontend dan backend | Fetch API |
| Menjalankan proyek | Scala CLI |

## Cara menjalankan

Pastikan Scala CLI tersedia dan ada koneksi internet untuk mengunduh dependensi pada penggunaan pertama.

1. Unduh repository ini atau clone melalui terminal:

   ```sh
   git clone https://github.com/bennyjaverson/Demo-Scala.git
   cd Demo-Scala
   ```

   Jika mengunduh ZIP, ekstrak dan buka terminal di folder yang berisi `main.scala`, `index.html`, dan `main.js`.

2. Jalankan backend:

   ```sh
   scala-cli run main.scala --server=false --main-class Main
   ```

3. Buka [http://localhost:8080](http://localhost:8080) di browser.
4. Pilih metode sorting dan urutan harga, lalu klik **Urutkan**.

Tekan `Ctrl+C` di terminal untuk menghentikan backend. Jalankan perintah dari folder aplikasi karena backend membaca file HTML dan JavaScript dari folder kerja.

## Struktur file

```text
Demo-Scala/
├── .gitignore    # Folder cache dan konfigurasi lokal yang diabaikan Git
├── main.scala    # Dataset, fungsi sorting, dan endpoint Cask
├── index.html    # Tampilan dan penjelasan metode
├── main.js       # Permintaan fetch dan pengisian tabel
└── README.md     # Dokumentasi proyek
```

## Metode sorting

| Metode | Cara kerja |
| --- | --- |
| `sorted` | Menggunakan `Ordering[Produk]` berdasarkan harga. |
| `sortBy` | Menerima fungsi `_.harga` untuk menentukan kunci pengurutan. |
| `sortWith` | Menerima fungsi pembanding dua produk yang menghasilkan `Boolean`. |
| Recursive Insertion Sort | Mengurutkan `tail` secara rekursif, lalu menyisipkan `head` ke posisi yang sesuai melalui fungsi `insert`. List kosong menjadi base case. |

Fungsi `insertionSort` dan `insert` dibuat sendiri tanpa memanggil `sorted`, `sortBy`, atau `sortWith`.

### Kaitan dengan pemrograman fungsional

- **Immutable collection:** pengurutan menghasilkan `List` baru sehingga data awal tetap sama.
- **Higher-order function:** `sortBy` menerima fungsi untuk mengambil harga produk.
- **Function as predicate:** `sortWith` menerima fungsi pembanding yang menghasilkan `true` atau `false`.
- **Rekursi dan pattern matching:** `insertionSort` dan `insert` memproses pola `Nil` serta `head :: tail`.

## Endpoint API

```http
GET /api/produk?metode=rekursif&urutan=asc
```

| Parameter | Pilihan | Nilai default |
| --- | --- | --- |
| `metode` | `sorted`, `sortBy`, `sortWith`, `rekursif` | `rekursif` |
| `urutan` | `asc`, `desc` | `asc` |

Respons JSON berisi:

- `awal`: data produk sebelum diurutkan.
- `hasil`: data produk setelah diurutkan dengan metode yang dipilih.
- `perbandingan`: nilai `Boolean` yang menyatakan apakah hasil rekursif sama dengan hasil masing-masing method bawaan pada urutan yang dipilih.

Pilihan metode atau urutan yang tidak valid menghasilkan HTTP `400` dan pesan kesalahan.

## Hasil pengujian

- Kompilasi backend berhasil.
- Delapan kombinasi metode dan urutan menghasilkan urutan harga yang benar pada API dan browser.
- Hasil rekursif sama dengan `sorted`, `sortBy`, dan `sortWith` untuk kedua urutan pada dataset tugas.
- Data awal tetap sama setelah pengurutan.
- Parameter yang tidak valid menghasilkan HTTP `400`.
- Kondisi loading, data kosong, pesan koneksi gagal, dan mencoba ulang sudah diperiksa.
- Tampilan diperiksa pada lebar 1280, 375, dan 320 piksel, termasuk navigasi keyboard.

## Referensi

- [Dokumentasi Cask](https://com-lihaoyi.github.io/cask/).
