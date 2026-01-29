# TicTacToe

A feature-rich Android Tic Tac Toe game with multiple game modes including offline play, AI opponent, and online multiplayer support.

## Features

- **Offline Mode**: Play against another player on the same device
- **AI Opponent**: Challenge the bot with two difficulty levels (Easy and Hard)
- **Online Multiplayer**: Play against opponents online using Firebase Realtime Database
- **Celebration Effects**: Enjoy confetti animations when you win
- **Intuitive UI**: Clean and user-friendly interface built with Android Material Design
- **Turn Indicators**: Real-time display of whose turn it is

## Getting Started

### Prerequisites

- Android Studio (latest version recommended)
- Android SDK 28 or higher
- Android Device or Emulator running Android 9.0+
- Firebase account (for online multiplayer features)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/tictactoe-app.git
   cd tictactoe-app
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the `tictactoe-app` folder

3. **Configure Firebase** (for online multiplayer)
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project or use an existing one
   - Add an Android app to your project
   - Download the `google-services.json` file
   - Place it in the `app/` directory

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run 'app'" or press `Shift + F10`

## Usage

### Starting a Game

1. **Launch the app** - You'll see the main menu with game mode options
2. **Choose your game mode**:
   - **Play Offline**: Two-player game on the same device
   - **Play Online**: Connect with another player online
3. **Optional Bot Settings**: Toggle the "Play with Bot" switch to play against AI
   - Select difficulty level:
     - **Easy**: Bot makes random moves
     - **Hard**: Bot uses optimal strategy

### Game Rules

- Players take turns marking spaces with X or O
- First player to get three marks in a row (horizontal, vertical, or diagonal) wins
- If all 9 spaces are filled without a winner, the game is a draw
- Tap "Rewanż?" (Rematch) to play another round

## Project Structure

```
tictactoe-app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/tictactoe/
│   │   │   │   ├── MainActivity.java          # Main menu and game mode selection
│   │   │   │   ├── OfflineActivity.java       # Two-player offline mode
│   │   │   │   ├── BotActivity.java           # AI opponent gameplay
│   │   │   │   ├── OnlineActivity.java        # Online multiplayer mode
│   │   │   │   └── Lobby.java                 # Online game lobby management
│   │   │   ├── res/
│   │   │   │   ├── layout/                    # XML layout files for each activity
│   │   │   │   ├── values/                    # Resources (colors, strings, themes)
│   │   │   │   └── drawable/                  # App icons and drawables
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                              # Unit tests
│   │   └── androidTest/                       # Instrumented tests
│   ├── build.gradle                           # App-level build configuration
│   └── google-services.json                   # Firebase configuration
├── build.gradle                               # Project-level build configuration
├── gradle.properties                          # Gradle settings
└── settings.gradle                            # Gradle module settings
```

## Dependencies

- **AndroidX**: Core Android libraries (`appcompat`, `constraintlayout`)
- **Material Design**: `com.google.android.material:material`
- **Firebase**: Real-time database for online multiplayer (`firebase-database`)
- **Konfetti**: Celebration effects library (`konfetti-xml`)

## Architecture

The app follows the MVC (Model-View-Controller) pattern:

- **Views**: Activity classes handle UI and user interactions
- **Logic**: Game logic and AI algorithm implementation in each activity
- **Data**: Firebase Realtime Database for online game synchronization

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Java |
| Framework | Android (API 28+) |
| Database | Firebase Realtime Database |
| Build System | Gradle |
| Minimum SDK | 28 |
| Target SDK | 33 |

## Building from Source

```bash
# Build the APK
./gradlew build

# Build and install on connected device
./gradlew installDebug

# Run tests
./gradlew test
```

---

**Happy Gaming!** 🎮 Enjoy playing TicTacToe!
