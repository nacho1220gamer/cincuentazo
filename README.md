# 🃏 Cincuentazo

**Cincuentazo** is a digital recreation of the traditional Colombian card game *Cincuentazo*, developed using **JavaFX** and **Maven**.  
This project was created as part of a university programming course and focuses on implementing **object-oriented principles**, **design patterns**, and **graphical user interfaces** in Java.

---

## 🎯 Introduction

The game of *Cincuentazo* is a beloved Colombian card game where players compete to reach the target score through strategy, risk-taking, and a bit of luck.  
This digital version was built to simulate the same excitement and interaction, offering an intuitive and polished graphical interface where users can enjoy the experience virtually.

The project demonstrates good practices in **software design**, including modularization, use of interfaces, exception handling, and separation of logic using the **MVC architecture**.

---

## 🧩 Objectives

- Develop a playable and visually appealing version of *Cincuentazo* using JavaFX.  
- Apply **object-oriented programming** concepts such as abstraction, inheritance, and polymorphism.  
- Implement a **robust architecture** that separates data, logic, and presentation layers.  
- Create a **user-friendly experience** through FXML-based views and CSS styling.  
- Handle possible user or system errors using **custom exceptions** and fail-safe mechanisms.

---

## 🎮 Features

- Dynamic JavaFX interface with smooth transitions between stages:
  - **Welcome Screen** — choose the number of players and start a match.  
  - **Game Board** — main gameplay area where rounds are played.  
  - **Help Screen** — explains the game rules and mechanics.  
  - **Winner Screen** — displays the results and declares the champion.  
- Support for multiple players (1 to 3).  
- Custom card and deck system designed through composition and interfaces.  
- Implementation of **exception classes** like `InvalidMoveException` and `EmptyDeckException`.  
- Visual assets, sound design potential, and flexible scalability for future versions.  

---

## 🧱 Project Structure

```
cincuentazo-control/
│
├── src/main/java/cincuentazo/
│   ├── controller/          # JavaFX controllers for each stage
│   ├── model/               # Core game logic (cards, deck, players)
│   ├── view/                # Stage management classes for GUI
│   └── Main.java            # Entry point of the application
│
├── src/main/resources/com/example/miniproyecto3/
│   ├── css/                 # Stylesheets for JavaFX UI
│   ├── fxml/                # FXML view definitions
│   └── images/              # Game assets (buttons, players, etc.)
│
├── pom.xml                  # Maven configuration file
└── module-info.java         # Java module descriptor
```

---

## ⚙️ Technologies Used

- **Java 17+**  
- **JavaFX** for the user interface  
- **FXML** for UI layout definitions  
- **CSS** for styling and visual customization  
- **Maven** for dependency management and project automation  
- **IntelliJ IDEA** / **VS Code** (recommended IDEs)

---

## 🚀 How to Run the Project

### 🧩 Requirements

Before running the project, make sure you have the following installed:

- **Java JDK 17** or higher  
- **Apache Maven 3.8+**  
- A JavaFX-compatible IDE such as IntelliJ IDEA, Eclipse, or VS Code  

### ▶️ Steps to Run

1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/your-username/cincuentazo-control.git
   cd cincuentazo-control
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

3. Run the project with:
   ```bash
   mvn javafx:run
   ```

If JavaFX is not included in your environment, configure the SDK path in your IDE’s **Project Structure → Libraries → JavaFX** section.

---

## 🧠 Architecture and Design Patterns

The project is structured following the **Model–View–Controller (MVC)** pattern:

| Layer | Description |
|--------|-------------|
| **Model** | Contains core game logic (cards, players, deck, and rules). |
| **View** | Defines the visual layout through FXML and handles the presentation layer. |
| **Controller** | Manages user input, updates the model, and refreshes the view accordingly. |

Other applied principles and design choices include:

- **Encapsulation** to maintain clean access to class attributes.  
- **Interfaces and Adapters** (`ICard`, `IDeck`, `IPlayer`) to improve flexibility.  
- **Exception Handling** for invalid moves, empty decks, or unexpected conditions.  
- **Separation of Concerns** for modular, maintainable code.  

---

## 🧩 Key Classes Overview

| Package | Class | Description |
|----------|--------|-------------|
| `controller` | `CincuentazoGameController`, `CincuentazoHelpController`, etc. | Handle user interaction and transitions between game stages. |
| `model.card` | `Card`, `ICard` | Represent card behavior and attributes. |
| `model.deck` | `Deck`, `DeckAdapter`, `IDeck` | Manage the deck’s lifecycle, drawing, and shuffling. |
| `model.player` | `Player`, `IPlayer`, `PlayerAdapter` | Define player logic and interaction with cards. |
| `model.game` | `Game` | Main game logic and control flow. |
| `view` | `CincuentazoGameStage`, `CincuentazoWelcomeStage`, etc. | Display each visual window using JavaFX stages. |

---

## 🧩 Gameplay Overview

Players draw and play cards in turns, following the standard *Cincuentazo* rules.  
The system manages the deck, validates moves, and determines the winner automatically.  
Each round progresses visually through the JavaFX interface, updating the display and state dynamically.

---

## 💡 Future Improvements

- Add multiplayer mode using **network sockets** or **online connectivity**.  
- Include animations and sound effects for more immersive gameplay.  
- Implement persistent scores and player profiles.  
- Expand rule customization and difficulty settings.  

---

## 🧪 Testing

Unit tests can be added using **JUnit 5** for model validation and logic verification.  
Each package can be extended with test classes to ensure correct behavior of the core components.

---

## 🪪 License

This project is released under the **MIT License** — you are free to use, modify, and distribute it for educational or personal purposes.

---

## 👥 Authors

**Samuel Saldaña Giraldo**  
**Ignacio Henao Henao**  
**Juan Fernando Marmolejo Valencia**  

Developed as part of a university project in Object-Oriented Programming, focusing on interactive software design and JavaFX application development.

---
