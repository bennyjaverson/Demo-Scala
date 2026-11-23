const form = document.getElementById("form-sorting");
const tombol = document.getElementById("tombol");
const status = document.getElementById("status");
const rupiah = new Intl.NumberFormat("id-ID");

function tampilkanTabel(id, produk) {
  const tabel = document.getElementById(id);
  tabel.replaceChildren();
  if (produk.length === 0) {
    const sel = tabel.insertRow().insertCell();
    sel.colSpan = 5;
    sel.textContent = "Tidak ada produk.";
    return;
  }
  produk.forEach((p, i) => {
    const baris = tabel.insertRow();
    [i + 1, p.nama, p.kategori, rupiah.format(p.harga), p.stok].forEach(nilai => {
      baris.insertCell().textContent = nilai;
    });
  });
}

async function urutkan() {
  const metode = form.elements.metode.value;
  const urutan = form.elements.urutan.value;
  tombol.disabled = true;
  status.dataset.error = "false";
  status.textContent = "Memuat hasil pengurutan...";
  try {
    const parameter = new URLSearchParams({ metode, urutan });
    const response = await fetch(`/api/produk?${parameter}`);
    const data = await response.json();
    if (!response.ok) throw new Error(data.error || "Permintaan gagal.");

    tampilkanTabel("data-awal", data.awal);
    tampilkanTabel("data-hasil", data.hasil);
    document.getElementById("pilihan-hasil").textContent =
      `${metode === "rekursif" ? "Recursive Insertion Sort" : metode} | ${urutan === "asc" ? "Ascending" : "Descending"}`;
    document.getElementById("perbandingan").textContent =
      "Hasil Recursive Insertion Sort dibandingkan dengan " +
      Object.entries(data.perbandingan).map(([nama, sama]) =>
        `${nama}: ${sama ? "sama" : "berbeda"}`
      ).join(", ") + ".";
    status.textContent = `${data.hasil.length} produk berhasil diurutkan. Data awal tetap sama.`;
  } catch (error) {
    status.dataset.error = "true";
    status.textContent = `Gagal memuat data. Pastikan backend berjalan, lalu klik Urutkan untuk mencoba lagi. ${error.message}`;
  } finally {
    tombol.disabled = false;
  }
}

form.addEventListener("submit", event => {
  event.preventDefault();
  urutkan();
});

urutkan();
