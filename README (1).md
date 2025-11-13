# 🃏 Cincuentazo

**Cincuentazo** is a JavaFX-based digital card game that recreates the excitement of the classic Colombian game *Cincuentazo*.  
It allows one or more players to compete by drawing and playing cards strategically until one achieves victory.

---

## 🎮 Features

- Modern JavaFX interface with multiple game stages:
  - Welcome screen  
  - Game board  
  - Help section  
  - Winner screen
- Support for multiple players  
- Custom deck and card management system  
- Robust exception handling for invalid moves and empty decks  
- Modular structure following the MVC (Model–View–Controller) pattern  

---

## 🧱 Project Structure

```
cincuentazo-control/
│
├── src/main/java/cincuentazo/
│   ├── controller/          # JavaFX controllers for each stage
│   ├── model/               # Core game logic (cards, deck, players)
│   ├── view/                # JavaFX stage management classes
│   └── Main.java            # Entry point of the application
│
├── src/main/resources/com/example/miniproyecto3/
│   ├── css/                 # Stylesheets
│   ├── fxml/                # FXML layouts
│   └── images/              # Game assets
│
├── pom.xml                  # Maven configuration file
└── module-info.java         # Java module descriptor
```

---

## ⚙️ Technologies Used

- **Java 17+**  
- **JavaFX**  
- **Maven**  
- **FXML**  
- **CSS**

---

## 🚀 How to Run

### Prerequisites
- Install **Java JDK 17** or higher  
- Install **Apache Maven**  
- Ensure **JavaFX SDK** is available (if not included in your IDE)

### Steps
1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/cincuentazo-control.git
   cd cincuentazo-control
   ```
2. Build the project using Maven:
   ```bash
   mvn clean install
   ```
3. Run the game:
   ```bash
   mvn javafx:run
   ```

---

## 🧩 Key Classes

| Package | Description |
|----------|-------------|
| `controller` | Manages user interaction and game flow |
| `model.card` | Defines card objects and interfaces |
| `model.deck` | Handles deck creation, shuffling, and drawing |
| `model.player` | Defines player logic and behavior |
| `view` | Controls stage transitions and window layouts |

---

## 🧠 Design Pattern

The project follows an **MVC architecture**, ensuring a clear separation of concerns:
- **Model** → Game logic (cards, players, deck)  
- **View** → FXML interfaces and visuals  
- **Controller** → User interactions and event handling  

---

## 🪪 License

This project is released under the **MIT License** — feel free to modify and distribute it.

---

## 👥 Authors

Developed by **Samuel Saldaña** and collaborators as part of a university programming project.

---
