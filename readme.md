# JavaFX Image Processor – Laboratorium 6

Aplikacja desktopowa stworzona w ramach przedmiotu **Platformy programistyczne .NET i Java** na Politechnice Wrocławskiej.

## 🧠 Opis

Program umożliwia:
- Wczytanie obrazu (.jpg),
- Wykonanie operacji graficznych (negatyw, progowanie, konturowanie, skalowanie, obrót),
- Przetwarzanie obrazów z użyciem **wielowątkowości** (maks. 4 wątki),
- Logowanie akcji i błędów do pliku `logs.txt`,
- Zapis przetworzonego obrazu na dysku.

---

## 📂 Struktura projektu
```
src/
├── main/
│ ├── java/
│ │ └── app/
│ │ ├── HelloApplication.java # Główna klasa JavaFX
│ │ ├── HelloController.java # Kontroler GUI
│ │ ├── ImageProcessor.java # Logika przetwarzania obrazu
│ │ ├── LoggerService.java # Logowanie do pliku logs.txt
│ │ └── module-info.java # Deklaracje modułu JavaFX
│ └── resources/
│ ├── app/
│ │ └── hello-view.fxml # Widok GUI w FXML
│ └── assets/
│ │ └── PWR.jpeg # Logo PWR
```

---

## 🔍 Opis klas i metod

### `HelloApplication.java`
- Główna klasa startowa (`extends Application`)
- Inicjalizuje scenę i widok
- Rejestruje uruchomienie i zamknięcie aplikacji w logach

### `HelloController.java`
- Obsługuje przyciski i listę operacji w GUI
- Wywołuje metody z `ImageProcessor`
- Obsługuje wybór plików i komunikaty Toast/Alert

### `ImageProcessor.java`
- Zawiera logikę przetwarzania obrazu:
  - `generateNegative(Image)` – tworzy negatyw
  - `applyThreshold(Image, int)` – progowanie z zadanym progiem
  - `applyEdgeDetection(Image)` – detekcja konturów
  - `scaleImage(Image, width, height)` – skalowanie do nowych rozmiarów
  - `rotate(Image, clockwise)` – obrót obrazu w lewo lub w prawo
  - `setThreadCount(int)` – ustawia liczbę wątków (maks. 4)

### `LoggerService.java`
- Rejestruje akcje do `logs.txt`
- `log(String msg, String level)` – log podstawowy
- `log(String msg, String level, long ms)` – log z czasem wykonania
- `logError(String msg, Exception e)` – rejestruje wyjątki

### `hello-view.fxml`
- Graficzny interfejs aplikacji JavaFX (FXML)
- Zawiera listę rozwijaną, przyciski i podgląd obrazów

### `PWR.jpeg`
- Logo Politechniki Wrocławskiej, widoczne na górze aplikacji

---

## ✅ Funkcjonalności

- [x] Aplikacja okienkowa (JavaFX)
- [x] Wczytywanie pliku .jpg
- [x] Operacje graficzne (negatyw, próg, kontur)
- [x] Skalowanie i obrót obrazu
- [x] Równoległość przetwarzania (do 4 wątków)
- [x] Logowanie akcji do pliku
- [x] Komunikaty błędów i sukcesów (Toast / Alert)
- [x] Rejestracja startu i zamknięcia aplikacji

---

## 🛠️ Wymagania techniczne

- Java 23
- JavaFX 17–20
- Maven
- IDE: IntelliJ IDEA

---

