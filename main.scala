//> using scala 3.3.8
//> using dep com.lihaoyi::cask:0.11.3

import java.nio.file.{Files, Path}

case class Produk(nama: String, kategori: String, harga: Int, stok: Int)

object Sorting {
  val produk: List[Produk] = List(
    Produk("Benih Padi Inpari 32", "Benih", 85000, 24),
    Produk("Benih Jagung Hibrida", "Benih", 95000, 18),
    Produk("Pupuk NPK 16-16-16", "Pupuk", 78000, 35),
    Produk("Pupuk Kompos 5 kg", "Pupuk", 52000, 12),
    Produk("Pestisida Nabati", "Perlindungan", 45000, 0),
    Produk("Sprayer Manual 2L", "Peralatan", 125000, 8),
    Produk("Selang Irigasi 1/2 inci", "Irigasi", 65000, 20),
    Produk("Cangkul Baja", "Peralatan", 110000, 5),
    Produk("Polybag 20x25", "Perlengkapan", 30000, 50),
    Produk("Mulsa Plastik", "Perlengkapan", 145000, 7)
  )

  def sortingBawaan(data: List[Produk], metode: String, naik: Boolean): List[Produk] = {
    val harga = Ordering.by[Produk, Int](_.harga)
    val aturan = if (naik) harga else harga.reverse

    metode match {
      case "sorted" => data.sorted(using aturan)
      case "sortBy" =>
        if (naik) data.sortBy(_.harga)
        else data.sortBy(_.harga)(using Ordering.Int.reverse)
      case "sortWith" =>
        data.sortWith((a, b) => if (naik) a.harga < b.harga else a.harga > b.harga)
      case _ => throw new IllegalArgumentException("Metode bawaan tidak valid.")
    }
  }

  def insert(produk: Produk, data: List[Produk], naik: Boolean): List[Produk] = data match {
    case Nil => List(produk)
    case head :: tail =>
      val diDepan = if (naik) produk.harga <= head.harga else produk.harga >= head.harga
      if (diDepan) produk :: data
      else head :: insert(produk, tail, naik)
  }

  def insertionSort(data: List[Produk], naik: Boolean): List[Produk] = data match {
    case Nil => Nil
    case head :: tail => insert(head, insertionSort(tail, naik), naik)
  }

  def keJson(data: List[Produk]): ujson.Arr = ujson.Arr.from(data.map(p =>
    ujson.Obj("nama" -> p.nama, "kategori" -> p.kategori, "harga" -> p.harga, "stok" -> p.stok)
  ))
}

object Main extends cask.MainRoutes {
  @cask.get("/")
  def halaman() = cask.Response(
    Files.readString(Path.of("index.html")),
    headers = Seq("Content-Type" -> "text/html; charset=utf-8")
  )

  @cask.get("/main.js")
  def javascript() = cask.Response(
    Files.readString(Path.of("main.js")),
    headers = Seq("Content-Type" -> "application/javascript; charset=utf-8")
  )

  @cask.get("/api/produk")
  def urutkan(metode: String = "rekursif", urutan: String = "asc") = {
    val metodeValid = List("sorted", "sortBy", "sortWith", "rekursif")
    if (!metodeValid.contains(metode) || !List("asc", "desc").contains(urutan)) {
      cask.Response(
        ujson.Obj("error" -> "Metode atau urutan tidak valid."),
        statusCode = 400
      )
    } else {
      val naik = urutan == "asc"
      val rekursif = Sorting.insertionSort(Sorting.produk, naik)
      val hasil = if (metode == "rekursif") rekursif
                  else Sorting.sortingBawaan(Sorting.produk, metode, naik)
      val perbandingan = ujson.Obj.from(List("sorted", "sortBy", "sortWith").map(m =>
        m -> ujson.Bool(rekursif == Sorting.sortingBawaan(Sorting.produk, m, naik))
      ))

      cask.Response(ujson.Obj(
        "awal" -> Sorting.keJson(Sorting.produk),
        "hasil" -> Sorting.keJson(hasil),
        "perbandingan" -> perbandingan
      ))
    }
  }

  initialize()
}
