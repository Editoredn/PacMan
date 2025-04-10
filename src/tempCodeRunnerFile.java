// src is my source folder. i had to insert my images in this folder only 
// App.java is the main file where we will run our code 



import javax.swing.JFrame;   // Jframe is going to be our window. before creating a window we need to determine how big our window is going to be. therefore we need to specify its width and height 
// it is a import statement that allows you to use the JFrame class from the swing package
// j frame class in Java swing is used to create a GUI window
// it acts as a main container for java swing applications
// we can add buttons , text fileds , labesl and other UI componets inside a JFrame




public class App {
    public static void main(String[] args) throws Exception {
        
        /*
 
            creating our frame

            we create a map and this will be our frame so we divide it into tiles/square
            suppose our map has 19 columns ( 0 to 18) and 21 rows ( 0 to 20).  and our tile size is 32px therefore the width of the frame is going to be 19*32px and height of the frame is going to be 21*32px

            // lets implement it 
 
        */

        int rowCount = 21;
        int colCount = 19;
        int tileSize = 32;

        int widthBoard = colCount*tileSize;
        int heightBoard = rowCount*tileSize;


        // lets create our window

        JFrame frame = new JFrame("Pac Man");  // here we have created a instance object/ object frame to the JFrame class. we will use this object to access various methods under this JFrame class
        // "Pac Man" is basically the name of the window/frame
        // frame.setVisible(true);   // making our frame visible to user
        // the above line is commented out bcz we dont 
        frame.setSize(widthBoard  , heightBoard);   // setting the size of the board 
        frame.setLocationRelativeTo(null);  // making our window to be visible at centre of screen 
        frame.setResizable(false);  // i dont want the user to resize the widht and height of the frame so i set the boolean false

        // since in the window we had the option to close the window ( a cross sign ) . so now i will create a method to allow the user to close the window when clicked on that cross
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // setVisible() , setSize..... all these are in built methods in JFrame class

        



        // till the above code we have created our frame window when we run this code we get a screen where apllication name is PAC MAN as i written 
        // but the window is blank white window since i did nothing in the designing work. designing is done in JPanel. Jpanel will show our game 
        
        // i could have create JPanel here but i will create another java class file in source folder i.e in src




        // creating an instance of the JPanel created inside PacMan file 
        PacMan pacmanGame = new PacMan();
        // therefore we have our JPanel and now i have to add this JPanel to our frame 
        // therefore
        frame.add(pacmanGame);  // therefore our JPanel is in the frame now 
        frame.pack();   // this is done to ensure that we get a full size of our JPanel into the frame window i.e JPanel is fitted inside the frame 


        pacmanGame.requestFocus();



        // bcz we want the the frame to be visible to the user only when all the components are visible therefore i commnet out the above line setVisible and add it here bczz i made some changes 
        frame.setVisible(true);

        // if i run at this stage then it was not running and error was that 'cannot access PacMan'. it was happening bcz we had to compile and run the PacMan.java file first before accessing it in other file




        // alright we had build our window. now we will build the game. first lets add images
        // lets go to PacMan.java
    }
}
