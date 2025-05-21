// // the idea here is i am going to have PacMan inherit in JPanel. basically i am going to have this pacman class inherit the JPanel so that this class is basically a version of JPanel


// import java.awt.*;  // AWT (Abstract Window Toolkit) is a part of Java's GUI toolkit that allows creating graphical user interfaces.The * means all classes from java.awt are imported. common classes are Frame(creates a window) , Button( creates a clickable window) , Label(displays text) , TextField ( accepts user input)
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.awt.event.KeyEvent;
// import java.awt.event.KeyListener;
// import java.util.HashSet;
// import java.util.Random;
// import javax.swing.*;  // This package contains event handling classes needed for user interaction. For example, clicking a button, pressing a key, or moving the mouse are events. commons classes from java.awt.event are ActionListener( handles button clicks) , KeyListener ( detects keyboard input) , MouseListener(detects mouse clicks)
// import javax.swing.text.html.HTMLDocument;

// public class PacMan extends JPanel implements ActionListener , KeyListener{   // i have written extends JPanel bcz this PacMan class inherits JPanel

    
//     // to specify the position of ghost , food , wall and pacmn
//     class Block{
//         int x; // x position
//         int y; // y positon
//         int width; 
//         int height;
//         Image image ;

//         // declaring some properties to make our pacman movable 
//         char direction = 'U';  // U D L R  ( it can be anything)
//         int velocityX = 0;  // 0 means we are not moving therefore here we are not moving along rows 
//         int velocityY = 0;  // here we are not moving along columns
//         // note: we will use the same member variable for our ghost also bcz since we are in the same class Block and initially we used the same class for walls
//         // we just want to pack all in the same class just to make things easier 
//         // now when i press any key i want to update te direction so i create a function in this class updateDirection



//         // now as we start the game our ghost , pacman will be moving around so we need to store the original x and y starting position bcz as the game begins the x and y positions will be changing 
//         int startX;
//         int startY;

//         // creating a constructor ( since it is a constructor so name should be same as class name)

//         Block( Image image , int x , int y , int width , int height)
//         {

//             this.image = image;
//             this.x = x;
//             this.y = y;
//             this.width = width;
//             this.height = height;
//             this.startX =x;
//             this.startY = y;

//             // we had used this to differentiate between the paramters and the variables
//             // this.image means the variable image declared in class BLOCK and = image measn the paramter which is being passed in this constructor

//         }


//         // when i presss a key i will call this function which first updates the direction and then calls the another function to update the velocity
//         void updateDirection(char direction){  // INPUTIING A PARAMTER DIRECTION
            
//             // storing the previous direction;
//             char prevDirection = this.direction;
//             this.direction = direction;  // now we had created a variable direction in the class Block so this.direction refers to that variable i.e we are talking here about the variable declared in the class Block not the one passed by you. on the right side of equal to we have direction this is the one which is being passed in the form of paramter
//             updateVelocity();  // now we are calling this function

//             // now i will iterate through all the walls. and everytime i update the direction i need to make sure that pacman is able to change direction without crashing into the wall 
//             this.x += this.velocityX;
//             this.y += this.velocityY;
//             // ieterating through all the walls
//             for ( Block wall: walls)
//             {
//                 if ( collision(this , wall))  // if this object collides with the wall. here this is being refered as pacman. we will use the same function for ghost as well 
//                 {
//                     // then we gonna take a step back 
//                     this.x -= this.velocityX;
//                     this.y -= this.velocityY;
//                     // now we need to update the direction
//                     this.direction = prevDirection;
//                     updateVelocity(); // we need to call update velocity again 

//                 }
//             }

//             // now our problem is solved 
//             // now we have to change the image of pacman like when it moves upward then its face should point upward like that 
//             // so we will do that in key release function
//         }

//         void updateVelocity(){

//             if ( this.direction == 'U')
//             {
//                 this.velocityX = 0;  // bcz it is now moving only in the y axis so x axis zero
//                 this.velocityY = -tileSize/4;  // it is negative bcz we are going towards zero. and i had made it 32/4 therefore it is 8px therefore every frame is going to go up in pixels or we can say every frame is going to go a quarter of the tile size per sec
//             }

//             else if ( this.direction == 'D'){
//                 this.velocityX = 0;
//                 this.velocityY = tileSize/4;
//             }

//             else if ( this.direction == 'L')
//             {
//                 this.velocityX = -tileSize/4;
//                 this.velocityY = 0;
//             }

//             else if( this.direction == 'R')
//             {
//                 this.velocityX = tileSize/4;
//                 this.velocityY = 0;
//             }

//         }
//         // now go to keyreleased function at last





//         // making a reset function to reset the positions of every object after the ghost collides with pacman 
//         void reset()
//         {
//             this.x = this.startX;
//             this.y = this.startY;
//         }

//         // now go back to move function to the ghost collision loop

//     }
//     // defining few properties 
//     private int rowCount = 21;
//     private int colCount = 19;
//     private int tileSize = 32;

//     private int widthBoard = colCount*tileSize;
//     private int heightBoard = rowCount*tileSize;

//     // done this so that our JPanel has same size as the window 
//     // VERY IMPORTANT POINT
//     // why made them private?
//     // bcz we dont want their values to get changed by someone if they were public . these are the base of our game and we dont want bugs in them
       

//     // creating variables to store images 
//     private Image wallImage;
//     private Image blueGhostImage;
//     private Image pinkGhostImage;
//     private Image redGhostImage;
//     private Image orangeGhostImage;
//     private Image scaredGhostImage;
//     private Image cherryImage;
//     private Image cherry2Image;
//     private Image powerFoodImage;

//     private Image pacmanUpImage;
//     private Image pacmanDownImage;
//     private Image pacmanLeftImage;
//     private Image pacmanRightImage;
        

//     //X = wall, O = skip(dont do anything), P = pac man, ' ' = food   
//     //Ghosts: b = blue, o = orange, p = pink, r = red
//     private String[] tileMap ={     // tileMap is an array of strings
//         "XXXXXXXXXXXXXXXXXXX\r\n" , //
//                         "X        X        X\r\n" , //
//                         "X XXXXX  X  XXXXX X\r\n" , //
//                         "X X             X X\r\n" , //
//                         "X X XXXXXXXXXXX X X\r\n" , //
//                         "X                 X\r\n" , //
//                         "XXXXX XXXXX XXXXX X\r\n" , //
//                         "X                 X\r\n" , //
//                         "X XXXXXXX XXXXXXX X\r\n" , //
//                         "X        r        X\r\n" , //
//                         "X XXXXXXX XXXXXXX X\r\n" , //
//                         "X    P            X\r\n" , //
//                         "X XXXXX XXXXX XXXXX\r\n" , //
//                         "X                 X\r\n" , //
//                         "X X XXXXXXXXXXX X X\r\n" , //
//                         "X X             X X\r\n" , //
//                         "X XXXXX  X  XXXXX X\r\n" , //
//                         "X        X        X\r\n" , //
//                         "X XXXXXXXXXXXXXXX X\r\n" , //
//                         "X                 X\r\n" , //
//                         "XXXXXXXXXXXXXXXXXXX" ,

// };
//     // this is the tile map which i am using we can create anything which we want
//     // here we have array of strings and each string is a row 

//     // now i had to go through the tile map and create objects like walls , foods , and ghosts , etc
//     // for this i create another function below the PacMan function and i name it as loadMap



//     // creating hashset for the constructor block used above
//     HashSet<Block> walls;   // it is called as hashset of block and is named as walls
//     HashSet<Block> foods;  // hashset of block and is named as foods
//     HashSet<Block> ghosts;  // hashset of block and is named as ghost
//     Block pacman;   // we had declared a block for pacman 

//     // now we had done the representation of the objects in the game 
//     // moving further with the tile map




//     // creating a timer for game loop
//     Timer gameLoop;
//     // now inside the below constructor i initialize this variable




//     // making the ghost move
//     // creating an array of character
//     char [] directions = {'U' , 'D' , 'R' , 'L'}; // up down right left
//     // creating a rando object
//     Random random = new Random();
//     // basically for each ghost we are going to randomly select the direction
//     // moving to the constructor PacMan



//     // adding reamining things like score , lives and etc
//     int score =0;
//     int lives  = 3; // by default we start with 3 lives 
//     boolean gameOver = false;  
//     // pacman has 3 lives and if he collides with the ghost it will lose one life and when lives = 0 then game over becomes true . and if it is gameover then we stop the pacman from moving 
    
//     // lets starts with the score
//     // everytime the packman collides the the food it gonna eat it and gain 10 points . and everytime the packman eats the food we want to remove that food from hashSet

//     // go to move function





//     // creating a constructor ( constrictor is a special method that rus automaticaly when an object of the class is created)
//     PacMan(){   // here the PacMan constructor sets up the game board ( which extends JPanel)

//         setPreferredSize(new Dimension(widthBoard , heightBoard));   // this sets the preffered size of the JPanel which means it tells the layout manager that widht = widthboard and height = heightboard
//         setBackground(Color.BLACK);

//         addKeyListener(this);  // when the class PacMan implements keylistener it takes on the properties of keylistener . so i dont need to create a seperate key listener object i can just reference pacman using this 
//         // this keylistner is going to listner the three functions which are written keytyped, keypressed , keyreleased to process the key processes

//         // now i need to ensure that our JPanel listens to key presses. in a window we can have multiple components which all can be listening key presses. but in this key we had one component ( declared just above this) but we need to make sure that this is the component that listens to the key presses
//         // so i write 
//         setFocusable(true);
//         // now go back to App.java file and write pacmanGame.requestFocus(); but make sure to write it before setvisible
//         // after running the file when we press any key we got keyevent printed as the code number so the arrow keys as specific number such as 
//         // 37 -> left arrow
//         // 39 -> right arrow
//         // 38 - > up arrow
//         // 40 -> down arrow

//         // now we want to make our pacman movable 
//         // if i want to move the pacman left or right it is basically the x axis line. for left i will go -ve x axis and for right i will go towards +ve x axis as i am going away from zero
//         // similary top is moving -ve y axis bcz i am moving close to zero and +ve y axis when moving down bcz i am moving away from zero 
//         // therefore in order to move the pacman we need to set the velocity in x direction and y direction 
//         // therefore i will add three more variable in the class Block




//         // now we had our JPanel . now go back to App.java file and create an instance/object of this JPanel now 




//         // coming back from there
//         // lets add images

//         // for the images we are creating member variables to store the images

//         // so i created them outside the constructor but inside the class
        

//         // loading images inside the constructor 
//         wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();   // in the resource we need to specify the path of the image where it is located . getClass refers to the class PacMan where we are writing the code. and this file is located inside src folder and our images are also located inside the same folder so we made a string inside getResource "./" this basically means that we are going to look from the same folder and we just need to specify the image name. if i had not written getImage then it would have created an image icon. but since we need the image so i used .getImage()
//         blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage(); 
//         pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage(); 
//         redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage(); 
//         orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage(); 
//         scaredGhostImage = new ImageIcon(getClass().getResource("./scaredGhost.png")).getImage(); 
//         pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage(); 
//         pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage(); 
//         pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage(); 
//         pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage(); 
//         powerFoodImage = new ImageIcon(getClass().getResource("./powerFood.png")).getImage(); 
//         cherryImage = new ImageIcon(getClass().getResource("./cherry.png")).getImage(); 
//         cherry2Image = new ImageIcon(getClass().getResource("./cherry2.png")).getImage();
        
//         // in order to draw the image i need to specify where it is i.e the x and y position and widht and height. and i need to store all these info for fodd , wall , ghost , and pacman 
//         // for this i will use a class BLOCK
        


//         // now talking about the tile map
//         /*
//          it will be a 2d array and i will use characters ( we can use numbers also ). therefore i am going to use an array of strings which basically means an array of array of characters 
//          therefore we can think of each row as a string within our array. each tile is going to be represented by a character. if a tile has X then it means there is a wall at there. and where we have empty tiles it is whitespace character.
//          in these tiles i am going to place a food here and it is in this tiles that the pacman is moving. and O will simply mean that the tile is empty. in this we had like there cannot be placed any food. and this is the one where pacman can bypass the area and like if it 
//          goes to left it comes out from right. this is represented as O in this code

//          i had done this bcz it is easier to place some O in the maze rather than placing them everywhere inside the maze instead of empty space. 
//          also we had p,o,r,b which stands for pink , orange , red , blue. and also we have P which stands for pacman. like they are the starting positions where these all will be placed 


//          we start from the 0,0 and if we say we had to go to 4,2 tile then it will be 4*32 , 2*32 bcz tile size will also be have to considered.
//          since it had moved 4 tiles across right so 4*32 and 2 tiles down so 2*32
//          therefore we create variables specifying the tile widht and height 

//          therefore we traverse the array such that if we occured across X then we place a wall , if we occur across p,r,o,b then we place their a ghost and if we came across whitespace then we lace their a food

//          therefore we had three main repaeting things - wall , ghost , and food so i will create a 3 different hash sets to ietrate through it and i need not have a hashset for pacman bcz it is used a single time and hence i will create a variable for it 

//          why are we using a hashset?
        
//          -> hashset does not allow duplicates ensuring each tile is only stored once 
//          -> Useful when tracking walls, ghosts, and food because their positions do not repeat in the grid
//          -> If you stored wall positions in a list (ArrayList), checking if a tile is a wall would take O(n). 
//          -> With a HashSet, checking if a tile is a wall (or contains a ghost/food) takes O(1).
//          -> Instead of looping through an entire 2D array, we can just iterate over a HashSet containing the required tiles
//          -> HashSet directly checks if (x,y) exists in O(1) time
         

//          why not use a 2d array?

//          -> Searching for walls, ghosts, and food would still take O(n²) in worst cases.
//          -> Memory usage is higher than a HashSet, since it stores all tiles, even empty one


//          */



//          // checking number of walls , foods , ghosts
//          loadMap();  // calling the function loadmap bcz we want to check the number of walls , foods , ghosts and its algo is written inside the function loadMap();
//         //  System.out.println(walls.size());  // walls was the hashset where all the walls when encountered were stored 
//         //  System.out.println(foods.size());  // same here
//         //  System.out.println(ghosts.size()); // same here
//          // now i run the code and i see that it prints the number 

//          // but when i run this file then it gives error that the main method not found bcz i have declared main method in app.java so i need to run that file and not PacMan.java
//          // therefore our loadmap is working and we have all resources in our asset. now i comment our these print statements




//          // ghost movement  ( after loadMap())
//          // iterating through each ghost
//          for ( Block ghost : ghosts)
//          {
//             char newDirection = directions[random.nextInt(4)];  // since directions has four characters and random.nexint(4) gives a number betwen zero and 4 but not 4 so total of 4 numbers(0,1,2,3) it can give therefore we have directions[i] where i can be {0,1,2,3} . and the i is the index therefore if directions[0] then U character if 1 then D characters as according we had made the array of charcaters 
//             ghost.updateDirection(newDirection);  // therefore basicallt we assign each ghost a new direction

//          }
//          // now go to the move function to consider abt the collisions of the ghost




//          gameLoop = new Timer(50 , this);  // the delay is in milliseconds . and this refers to the ActionListener which is implemented in the class PacMan. basically it means that this class where you are using me must have implemented action listener
//          // there are 1000 ms in 1 sec so there are 1000/50 = 2ofps i.e 20 frame per second 

//          // It creates a repeating timer that calls an actionPerformed() method every 50ms. This is typically used for game loops to update animations, movement, or physics.
//          // how does it work?
//          // -> first we will start the timer by gameLoop.start();
//          // -> this will start the timer triggering actionPerformed() every 50ms
//          // -> since this refers to the class implementing ActionListener, we must have 
//         //  @Override
//         //  public void actionPerformed(ActionEvent e) {
//         //  // Update game logic (e.g., move Pac-Man, detect collisions, repaint screen)
//         //  repaint();
//         //  }
//         // -> This repaints the game screen every 50ms, creating the illusion of motion.

//         gameLoop.start();  // starting the timer 

//         // now the loop has started and when i run the loop is running but i dont see any changes so lets add some features of moving the pacman up down when pressed key
//         // for that we need key listner like wise action listener . so we write it above where we written ActionListner

//     }  //  this is the end of PacMan function



//     public void loadMap(){
            
//         // i need to initilise all the hashset first 

//         walls = new HashSet<Block>();
//         ghosts = new HashSet<Block>();
//         foods = new HashSet<Block>();


//         for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
//         {
//             for (  int c = 0 ; c<colCount ; c++)   // c stands for column
//             {
//                 String row = tileMap[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
//                 char tileMapChar = row.charAt(c);  // getting the current character
//                 // what we had done in the above two lines is that we getting the row and then we are getting the character present at that row with the col index
//                 // we had declared two variables 1) row 2) tileMapChar
//                 // the string row represents the single row in the tile map from index r=0 to r<rowCount
//                 // now we are using inbuilt string operations charAt() to acces the character present at that position
//                 // row.charAt(c) acceses the character from the row at index c where c represents the columns 


//                 // we got a char at a specific tile now we need to figure out where this tile is. therefore to draw we need x , y positions and width and height of tile 

//                 // getting x position ( it will be the 'how many columns we are from the left')
//                 int x = c*tileSize;
//                 // getting y position (  it will be the 'how many rows from the top')
//                 int y = r*tileSize;


//                 if ( tileMapChar == 'X'){  // block wall

//                     Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
//                     walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
//                     // this ensures that all walls are stored in one list which is walls here
//                     // it makes easier to draw all the walls in the game 
//                     // used for collision detection so that pacman cant go throught walls
//                 }

//                 else if ( tileMapChar == ' ')  // food
//                 {
//                     Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
//                     foods.add(food);   // it is of the form  HashSet.add(objt);

//                     // x+14? y+14?
//                     // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

//                 }


//                 else if ( tileMapChar == 'O')   // skip
//                 {
//                     continue;  // skip it dont do anything
//                 }
//                 // there is no need for this but we still added 

//                 else if( tileMapChar == 'o')   // orange ghost
//                 {

//                     Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
//                     ghosts.add(OrangeGhost);
//                 }


//                 else if( tileMapChar == 'b')   // blue ghost
//                 {

//                     Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
//                     ghosts.add(BlueGhost);
//                 }


//                 else if( tileMapChar == 'r')   // red ghost
//                 {

//                     Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
//                     ghosts.add(RedGhost);
//                 }


//                 else if( tileMapChar == 'p')   // pink ghost
//                 {

//                     Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
//                     ghosts.add(PinkGhost);
//                 }


//                 else if ( tileMapChar == 'P')  // pacman
//                 {
//                     pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
//                     // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
//                     // also bcz pacman was single entity so we didnt created a hashset for it
//                     // we had done this to treat pac man as a game object making collision detection easier
//                 }

//                 // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

//             }
//         }
//     } 





//     // creating a function paintComponent in this we will draw all of our objects onto the game

//     public void paintComponent(Graphics g)   // it will take in graphics g
//     {
//         super.paintComponent(g);  // basically super is going to import the function "paintComponent" from JPanel

//         // calling draw function
//         draw(g);  // this will draw graphics g
//     }

//     public void draw(Graphics g)  // taking graphics g 
//     {
//         // lets draw our pacman first
//         // since we have pacman as a block object and it has the information x,y position and width and height of pacman
//         // 1) firsly lets draw a rectangle at that position just for testing
//         // g.fillRect(pacman.x, pacman.y, pacman.width, pacman.height);   // this prints a rectangle at the position we had assigned for pacman

//         // 2) but i want pacman to be there so
//         g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width , pacman.height , null);   // here we had done two extra things . 1) we had written the name of variable pacman where we had stpred the image pacman so we write pacman.image and at last we had written null which is used so that the java automatically loads the image

//         // doing the same for other images

//         // displaying ghost
//         for ( Block ghost : ghosts)  // here i had created a for each loop where ghost is the element declared by us it will be used to traverse the whole hashset ghosts written after : . therefore this for loop displays all the items in the ghosts hashset
//         {
//             g.drawImage(ghost.image , ghost.x, ghost.y, ghost.width , ghost.height , null);
            
//         }


//         // displaying walls
//         for ( Block wall : walls){
//             g.drawImage(wall.image, wall.x, wall.y, wall.width , wall.height , null);
//         }


//         // displaying foods

//         // changing colour to white
//         g.setColor(Color.WHITE);
//         for ( Block food : foods){
//             // g.drawImage(food.image, food.x, food.y, food.width , food.height , null);
//             // this is wrong bcz we dont have image for the food 
//             //  we will use rectangle

//             g.fillRect(food.x, food.y, food.width, food.height);

//         }

//         // now move back to paintComponet function
//         // since the paintcomponet function only gets called once so we only draw one time
//         // but in game we are constantly moving all the characters so the ghost move up down left right so we need to constantly redraw on the JPanel to reflect those new positions
//         // to do that we need a game loop
//         // for that i implement a ActionListener in the main class i.e PacMan extends JPanel implements ActionListener





//         // score

//         g.setFont(new Font("Arial" , Font.PLAIN , 18));
//         if ( gameOver)
//         {
//             g.drawString("GAME OVER" + String.valueOf(score), tileSize/2 , tileSize/2);
//         }
//         else
//         {
//             g.drawString("x" + String.valueOf(lives)+ " Score: " + String.valueOf(score), tileSize/2 , tileSize/2);
//         }

//     }



//     public void move(){
//         pacman.x += pacman.velocityX;
//         pacman.y += pacman.velocityY;
//         // based on our code one of these ( on the right side of equal to ) are going to be zero [ since if pacman is moving up then x velocity is zero bcz it is moving in y axis ...]
//         // so i added both to the x and y position of pacman 
//         // therefore our move function updates the x and y positons of pacman 
//         // now we need to call this function before paint
//         // i.e inside the below function

//         // since velocity is tilesize/4 so we expect the pacman to move one quarter of the tile size 



//         // checking wall collision
//         // now i want to iterate through all the walls in the hashset
//         // so
//         for ( Block wall : walls){
//             if (collision(pacman , wall))   // if collison happens between pacman and this current wall
//             {
//                 pacman.x -= pacman.velocityX;  
//                 pacman.y -= pacman.velocityY;
//                 break; // once we found a collision we dont need to continue with hashset bcz we already foubd the wall that we are colliding to 
//                 // i will take a step back 
//                 // what is happening here is 
//                 // -> If a collision is detected, we undo the last movement, so Pac-Man stays in place.
//                 // -> Since we break out of the loop after detecting a collision, we avoid unnecessary checks for other walls.
//                 // -> Instead of stopping abruptly, we just reverse Pac-Man's last movement, making the transition feel natural.
//                 // -> Pac-Man stays in place instead of going through the wall
//                 // -> it does not means that the pacman will start moving backwards 

//             }
//         }

//         // now our pacman stops after colliding with the wall but it got stuck sometimes when we there is no way
//         // so to make that fix the pacman should only change the direction if it is able to which basically means that it should not change the direction if there is a wall 
//         // therefore go towards the update direction function




//         // ghost collison

//         for ( Block ghost : ghosts){  // for each ghost
            

//             // ghost and pacman collision
//             if ( collision(ghost, pacman))
//             {

//                 lives -= 1;
//                 if ( lives == 0)
//                 {
//                     gameOver = true; 
//                     return;   // now since the game ends therefore i want to stop the gameloop so go to actionperformed
//                 }
//                 resetPositions();  // i have to declare this new function and i will acces the reset function through it 
//                 // bcz the reset function is in the class Block and i will create this function inside the main PacMan class
//                 // i had made this function at last just above override

//             }



//             // adding a condition first to solve the problem 2 we faced
//             if ( ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D')
//             {
//                 // ghost.updateDirection('U');  // instead of making the ghost only move up we can make another char array which will choose from up and down only since it is of bound 2 therefore 0 or 1 therefore first two indexes
//                 char newDirection2 = directions[random.nextInt(2)];
//                 ghost.updateDirection(newDirection2);
//             }
//             // we are moving a step forward 
//             ghost.x += ghost.velocityX;
//             ghost.y += ghost.velocityY;
//             // until now the ghost are moving in any random direction and are moving through the walls so we need to do the same for them also like we didi for pacman and wall collision
//             for ( Block wall : walls){
//                 if ( collision(ghost, wall))
//                 {
//                     // stepping backwards
//                     ghost.x -= ghost.velocityX;
//                     ghost.y -= ghost.velocityY;
//                     // now we want the ghost to change the direction immediately if it collides into a wall so
//                     char newDirection = directions[random.nextInt(4)];
//                     ghost.updateDirection(newDirection);
//                 }
//             }
//         }

//         // problem 1: now we are facing a problem that the space where i had made no food place there the ghost/pacman when passed if from left then will get out from right and vice versa
        

//         // problem 2: since this problem is solved but now they are running on the same line they are not going else where so since theis is specially happening for a single row so we made a change in the above loop
        

//         // now it is all done all are moving perfectly now we need to do pacman and ghost collision , pacman eating food , increasing score , showing count 
//         // therefore go in the class PacMan just before constructor




//         // checking food collision
//         Block foodEaten = null; // currently pacman has not eaten anything 
    
//         // iterating over the loop
//         for ( Block food : foods)
//         {
//             if ( collision(pacman , food))
//             {
//                 // pacman will eat the food
//                 foodEaten = food;
//                 score += 10;
//                 // now we want to remove the food from hashSet
                
//             }
//         }
//         foods.remove(foodEaten);  // it removes that food  . note: it should not be placed inside the for loop as we are using for each loop. so any modifications we wanna made are to done outside it 
//         // now we want to update the score on the screen so move towards draw function


//         // now we are making ghost collison with pacman leading to resetting all the positons therefore we create a reset function in the class Block




//         // if food eaten is empty then we reload the map

//         if ( foods.isEmpty())
//         {
//             loadMap();
//             resetPositions();

//             // map will be reloaded but the lives will not change           
//         }




        
//     }


//     // to detect collision between pacman and wall , pacman and ghost , ghost and wall , pacman with food 
//     public boolean collision(Block a , Block b){  // taking two blocks
//         // we will use a specific formula to detect collison between two blocks 
//         // note: every image/ object on our screen is a rectangle. although pacman is round but its image is a rectagngle and is not visible bcz it is transparent


//         return a.x < b.x + b.width &&
//                a.x + a.width > b.x &&
//                a.y < b.y + b.height &&
//                a.y + a.height > b.y;


//         // now i am going to move function to make some checks
//             /*
//                 This is the AABB (Axis-Aligned Bounding Box) Collision Detection formula, which checks whether two rectangular objects (like Pac-Man and ghosts, walls, or food) are colliding.
//                 a → Represents Pac-Man, a ghost, or any moving object.
//                 b → Represents another object like a wall, ghost, or food.
//                 For collision to occur, all these conditions must be true:

//                 a.x < b.x + b.width
//                 → Checks if the left side of a is to the left of b's right side.
//                 a.x + a.width > b.x
//                 → Checks if the right side of a is to the right of b's left side.
//                 a.y < b.y + b.height
//                 → Checks if the top side of a is above b's bottom side.
//                 a.y + a.height > b.y
//                 → Checks if the bottom side of a is below b's top side.
//                 If all four conditions are met, it means a and b are overlapping, so collision has occurred.
                


//                 Pac-Man collides with walls → Stop movement.
//                 ->Pac-Man collides with food → Eat food & increase score.
//                 ->Pac-Man collides with a ghost → Lose a life or game over.
//                 ->Ghosts colliding with walls → Change direction.
//             */



        
//     }



//     public void resetPositions()
//     {
//         pacman.reset();  // this sets the x , and y positions back to the original position
//         // now since it is resetted therefore now i dont want the pacman to move until the user press a key so
//         pacman.velocityX = 0;
//         pacman.velocityY = 0;
//         // lets do the same for each ghost
//         for ( Block ghost : ghosts)
//         {
//             ghost.reset();
//             // updating the velocity 
//             // in case of ghost it will give them a new direction so 
//             char newDirection = directions[random.nextInt(4)];
//             ghost.updateDirection(newDirection);
//         }
//     }



//     @Override   // this gets created automatically when i press quick fix at the error i was getting when written implements ActionListener
//     public void actionPerformed(ActionEvent e) {
//         move();  
//         repaint();   // it is going to call paintComponent

//         if ( gameOver)   // if this is true 
//         {
//             gameLoop.stop();  // then do this 
//         }

//         // but we noticed that it got stuck at the same place but i want to reset everything to same position when i press any key.  so go to key released  


//     }// in order to make this function run we need a game loop so we create a timer

//     // therefore 
//     // updating the positions of all the objects and redraw the frame and update again and redraw again

//     // now what we had to do is we need to check if the pacman is colliding against the wall. if so then we need to stop it so lets create a function for it



//     // keylistner automatically generates three methods when done quickfix
//     @Override
//     public void keyTyped(KeyEvent e) {  // when we press a key that has a corresponding character
        
//     }



//     @Override
//     public void keyPressed(KeyEvent e) {  // it is basically when you press on any key . as long as i press a key i would trigger this function. i can hold onto the key as well
        
//     }



//     @Override
//     public void keyReleased(KeyEvent e) {   // it only gets triggered when we press a key and let it go and release the key 
//         // System.out.println("KeyEvent: "+ e.getKeyCode());  
//         // now to make our key presses work . we need to add two things in our constructor adding key listner 

//         // now i comment out that prinitng statement


//         if ( gameOver)
//         {
//             loadMap(); // reloading the map . this is done bcz i want to add all the foods back into the hashset 
//             resetPositions(); // resetting the positions 
//             // now we want each object a new direction to move in  so
//             lives = 3;  // making lives again 3
//             score  = 0;  // making score again 0
//             gameOver = false;  // setting gameover to be false
//             gameLoop.start();  // starting the game loop 

//         }

//         // now there maybe a case where the pacman has eaten all the food so i can load new levels but here i will load the same map




//         if (e.getKeyCode() == KeyEvent.VK_UP)   // when i pressed the up arrow then i call this function
//         {
//             pacman.updateDirection('U');   // passing U
//         }

//         else if (e.getKeyCode() == KeyEvent.VK_DOWN)   // when i pressed the down arrow then i call this function
//         {
//             pacman.updateDirection('D');   // passing D
//         }

//         else if (e.getKeyCode() == KeyEvent.VK_LEFT)   // when i pressed the left arrow then i call this function
//         {
//             pacman.updateDirection('L');   // passing L
//         }

//         else if (e.getKeyCode() == KeyEvent.VK_RIGHT)   // when i pressed the right arrow then i call this function
//         {
//             pacman.updateDirection('R');   // passing R
//         }

//         // this will update the direction and velocity but we actually need to move the pacman so we need to keep updating the paintComponent also 
//         // so create a function move just above the override function of action listener


//         // now we are trying to update the image with the direction updation

//         if ( pacman.direction == 'U')
//         {
//             pacman.image = pacmanUpImage;
//         }


//         else if ( pacman.direction == 'D')
//         {
//             pacman.image = pacmanDownImage;
//         }


//         else if ( pacman.direction == 'L')
//         {
//             pacman.image = pacmanLeftImage;
//         }


//         else if ( pacman.direction == 'R')
//         {
//             pacman.image = pacmanRightImage;
//         }

//         // now we had created different cases to do this instead of writing this one line inside the above if else cases where we are checking for the key up down left and right
//         // and updating the direction
//         // but if you press the key up then it does not mean that the packman will move upwards bcz there maybe a wall so if i could have placed this image upadation line inisde them just below updateDirection('U') then it would have resulted in falsy manner 



//         // now we are going to make the ghost move 
//         // they will move randomly 

//         // for that lets move on to the class PacMan
//     }


// }









// the idea here is i am going to have PacMan inherit in JPanel. basically i am going to have this pacman class inherit the JPanel so that this class is basically a version of JPanel


import java.awt.*;  // AWT (Abstract Window Toolkit) is a part of Java's GUI toolkit that allows creating graphical user interfaces.The * means all classes from java.awt are imported. common classes are Frame(creates a window) , Button( creates a clickable window) , Label(displays text) , TextField ( accepts user input)
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;  // This package contains event handling classes needed for user interaction. For example, clicking a button, pressing a key, or moving the mouse are events. commons classes from java.awt.event are ActionListener( handles button clicks) , KeyListener ( detects keyboard input) , MouseListener(detects mouse clicks)
import javax.swing.text.html.HTMLDocument;

public class PacMan extends JPanel implements ActionListener , KeyListener{   // i have written extends JPanel bcz this PacMan class inherits JPanel

    
    // to specify the position of ghost , food , wall and pacmn
    class Block{
        int x; // x position
        int y; // y positon
        int width; 
        int height;
        Image image ;

        // declaring some properties to make our pacman movable 
        char direction = 'U';  // U D L R  ( it can be anything)
        int velocityX = 0;  // 0 means we are not moving therefore here we are not moving along rows 
        int velocityY = 0;  // here we are not moving along columns
        // note: we will use the same member variable for our ghost also bcz since we are in the same class Block and initially we used the same class for walls
        // we just want to pack all in the same class just to make things easier 
        // now when i press any key i want to update te direction so i create a function in this class updateDirection



        // now as we start the game our ghost , pacman will be moving around so we need to store the original x and y starting position bcz as the game begins the x and y positions will be changing 
        int startX;
        int startY;

        // creating a constructor ( since it is a constructor so name should be same as class name)

        Block( Image image , int x , int y , int width , int height)
        {

            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX =x;
            this.startY = y;

            // we had used this to differentiate between the paramters and the variables
            // this.image means the variable image declared in class BLOCK and = image measn the paramter which is being passed in this constructor

        }


        // when i presss a key i will call this function which first updates the direction and then calls the another function to update the velocity
        void updateDirection(char direction){  // INPUTIING A PARAMTER DIRECTION
            
            // storing the previous direction;
            char prevDirection = this.direction;
            this.direction = direction;  // now we had created a variable direction in the class Block so this.direction refers to that variable i.e we are talking here about the variable declared in the class Block not the one passed by you. on the right side of equal to we have direction this is the one which is being passed in the form of paramter
            updateVelocity();  // now we are calling this function

            // now i will iterate through all the walls. and everytime i update the direction i need to make sure that pacman is able to change direction without crashing into the wall 
            this.x += this.velocityX;
            this.y += this.velocityY;
            // ieterating through all the walls
            for ( Block wall: walls)
            {
                if ( collision(this , wall))  // if this object collides with the wall. here this is being refered as pacman. we will use the same function for ghost as well 
                {
                    // then we gonna take a step back 
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    // now we need to update the direction
                    this.direction = prevDirection;
                    updateVelocity(); // we need to call update velocity again 

                }
            }

            // now our problem is solved 
            // now we have to change the image of pacman like when it moves upward then its face should point upward like that 
            // so we will do that in key release function
        }

        void updateVelocity(){

            if ( this.direction == 'U')
            {
                this.velocityX = 0;  // bcz it is now moving only in the y axis so x axis zero
                this.velocityY = -tileSize/4;  // it is negative bcz we are going towards zero. and i had made it 32/4 therefore it is 8px therefore every frame is going to go up in pixels or we can say every frame is going to go a quarter of the tile size per sec
            }

            else if ( this.direction == 'D'){
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }

            else if ( this.direction == 'L')
            {
                this.velocityX = -tileSize/4;
                this.velocityY = 0;
            }

            else if( this.direction == 'R')
            {
                this.velocityX = tileSize/4;
                this.velocityY = 0;
            }

        }
        // now go to keyreleased function at last





        // making a reset function to reset the positions of every object after the ghost collides with pacman 
        void reset()
        {
            this.x = this.startX;
            this.y = this.startY;
        }

        // now go back to move function to the ghost collision loop

    }
    // defining few properties 
    private int rowCount = 21;
    private int colCount = 19;
    private int tileSize = 32;

    private int widthBoard = colCount*tileSize;
    private int heightBoard = rowCount*tileSize;

    // done this so that our JPanel has same size as the window 
    // VERY IMPORTANT POINT
    // why made them private?
    // bcz we dont want their values to get changed by someone if they were public . these are the base of our game and we dont want bugs in them
       

    // creating variables to store images 
    private Image wallImage;
    private Image blueGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image orangeGhostImage;
    private Image scaredGhostImage;
    private Image cherryImage;
    private Image cherry2Image;
    private Image powerFoodImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;
        

    //X = wall, O = skip(dont do anything), P = pac man, ' ' = food   
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap ={     // tileMap is an array of strings
        "XXXXXXXXXXXXXXXXXXX\r\n" , //
                        "X        X        X\r\n" , //
                        "X XXXXX  X  XXXXX X\r\n" , //
                        "X X             X X\r\n" , //
                        "X X XXXXXXXXXXX X X\r\n" , //
                        "X                 X\r\n" , //
                        "XXXXX XXXXX XXXXX X\r\n" , //
                        "X                 X\r\n" , //
                        "X XXXXXXX XXXXXXX X\r\n" , //
                        "X        r        X\r\n" , //
                        "X XXXXXXX XXXXXXX X\r\n" , //
                        "X    P            X\r\n" , //
                        "X XXXXX XXXXX XXXXX\r\n" , //
                        "X                 X\r\n" , //
                        "X X XXXXXXXXXXX X X\r\n" , //
                        "X X             X X\r\n" , //
                        "X XXXXX  X  XXXXX X\r\n" , //
                        "X        X        X\r\n" , //
                        "X XXXXXXXXXXXXXXX X\r\n" , //
                        "X                 X\r\n" , //
                        "XXXXXXXXXXXXXXXXXXX" ,

};
    // this is the tile map which i am using we can create anything which we want
    // here we have array of strings and each string is a row 

    // now i had to go through the tile map and create objects like walls , foods , and ghosts , etc
    // for this i create another function below the PacMan function and i name it as loadMap



    // creating hashset for the constructor block used above
    HashSet<Block> walls;   // it is called as hashset of block and is named as walls
    HashSet<Block> foods;  // hashset of block and is named as foods
    HashSet<Block> ghosts;  // hashset of block and is named as ghost
    Block pacman;   // we had declared a block for pacman 

    // now we had done the representation of the objects in the game 
    // moving further with the tile map




    // creating a timer for game loop
    Timer gameLoop;
    // now inside the below constructor i initialize this variable




    // making the ghost move
    // creating an array of character
    char [] directions = {'U' , 'D' , 'R' , 'L'}; // up down right left
    // creating a rando object
    Random random = new Random();
    // basically for each ghost we are going to randomly select the direction
    // moving to the constructor PacMan



    // adding reamining things like score , lives and etc
    int score =0;
    int lives  = 3; // by default we start with 3 lives 
    boolean gameOver = false;  
    // pacman has 3 lives and if he collides with the ghost it will lose one life and when lives = 0 then game over becomes true . and if it is gameover then we stop the pacman from moving 
    
    // lets starts with the score
    // everytime the packman collides the the food it gonna eat it and gain 10 points . and everytime the packman eats the food we want to remove that food from hashSet

    // go to move function





    // creating a constructor ( constrictor is a special method that rus automaticaly when an object of the class is created)
    PacMan(){   // here the PacMan constructor sets up the game board ( which extends JPanel)

        setPreferredSize(new Dimension(widthBoard , heightBoard));   // this sets the preffered size of the JPanel which means it tells the layout manager that widht = widthboard and height = heightboard
        setBackground(Color.BLACK);

        addKeyListener(this);  // when the class PacMan implements keylistener it takes on the properties of keylistener . so i dont need to create a seperate key listener object i can just reference pacman using this 
        // this keylistner is going to listner the three functions which are written keytyped, keypressed , keyreleased to process the key processes

        // now i need to ensure that our JPanel listens to key presses. in a window we can have multiple components which all can be listening key presses. but in this key we had one component ( declared just above this) but we need to make sure that this is the component that listens to the key presses
        // so i write 
        setFocusable(true);
        // now go back to App.java file and write pacmanGame.requestFocus(); but make sure to write it before setvisible
        // after running the file when we press any key we got keyevent printed as the code number so the arrow keys as specific number such as 
        // 37 -> left arrow
        // 39 -> right arrow
        // 38 - > up arrow
        // 40 -> down arrow

        // now we want to make our pacman movable 
        // if i want to move the pacman left or right it is basically the x axis line. for left i will go -ve x axis and for right i will go towards +ve x axis as i am going away from zero
        // similary top is moving -ve y axis bcz i am moving close to zero and +ve y axis when moving down bcz i am moving away from zero 
        // therefore in order to move the pacman we need to set the velocity in x direction and y direction 
        // therefore i will add three more variable in the class Block




        // now we had our JPanel . now go back to App.java file and create an instance/object of this JPanel now 




        // coming back from there
        // lets add images

        // for the images we are creating member variables to store the images

        // so i created them outside the constructor but inside the class
        

        // loading images inside the constructor 
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();   // in the resource we need to specify the path of the image where it is located . getClass refers to the class PacMan where we are writing the code. and this file is located inside src folder and our images are also located inside the same folder so we made a string inside getResource "./" this basically means that we are going to look from the same folder and we just need to specify the image name. if i had not written getImage then it would have created an image icon. but since we need the image so i used .getImage()
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage(); 
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage(); 
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage(); 
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage(); 
        scaredGhostImage = new ImageIcon(getClass().getResource("./scaredGhost.png")).getImage(); 
        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage(); 
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage(); 
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage(); 
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage(); 
        powerFoodImage = new ImageIcon(getClass().getResource("./powerFood.png")).getImage(); 
        cherryImage = new ImageIcon(getClass().getResource("./cherry.png")).getImage(); 
        cherry2Image = new ImageIcon(getClass().getResource("./cherry2.png")).getImage();
        
        // in order to draw the image i need to specify where it is i.e the x and y position and widht and height. and i need to store all these info for fodd , wall , ghost , and pacman 
        // for this i will use a class BLOCK
        


        // now talking about the tile map
        /*
         it will be a 2d array and i will use characters ( we can use numbers also ). therefore i am going to use an array of strings which basically means an array of array of characters 
         therefore we can think of each row as a string within our array. each tile is going to be represented by a character. if a tile has X then it means there is a wall at there. and where we have empty tiles it is whitespace character.
         in these tiles i am going to place a food here and it is in this tiles that the pacman is moving. and O will simply mean that the tile is empty. in this we had like there cannot be placed any food. and this is the one where pacman can bypass the area and like if it 
         goes to left it comes out from right. this is represented as O in this code

         i had done this bcz it is easier to place some O in the maze rather than placing them everywhere inside the maze instead of empty space. 
         also we had p,o,r,b which stands for pink , orange , red , blue. and also we have P which stands for pacman. like they are the starting positions where these all will be placed 


         we start from the 0,0 and if we say we had to go to 4,2 tile then it will be 4*32 , 2*32 bcz tile size will also be have to considered.
         since it had moved 4 tiles across right so 4*32 and 2 tiles down so 2*32
         therefore we create variables specifying the tile widht and height 

         therefore we traverse the array such that if we occured across X then we place a wall , if we occur across p,r,o,b then we place their a ghost and if we came across whitespace then we lace their a food

         therefore we had three main repaeting things - wall , ghost , and food so i will create a 3 different hash sets to ietrate through it and i need not have a hashset for pacman bcz it is used a single time and hence i will create a variable for it 

         why are we using a hashset?
        
         -> hashset does not allow duplicates ensuring each tile is only stored once 
         -> Useful when tracking walls, ghosts, and food because their positions do not repeat in the grid
         -> If you stored wall positions in a list (ArrayList), checking if a tile is a wall would take O(n). 
         -> With a HashSet, checking if a tile is a wall (or contains a ghost/food) takes O(1).
         -> Instead of looping through an entire 2D array, we can just iterate over a HashSet containing the required tiles
         -> HashSet directly checks if (x,y) exists in O(1) time
         

         why not use a 2d array?

         -> Searching for walls, ghosts, and food would still take O(n²) in worst cases.
         -> Memory usage is higher than a HashSet, since it stores all tiles, even empty one


         */



         // checking number of walls , foods , ghosts
         loadMap();  // calling the function loadmap bcz we want to check the number of walls , foods , ghosts and its algo is written inside the function loadMap();
        //  System.out.println(walls.size());  // walls was the hashset where all the walls when encountered were stored 
        //  System.out.println(foods.size());  // same here
        //  System.out.println(ghosts.size()); // same here
         // now i run the code and i see that it prints the number 

         // but when i run this file then it gives error that the main method not found bcz i have declared main method in app.java so i need to run that file and not PacMan.java
         // therefore our loadmap is working and we have all resources in our asset. now i comment our these print statements




         // ghost movement  ( after loadMap())
         // iterating through each ghost
         for ( Block ghost : ghosts)
         {
            char newDirection = directions[random.nextInt(4)];  // since directions has four characters and random.nexint(4) gives a number betwen zero and 4 but not 4 so total of 4 numbers(0,1,2,3) it can give therefore we have directions[i] where i can be {0,1,2,3} . and the i is the index therefore if directions[0] then U character if 1 then D characters as according we had made the array of charcaters 
            ghost.updateDirection(newDirection);  // therefore basicallt we assign each ghost a new direction

         }
         // now go to the move function to consider abt the collisions of the ghost




         gameLoop = new Timer(50 , this);  // the delay is in milliseconds . and this refers to the ActionListener which is implemented in the class PacMan. basically it means that this class where you are using me must have implemented action listener
         // there are 1000 ms in 1 sec so there are 1000/50 = 2ofps i.e 20 frame per second 

         // It creates a repeating timer that calls an actionPerformed() method every 50ms. This is typically used for game loops to update animations, movement, or physics.
         // how does it work?
         // -> first we will start the timer by gameLoop.start();
         // -> this will start the timer triggering actionPerformed() every 50ms
         // -> since this refers to the class implementing ActionListener, we must have 
        //  @Override
        //  public void actionPerformed(ActionEvent e) {
        //  // Update game logic (e.g., move Pac-Man, detect collisions, repaint screen)
        //  repaint();
        //  }
        // -> This repaints the game screen every 50ms, creating the illusion of motion.

        gameLoop.start();  // starting the timer 

        // now the loop has started and when i run the loop is running but i dont see any changes so lets add some features of moving the pacman up down when pressed key
        // for that we need key listner like wise action listener . so we write it above where we written ActionListner

    }  //  this is the end of PacMan function



    public void loadMap(){
            
        // i need to initilise all the hashset first 

        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();


        for ( int r = 0 ; r<rowCount ; r++)   // r stands for row
        {
            for (  int c = 0 ; c<colCount ; c++)   // c stands for column
            {
                String row = tileMap[r];   // getting the current row bcz tileMap is our map and r index is the row index so we are getting the loop indexing 
                char tileMapChar = row.charAt(c);  // getting the current character
                // what we had done in the above two lines is that we getting the row and then we are getting the character present at that row with the col index
                // we had declared two variables 1) row 2) tileMapChar
                // the string row represents the single row in the tile map from index r=0 to r<rowCount
                // now we are using inbuilt string operations charAt() to acces the character present at that position
                // row.charAt(c) acceses the character from the row at index c where c represents the columns 


                // we got a char at a specific tile now we need to figure out where this tile is. therefore to draw we need x , y positions and width and height of tile 

                // getting x position ( it will be the 'how many columns we are from the left')
                int x = c*tileSize;
                // getting y position (  it will be the 'how many rows from the top')
                int y = r*tileSize;


                if ( tileMapChar == 'X'){  // block wall

                    Block wall = new Block(wallImage , x , y , tileSize , tileSize);  // since we had created a class Block and a constructor Block also so here we are creating an object for the constructor Block. this is the object for constructor bcz here i had declared paramters also so it tells the program that we creating an object for constructor
                    walls.add(wall);  // Adds the newly created wall to a HashSet that stores all the walls in the game.
                    // this ensures that all walls are stored in one list which is walls here
                    // it makes easier to draw all the walls in the game 
                    // used for collision detection so that pacman cant go throught walls
                }

                else if ( tileMapChar == ' ')  // food
                {
                    Block food = new Block(null, x+14, y+14, 4,  4);  // here there wont be a image so i draw a rectangle programatically. for image i set it to null . since the rectangle will be made small so we decrease the size to 4 pixels and we offset the position to x+14 , y+14
                    foods.add(food);   // it is of the form  HashSet.add(objt);

                    // x+14? y+14?
                    // since our tile size is 32x32 but we had made here 4x4 so our image here will be a small particle therefore 32-4 is 28 therefore to go to this 4x4 rectangle we had to traverse 14 to the rigth from left and 14 from top to down 

                }


                else if ( tileMapChar == 'O')   // skip
                {
                    continue;  // skip it dont do anything
                }
                // there is no need for this but we still added 

                else if( tileMapChar == 'o')   // orange ghost
                {

                    Block OrangeGhost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(OrangeGhost);
                }


                else if( tileMapChar == 'b')   // blue ghost
                {

                    Block BlueGhost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(BlueGhost);
                }


                else if( tileMapChar == 'r')   // red ghost
                {

                    Block RedGhost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(RedGhost);
                }


                else if( tileMapChar == 'p')   // pink ghost
                {

                    Block PinkGhost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(PinkGhost);
                }


                else if ( tileMapChar == 'P')  // pacman
                {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);   // here we had created a pacman character and have stored it in pacman variable
                    // in the starting of the game we want our pacman to be facing towards right so we right pacmanRightImage
                    // also bcz pacman was single entity so we didnt created a hashset for it
                    // we had done this to treat pac man as a game object making collision detection easier
                }

                // now moving back to PacMan constructor and checking the number of walls, foods , ghosts

            }
        }
    } 





    // creating a function paintComponent in this we will draw all of our objects onto the game

    public void paintComponent(Graphics g)   // it will take in graphics g
    {
        super.paintComponent(g);  // basically super is going to import the function "paintComponent" from JPanel

        // calling draw function
        draw(g);  // this will draw graphics g
    }

    public void draw(Graphics g)  // taking graphics g 
    {
        // lets draw our pacman first
        // since we have pacman as a block object and it has the information x,y position and width and height of pacman
        // 1) firsly lets draw a rectangle at that position just for testing
        // g.fillRect(pacman.x, pacman.y, pacman.width, pacman.height);   // this prints a rectangle at the position we had assigned for pacman

        // 2) but i want pacman to be there so
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width , pacman.height , null);   // here we had done two extra things . 1) we had written the name of variable pacman where we had stpred the image pacman so we write pacman.image and at last we had written null which is used so that the java automatically loads the image

        // doing the same for other images

        // displaying ghost
        for ( Block ghost : ghosts)  // here i had created a for each loop where ghost is the element declared by us it will be used to traverse the whole hashset ghosts written after : . therefore this for loop displays all the items in the ghosts hashset
        {
            g.drawImage(ghost.image , ghost.x, ghost.y, ghost.width , ghost.height , null);
            
        }


        // displaying walls
        for ( Block wall : walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width , wall.height , null);
        }


        // displaying foods

        // changing colour to white
        g.setColor(Color.WHITE);
        for ( Block food : foods){
            // g.drawImage(food.image, food.x, food.y, food.width , food.height , null);
            // this is wrong bcz we dont have image for the food 
            //  we will use rectangle

            g.fillRect(food.x, food.y, food.width, food.height);

        }

        // now move back to paintComponet function
        // since the paintcomponet function only gets called once so we only draw one time
        // but in game we are constantly moving all the characters so the ghost move up down left right so we need to constantly redraw on the JPanel to reflect those new positions
        // to do that we need a game loop
        // for that i implement a ActionListener in the main class i.e PacMan extends JPanel implements ActionListener





        // score

        g.setFont(new Font("Arial" , Font.PLAIN , 18));
        if ( gameOver)
        {
            g.drawString("GAME OVER" + String.valueOf(score), tileSize/2 , tileSize/2);
        }
        else
        {
            g.drawString("x" + String.valueOf(lives)+ " Score: " + String.valueOf(score), tileSize/2 , tileSize/2);
        }

    }



    public void move(){
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;
        // based on our code one of these ( on the right side of equal to ) are going to be zero [ since if pacman is moving up then x velocity is zero bcz it is moving in y axis ...]
        // so i added both to the x and y position of pacman 
        // therefore our move function updates the x and y positons of pacman 
        // now we need to call this function before paint
        // i.e inside the below function

        // since velocity is tilesize/4 so we expect the pacman to move one quarter of the tile size 



        // checking wall collision
        // now i want to iterate through all the walls in the hashset
        // so
        for ( Block wall : walls){
            if (collision(pacman , wall))   // if collison happens between pacman and this current wall
            {
                pacman.x -= pacman.velocityX;  
                pacman.y -= pacman.velocityY;
                break; // once we found a collision we dont need to continue with hashset bcz we already foubd the wall that we are colliding to 
                // i will take a step back 
                // what is happening here is 
                // -> If a collision is detected, we undo the last movement, so Pac-Man stays in place.
                // -> Since we break out of the loop after detecting a collision, we avoid unnecessary checks for other walls.
                // -> Instead of stopping abruptly, we just reverse Pac-Man's last movement, making the transition feel natural.
                // -> Pac-Man stays in place instead of going through the wall
                // -> it does not means that the pacman will start moving backwards 

            }
        }

        // now our pacman stops after colliding with the wall but it got stuck sometimes when we there is no way
        // so to make that fix the pacman should only change the direction if it is able to which basically means that it should not change the direction if there is a wall 
        // therefore go towards the update direction function




        // ghost collison

        for ( Block ghost : ghosts){  // for each ghost
            

            // ghost and pacman collision
            if ( collision(ghost, pacman))
            {

                lives -= 1;
                if ( lives == 0)
                {
                    gameOver = true; 
                    return;   // now since the game ends therefore i want to stop the gameloop so go to actionperformed
                }
                resetPositions();  // i have to declare this new function and i will acces the reset function through it 
                // bcz the reset function is in the class Block and i will create this function inside the main PacMan class
                // i had made this function at last just above override

            }



            // adding a condition first to solve the problem 2 we faced
            if ( ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D')
            {
                // ghost.updateDirection('U');  // instead of making the ghost only move up we can make another char array which will choose from up and down only since it is of bound 2 therefore 0 or 1 therefore first two indexes
                char newDirection2 = directions[random.nextInt(2)];
                ghost.updateDirection(newDirection2);
            }
            // we are moving a step forward 
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            // until now the ghost are moving in any random direction and are moving through the walls so we need to do the same for them also like we didi for pacman and wall collision
            for ( Block wall : walls){
                if ( collision(ghost, wall))
                {
                    // stepping backwards
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    // now we want the ghost to change the direction immediately if it collides into a wall so
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        }

        // problem 1: now we are facing a problem that the space where i had made no food place there the ghost/pacman when passed if from left then will get out from right and vice versa
        

        // problem 2: since this problem is solved but now they are running on the same line they are not going else where so since theis is specially happening for a single row so we made a change in the above loop
        

        // now it is all done all are moving perfectly now we need to do pacman and ghost collision , pacman eating food , increasing score , showing count 
        // therefore go in the class PacMan just before constructor




        // checking food collision
        Block foodEaten = null; // currently pacman has not eaten anything 
    
        // iterating over the loop
        for ( Block food : foods)
        {
            if ( collision(pacman , food))
            {
                // pacman will eat the food
                foodEaten = food;
                score += 10;
                // now we want to remove the food from hashSet
                
            }
        }
        foods.remove(foodEaten);  // it removes that food  . note: it should not be placed inside the for loop as we are using for each loop. so any modifications we wanna made are to done outside it 
        // now we want to update the score on the screen so move towards draw function


        // now we are making ghost collison with pacman leading to resetting all the positons therefore we create a reset function in the class Block




        // if food eaten is empty then we reload the map

        if ( foods.isEmpty())
        {
            loadMap();
            resetPositions();

            // map will be reloaded but the lives will not change           
        }




        
    }


    // to detect collision between pacman and wall , pacman and ghost , ghost and wall , pacman with food 
    public boolean collision(Block a , Block b){  // taking two blocks
        // we will use a specific formula to detect collison between two blocks 
        // note: every image/ object on our screen is a rectangle. although pacman is round but its image is a rectagngle and is not visible bcz it is transparent


        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;


        // now i am going to move function to make some checks
            /*
                This is the AABB (Axis-Aligned Bounding Box) Collision Detection formula, which checks whether two rectangular objects (like Pac-Man and ghosts, walls, or food) are colliding.
                a → Represents Pac-Man, a ghost, or any moving object.
                b → Represents another object like a wall, ghost, or food.
                For collision to occur, all these conditions must be true:

                a.x < b.x + b.width
                → Checks if the left side of a is to the left of b's right side.
                a.x + a.width > b.x
                → Checks if the right side of a is to the right of b's left side.
                a.y < b.y + b.height
                → Checks if the top side of a is above b's bottom side.
                a.y + a.height > b.y
                → Checks if the bottom side of a is below b's top side.
                If all four conditions are met, it means a and b are overlapping, so collision has occurred.
                


                Pac-Man collides with walls → Stop movement.
                ->Pac-Man collides with food → Eat food & increase score.
                ->Pac-Man collides with a ghost → Lose a life or game over.
                ->Ghosts colliding with walls → Change direction.
            */



        
    }



    public void resetPositions()
    {
        pacman.reset();  // this sets the x , and y positions back to the original position
        // now since it is resetted therefore now i dont want the pacman to move until the user press a key so
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        // lets do the same for each ghost
        for ( Block ghost : ghosts)
        {
            ghost.reset();
            // updating the velocity 
            // in case of ghost it will give them a new direction so 
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }



    @Override   // this gets created automatically when i press quick fix at the error i was getting when written implements ActionListener
    public void actionPerformed(ActionEvent e) {
        move();  
        repaint();   // it is going to call paintComponent

        if ( gameOver)   // if this is true 
        {
            gameLoop.stop();  // then do this 
        }

        // but we noticed that it got stuck at the same place but i want to reset everything to same position when i press any key.  so go to key released  


    }// in order to make this function run we need a game loop so we create a timer

    // therefore 
    // updating the positions of all the objects and redraw the frame and update again and redraw again

    // now what we had to do is we need to check if the pacman is colliding against the wall. if so then we need to stop it so lets create a function for it



    // keylistner automatically generates three methods when done quickfix
    @Override
    public void keyTyped(KeyEvent e) {  // when we press a key that has a corresponding character
        
    }



    @Override
    public void keyPressed(KeyEvent e) {  // it is basically when you press on any key . as long as i press a key i would trigger this function. i can hold onto the key as well
        
    }



    @Override
    public void keyReleased(KeyEvent e) {   // it only gets triggered when we press a key and let it go and release the key 
        // System.out.println("KeyEvent: "+ e.getKeyCode());  
        // now to make our key presses work . we need to add two things in our constructor adding key listner 

        // now i comment out that prinitng statement


        if ( gameOver)
        {
            loadMap(); // reloading the map . this is done bcz i want to add all the foods back into the hashset 
            resetPositions(); // resetting the positions 
            // now we want each object a new direction to move in  so
            lives = 3;  // making lives again 3
            score  = 0;  // making score again 0
            gameOver = false;  // setting gameover to be false
            gameLoop.start();  // starting the game loop 

        }

        // now there maybe a case where the pacman has eaten all the food so i can load new levels but here i will load the same map




        if (e.getKeyCode() == KeyEvent.VK_UP)   // when i pressed the up arrow then i call this function
        {
            pacman.updateDirection('U');   // passing U
        }

        else if (e.getKeyCode() == KeyEvent.VK_DOWN)   // when i pressed the down arrow then i call this function
        {
            pacman.updateDirection('D');   // passing D
        }

        else if (e.getKeyCode() == KeyEvent.VK_LEFT)   // when i pressed the left arrow then i call this function
        {
            pacman.updateDirection('L');   // passing L
        }

        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)   // when i pressed the right arrow then i call this function
        {
            pacman.updateDirection('R');   // passing R
        }

        // this will update the direction and velocity but we actually need to move the pacman so we need to keep updating the paintComponent also 
        // so create a function move just above the override function of action listener


        // now we are trying to update the image with the direction updation

        if ( pacman.direction == 'U')
        {
            pacman.image = pacmanUpImage;
        }


        else if ( pacman.direction == 'D')
        {
            pacman.image = pacmanDownImage;
        }


        else if ( pacman.direction == 'L')
        {
            pacman.image = pacmanLeftImage;
        }


        else if ( pacman.direction == 'R')
        {
            pacman.image = pacmanRightImage;
        }

        // now we had created different cases to do this instead of writing this one line inside the above if else cases where we are checking for the key up down left and right
        // and updating the direction
        // but if you press the key up then it does not mean that the packman will move upwards bcz there maybe a wall so if i could have placed this image upadation line inisde them just below updateDirection('U') then it would have resulted in falsy manner 



        // now we are going to make the ghost move 
        // they will move randomly 

        // for that lets move on to the class PacMan
    }


}