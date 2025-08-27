/*
 * PacMan Game
 * A classic arcade game where the player controls PacMan to eat pellets and avoid ghosts.
 * This code implements the game logic, rendering, and user input handling.
 * It includes features like different themes, ghost behaviors, teleportation, and special tiles.
 * This code is part of a larger project and is designed to be run within a Java Swing application.
 * It uses classes like JPanel for the game board, ActionListener for game actions, and KeyListener for user input.
 * The game features multiple levels with different themes, each with unique challenges and ghost behaviors.
 * It also includes sound management for effects and background music.
 * The game is designed to be modular, allowing for easy expansion and customization of levels and features.
 * This code is intended for educational purposes and can be modified to create new levels, themes, and game mechanics.
 * This code is free to use and modify, but please give credit to the original author if you use it in your projects.
 * Feel free to reach out for any questions or suggestions regarding the code.
 * 
 * Author: Sourav Kumar Verma
 * Date: 2025-07-03
 * License: CIC , UNIVERSITY OF DELHI
 * You are free to use, modify, and distribute this code as long as you include this notice.
 * 
 */

package src;

import src.SoundManager; 
import src.MapLoader; 
import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.*;
import java.util.Iterator;

public class PacMan extends JPanel implements ActionListener, KeyListener{ 

    int debugMapLevel = 1; 

    public SoundManager soundManager;


    boolean controlsReversed = false;
    boolean pacmanIsOnReverse = false;


    boolean lavaEnabled = true;
    boolean pacmanOnLavaButton = false;
    boolean ghostOnLavaButton = false;


    boolean ghostLavaKillTemporary = false;
    boolean ghostLavaKillPermanent = false;


    boolean gate1Open = true;
    boolean gate2Open = true;
    boolean pacmanOnButton1 = false;
    boolean pacmanOnButton2 = false;
    boolean ghostOnButton1 = false;
    boolean ghostOnButton2 = false;


    boolean isInSpiderNet = false;
    boolean isInBushes = false;
    boolean isInSacredZone = false;


    Map<Block, Long> ghostStunEndTimes = new HashMap<>();
    Map<Block, Long> pacmanStunEndTimes = new HashMap<>();


    private boolean isTeleporting = false;
    private long lastTeleportTime = 0;
    private static final long TELEPORT_COOLDOWN = 500;


    boolean hitWall = false;


    class Block {
        int x; 
        int y; 
        int width;
        int height;
        Image image;
        int phaseTilesRemaining = 0; 
        char direction = 'U'; 
        int velocityX = 0; 
        int velocityY = 0; 
        int startX;
        int startY;


        Block(Image image, int x, int y, int width, int height) {

            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;

        }

        void updateDirection(char direction) { 

            char prevDirection = this.direction;
            this.direction = direction; 
            
            updateVelocity(); 

            this.x += this.velocityX;
            this.y += this.velocityY;

            for (Block wall : walls) {
                if (collision(this, wall))
                {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity() {

            int speed;
            speed = tileSize / 4;
            if (this.direction == 'U') 
            {
                this.velocityX = 0;
                this.velocityY = -speed;

            }
            else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = speed;
            }
            else if (this.direction == 'L') {
                this.velocityX = -speed;
                this.velocityY = 0;
            }
            else if (this.direction == 'R') {
                this.velocityX = speed;
                this.velocityY = 0;
            }
        }

        void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }

    }


    private int rowCount = 23;
    private int colCount = 21;
    private int tileSize = 32;
    private int widthBoard = colCount * tileSize;
    private int heightBoard = rowCount * tileSize;
    private Image woodWallImage; 
    private Image redGhost1Image;
    private Image orangeGhost1Image;
    private Image bushes;
    private Image desertWallImage;
    private Image redGhost2Image;
    private Image orangeGhost2Image;
    private Image pinkGhostImage;
    private Image speedImage;
    private Image warpEntranceImage;
    private Image warpExitImage;
    private Image seaWallImage;
    private Image redGhost3Image;
    private Image orangeGhost3Image;
    private Image pinkGhost2Image;
    private Image blueGhostImage;
    private Image teleportImage;
    private Image electric;
    private Image bonus1Wall;
    private Image frostWallImage;
    private Image redGhost4Image;
    private Image orangeGhost4Image;
    private Image pinkGhost3Image;
    private Image blueGhost2Image;
    private Image iceImage;
    private Image button1Image;
    private Image gate1Image;
    private Image lavaWallImage;
    private Image redGhost5Image;
    private Image orangeGhost5Image;
    private Image pinkGhost4Image;
    private Image blueGhost3Image;
    private Image lavaImage;
    private Image button2Image;
    private Image gate2Image;
    private Image teleport2Image;
    private Image bonus2Wall;
    private Image machineWallImage;
    private Image redGhost6Image;
    private Image orangeGhost6Image;
    private Image pinkGhost5Image;
    private Image blueGhost4Image;
    private Image phantomImage;
    private Image reverseImage;
    private Image moltenSphireWallImage;
    private Image redGhost7Image;
    private Image orangeGhost7Image;
    private Image pinkGhost6Image;
    private Image blueGhost5Image;
    private Image spiderWeb;
    private Image sacredZone;
    private Image slime;
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;


    HashSet<Block> foods; 
    HashSet<Block> spaces1; 
    HashSet<Block> exitSpaces1; 
    HashSet<Block> ghosts; 
    Block pacman;
    HashSet<Block> blackSpace;
    HashSet<Block> walls;
    HashSet<Block> bush;
    HashSet<Block> spaces2; 
    HashSet<Block> exitSpaces2;
    HashSet<Block> iceBlocks; 
    HashSet<Block> teleportPads; 
    HashSet<Block> speedZones; 
    HashSet<Block> phantomZones; 
    HashSet<Block> reverseControls;
    HashSet<Block> warpEntrance; 
    HashSet<Block> warpExit; 
    HashSet<Block> Button1; 
    HashSet<Block> Gate1;
    HashSet<Block> Button2; 
    HashSet<Block> Gate2;
    HashSet<Block> spiderNets; 
    HashSet<Block> lavaTiles;
    HashSet<Block> sacredZones;
    HashSet<Block> stickyMist;
    HashSet<Block> slimePuddle;
    HashSet<Block> electricShocks;

    Timer gameLoop;

    char[] directions = { 'U', 'D', 'R', 'L' }; 
    Random random = new Random();

    int score = 0;
    int lives = 3; 
    boolean gameOver = false;

    Runnable[] levelLoaders;

    public PacMan(SoundManager soundManager) { 

        setPreferredSize(new Dimension(widthBoard, heightBoard)); 
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        woodWallImage = new ImageIcon(getClass().getResource("./woodTile.png")).getImage();
        bushes = new ImageIcon(getClass().getResource("./bush.png")).getImage();
        redGhost1Image = new ImageIcon(getClass().getResource("./redGhost1.png")).getImage();
        orangeGhost1Image = new ImageIcon(getClass().getResource("./orangeGhost1.png")).getImage();


        desertWallImage = new ImageIcon(getClass().getResource("./desertWallImage.png")).getImage();
        redGhost2Image = new ImageIcon(getClass().getResource("./redGhost2.png")).getImage();
        orangeGhost2Image = new ImageIcon(getClass().getResource("./orangeGhost2.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        speedImage = new ImageIcon(getClass().getResource("./speed.png")).getImage();
        warpEntranceImage = new ImageIcon(getClass().getResource("./wrapEntrance.png")).getImage();
        warpExitImage = new ImageIcon(getClass().getResource("./wrapExit.png")).getImage();


        seaWallImage = new ImageIcon(getClass().getResource("./seaWall.png")).getImage();
        redGhost3Image = new ImageIcon(getClass().getResource("./redGhost3.png")).getImage();
        orangeGhost3Image = new ImageIcon(getClass().getResource("./orangeGhost3.png")).getImage();
        pinkGhost2Image = new ImageIcon(getClass().getResource("./pinkGhost2.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        teleportImage = new ImageIcon(getClass().getResource("./teleport.png")).getImage();
        electric = new ImageIcon(getClass().getResource("./electric.png")).getImage();


        bonus1Wall = new ImageIcon(getClass().getResource("./bonus1Wall.png")).getImage();

        
        frostWallImage = new ImageIcon(getClass().getResource("./frostWallImage.png")).getImage();
        redGhost4Image = new ImageIcon(getClass().getResource("./redGhost4.png")).getImage();
        orangeGhost4Image = new ImageIcon(getClass().getResource("./orangeGhost4.png")).getImage();
        pinkGhost3Image = new ImageIcon(getClass().getResource("./pinkGhost3.png")).getImage();
        blueGhost2Image = new ImageIcon(getClass().getResource("./blueGhost2.png")).getImage();
        iceImage = new ImageIcon(getClass().getResource("./ice.png")).getImage();
        button1Image = new ImageIcon(getClass().getResource("./button1.png")).getImage();
        gate1Image = new ImageIcon(getClass().getResource("./gate1.png")).getImage();
        

        lavaWallImage = new ImageIcon(getClass().getResource("./lavaWallImage.png")).getImage();
        redGhost5Image = new ImageIcon(getClass().getResource("./redGhost5.png")).getImage();
        orangeGhost5Image = new ImageIcon(getClass().getResource("./orangeGhost5.png")).getImage();
        pinkGhost4Image = new ImageIcon(getClass().getResource("./pinkGhost4.png")).getImage();
        blueGhost3Image = new ImageIcon(getClass().getResource("./blueGhost3.png")).getImage();
        lavaImage = new ImageIcon(getClass().getResource("./lava.png")).getImage();
        button2Image = new ImageIcon(getClass().getResource("./button2.png")).getImage();


        teleport2Image = new ImageIcon(getClass().getResource("./teleport2.png")).getImage();


        bonus2Wall = new ImageIcon(getClass().getResource("./bonus2Wall.png")).getImage();


        machineWallImage = new ImageIcon(getClass().getResource("./machineWall.png")).getImage();
        redGhost6Image = new ImageIcon(getClass().getResource("./redGhost6.png")).getImage();
        orangeGhost6Image = new ImageIcon(getClass().getResource("./orangeGhost6.png")).getImage();
        pinkGhost5Image = new ImageIcon(getClass().getResource("./pinkGhost5.png")).getImage();
        blueGhost4Image = new ImageIcon(getClass().getResource("./blueGhost4.png")).getImage();
        reverseImage = new ImageIcon(getClass().getResource("./reverse.png")).getImage();


        moltenSphireWallImage = new ImageIcon(getClass().getResource("./moltenSphire.png")).getImage();
        redGhost7Image = new ImageIcon(getClass().getResource("./redGhost7.png")).getImage();
        orangeGhost7Image = new ImageIcon(getClass().getResource("./orangeGhost7.png")).getImage();
        pinkGhost6Image = new ImageIcon(getClass().getResource("./pinkGhost6.png")).getImage();
        blueGhost5Image = new ImageIcon(getClass().getResource("./blueGhost5.png")).getImage();
        spiderWeb = new ImageIcon(getClass().getResource("./spiderWeb.png")).getImage();
        sacredZone = new ImageIcon(getClass().getResource("./sacredZone.png")).getImage();
        slime = new ImageIcon(getClass().getResource("./slime.png")).getImage();


        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();


        this.soundManager = soundManager;
        this.debugMapLevel = 1;
        levelLoaders = new Runnable[MapLoader.getMapCount()];


        for (int i = 0; i < levelLoaders.length; i++) {
            final int index = i;
            levelLoaders[i] = new Runnable() {
                @Override
                public void run() {
                    String[] mapData = MapLoader.getMap(index);
                    loadMap(mapData); 
                }
            };
        }

        
        levelLoaders[debugMapLevel].run(); 
        for (Block ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)]; 
            ghost.updateDirection(newDirection); 
        }

        gameLoop = new Timer(50, this); 
        gameLoop.start(); 
        soundManager.loadThemeMusic();
    }

    public void loadMap(String[] mapData) {

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>(); 
        teleportPads = new HashSet<Block>(); 
        iceBlocks = new HashSet<Block>(); 
        speedZones = new HashSet<Block>(); 
        reverseControls = new HashSet<Block>(); 
        phantomZones = new HashSet<Block>(); 
        warpEntrance = new HashSet<Block>(); 
        warpExit = new HashSet<Block>(); 
        spaces1 = new HashSet<Block>(); 
        spaces2 = new HashSet<Block>(); 
        exitSpaces1 = new HashSet<Block>(); 
        exitSpaces2 = new HashSet<Block>(); 
        Button1 = new HashSet<Block>(); 
        Gate1 = new HashSet<Block>();  
        Button2 = new HashSet<Block>(); 
        Gate2 = new HashSet<Block>(); 
        spiderNets = new HashSet<Block>(); 
        lavaTiles = new HashSet<Block>(); 
        sacredZones = new HashSet<Block>(); 
        slimePuddle = new HashSet<Block>(); 
        electricShocks = new HashSet<Block>(); 
        bush = new HashSet<Block>(); 
        blackSpace = new HashSet<Block>();

        for (int row = 0; row < mapData.length; row++) {
            String line = mapData[row];
            for (int col = 0; col < line.length(); col++) {

                char tile = line.charAt(col);
                int x = col * tileSize;
                int y = row * tileSize;

                if (tile == 'X') { 

                    Block wood = new Block(woodWallImage, x, y, tileSize, tileSize);
                    walls.add(wood);

                }
                else if (tile == '<') {

                    Block bS = new Block(null, x, y, tileSize, tileSize);
                    blackSpace.add(bS);

                }
                else if (tile == '[') { 

                    Block wood = new Block(desertWallImage, x, y, tileSize, tileSize);
                    walls.add(wood);

                }
                else if (tile == ']') { 

                    Block wood = new Block(seaWallImage, x, y, tileSize, tileSize);
                    walls.add(wood);

                }
                else if (tile == '{') { 

                    Block wood = new Block(bonus1Wall, x, y, tileSize, tileSize);
                    walls.add(wood);

                }
                else if (tile == '}') { 

                    Block wood = new Block(frostWallImage, x, y, tileSize, tileSize);
                    walls.add(wood);

                }
                else if (tile == '`') {

                    Block wood = new Block(lavaWallImage, x, y, tileSize, tileSize);
                    walls.add(wood);

                }
                else if (tile == '/') { 

                    Block wood = new Block(bonus2Wall, x, y, tileSize, tileSize);
                    walls.add(wood);

                }
                else if (tile == '_') { 

                    Block wood = new Block(machineWallImage, x, y, tileSize, tileSize);
                    walls.add(wood);

                }
                else if (tile == '=') { 

                    Block wood = new Block(moltenSphireWallImage, x, y, tileSize, tileSize);
                    walls.add(wood);

                }
                else if (tile == ' ')
                {

                    Block food = new Block(null, x + 14, y + 14, 4, 4); 
                    foods.add(food);

                }
                else if (tile == '.') 
                {

                    Block space1 = new Block(null, x, y, tileSize, tileSize);
                    spaces1.add(space1);

                }
                else if (tile == 'E') {

                    Block exit1 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces1.add(exit1);

                }
                else if (tile == '-') 
                {

                    Block space2 = new Block(null, x, y, tileSize, tileSize);
                    spaces2.add(space2);

                }
                else if (tile == 'e') {

                    Block exit2 = new Block(null, x, y, tileSize, tileSize);
                    exitSpaces2.add(exit2);

                }
                else if (tile == 'a') 
                {

                    Block OrangeGhost1 = new Block(orangeGhost1Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost1);

                }
                else if (tile == 's') 
                {

                    Block OrangeGhost2 = new Block(orangeGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost2);
                
                }
                else if (tile == 'd') 
                {

                    Block OrangeGhost3 = new Block(orangeGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost3);

                }
                else if (tile == 'f')
                {

                    Block OrangeGhost4 = new Block(orangeGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost4);

                }
                else if (tile == 'z')
                {

                    Block OrangeGhost5 = new Block(orangeGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost5);

                }
                else if (tile == 'x') 
                {

                    Block OrangeGhost6 = new Block(orangeGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost6);

                }
                else if (tile == 'c') 
                {

                    Block OrangeGhost7 = new Block(orangeGhost7Image, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost7);

                }
                else if (tile == 'r')
                {

                    Block RedGhost1 = new Block(redGhost1Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost1);

                }
                else if (tile == 'g')
                {

                    Block RedGhost2 = new Block(redGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost2);

                }
                else if (tile == 'h') 
                {

                    Block RedGhost3 = new Block(redGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost3);

                }
                else if (tile == 'j') 
                {

                    Block RedGhost4 = new Block(redGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost4);

                }
                else if (tile == 'k')
                {

                    Block RedGhost5 = new Block(redGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost5);

                }
                else if (tile == 'y') 
                {

                    Block RedGhost6 = new Block(redGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost6);

                }
                else if (tile == 'u') 
                {

                    Block RedGhost7 = new Block(redGhost7Image, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost7);

                }
                else if (tile == 't') 
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);

                }
                else if (tile == 'b') 
                {

                    Block PinkGhost2 = new Block(pinkGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost2);

                }
                else if (tile == 'n') 
                {

                    Block PinkGhost3 = new Block(pinkGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost3);

                }
                else if (tile == 'm') 
                {

                    Block PinkGhost4 = new Block(pinkGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost4);

                }
                else if (tile == 'q')
                {

                    Block PinkGhost5 = new Block(pinkGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost5);

                }
                else if (tile == 'w') 
                {

                    Block PinkGhost6 = new Block(pinkGhost6Image, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost6);

                }
                else if (tile == 'Y') 
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);

                }
                else if (tile == 'U') 
                {

                    Block BlueGhost2 = new Block(blueGhost2Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost2);

                }
                else if (tile == 'O') 
                {

                    Block BlueGhost3 = new Block(blueGhost3Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost3);

                }
                else if (tile == 'K')
                {

                    Block BlueGhost4 = new Block(blueGhost4Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost4);

                }
                else if (tile == 'J')
                {

                    Block BlueGhost5 = new Block(blueGhost5Image, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost5);

                }
                else if (tile == 'P') 
                {

                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize); 

                }
                else if (tile == 'T') {

                    Block teleport = new Block(teleportImage, x, y, tileSize, tileSize);
                    teleportPads.add(teleport);

                }
                else if (tile == '%') {

                    Block teleport2 = new Block(teleport2Image, x, y, tileSize, tileSize);
                    teleportPads.add(teleport2);

                }
                else if (tile == ':') {

                    Block bushe = new Block(bushes, x, y, tileSize, tileSize);
                    bush.add(bushe);

                }
                else if (tile == 'I') {

                    Block ice = new Block(iceImage, x, y, tileSize, tileSize);
                    iceBlocks.add(ice);

                }
                else if (tile == 'S') {

                    Block speed = new Block(speedImage, x, y, tileSize, tileSize);
                    speedZones.add(speed);

                }
                else if (tile == '~') {

                    Block phantom = new Block(null, x, y, tileSize, tileSize); 
                    phantomZones.add(phantom);

                }
                else if (tile == 'R') {

                    Block rev = new Block(reverseImage, x, y, tileSize, tileSize);
                    reverseControls.add(rev);

                }
                else if (tile == 'N') {

                    Block Wenter = new Block(warpEntranceImage, x, y, tileSize, tileSize);
                    warpEntrance.add(Wenter);

                }
                else if (tile == '&') {

                    Block wexit = new Block(warpExitImage, x, y, tileSize, tileSize);
                    warpExit.add(wexit);
                    walls.add(wexit);

                }
                else if (tile == '1') {

                    Block button1 = new Block(button1Image, x, y, tileSize, tileSize);
                    Button1.add(button1);

                }
                else if (tile == 'A') {

                    Block gate1 = new Block(gate1Image, x, y, tileSize, tileSize);
                    Gate1.add(gate1);
                    walls.add(gate1);

                }
                else if (tile == '2') {

                    Block button2 = new Block(button2Image, x, y, tileSize, tileSize);
                    Button2.add(button2);

                }
                else if (tile == 'B') {

                    Block gate2 = new Block(gate2Image, x, y, tileSize, tileSize);
                    Gate2.add(gate2);
                    walls.add(gate2);

                }
                else if (tile == 'W') {

                    Block web = new Block(spiderWeb, x, y, tileSize, tileSize);
                    spiderNets.add(web);

                }
                else if (tile == 'V') {

                    Block lava = new Block(lavaImage, x, y, tileSize, tileSize);
                    lavaTiles.add(lava);

                }
                else if (tile == 'Z') {

                    Block sacred = new Block(sacredZone, x, y, tileSize, tileSize);
                    sacredZones.add(sacred);

                }
                else if (tile == 'L') {

                    Block slimemist = new Block(slime, x, y, tileSize, tileSize);
                    slimePuddle.add(slimemist);

                }
                else if (tile == 'Q') {

                    Block electricS = new Block(electric, x, y, tileSize, tileSize);
                    electricShocks.add(electricS);

                }
            }
        }
    }




    @Override 
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g); 
        draw(g); 
    }


    public void draw(Graphics g) 
    {
        for (Block lava : lavaTiles) {
            if (lavaEnabled) {
                g.drawImage(lava.image, lava.x, lava.y, tileSize, tileSize, null);
            } else {
                g.setColor(Color.darkGray);
                g.fillRect(lava.x, lava.y, tileSize, tileSize);
            }
        }

        g.setColor(Color.BLACK);
        for (Block bS : blackSpace) {
            g.fillRect(bS.x, bS.y, tileSize, tileSize);
        }

        for (Block web : spiderNets) {
            g.drawImage(web.image, web.x, web.y, web.width, web.height, null);
        }

        for (Block ice : iceBlocks) {
            g.drawImage(ice.image, ice.x, ice.y, ice.width, ice.height, null);
        }

        for (Block elec : electricShocks) {
            g.drawImage(elec.image, elec.x, elec.y, elec.width, elec.height, null);
        }

        for (Block sacred : sacredZones) {
            g.drawImage(sacred.image, sacred.x, sacred.y, sacred.width, sacred.height, null);
        }

        for (Block slime : slimePuddle) {
            g.drawImage(slime.image, slime.x, slime.y, slime.width, slime.height, null);
        }

        for (Block button : Button1) {
            g.drawImage(button.image, button.x, button.y, button.width, button.height, null);
        }

        for (Block button : Button2) {
            g.drawImage(button.image, button.x, button.y, button.width, button.height, null);
        }


        if (gate1Open) {

            g.setColor(Color.BLACK);
            for (Block gate : Gate1) {
                g.fillRect(gate.x, gate.y, tileSize, tileSize);
            }
        }

        if (gate2Open) {

            g.setColor(Color.BLACK); 
            for (Block gate : Gate2) {
                g.fillRect(gate.x, gate.y, tileSize, tileSize);
            }
        }

        for (Block tele : teleportPads) {
            g.drawImage(tele.image, tele.x, tele.y, tele.width, tele.height, null);
        }

        g.setColor(Color.BLACK);
        for (Block space1 : spaces1) {
            g.fillRect(space1.x, space1.y, tileSize, tileSize);
        }

        g.setColor(Color.BLACK);
        for (Block exit1 : exitSpaces1) {
            g.fillRect(exit1.x, exit1.y, tileSize, tileSize);
        }

        g.setColor(Color.BLACK);
        for (Block space2 : spaces2) {
            g.fillRect(space2.x, space2.y, tileSize, tileSize);
        }

        g.setColor(Color.BLACK);
        for (Block exit2 : exitSpaces2) {
            g.fillRect(exit2.x, exit2.y, tileSize, tileSize);
        }

        for (Block speed : speedZones) {
            g.drawImage(speed.image, speed.x, speed.y, speed.width, speed.height, null);
        }

        for (Block rev : reverseControls) {
            g.drawImage(rev.image, rev.x, rev.y, rev.width, rev.height, null);
        }

        for (Block exit : warpExit) {
            g.drawImage(exit.image, exit.x, exit.y, exit.width, exit.height, null);
        }


        for (Block entrance : warpEntrance) {
            g.drawImage(entrance.image, entrance.x, entrance.y, entrance.width, entrance.height, null);
        }


        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);


        for (Block ghost : ghosts)
        {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Block bu : bush) {
            g.drawImage(bu.image, bu.x, bu.y, bu.width, bu.height, null);
        }

        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(new Color(100, 130, 160, 120)); 

        for (Block phantom : phantomZones) {
            g.fillRect(phantom.x, phantom.y, phantom.width, phantom.height);
        }

        g.setColor(Color.WHITE);

        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));

        if (gameOver) 
        {
            g.drawString("GAME OVER" + String.valueOf(score), tileSize / 2, tileSize / 2);
        } 
        else 
        {
            g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize / 2, tileSize / 2);
        }
    }


    public void move() {
    
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;


        long currentTime = System.currentTimeMillis();
        boolean pacmanStunnedThisFrame = false;

        if (pacmanStunEndTimes.containsKey(pacman)) {

            long end1 = pacmanStunEndTimes.get(pacman);
            if (System.currentTimeMillis() < end1)
            {
                pacman.velocityX = 0;
                pacman.velocityY = 0;
                pacman.x += pacman.velocityX; 
                pacman.y += pacman.velocityY;
                pacmanStunnedThisFrame = true; 
            }
            else 
            {
                pacmanStunEndTimes.remove(pacman); 
            }
        }

        for (Block shock : electricShocks) {
            if (collision(pacman, shock))
            {
                soundManager.playEffect("electricshock"); 
                pacmanStunEndTimes.put(pacman, System.currentTimeMillis() + 2000);
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                pacman.velocityX = 0;
                pacman.velocityY = 0;
                pacmanStunnedThisFrame = false;
                System.out.println("PAC-MAN STUNNED!");
                break; 
            }
        }


        if (pacmanStunnedThisFrame) {
            return;
        }

        boolean currentlyOnReverse = false;

        for (Block reverse : reverseControls) {
            if (collision(pacman, reverse)) {

                currentlyOnReverse = true;

                if (!pacmanIsOnReverse) 
                {
                    controlsReversed = !controlsReversed;
                }
                break;
            }
        }

        pacmanIsOnReverse = currentlyOnReverse;

        for (Block phantom : phantomZones) {
            if (collision(pacman, phantom)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        boolean alreadyInWeb = isInsideSpiderNet(pacman); 
        boolean alreadyInBush = isInsideBushes(pacman);

        if (alreadyInWeb) {

            if (pacman.direction == 'L') {
                pacman.velocityX = -tileSize / 8; 
                pacman.velocityY = 0; 
            }

            else if (pacman.direction == 'R') {
                pacman.velocityX = +tileSize / 8;
                pacman.velocityY = 0;
            }

            else if (pacman.direction == 'U') {
                pacman.velocityY = -tileSize / 8; 
                pacman.velocityX = 0;
            }

            else if (pacman.direction == 'D') {
                pacman.velocityX = 0;
                pacman.velocityY = +tileSize / 8;
            }

        }

        else if (alreadyInBush) {

            if (pacman.direction == 'L') {
                pacman.velocityX = -tileSize / 8; 
                pacman.velocityY = 0; 
            }

            else if (pacman.direction == 'R') {
                pacman.velocityX = +tileSize / 8;
                pacman.velocityY = 0;
            }

            else if (pacman.direction == 'U') {
                pacman.velocityY = -tileSize / 8; 
                pacman.velocityX = 0;
            }

            else if (pacman.direction == 'D') {
                pacman.velocityX = 0;
                pacman.velocityY = +tileSize / 8;
            }

        }

        else if (isOnIce(pacman)) {

            boolean onIceNow = isOnIce(pacman);
            pacman.updateVelocity();
            pacman.x += pacman.velocityX;
            pacman.y += pacman.velocityY;

            for (Block wall : walls) {
                if (collision(pacman, wall)) {
                    
                    pacman.x -= pacman.velocityX;
                    pacman.y -= pacman.velocityY;
                    pacman.velocityX = 0;
                    pacman.velocityY = 0;
                    break; 

                }
            }

            if (!isOnIce(pacman)) {
                pacman.velocityX = 0;
                pacman.velocityY = 0;
            }

        } 
        else 
        {
            pacman.updateVelocity();
        }

        boolean currentlyOnLavaButton = false;

        for (Block button : Button2) {
            if (collision(pacman, button)) {

                currentlyOnLavaButton = true;
                if (!pacmanOnLavaButton)
                { 
                 
                    lavaEnabled = !lavaEnabled;
                    if (lavaEnabled) {
                        System.out.println("LAVA TURNED ON");
                    } else {
                        System.out.println("LAVA TURNED OFF");
                    }
                }
                break;
            }
        }
        pacmanOnLavaButton = currentlyOnLavaButton;

        for (Block warpIn : warpEntrance) {
            if (collision(pacman, warpIn)) {
                if (!warpExit.isEmpty()) 
                {
                    Block[] exitsArray = warpExit.toArray(new Block[0]);
                    int index = random.nextInt(exitsArray.length);
                    Block chosenExit = exitsArray[index];
                    switch (pacman.direction) {
                        case 'U':
                            pacman.x = chosenExit.x;
                            pacman.y = chosenExit.y - tileSize;
                            break;
                        case 'D':
                            pacman.x = chosenExit.x;
                            pacman.y = chosenExit.y + tileSize;
                            break;
                        case 'L':
                            pacman.x = chosenExit.x - tileSize;
                            pacman.y = chosenExit.y;
                            break;
                        case 'R': 
                            pacman.x = chosenExit.x + tileSize;
                            pacman.y = chosenExit.y;
                            break;
                    }
                }
                break; 
            }
        }


        for (Block warpOut : warpExit) {
            if (collision(pacman, warpOut)) {

                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                pacman.velocityX = 0;
                pacman.velocityY = 0;
                hitWall = true;
                break;
            }
        }

        for (Block space1 : spaces1) {
            if (collision(pacman, space1)) {
                for (Block exit1 : exitSpaces1) {
                    teleport(space1, exit1, pacman); 
                    break;
                }
                break;
            }
        }

        for (Block exit1 : exitSpaces1) {
            if (collision(pacman, exit1)) {
                for (Block space1 : spaces1) {
                    teleport(exit1, space1, pacman);
                    break;
                }
                break;
            }
        }


        for (Block space2 : spaces2) {
            if (collision(pacman, space2)) {
                for (Block exit2 : exitSpaces2) {
                    teleport(space2, exit2, pacman);
                    break;
                }
                break;
            }
        }

        for (Block exit2 : exitSpaces2) {
            if (collision(pacman, exit2)) {
                for (Block space2 : spaces2) {
                    teleport(exit2, space2, pacman);
                    break;
                }
                break;
            }
        }



        for (Block space1 : spaces1) {
            for (Block ghost : ghosts) {
                if (collision(ghost, space1)) {
                    for (Block exit1 : exitSpaces1) {
                        teleport(space1, exit1, ghost);
                        break;
                    }
                    break;
                }
            }

        }

        for (Block exit1 : exitSpaces1) {
            for (Block ghost : ghosts) {
                if (collision(ghost, exit1)) {
                    for (Block space1 : spaces1) {
                        teleport(exit1, space1, ghost);
                        break;
                    }
                    break;
                }
            }
        }


        for (Block space2 : spaces2) {
            for (Block ghost : ghosts) {
                if (collision(ghost, space2)) {
                    for (Block exit2 : exitSpaces2) {
                        teleport(space2, exit2, ghost);
                        break;
                    }
                    break;
                }
            }
        }

        for (Block exit2 : exitSpaces2) {
            for (Block ghost : ghosts) {
                if (collision(ghost, exit2)) {
                    for (Block space2 : spaces2) {
                        teleport(exit2, space2, ghost);
                        break;
                    }
                    break;
                }
            }
        }


        for (Block tele : teleportPads) {
            if (collision(tele, pacman))
            {
                long currenttTime = System.currentTimeMillis();

                if (currentTime - lastTeleportTime > TELEPORT_COOLDOWN) {
                    isTeleporting = true; 
                    teleportPacMan();  
                    lastTeleportTime = currentTime; 
                }
                break; 
            }
        }

        for (Block ghost : ghosts) {

            boolean onSpeedZone = isOnSpeedZone(ghost); 
            ghost.updateVelocity();

            if (onSpeedZone) {
                ghost.velocityX *= 2;
                ghost.velocityY *= 2;
            }
            if (!onSpeedZone) {
                ghost.velocityX /= 2; 
                ghost.velocityY /= 2;
            }

            int newX = ghost.x + ghost.velocityX;
            int newY = ghost.y + ghost.velocityY;

            boolean collisionDetected = false;
            for (Block wall : walls) {
                if (collision(newX, newY, wall)) {
                    collisionDetected = true;
                    break;
                }
            }

            if (!collisionDetected) {
                ghost.x = newX;
                ghost.y = newY;
            }
        }


        boolean currentlyOnButton1 = false;

        for (Block button : Button1) {
            if (collision(pacman, button)) {
                currentlyOnButton1 = true;
                if (!pacmanOnButton1) {
                    gate1Open = !gate1Open;
                    if (gate1Open) {
                        for (Block gate : Gate1) {
                            walls.remove(gate); 
                        }
                    }
                    else 
                    {
                        for (Block gate : Gate1) {
                            walls.add(gate); 
                        }
                    }

                }
                break;
            }
        }
        pacmanOnButton1 = currentlyOnButton1;


        for (Block wall : walls) {
            if (collision(pacman, wall)) {

                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                pacman.velocityX = 0;
                pacman.velocityY = 0;
                hitWall = true;
                break;
            }
        }

        boolean currentlyGhostOnLavaButton = false;
        for (Block ghost : ghosts) {
            for (Block button : Button2) {
                if (collision(ghost, button)) {
                    currentlyGhostOnLavaButton = true;
                    if (!ghostOnLavaButton) { 
                        lavaEnabled = !lavaEnabled;
                        if (lavaEnabled) {
                            System.out.println("LAVA TURNED ON BY GHOST!");
                        }

                        else {
                            System.out.println("LAVA TURNED OFF BY GHOST!");
                        }
                    }
                    break; 
                }
            }
            if (currentlyGhostOnLavaButton) {
                break; 
            }
        }
        ghostOnLavaButton = currentlyGhostOnLavaButton;

        if (lavaEnabled) {
            for (Block lava : lavaTiles) {
                if (collision(pacman, lava)) {
                    
                    lives--;
                    System.out.println("PAC-MAN STEPPED ON LAVA! LIVES REMAINING: " + lives);
                    if (lives <= 0) {
                        gameOver = true;
                        return;
                    }
                    resetPositions();
                    break;

                }
            }
        }

        if (lavaEnabled && (ghostLavaKillTemporary)) {
            Iterator<Block> ghostIterator = ghosts.iterator();
            while (ghostIterator.hasNext()) {
                Block ghost = ghostIterator.next();

                for (Block lava : lavaTiles) {
                    if (lavaEnabled && collision(ghost, lava)) {
                        ghostIterator.remove(); 
                        System.out.println("GHOST REMOVED ON LAVA!");
                        break;
                    }
                }
            }

        }

        boolean currentlyGhostOnIceButton1 = false;

        for (Block ghost : ghosts) {
            for (Block button : Button1) {
                if (collision(ghost, button)) {
                    currentlyGhostOnIceButton1 = true;
                    if (!ghostOnButton1) {

                        gate1Open = !gate1Open;

                        if (gate1Open) {
                            for (Block gate : Gate1) {
                                walls.remove(gate);
                                System.out.println("GATE 1 ON BY GHOST!");
                            }

                        }
                        else {
                            for (Block gate : Gate1) {
                                walls.add(gate);
                                System.out.println("GATE 2 OFF BY GHOST!");
                            }
                        }

                    }

                    break; 
                }
            }
            if (currentlyGhostOnIceButton1)
                break;
        }

        ghostOnButton1 = currentlyGhostOnIceButton1;

        for (Block ghost : ghosts) {
            for (Block phantom : phantomZones) {
                if (collision(ghost, phantom)) {

                    ghost.x += ghost.velocityX;
                    ghost.y += ghost.velocityY;
                    ghost.phaseTilesRemaining = (tileSize / 8) * 2; 
                    ghost.x += ghost.velocityX;
                    ghost.y += ghost.velocityY;
                    break;
                }

                if (ghost.phaseTilesRemaining > 0) {
                    ghost.phaseTilesRemaining--;
                }

                else {
                    for (Block wall : walls) {
                        if (collision(ghost, wall)) {
                            ghost.x += ghost.velocityX;
                            ghost.y += ghost.velocityY;
                            ghost.updateDirection(directions[random.nextInt(4)]);
                            break;
                        }
                    }
                }

            }


            if (ghostStunEndTimes.containsKey(ghost)) {
                long end = ghostStunEndTimes.get(ghost);
                if (System.currentTimeMillis() < end) {
                    ghost.velocityX = 0;
                    ghost.velocityY = 0;
                    continue; 
                }
                else {
                    ghostStunEndTimes.remove(ghost);
                }
            }

            for (Block shock : electricShocks) {
                if (collision(ghost, shock)) {
                    ghostStunEndTimes.put(ghost, System.currentTimeMillis() + 2000); 
                    ghost.velocityX = 0;
                    ghost.velocityY = 0;
                    System.out.println("GHOST STUNNED!");
                    continue; 
                }
            }

            boolean inSacred = isInsideSacredZone(ghost); 

            if (inSacred) {
                if (ghost.direction == 'L') {
                    ghost.velocityX = -tileSize / 32;
                    ghost.velocityY = 0;
                }
                else if (ghost.direction == 'R') {
                    ghost.velocityX = +tileSize / 32;
                    ghost.velocityY = 0;
                }
                else if (ghost.direction == 'U') {
                    ghost.velocityY = -tileSize / 32;
                    ghost.velocityX = 0;
                }
                else if (ghost.direction == 'D') {
                    ghost.velocityX = 0;
                    ghost.velocityY = +tileSize / 32;
                }
            }
            else {

            }

            boolean inSlime = isInsideSlimePuddle(ghost);

            if (inSlime) {
                if (ghost.direction == 'L') {
                    ghost.velocityX = -tileSize / 32;
                    ghost.velocityY = 0;
                }
                else if (ghost.direction == 'R') {
                    ghost.velocityX = +tileSize / 32;
                    ghost.velocityY = 0;
                }
                else if (ghost.direction == 'U') {
                    ghost.velocityY = -tileSize / 32;
                    ghost.velocityX = 0;
                }
                else if (ghost.direction == 'D') {
                    ghost.velocityX = 0;
                    ghost.velocityY = +tileSize / 32;
                }
            }
            else {

            }

            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            for (Block wall : walls) {
                if (collision(ghost, wall)) {

                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                    break;

                }
            }

            for (Block warpIn : warpEntrance) {
                if (collision(ghost, warpIn)) {

                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                    break;

                }
            }

            for (Block warpOut : warpExit) {
                if (collision(ghost, warpOut)) {
                    
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                    break;

                }
            }

            if (collision(ghost, pacman)) {

                lives -= 1;
                soundManager.playEffect("die");
                if (lives == 0) {
                    
                    gameOver = true;
                    return;

                }
                resetPositions(); 
            }

            if (ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') {

                char newDirection2 = directions[random.nextInt(2)];
                ghost.updateDirection(newDirection2);

            }


            switch (pacman.direction) {
                case 'U':
                    pacman.image = pacmanUpImage;
                    break;
                case 'D':
                    pacman.image = pacmanDownImage;
                    break;
                case 'L':
                    pacman.image = pacmanLeftImage;
                    break;
                case 'R':
                    pacman.image = pacmanRightImage;
                    break;
            }


            Block foodEaten = null; 

            for (Block food : foods) {
                if (collision(pacman, food)) {

                    foodEaten = food;
                    score += 10;
                    soundManager.playEffect("eat");

                }
            }
            foods.remove(foodEaten); 
            updateGameLogic();

        }
    }


    private boolean isInsideSpiderNet(Block pacman) {
        for (Block net : spiderNets) {
            if (collision(pacman, net)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInsideBushes(Block pacman) {
        for (Block bushBlock : bush) {
            if (collision(pacman, bushBlock)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInsideSacredZone(Block ghost) {
        for (Block sacredZ : sacredZones) {
            if (collision(ghost, sacredZ)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInsideSlimePuddle(Block ghost) {
        for (Block slime : slimePuddle) {
            if (collision(ghost, slime)) {
                return true;
            }
        }
        return false;
    }

    public void teleport(Block entry, Block exit, Block pacman) {

        pacman.x = exit.x;
        pacman.y = exit.y;

        switch (pacman.direction) {
            case 'U':
                pacman.y -= tileSize;
                break;
            case 'D':
                pacman.y += tileSize;
                break;
            case 'L':
                pacman.x -= tileSize;
                break;
            case 'R':
                pacman.x += tileSize;
                break;
        }
    }

    public boolean isOnIce(Block pacman) {
        for (Block ice : iceBlocks) {
            if (collision(pacman, ice)) {
                return true;
            }
        }
        return false;
    }


    public boolean isOnSpeedZone(Block ghost) {
        for (Block speedZone : speedZones) {
            if (collision(ghost, speedZone)) {
                return true;
            }
        }
        return false;
    }

    public boolean collision(int newX, int newY, Block block) {

        return newX < block.x + block.width &&
                newX + block.width > block.x &&
                newY < block.y + block.height &&
                newY + block.height > block.y;
    }

    public void teleportPacMan() {
    
        List<Block> teleportPadList = new ArrayList<>(teleportPads);
        Block randomPad;
        do {
            randomPad = teleportPadList.get((int) (Math.random() * teleportPadList.size()));
        } while (randomPad.x == pacman.x && randomPad.y == pacman.y); 

        pacman.x = randomPad.x;
        pacman.y = randomPad.y;
        isTeleporting = false;

    }


    int currentLevel = 0;
    public void updateGameLogic() 
    {    
        if (foods.isEmpty()) 
        {
            currentLevel++;
            int nextThemeSound = debugMapLevel + currentLevel;
            if (debugMapLevel + currentLevel < levelLoaders.length) 
            {
                levelLoaders[debugMapLevel + currentLevel].run();
                System.out.println("ADVANCING TO LEVEL : " + nextThemeSound);
                soundManager.setLevel(nextThemeSound);
                resetPositions();
            }
            else{
                System.out.println("GAME OVER AT LEVEL : " + nextThemeSound);
                soundManager.playEffect("gameover");
                soundManager.stopThemeMusic();
                gameOver = true;
            }
        }
    }

    public boolean collision(Block a, Block b) 
    { 
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;

    }

    public void resetPositions() {
        
        pacman.reset(); 
        pacman.velocityX = 0;
        pacman.velocityY = 0;

        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }


    @Override 
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver)
        {
            soundManager.playEffect("gameover");
            soundManager.stopThemeMusic();
            gameLoop.stop();
        }
    } 

    @Override
    public void keyTyped(KeyEvent e) { 

    }

    @Override
    public void keyPressed(KeyEvent e) { 

    }

    @Override
    public void keyReleased(KeyEvent e) { 
        if (gameOver) {

            levelLoaders[debugMapLevel].run();
            soundManager.playEffect("theme1");
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
            return;
        }

        int futureX = pacman.x;
        int futureY = pacman.y;
        if (controlsReversed) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                futureY += tileSize / 4;
                if (!willHitWall(futureX, futureY))
                    pacman.updateDirection('D');
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                futureY -= tileSize / 4;
                if (!willHitWall(futureX, futureY))
                    pacman.updateDirection('U');
            }
            else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                futureX += tileSize / 4;
                if (!willHitWall(futureX, futureY))
                    pacman.updateDirection('R');
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                futureX -= tileSize / 4;
                if (!willHitWall(futureX, futureY))
                    pacman.updateDirection('L');
            }
        }
        else {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                futureY -= tileSize / 4;
                if (!willHitWall(futureX, futureY))
                    pacman.updateDirection('U');
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                futureY += tileSize / 4;
                if (!willHitWall(futureX, futureY))
                    pacman.updateDirection('D');
            }
            else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                futureX -= tileSize / 4;
                if (!willHitWall(futureX, futureY))
                    pacman.updateDirection('L');
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                futureX += tileSize / 4;
                if (!willHitWall(futureX, futureY))
                    pacman.updateDirection('R');
            }
        }

    }


    private boolean willHitWall(int futureX, int futureY) {
        for (Block wall : walls) {
            if (collision(futureX, futureY, wall)) {
                return true;
            }
        }
        return false;
    }

}
