# 🌙 Mood Tracker App

Aplikasi Android untuk melacak suasana hati harian, membaca quotes positif, menyimpan jurnal singkat, dan memantau perkembangan emosi melalui mood board.

---

## 📌 Fitur Utama

### 🗒️ 1. Diary / Dashboard

* Ringkasan suasana hati harian
* Kalender
* Responsif untuk portrait & landscape

### 💬 2. Quotes

* Quotes motivasi harian
* Bisa dicopy & dibagikan
* Mode portrait & landscape

### ➕ 3. Add Mood

* Pilih mood (happy, sad, angry, neutral, sleep)
* Tambahkan catatan/jurnal
* Tersimpan di database lokal

### 📊 4. Mood Board

* Chart mood jangka panjang
* Mendukung landscape

### ⚙️ 5. Settings

* Bahasa (ID / EN)
* Reminder mood
* Preferensi tampilan

---

## 🖼️ Screenshot Aplikasi

---

## 📍 Dashboard

|                                                         |                                                                              |
| ------------------------------------------------------- | ---------------------------------------------------------------------------- |
| ![dashboard](./app/src/main/res/drawable/dashboard.png) | ![dashboard\_landscape](./app/src/main/res/drawable/dashboard_landscape.png) |

---

## 💬 Quotes

|                                                                  |                                                                        |                                                                |
| ---------------------------------------------------------------- | ---------------------------------------------------------------------- | -------------------------------------------------------------- |
| ![page\_quotes](./app/src/main/res/drawable/page_quotes.png)     | ![landscape\_quotes](./app/src/main/res/drawable/landscape_quotes.png) | ![share\_quotes](./app/src/main/res/drawable/share_quotes.png) |
| ![search\_quotes](./app/src/main/res/drawable/search_quotes.png) | ![copy\_quotes](./app/src/main/res/drawable/copy_quotes.png) |                                                                |

--- 

## ➕ Add Mood

|                                                        |
| ------------------------------------------------------ |
| ![add\_mood](./app/src/main/res/drawable/add_mood.png) |

---

## 📊 Mood Board

|                                                            |                                                                              |
| ---------------------------------------------------------- | ---------------------------------------------------------------------------- |
| ![mood\_chart](./app/src/main/res/drawable/mood_chart.png) | ![landscape\_moodchart](./app/src/main/res/drawable/landscape_moodchart.png) |

---

## 😊 Kalender Berdasarkan Mood

|                                                                  |                                                                |                                                            |
| ---------------------------------------------------------------- | -------------------------------------------------------------- | ---------------------------------------------------------- |
| ![moodchart\_isi](./app/src/main/res/drawable/moodchart_isi.png) | ![mood\_happy](./app/src/main/res/drawable/mood_happy.png)     | ![mood\_sad](./app/src/main/res/drawable/mood_sad.png)     |
| ![mood\_angry](./app/src/main/res/drawable/mood_angry.png)       | ![mood\_neutral](./app/src/main/res/drawable/mood_neutral.png) | ![mood\_sleep](./app/src/main/res/drawable/mood_sleep.png) |

---

## ⚙️ Settings

|                                                                  |                                                                            |                                                       |
| ---------------------------------------------------------------- | -------------------------------------------------------------------------- | ----------------------------------------------------- |
| ![settings](./app/src/main/res/drawable/settings.png)            | ![landscape\_settings](./app/src/main/res/drawable/landscape_settings.png) | ![language](./app/src/main/res/drawable/language.png) |
| ![reminder\_time](./app/src/main/res/drawable/reminder_time.png) | ![notifikasi\_mood](./app/src/main/res/drawable/notifikasi_mood.png)       |                                                       |

---

## 🌐 Dashboard Berdasarkan Bahasa

|                                                                |                                                          |
| -------------------------------------------------------------- | -------------------------------------------------------- |
| ![dash\_english](./app/src/main/res/drawable/dash_english.png) | ![dash\_indo](./app/src/main/res/drawable/dash_indo.png) |

---

## 🧭 Navigation Flow (Bottom Navigation)

```
[ Diary ]      → Dashboard utama
[ Quotes ]     → Kumpulan motivasi
[ Add Mood ]   → Input suasana hati
[ Mood Board ] → Riwayat & grafik mood
[ Settings ]   → Bahasa, reminder, preferensi
```

Menggunakan **Jetpack Navigation Compose**, setiap tab memakai `Composable` screen masing-masing.

---

## 🛠️ Teknologi yang Digunakan

* Kotlin
* Jetpack Compose
* Material 3
* Room
* Navigation Compose
* ViewModel + State Management

---

## 🚀 Cara Menjalankan

1. Clone repository:

   ```
   git clone https://github.com/tiarasyahrani/moodtrackerapp.git
   ```

2. Buka di **Android Studio**

3. Sync Gradle

4. Run di emulator / device

---

## 🧑‍💻 Developer

**TIARA SYAHRANI**
