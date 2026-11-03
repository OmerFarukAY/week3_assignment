# week3_assignment - Akıllı Ev (IoT) Kontrol Merkezi

**BIL 403 - Mobil Uygulama Geliştirme**  
**Hafta 3 Ödevi: Kurumsal MVVM, Jetpack Compose, Coroutines ve StateFlow Mimarisi**

---

## 📌 Proje Genel Bakış ve Konsept

Bu proje, evdeki akıllı cihazların durumunu (açık/kapalı), odalarını (Salon, Mutfak, Bahçe...) ve anlık elektrik tüketimlerini (Watt) reaktif ve asenkron olarak yöneten bir **"Akıllı Ev (IoT) Kontrol Merkezi"** uygulamasıdır.

Tüm isterler ve kurallar ders mimari standartlarına %100 uygun olarak geliştirilmiştir.

---

## 🏛️ Mimari Kurallar ve Uygulama Prensipleri

### 1. Kural 1 (Katman Ayrımı): %100 Stateless Compose UI
* **Tasarım Katmanı (Compose UI)** içerisinde hiçbir filtreleme (`.filter`), liste dönüştürme veya matematiksel hesaplama (toplama/çıkarma) kodu yer almaz.
* Arayüz tamamen **durumsuz (stateless)** tasarlanmıştır.
* Tüm filtrelemeler, oda süzmeleri, aktif/çevrimiçi durum kontrolleri ve canlı Watt toplamları **`SmartHomeViewModel`** katmanında hesaplanır.
* Hazırlanan veri modeli (`SmartHomeUiState`), Compose katmanına `StateFlow` aracılığıyla reaktif aktarılır.

### 2. Kural 2 (Performans & 120 FPS Akıcılık): Doğru Thread Yönetimi
* Butonlara basıldığında veya arama yapıldığında arayüzde donma ve takılmaları önlemek için;
  * Tüm hesaplama, süzme ve toplama operasyonları **`Dispatchers.Default`** arka plan worker thread havuzunda yürütülür (`flowOn(Dispatchers.Default)`).
  * `LazyColumn` içinde `key = { it.id }` kullanılarak yalnızca durumu değişen öğelerin recomposition'a girmesi sağlanmıştır.
  * 120 Hz / 120 FPS ekranlarda sıfır takılma ve tam akıcılık hedeflenmiştir.

---

## ⚡ Ham Veri Seti (Doğrulama Tablosu)

| ID | Cihaz Adı | Kategori | Oda | Online | Aktif | Tüketim (W) | Başlangıçta Sayaca Dahil mi? |
|---|---|---|---|---|---|---|---|
| 1 | Köşe Lambası | LIGHT | Salon | Evet | Evet | 15.0 W | ✅ Evet |
| 2 | Tavan Avizesi | LIGHT | Salon | Evet | Hayır | 60.0 W | ❌ Hayır (Kapalı) |
| 3 | Bahçe Kamerası | CAMERA | Bahçe | Evet | Evet | 8.5 W | ✅ Evet |
| 4 | Giriş Kamerası | CAMERA | Antre | Evet | Evet | 8.5 W | ✅ Evet |
| 5 | Merkezi Termostat | THERMOSTAT | Salon | Evet | Evet | 12.0 W | ✅ Evet |
| 6 | Yatak Odası Kliması | THERMOSTAT | Yatak Odası | Evet | Hayır | 1200.0 W | ❌ Hayır (Kapalı) |
| 7 | Mutfak Spotları | LIGHT | Mutfak | Evet | Hayır | 45.0 W | ❌ Hayır (Kapalı) |
| 8 | Roborock S7 | VACUUM | Koridor | Evet | Evet | 50.0 W | ✅ Evet |
| 9 | Balkon Aydınlatması | LIGHT | Balkon | **Hayır** | Hayır | 25.0 W | ❌ Hayır (Offline) |
| 10 | Çalışma Odası Lambası | LIGHT | Ofis | Evet | Evet | 12.0 W | ✅ Evet |

> **Başlangıç Canlı Enerji Toplamı:**  
> `15.0 + 8.5 + 8.5 + 12.0 + 50.0 + 12.0 = 106.0 Watt` (6 Aktif Cihaz)

---

## 🧪 Birim Testleri (Unit Tests)

Uygulamanın reaktif hesaplama doğruluğu `SmartHomeViewModelTest` sınıfında kapsamlı testlerle doğrulanmıştır:
- `initialState_calculatesCorrectTotalWattForOnlyActiveAndOnlineDevices`: Başlangıçta yalnızca online ve aktif olan cihazların tüketiminin 106.0 W olduğunu doğrular.
- `toggleDevice_updatesDeviceStateAndEnergyReactively`: Klima açıldığında sayacın anında `1306.0 W` olduğunu, kapatıldığında reaktif olarak `106.0 W` değerine döndüğünü doğrular.
- `toggleOfflineDevice_doesNotChangeStateNorEnergy`: Çevrimdışı (Balkon Aydınlatması) cihazın açılamayacağını ve sayacı etkilemediğini doğrular.
- `roomFilter_filtersCorrectDevices`: "Salon" seçildiğinde yalnızca salona ait 3 cihazın listelendiğini doğrular.
- `searchQuery_filtersCorrectly`: "Kamera" aramasında yalnızca 2 kameranın süzüldüğünü doğrular.

```bash
./gradlew test
# BUILD SUCCESSFUL
```

---

## 📱 Ekran Görüntüleri

Tüm ekran görüntüleri `screenshots/` klasöründe yer almaktadır:
1. `screenshot_initial.png`: 106.0 W başlangıç tüketimi ve 10 cihaz listesi.
2. `screenshot_toggled.png`: Köşe Lambası kapatıldığında sayacın reaktif 91.0 W'a düşmesi.
3. `screenshot_filter_bahce_active.png`: "Bahçe" odası filtresi.
4. `screenshot_search_kamera.png`: Arama çubuğu filtreleme testi.

---

## 🛠️ Proje Kurulumu ve Çalıştırma

1. Depoyu klonlayın:
   ```bash
   git clone <REPO_URL>
   cd week3_assignment
   ```
2. Android Studio ile açın ve Gradle Sync işlemini tamamlayın.
3. Testleri çalıştırmak için:
   ```bash
   ./gradlew test
   ```
4. Uygulamayı cihazda/emülatörde çalıştırmak için:
   ```bash
   ./gradlew installDebug
   ```
