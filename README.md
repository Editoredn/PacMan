# PacMan (Pulse Edition)
>### Author: Sourav Kumar Verma

--- 

PacPulse(short form) is a modern, Java-based reimagination of the classic Pacman arcade game. Built with Java Swing, it features 37 levels with unique themed music, a dynamic landing page, and enhanced gameplay mechanics. Navigate mazes, eat pellets, avoid ghosts, and enjoy a nostalgic yet fresh arcade experience.

---

> ### Features

>- 37 Levels: Progress through 37 challenging levels, each with distinct maze layouts.
>- Custom Tile Mechanics: ice sliding, sacred zones, phantom walls, lava, shock traps and many more
>- Themed Music: Enjoy unique background music for level groups.
>- Dark, hand-crafted aesthetic with pixel art tiles and background
>- Landing Page: A sleek start screen with a dynamic interface and a cube to start the game and looping opening music.
>- Responsive Gameplay: Smooth controls for Pacman movement and pellet collection.
>- Custom Audio: Sound effects for eating pellets, game over, and more, with seamless theme transitions.


--- 

> ### Chapters & Lore

> _Explore 50+ realms through time, memory. Each chapter introduces new mechanics, themes, and emotional beats_

---

>#### Chapter 1 – Lightkeeper’s Grove *(Levels 1–5)*  
**Theme:** Lush village forest, soft golden glow, beginner’s warmth  
*You awaken in a fading forest where sunlight still remembers your name.*

---

>#### Chapter 2 – Dunes of the Hollow Sun *(Levels 6–10)*  
**Theme:** Desert ruins, lost temples, flickering mirages  
*Shifting sands obscure paths, and ancient buttons control buried gates.*

---

> #### Chapter 3 – Abyssmere Wellspring *(Levels 11–14)*  
**Theme:** Deep underwater glow, kelp forests, bioluminescent caves  
*Navigate slime trails and phantom zones in the drowned echoes of time.*

---

>#### Chapter 4 – Coralheart Sanctuary *(Level 15 – Peaceful)*  
**Theme:** Dreamlike underwater haven  
*A memory preserved in coral. No enemies. Just wonder and peace.*

---

>#### Chapter 5 – Frostveil Plateau *(Levels 16–20)*  
**Theme:** Icy wastelands, solemn silence, fallen monuments  
*Slippery ice tiles and sacred slow zones test your control and patience.*

---

>#### Chapter 6 – Ashen Ember Ruins *(Levels 21–24)*  
**Theme:** Burnt cities, flickering red, broken memories  
*Lava tiles burn, ghosts are relentless, and the path is forged in fire.*

---

>#### Chapter 7 – Rooftop Serenity *(Level 25 – Peaceful)*  
**Theme:** Lo-fi rooftop at twilight  
*Above the chaos. No danger. Just soft beats and city lights.*

---

>#### Chapter 8 – Gears of Reverie *(Levels 26–30)*  
**Theme:** Twisted mechanical labyrinth, time distortion  
*Gears rotate, clocks tick, walls move. Time is no longer your ally.*

---

>#### Chapter 9 – Ember Rush *(Levels 31–35)*  
**Theme:** Volcanic eruption, collapsing pathways, final escape  
*Lava floods each corner. Ghosts vanish. You must run… now.*

---

>#### Final Chapter – The Great Escape *(Levels 36–37)*  
**Theme:** Open skies, tranquil world  
*You survived. The Ember is restored. Silence, at last.*

---

>### Built With

> Java 17
> Java Swing for GUI
> Java Sound API for audio (WAV support)
> Pure OOP + MVC-style design
---

>### Controls

| Action        | Key         |
|---------------|-------------|
| Move Up       | Arrow ↑     |
| Move Down     | Arrow ↓     |
| Move Left     | Arrow ←     |
| Move Right    | Arrow →     |
| Start Game    | Mouse click on "CUBE" |
| Exit          | Window close (X) |

---
>### Sound Design

>Background music per level
>Landing screen music
>Sound effects: eat, die, shock, gates, lava
>All managed using Java `Clip` API

--- 
>### Installation





>Clone or Download:

- Clone this repository or download the project files to your local machine.

- git clone <repository-url>

- Ensure Java is Installed:

- Requires Java 8 or higher (JDK). Verify with:

- java -version


> Project Structure:


- Place the project in D:\java\PacMan\pacmanOriginalSourav (or your preferred directory).

- Ensure the sounds folder is in the project root with the following structure:

- sounds/
├── effects/
│   ├── start.wav
│   ├── eat.wav
│   ├── die.wav
│   ├── eatOriginal1.wav
│   ├── electricshock1.wav
│   ├── LandingPage.wav
│   ├── gameover.wav
│   ├── electricshock.wav
│   ├── theme1music.wav
│   ├── theme2music.wav
│   ├── theme3music.wav
│   ├── theme4music.wav
│   ├── theme5music.wav
│   ├── theme6music.wav
│   ├── theme7music.wav
│   ├── theme8music.wav
│   ├── theme9music.wav
├── theme1/
│   ├── theme music.wav
├── theme2/
│   ├── theme music.wav
├── ...
├── theme9/
│   ├── theme music.wav
├── images/
│   ├── landing_background.png



> Compile the Project:

- Navigate to the project directory:

- cd D:\java\PacMan\pacmanOriginalSourav

- Compile all Java files in the src package:

- javac src/*.java



>Run the Game:

- java src.App



> Gameplay:

- Click on the Cube to start the game

- Use arrow keys to move Pacman through the maze, eating pellets and avoiding ghosts.

- Each level completion (all pellets eaten) advances to the next level with new themed music.

- The game ends after level 37 or on game over, with a "gameover" sound.

>Youtube video 
++ 

> Controls:

- Arrow Keys: Move Pacman (up, down, left, right).

- No additional controls are required for the landing page.

> File Structure

- src/

  - App.java: Entry point, initializes the game.

  - PacMan.java: Core game logic, including landing page and gameplay.

  - SoundManager.java: Handles audio (background music and effects).

  - MapLoader.java: Loads maze data for 37 levels.
 
  - sounds/

     - Contains audio files for effects and themed music (PCM-encoded, 16-bit, 44100 Hz WAV files).



> Dependencies

- Java: JDK 8 or higher.

- Audio Files: Ensure all .wav files are in the sounds/ directory with the correct paths as specified in SoundManager.java.

> Contributing

Contributions are welcome! To contribute:

- Fork the repository.

- Create a feature branch (git checkout -b feature-name).

- Commit changes (git commit -m "Add feature").

- Push to the branch (git push origin feature-name).

- Open a pull request.

- Please ensure code follows the existing style and includes comments for clarity.

 
---
>### License

_This project is unlicensed. Contact the author for permission to use or distribute._

Contact - verma123sourav@cic.du.ac.in



For questions or suggestions, contact Sourav Kumar Verma

>Email - verma123sourav@cic.du.ac.in
>Linkedin - https://www.linkedin.com/in/sourav-kumar-verma-2405932ba
> Github - https://github.com/Editoredn
