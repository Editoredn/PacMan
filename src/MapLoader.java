package src;

public class MapLoader {


    //X = wall, O = skip(dont do anything), P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red


    // THEME 1 : LEVEL 1 TO 5
    /*
     PACMAN = P
     BUSHES = :
     WALSS = X
     SPACEENTRY = .
     EXIT ENTRY = E
     red ghost = r
     orange ghost = a

     black boxes [ non one can enter there ] = <
     */


    // problem : ghost looks very small [fixed]
    private static final String[] tileMap1 ={     // tileMap is an array of strings

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<XXXXXXXXXXXXXXXXXXX<" ,
            "<X       :X        X<" ,
            "<X XXXXXX:X XXXXXX X<" ,
            "<X X     :       X X<" ,
            "<X X XXXXXXXXXXX X X<" ,
            "<X                 X<" ,
            "<XXXXX XXXXX XXXXX X<" ,
            "<X                 X<" ,
            "<X XXXXXXX XXXXXXX X<" ,
            "<X        r        X<" ,
            "<X XXXXXXX XXXXXXX X<" ,
            "E:::  P          :::." ,
            "<X XXXXX XXXXX XXXXX<" ,
            "<X              :  X<" ,
            "<X X XXXXXXXXXXX:X X<" ,
            "<X X            :X X<" ,
            "<X XXXXXX X XXXXXX X<" ,
            "<X        X        X<" ,
            "<X XXXXXXXXXXXXXXX X<" ,
            "<X      :::::      X<" ,
            "<XXXXXXXXXXXXXXXXXXX<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,





    };
    // this is the tile map which i am using we can create anything which we want
    // here we have array of strings and each string is a row

    // now i had to go through the tile map and create objects like walls , foods , and ghosts , etc
    // for this i create another function below the PacMan function and i name it as loadMap



    // level 2: easy

    // problem : orange ghost is also tiny [ fixed]
    private static final String[] tileMap2 = {


            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<XXXXXXXXXXXXXXXXXXX<" ,
            "<X        X        X<" ,
            "<X XXXXXX:X:XXXXXX X<" ,
            "<X X  X  :X:  X  X X<" ,
            "<X X XX X:X:X XX X X<" ,
            "<X X             X X<" ,
            "<X X XX XXXXX XX X X<" ,
            "<X        r        X<" ,
            "<XXX X XXX XXX X XXX<" ,
            "E::       a       ::." ,
            "<XXX X XXX XXX X XXX<" ,
            "<X   X         X   X<" ,
            "<X X XX XXXXX XX X X<" ,
            "<X X             X X<" ,
            "<X X XXXX:X:XXXX X X<" ,
            "<X X     :P:     X X<" ,
            "<X XXXXXX:X:XXXXXX X<" ,
            "<X        X        X<" ,
            "<X XXXXXXXXXXXXXXX X<" ,
            "<X                 X<" ,
            "<XXXXXXXXXXXXXXXXXXX<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,

    };



// level 3: easy

    private static final String[] tileMap3 = {
            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<XXXXXXXXXXXXXXXXXXX<" ,
            "<X         a       X<" ,
            "<X XXX XXXXXXX XXX X<" ,
            "<X X::           X X<" ,
            "<X X:XXXXX XXXXX X X<" ,
            "<X X X         X X X<" ,
            "<X X X XXXXXXX X X X<" ,
            "<X   X    r    X::::E" ,
            "<XXX XXXXXXXXXXX:XXX<" ,
            "<X        :     :  X<" ,
            "<XXX XXXXXXXXXXX:XXX<" ,
            "<X   X         X   X<" ,
            "<X X X XXXXXXX X X X<" ,
            "<X X X         X X X<" ,
            "<X X XXXXX XXXXX X X<" ,
            ".::::    P         X<" ,
            "<X XXX XXXXXXX XXX X<" ,
            "<X X       X     X:X<" ,
            "<X XXXXXXX X XXXXX:X<" ,
            "<X                :X<" ,
            "<XXXXXXXXXXXXXXXXXXX<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,


    };



// level 4: easy

    private static final String[] tileMap4 = {
            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<XXXXXXXXXXXXXXXXXXX<" ,
            "<X       :X        X<" ,
            "<X XXXXXX:X XXXXXX X<" ,
            "<X    :::: r       X<" ,
            "<XXX XXXXXXXXXXX XXX<" ,
            "<X                 X<" ,
            "<X XX XX XXXXX XX:XX<" ,
            "<X    X         X: X<" ,
            "<X XX X XXXXXXX X:XX<" ,
            ".       a           E" ,
            "<X:XX:X XXXXXXX X XX<" ,
            "<X:: :X         X  X<" ,
            "<X XX XX XXXXX XX XX<" ,
            "<X                 X<" ,
            "<XXX XXXXXXXXXXX XXX<" ,
            "<X        P        X<" ,
            "<X XXXXXX X XXXXXX X<" ,
            "<X X::    X      X X<" ,
            "<X X:XXXXXXXXXXXXX X<" ,
            "<X  :              X<" ,
            "<XXXXXXXXXXXXXXXXXXX<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,


    };



// level 5: easy

    // problem : in this bushes teleports the pacman [ RESOLVED]
    private static final String[] tileMap5 = {
            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<XXXXXXXXXXXXXXXXXXX<" ,
            "<X           r     X<" ,
            "<X XXXXX:XXX XXXXX X<" ,
            "<X X   a:X X     X X<" ,
            "<X X XXX:X X X X X X<" ,
            "<X X X     X X X X X<" ,
            "<X X X XXX X X X X X<" ,
            "<X   X X   X X X   X<" ,
            "<XXX X   X X X X XXX<" ,
            ".:::   X X   X   :::E" ,
            "<XXX X X XXX X X XXX<" ,
            "<X   X X     X X   X<" ,
            "<X X X XX XX X X X X<" ,
            "<X X X ::: X X X X X<" ,
            "<X X XXX X X X X X X<" ,
            "<X X     X X     X X<" ,
            "<X XXXXX XXX XXXXX X<" ,
            "<X    ::: P  :::   X<" ,
            "<X XXXXX XXX XXXXX X<" ,
            "<X                 X<" ,
            "<XXXXXXXXXXXXXXXXXXX<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,


    };


// level 6: easy
// theme 2: 6-10

/*

PACMAN = P
     SPEED ZONES = S
     WALLS = [
     SPACEENTRY = .
     EXIT ENTRY = E
     red ghost = g
     orange ghost = s
     PINK GHOST = t
     WRAP ENTRANCE = N
     WRAP EXIT = &

 */


    // problem : ghost images are wrong . their background needs to be removed , speed zones are not looking speed zone [ fixed all]
    private static final String[] tileMap6 = {
            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<[[[[[[[[[[[[[[[[[[[<" ,
            "<[               SSE<" ,
            "<[ [[[[[ [[[[[ [[[ [<" ,
            "<[ [  s[ [   [ [   [<" ,
            "<[ [ [ [ [ [ [ [ [ [<" ,
            "<[ [ [ [ [ [ [ [ [ [<" ,
            "<[ [ [ [[[ [ [[[ [ [<" ,
            "<[ [ [   g [     [ [<" ,
            "<[ [S[[[ [[[ [[[[[ [<" ,
            "<[ [SSS      [     [<" ,
            "<[ [[[[[[[[[S[ [[[[[<" ,
            "<[    t    [S[     [<" ,
            "<[ [[[[[[[[[S[ [[[ [<" ,
            "<[ [     [ [     [ [<" ,
            "<[ [ [[[ [ [[[ [ [ [<" ,
            "<[ [ [   [   [ [ [ [<" ,
            "<[ [ [ [[[[[ [ [ [ [<" ,
            "<[ [ [       [ [ [ [<" ,
            "<[ [ [[[[[[[[[ [ [ [<" ,
            ".  P               [<" ,
            "<[[[[[[[[[[[[[[[[[[[<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,


    };

// level 7: easy


    // problem is : the pacman can enter from wrapentrance and exit from exit. but the wrap exit is not working as wall it should. and for ghosts the wrap entrance and exit must be like wall but here it is not
// resolved the problem
    private static final String[] tileMap7 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<[[[[[[[[[[N[[[[[[[[<" ,
            "<[        [        [<" ,
            "<[S[[[[[[ [ [[[[[[ [<" ,
            "<[S[      [      [ [<" ,
            "<[S[ [[[[[[[[[[[ [ [<" ,
            "<[S[      g s    [ [<" ,
            "<[S[[[[[[[ [ [[[[[[[<" ,
            "<[       [ [ [     [<" ,
            "<[[[[[[[ [ [S[ [[[[[<" ,
            ".           S       E" ,
            "<[[[[[[[ [ [S[ [[[[[<" ,
            "<[       [St [     [<" ,
            "<[ [[[[[[[S[ [[[[[[[<" ,
            "<[ [      S      [ [<" ,
            "<[ [ [[[[[[[[[[[ [ [<" ,
            "<[ [      P      [ [<" ,
            "<[ [[[[[[ [ [[[[[[ [<" ,
            "<[  SSS   [        [<" ,
            "<[[[[[[[[[[ [[[[[[[[<" ,
            "<[                 [<" ,
            "<[[[[[[[[[[&[[[[[[[[<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,

    };


// level 8: easy


    // problem : in thi the same problem is their [ resolved]
    private static final String[] tileMap8 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<[&[[[[[[[[[[[[[[[[[<" ,
            "<[S  g    [        [<" ,
            "<[S[[[[[[ [ [[[[[[ [<" ,
            "<[S[s           t[ [<" ,
            "<[ [ [[[[[ [[[[[ [ [<" ,
            "<[ [ [ SSS SSS [ [ [<" ,
            "<[ [ [ [[   [[ [ [ [<" ,
            "<[   [ [     [ [   [<" ,
            "<[[[ [ [[[[[[[ [ [[[<" ,
            ".SSS             SSSE" ,
            "<[[[ [ [[[[[[[ [ [[[<" ,
            "<[   [ [     [ [   [<" ,
            "<[ [ [ [[[S[[[ [ [ [<" ,
            "<[ [ [         [ [ [<" ,
            "<[ [ [[[[[ [[[[[ [ [<" ,
            "<[ [      P      [ [<" ,
            "<[S[[[[[[ [ [[[[[[ [<" ,
            "<[S[      [      [ [<" ,
            "<[S[ [[[[[[[[[[[[[ [<" ,
            "<[                 [<" ,
            "<[[[[[[[[[[[[[[[[[N[<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,


    };


// level 9: easy

    private static final String[] tileMap9 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<[[[[[[[[[[[[[&[[[[[<" ,
            "<[         s       [<",
            "<[ [[[S[[[ [[[S[[[ [<",
            "<[ [[[S[[   [[S[[[ [<",
            "<[ [[[S[     [S[[[ [<",
            "<[        g        [<",
            "<[ [[[ [[[[[[[ [[[ [<",
            "<[                 [<",
            "<[ [[[[[[[ [[[[[[[ [<",
            ".     SSSSSSSt      E",
            "<[ [[[[[[[ [[[[[[[ [<",
            "<[                 [<",
            "<[ [[[S[[[[[[[ [[[ [<",
            "<[    S   P        [<",
            "<[ [[[S[     [ [[[ [<",
            "<[ [[[ [[ S [[ [[[ [<",
            "<[ [[[ [[[S[[[ [[[ [<",
            "<[        S        [<",
            "<[ [[ [[[[[[[[[[[[S[<",
            "<[             SSSS[<",
            "<[[[[N[[[[[[[[[[[[[[<",
            "<<<<<<<<<<<<<<<<<<<<<",


    };


// level 10: easy

    // SAME PROBLEM [ rsolved]
    private static final String[] tileMap10 = {

            "<<<<<<<<<<<<<<.<<<<<<" ,
            "<[[[[[[[[[[[[[ [[[[[<" ,
            "<[  t           s  [<" ,
            "<[ [[[[[ [ [ [[[[[ [<" ,
            "<[ [   [ [S[ [   [ [<" ,
            "<[ [ [ [ [S[ [ [ [ [<" ,
            "<[ [ [ [ [S[ [ [ [ [<" ,
            "<[S[ [ [[[S[[[ [ [S[<" ,
            "<[S[ [     g   [ [S[<" ,
            "<[S[ [[[[[N[[[[[ [S[<" ,
            "<[S[             [S[<" ,
            "<[S[[[[[ [[[ [[[[[S[<" ,
            "<[       [ P       [<" ,
            "<[S[[[[[ [[[ [[[[[S[<" ,
            "<[S[             [S[<" ,
            "<[S[ [ [[[&[[[ [ [S[<" ,
            "<[S[ [         [ [S[<" ,
            "<[S[ [ [[[[[[[ [ [S[<" ,
            "<[ [ [    S    [ [ [<" ,
            "<[ [ [ [[[S[[[ [ [ [<" ,
            "<[       SSS       [<" ,
            "<[[ [[[[[[[[[[[[[[[[<" ,
            "<<<E<<<<<<<<<<<<<<<<<",


    };





// level 11: easy

// theme 3: 11-14

/*

PACMAN = P

     WALLS = ]
     SPACEENTRY1 = .
     EXIT ENTRY1 = E
     SPACEENTRY2 = -
     EXIT ENTRY2 = e
     red ghost = h
     orange ghost = d
     PINK GHOST = b
     blue GHOST = y
     teleport = T
     electric shock = Q

 */



    // PROBLEM : ANIMATION OF ELECTRIC SHOCK IS TO BE ADDED[ add a full screen animation to the screen which looks amazing ], GHOST SIZE INREASE[ fixed]
    private static final String[] tileMap11 = {

            "<<<E<<<<<<<<<<<<E<<<<" ,
            "<]] ]]]]]]]]]]]] ]]]<" ,
            "<]          d      ]<" ,
            "<] ]]] ]]]] ]]]] ]]]<" ,
            "<] ]  b            ]<" ,
            "<] ] ]]]]]]]]]] ]] ]<" ,
            "<] ] ]           ] ]<" ,
            "<] ] ] ]]]]h ] ] ] ]<" ,
            "<] ] ] ]     ] ] ] ]<" ,
            "<] ] ]   ]]] ] ] ] ]<" ,
            "e    ] ] ] ] ] ] ] Q-" ,
            "<] ]]] ] ] y ] ] ]]]<" ,
            "<] ]   ] ]   ] ] ] ]<" ,
            "<] ] ]]] ]]] ] ] ] ]<" ,
            "<] ] ]         ] ] ]<" ,
            "<] ] ] ]]]]]]] ] ] ]<" ,
            "<] ] ]    P    ] ] ]<" ,
            "<] ] ]]]]]]]]]]] ] ]<" ,
            "<] ]   Q           ]<" ,
            "<] ]]]]]]]] ]]]]]]]]<" ,
            "<]                 ]<" ,
            "<]]]]] ]]]]]]]]]] ]]<" ,
            "<<<<<<.<<<<<<<<<<.<<<",


    };

// level 12: easy


    // PROBLEM :  GHOST SIZE LESS
    private static final String[] tileMap12 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<]]]]]]]]]]]]]]]]]]]<" ,
            "<]        Q        ]<" ,
            "<] ] ]]]]]]]]]]] ] ]<" ,
            "<] ]      h      ] ]<" ,
            "<] ]]]]] ]]]]] ]]] ]<" ,
            "<] ]             ] ]<" ,
            "<] ] ]]]]]]]]]]] ] ]<" ,
            "<] ]      y      ] ]<" ,
            "<] ]]]]] ]]]]] ]]]]]<" ,
            "E   T     ] P  Q    ." ,
            "<] ]]]]] ]]]]] ]]]]]<" ,
            "<] ]      d      ] ]<" ,
            "<] ] ]]]]]]]]]]] ] ]<" ,
            "<] ]             ] ]<" ,
            "<] ]]]]] ]]]]] ]]] ]<" ,
            "<] ]      b      ] ]<" ,
            "<] ] ]]]]]]]]]]] ] ]<" ,
            "<]                 ]<" ,
            "<]]]]]]]]]]]]]]]]]]]<" ,
            "<]        T        ]<" ,
            "<]]]]]]]]]]]]]]]]]]]<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,


    };



// medium levels

// level 13 : medium

    // PROBLEM : SAME
    private static final String[] tileMap13 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<]]]]]]]]]]]]]]]]]]]<" ,
            "<]       ]     ]   ]<" ,
            "<] ]]] ] ]]] ] ] ] ]<" ,
            "<] ]   ]  h  ] ] ] ]<" ,
            "<] ] ]]]]] ]]] ] ] ]<" ,
            "<] ]d      ] T   ] ]<" ,
            "<] ]]]]] ] ] ]]]]] ]<" ,
            "<]     ]Q] ] ]     ]<" ,
            "<]]] ] ] ] ] ] ] ]]]<" ,
            "E    ] ] ]y] ] ]    ." ,
            "<]]] ] ] ] ] ] ] ]]]<" ,
            "<]   ] ] ] ] ] ]   ]<" ,
            "<] ]]] ] ] ] ]]] ] ]<" ,
            "<] ]     ]   Q   ] ]<" ,
            "<] ] ]]] ]]]]]]] ] ]<" ,
            "<] ] ]b            ]<" ,
            "<] ] ]]]]] ]]]]] ] ]<" ,
            "<]   T   ] P ]     ]<" ,
            "<]]]]]]] ] ] ]]]]]]]<" ,
            "<]         ]       ]<" ,
            "<]]]]]]]]]]]]]]]]]]]<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,



    };


// level 14: medium

    private static final String[] tileMap14 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<]]]]]]]]]]]]]]]]]]]]<" ,
            "<]   ]  b    ]     ]<" ,
            "<] ] ] ]]]]]T] ]]] ]<" ,
            "<] ] ]  h  ] ] ]   ]<" ,
            "<] ] ]]] ] ] ] ] ]]]<" ,
            "<] ]   ] ] ] ] ]   ]<" ,
            "<] ]]] ] ] ] ]]]]] ]<" ,
            "<] ]Q  ] ] ]     ] ]<" ,
            "<] ] ]]] ] ]]]]] ] ]<" ,
            "e    ] ]y] ]   ]    -" ,
            "<] ]]] ] ] ] ] ]]] ]<" ,
            "<]     ] ] ] ]     ]<" ,
            "<] ]]]]] ] ] ]]] ]]]<" ,
            "<] ]     ] ] ]     ]<" ,
            "<] ] ]]]]] ] ]]]]] ]<" ,
            "<]   ]d    ]Q      ]<" ,
            "<] ]]]]] ]]]]]]]]] ]<" ,
            "<]     ] P   ]     ]<" ,
            "<]]]T] ]]]]] ] ]]] ]<" ,
            "<]   ]       ]     ]<" ,
            "<]]]]]]]]]]]]]]]]]]]<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,



    };


// level 15


// THEME 4: BONUS MAP
// only pacman
// bonus1wall = {

    // PROBLEM : NOT EATING FOOD [ not fixed ]
    private static final String[] tileMap15 = {

            "<<<<<<<<<<e<<<<<<<<<<" ,
            "<{{{{{{{{{ {{{{{{{{{<" ,
            "<{       {         {<" ,
            "<{ {{{{{ { {{{{{{  {<" ,
            "<{ {   {     {     {<" ,
            "<{ { { {{{{{ { {{ {{<" ,
            "<{ { {       {     {<" ,
            "<{ { {{{ { {{{{{ { {<" ,
            "<{ {     {     { { {<" ,
            "<{ {{ {{ {{{{{ { { {<" ,
            "-     {             e" ,
            "<{ {{{{  {{ {{ {{{ {<" ,
            "<{    {      { {   {<" ,
            "<{ {{ {{{{{{{{ { {{{<" ,
            "<{ {         { {   {<" ,
            "<{ { {{{{{{{ { {{{ {<" ,
            "<{   {           { {<" ,
            "<{ {{{{{ {{{{{ {{{ {<" ,
            "<{       P         {<" ,
            "<{{{{{{{ {{{ {{{{{{{<" ,
            "<{                 {<" ,
            "<{{{{{{{ {{{{{{{{{{{<" ,
            "<<<<<<<<-<<<<<<<<<<<<" ,

    };



// LEVEL 16

// theme 5: 16-20

/*

PACMAN = P

     WALLS = }
     SPACEENTRY1 = .
     EXIT ENTRY1 = E
     SPACEENTRY2 = -
     EXIT ENTRY2 = e
     red ghost = j
     orange ghost = f
     PINK GHOST = n
     blue GHOST = >
     ICE TILES = I
     Button 1 = 1
     gate 1 = A

 */


    // problem :  ghost size increase [fixed ], when pacman is at ice then the game stucks i.e ghost stop moving [ resolved this issue ]  , ICE GATE IMAGE CHNAGE [fixed ]
    private static final String[] tileMap16 = {

            "<<<<<<<<<<<<<<<-<<<<<" ,
            "<}}}}}}}}}}}}}} }}}}<" ,
            "<}       }      f  }<" ,
            "<}I}}}}} } }}}}}}} }<" ,
            "<}I}   }  j  }     }<" ,
            "<}I} } }}}}} } }}}}}<" ,
            "<}I} }       }     }<" ,
            "<}I} } }} }} }}}} }}<" ,
            "<}I}      }     } }}<" ,
            "<}I} }}}} }}}} }} }}<" ,
            ".         n      IIIE" ,
            "<} } }}}} }}}}} } }}<" ,
            "<} }      }     }  }<" ,
            "<} } } }}} } }}}} }}<" ,
            "<} } }       }     }<" ,
            "<} } }}}} }}}}} }}I}<" ,
            "<}   }  >  }       }<" ,
            "<} }}}} } }}}}} }}I}<" ,
            "<}       P         }<" ,
            "<}}} }}}I} }}}}}} }}<" ,
            "<}      I  III     }<" ,
            "<}}}}}}}I}}}}}}}}}}}<" ,
            "<<<<<<<<e<<<<<<<<<<<<" ,



    };


// LEVEL 17 : MEDIUM [TELEPORT]


    // problem :  ice stuck here also[ fixed this ], gate are first lokk like black square only and pacman can pass through it but after button toggle it makes the cave image, and now the pacman cannot pass through it [ fixed]
// fixed
    private static final String[] tileMap17 = {

            "<<E<<<<<<<<<<<<<<<<<<" ,
            "<} }}}}}}}}}}}}}}}}}<" ,
            "<} III   }         }<" ,
            "<}I}}}}} } }}}}}}} }<" ,
            "<}I}A  }  n  }     }<" ,
            "<} } } }}}}} } }}}}}<" ,
            "<} } }  P    }  III}<" ,
            "<} } }}}} }}}} } }I}<" ,
            "<} }     }     } }I}<" ,
            "<} }}}} } }}}} } }I}<" ,
            "e     }   1j    }   -" ,
            "<}I}}} }}} } }}}}} }<" ,
            "<}I  }>      }     }<" ,
            "<}I} }}}}}}}}} }}} }<" ,
            "<} }           }   }<" ,
            "<} }}}}}}}}}}}}} } }<" ,
            "<}   }  f      A   }<" ,
            "<} } }}}}}}}}}}} } }<" ,
            "<} }             } }<" ,
            "<}}}}}} }}} }}}} }I}<" ,
            "<}          IIII  I}<" ,
            "<}}}}}}}}}}}}}}}}}I}<" ,
            "<<<<<<<<<<<<<<<<<<.<<" ,




    };


    //
// LEVEL 18: MEDIUM [ TELEPORT ADVANCED]
// PROBLEM : SAME ISSUES
// fixed
    private static final String[] tileMap18 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<}}}}}}}}}}}}}}}}}}}<" ,
            "<}   f   }       I }<" ,
            "<} }}}}} }A}}}}}}}A}<" ,
            "<} }   }  j  }   I }<" ,
            "<} } } }}}}} } }}}}}<" ,
            "<}I} }       }     }<" ,
            "<}I} }}}}} }}}I} } }<" ,
            "<}I}     }     } } }<" ,
            "<}I}}}}} }I}}}I} } }<" ,
            ".     }    P     IIIE" ,
            "<}I}}}1}}}I} }}}}} }<" ,
            "<}I  }    I  }     }<" ,
            "<}I} }}}}}I}}} }}} }<" ,
            "<}I}           }   }<" ,
            "<} }}}}}}} }}} } } }<" ,
            "<}   }1 n I    }   }<" ,
            "<} } }}}}}I} }}} } }<" ,
            "<} }     }       } }<" ,
            "<}}}}}}}A}}}A}}}}}}}<" ,
            "<}          >      }<" ,
            "<}}}}}}}}}}}}}}}}}}}<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,




    };


// // LEVEL 19: MEDIUM [BUTTON GATES]
// // B -> BUTTON1 WHICH TOGGLES G WHICH IS GATE 1
// // Q -> BUTTON 2 WHICH TOGGLES H WHICH IS GATE 2
// private String [] tileMap19 = {

//     "XXXXXXXXXXXXXXXXXXX",
//     "X B      b        X",
//     "X XXXXX XXXXX XXX X",
//     "X X   X X X X X   X",
//     "X XGGGX X X X XHHHX",
//     "X X H X r o b X G X",
//     "X XXHXX XXXXX XXGXX",
//     "X     X     X     X",
//     "XXXXX X XXX X XXXXX",
//     "X     X X X X     X",
//     "X XXXXX X X XXXXX X",
//     "O   H       G     O",
//     "X XXXXXXX XXXXXXX X",
//     "X X     X X     X X",
//     "X X XXX X X XXX X X",
//     "X X X       X X X X",
//     "X X XXXXXpXXXXX X X",
//     "X X           X X X",
//     "X XXXXXXXXXXXXX X X",
//     "X               Q X",
//     "XXXXXXXXXXXXXXXXXXX"

// };



// // LEVEL 20: MEDIUM [ BUTTON GATES ADVANCED]
// private String[] tileMap20 = {
//     "XXXXXXXXXXXXXXXXXXX",
//     "X B    X       b X",
//     "X XXX XXXXXXXX XXX",
//     "X X G     X    H X",
//     "X X G XXXXXXXX H X",
//     "X X G X r  b X H X",
//     "X XXXXX XXXXXXX XX",
//     "X   X     X     X ",
//     "XXX X XXXXXXX X XX",
//     "X   X X     X X  X",
//     "X XXX X XXX X XXX ",
//     "O     G  X H     O",
//     "X XXXXXXX XXXXXXXX",
//     "X X     X X      X",
//     "X X XXX X X XXXX X",
//     "X X X       H X  X",
//     "X X XXXXXpXXXXXX X",
//     "X XQ           X X",
//     "X XXXXXXXXXXXXXX X",
//     "X           G    X",
//     "XXXXXXXXXXXXXXXXXXX"
// };



    // PROBLEM : SAME
// fixed
    private static final String[] tileMap19 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<}}}}}}}}}}}}}}}}}}}<" ,
            "<}       }      j  }<" ,
            "<} }}}}} }A}}}}}}} }<" ,
            "<} }   }  f  }     }<" ,
            "<} } } }}}}} } }}}}}<" ,
            "<} }1}   >   }     }<" ,
            "<} } }}}}}A}}}}} } }<" ,
            "<} }     }     } }I}<" ,
            "<} }}}}} } }}} } }I}<" ,
            ".   }   }n    }1IIIIE" ,
            "<} }}} }}}}} }}}}} }<" ,
            "<}   }   I   }     }<" ,
            "<} } }}}}I}}}} }}} }<" ,
            "<} }     I     }   }<" ,
            "<}I}}}}}}}}}}} } } }<" ,
            "<}I  }     }1} }   }<" ,
            "<}I} }}}A}}} } }}} }<" ,
            "<}I}    I} P     } }<" ,
            "<}}}}}}}I}}}A}}}}}}}<" ,
            "<}     III         }<" ,
            "<}}}}}}}}}}}}}}}}}}}<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,




    };


    // PROBLEM : SAME
// fixed
    private static final String[] tileMap20 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<}}}}}}}}}}}}}}}}}}}<" ,
            "<}A n   }          }<" ,
            "<} }}}}} } }}}}}}} }<" ,
            "<} }    j  }       }<" ,
            "<} } } }}}}}A} }}}}}<" ,
            "<} } }       }     }<" ,
            "<} } }}}}}f}}}}} } }<" ,
            "<} }         } }   }<" ,
            "<} }}}}}A} }}} } } }<" ,
            "e   IIII   1    IIII-" ,
            "<} }}} }}}A} }}}}} }<" ,
            "<}   }       }     }<" ,
            "<} } }}}}}}}}} }}} }<" ,
            "<} }           }   }<" ,
            "<} } }}}}}}}}} } } }<" ,
            "<}   A  >  }   }   }<" ,
            "<} } }}}}} } }}} } }<" ,
            "<}       } P     } }<" ,
            "<}}} }}} }}} }}} }}}<" ,
            "<}                 }<" ,
            "<}}}}}}}}}}}}}}}}}}}<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,



    };


// level 21:

// theme 6: 21-24

/*

PACMAN = P

     WALLS = `
     SPACEENTRY1 = .
     EXIT ENTRY1 = E
     SPACEENTRY2 = -
     EXIT ENTRY2 = e
     red ghost = k
     orange ghost = z
     PINK GHOST = m
     blue GHOST = O
     LAVA =  V
     TELEPORT 2 = %
     BUTTON 2 = 2
     GATE 2 = B

 */


    // PROBLEM : GHOST SIZE INCREASE [fixed ]
    private static final String[] tileMap21 = {



            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<```````````````````<" ,
            "<`    k            `<" ,
            "<` ` `````V` ``` ` `<" ,
            "<` `       `   O ` `<" ,
            "<` ` ``` ` ` ``` ` `<" ,
            "<` ` `   ` ` ` ` ` `<" ,
            "<` ` ` ``` ` ` ` ` `<" ,
            "<` ` `     `V` %   `<" ,
            "<` ` ````` ` ` `````<" ,
            ".      `  Vm    `   E" ,
            "<` ```` `````` ``` `<" ,
            "<` `        `      `<" ,
            "<` ` ``````V` ``````<" ,
            "<` ` `           ` `<" ,
            "<` ` ` ````` ``` ` `<" ,
            "<`   ` V   z   `   `<" ,
            "<` ````` ``` ````` `<" ,
            "<` `     ` P `     `<" ,
            "<` ` ````` ``````` `<" ,
            "<`   %     V       `<" ,
            "<```````````````````<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,




    };


    // PROBLEM : BUTTON TOGGLE IS NOT WORKING [ fixed ]
    private static final String[] tileMap22 = {




            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<```````````````````<" ,
            "<` k              %`<" ,
            "<` ````` `2``` ``` `<" ,
            "<` `VV ` ` ` ` `   `<" ,
            "<` `V` ` ` ` ` ` ```<" ,
            "<` ` `   `V`   `   `<" ,
            "<` ` ```V V V``` ` `<" ,
            "<` `    V   V  z ` `<" ,
            "<` `````V V V````` `<" ,
            ".       VVVVV       E" ,
            "<` `````V V V````` `<" ,
            "<` ` m  V   V    ` `<" ,
            "<` ` ```V`V`V``` ` `<" ,
            "<` ` `   `V`   ` ` `<" ,
            "<` ` ` ``` ``` `V` `<" ,
            "<`   `         `V` `<" ,
            "<` ``` ``````` `V` `<" ,
            "<` ` VVVV O     `V `<" ,
            "<` ` ````` `````V` `<" ,
            "<`  %     P    2   `<" ,
            "<```````````````````<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,




    };



    // problem : same [ fixed ]
    private static final String[] tileMap23 = {

            "<<<<<<<<<<<<<<<<e<<<<" ,
            "<``````````````` ```<" ,
            "<`   z          2VV`<" ,
            "<` ``` ````````` `V`<" ,
            "<`   `V`       `  V`<" ,
            "<` ` `V` ````` ``` `<" ,
            "<` ` `V` `   `   ` `<" ,
            "<` ` ``` ` ` ``` ` `<" ,
            "<` `VVVVV` m `   ` `<" ,
            "<` ```````` `` ` ` `<" ,
            ".           O       E" ,
            "<` ``````` ` ` ``` `<" ,
            "<`       `V`     ` `<" ,
            "<` ````` `V` ``` ` `<" ,
            "<` `     `Vk `2` ` `<" ,
            "<` ` ````` ``` ` ` `<" ,
            "<`V` `  VVVVV    ` `<" ,
            "<`V` ``````````` ` `<" ,
            "<`V`      P       V`<" ,
            "<` ``````` ```````V`<" ,
            "<`                V`<" ,
            "<``` ```````````````<" ,
            "<<<<-<<<<<<<<<<<<<<<<" ,




    };



    private static final String[] tileMap24 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<```````````````````<" ,
            "<`                 `<" ,
            "<`%` ````` ``` ``` `<" ,
            "<` ` `   `V` ` ` `%`<" ,
            "<` ` ` ` `V` ` ` ` `<" ,
            "<` ` ` ` `Vk` ` ` ``<" ,
            "<` ` ` ` ``` ` ` ` `<" ,
            "<`   ` `     `   ` `<" ,
            "<` ``` ``` ``` ``` `<" ,
            ". VVVV    m         E" ,
            "< ````` ``` ```V````<" ,
            "<`     `     `VV   `<" ,
            "<` ``` ` ``` ` ``` `<" ,
            "<`%`V` ` `z` ` `V  `<" ,
            "<` ` ` ` ` ` ` ` ` `<" ,
            "-   `   `   `     ` e" ,
            "< ` ````` ``` ``````<" ,
            "<`V      P    VVV  `<" ,
            "<`V`````` ````````%`<" ,
            "<`VV          O    `<" ,
            "<```````````````````<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,


    };


// theme 7: 25

/*

bonus 2

PACMAN = P

     WALLS = /
     SPACEENTRY1 = .
     EXIT ENTRY1 = E
     SPACEENTRY2 = -
     EXIT ENTRY2 = e


 */

    // problem :  pacman not eating food [not fixed]

    private static final String[] tileMap25 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<///////////////////<" ,
            "</       /         /<" ,
            "</ ///// / ///// / /<" ,
            "</ /   / / /   / / /<" ,
            "</ / / / / / / /// /<" ,
            "</ / / /   / /   / /<" ,
            "</ / /// /// /// / /<" ,
            "</ /     /     / / /<" ,
            "</ ///// / ///// / /<" ,
            ".      /   /        E" ,
            "</ /// /// /// /// /<" ,
            "</ /         / / / /<" ,
            "</ / /////// / / / /<" ,
            "</ /         / / / /<" ,
            "</ / / /// / / / / /<" ,
            "</ /           / / /<" ,
            "</ / /////// /// / /<" ,
            "</   /   P       / /<" ,
            "</ /// /////////// /<" ,
            "</     /           /<" ,
            "<///////////////////<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,



    };




// theme 8: 26-30

/*

PACMAN = P

     WALLS = _
     SPACEENTRY1 = .
     EXIT ENTRY1 = E
     SPACEENTRY2 = -
     EXIT ENTRY2 = e
     red ghost = y
     orange ghost = x
     PINK GHOST = q
     blue GHOST = K
     phantom = ~
     reverse = R

 */


    // problem :  the phantom zoens are not working for ghost[fixed] , wall image change[ fixed]
    private static final String[] tileMap26 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<___________________<" ,
            "<_      ~_~      y _<" ,
            "<_ _______________ _<" ,
            "<_ _~  _ _ _~  ~ _ _<" ,
            "<_ _ _ _ _ _ __ __ _<" ,
            "<_ _ _ _ _ _ _ _ _ _<" ,
            "<_ _ _ _   _ _ q _ _<" ,
            "<_ _ _ _ _ _ _ _ _ _<" ,
            "<_ _ _  x  _ _ _ _ _<" ,
            "e    _____ _ _ _~   -" ,
            "<_ _ _  K  _ _ _ _ _<" ,
            "<_ _ _ ___ _   _ _ _<" ,
            "<_ _~_ _ _ _ ___ _ _<" ,
            "<_ _____   _ _~  _ _<" ,
            "<_ _~_ _ _ _ _ ___ _<" ,
            "<_ _ _    ~_ _     _<" ,
            "<_ _ _______ _____ _<" ,
            ".         P         E" ,
            "<_ _______ _______ _<" ,
            "<_                 _<" ,
            "<___________________<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,




    };


    // LEVEL 27:


    // problem same
    private static final String[] tileMap27 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<___________________<" ,
            "<_                 _<" ,
            "<_ _____ _____ ___ _<" ,
            "<_ _~ ~_ _ _ _ _~  _<" ,
            "<_ _ _ _ _ _ _ _ _ _<" ,
            "<_ _ K _ y   x _~  _<" ,
            "<_ __ __ _____ __ __<" ,
            "<_     _     _     _<" ,
            "<_____ _ ___ _ _____<" ,
            "<_    ~_ _~_ _~    _<" ,
            "<_ _____ _ _ _____ _<" ,
            "e        P          -" ,
            "<_ _______ _______ _<" ,
            "<_ _     _ _     _ _<" ,
            "<_ _ ___ _ _ ___ _ _<" ,
            "<_ _ _       _~_ _ _<" ,
            "<_ _ _____q___ _ _ _<" ,
            "<_ _~           ~_ _<" ,
            "<_ _____________ _ _<" ,
            "<_                 _<" ,
            "<___________________<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,





    };


    // SAME PROBLEM
    private static final String[] tileMap28 = {

            "<<<<<<<<<<<<<<<<.<<<<" ,
            "<_______________R___<" ,
            "<_    y_    x_     _<" ,
            "<___ ___ ___ ___ _ _<" ,
            "e   R_       _~  _ _<" ,
            "<_ ___ _____ ___ _ _<" ,
            "<_ _       _  ~_ _ _<" ,
            "<_ _ _ _ ___ _ _ _ _<" ,
            "<_ _q_   _   _ _ _ _<" ,
            "<_ _ _ _ _ ___ _ _ _<" ,
            "<_   _ _ _ _~      _<" ,
            "<___ _ _ _ _ _____ _<" ,
            "<_   _ _~    _   _ _<" ,
            "<_ ___ _____ ___ _ _<" ,
            "<_ _~        _K  _ _<" ,
            "<_ _ _______ ___ _ _<" ,
            "<_ _ _~    _ _   _ _<" ,
            "<_ _ _ ___ _ _ ___ _<" ,
            "<_ _ _   _ _ _R     -" ,
            "<_ _ ___ _ _ _____ _<" ,
            "<_       P         _<" ,
            "<_____ _____________<" ,
            "<<<<<<E<<<<<<<<<<<<<<" ,



    };

    private static final String[] tileMap29 = {

            "<<<<<<<<<<<<<<<<<-<<<" ,
            "<________________ __<" ,
            "<_        K        _<" ,
            "<_ _____ ___ _____ _<" ,
            "<_ _~  _         _ _<" ,
            "<_ _ _R_______ _ _ _<" ,
            "<_ _ _     q   _ _ _<" ,
            "<_ _ _ ___ _____ _ _<" ,
            "<_ _       _     _ _<" ,
            "<_ ___ _ _ _ ___ _ _<" ,
            ".     ~_ y _~       E" ,
            "<_ ___ _ _ _ ___ _ _<" ,
            "<_ _   _ _ _     _ _<" ,
            "<_ _ ___ _ ___ ___ _<" ,
            "<_ _     _     _   _<" ,
            "<_ _____ _ _____ _ _<" ,
            "<_ _~    x      ~_ _<" ,
            "<_ _ _________ ___ _<" ,
            "<_ _    P          _<" ,
            "<_ _____ _____ ___ _<" ,
            "<_                 _<" ,
            "<__R________________<" ,
            "<<<e<<<<<<<<<<<<<<<<<" ,


    };



    // PROBLEM WALL IS DIFFERENT IT SHOULD NOT BE [ fixed ]
    private static final String[] tileMap30 = {

            "<<<<<<<<<<<<<<<<<-<<<" ,
            "<________________ __<" ,
            "<_ q       _    K  _<" ,
            "<_ _______ _ _____ _<" ,
            "<_ _~   ~_ _ _  ~_ _<" ,
            "<_ _ _ _ _ _ _ _ _ _<" ,
            "<_ _ _ _ _ _ _     _<" ,
            "<_ _ _ _ _ _ _ _ _ _<" ,
            "<_   _ _   R   _   _<" ,
            "<_____ _____ ___ ___<" ,
            "<_     _~ x  _     _<" ,
            "<_ ___ _ ___ _ ___ _<" ,
            ".    _   P     _    E" ,
            "<_ ___ _____ ___ _ _<" ,
            "<_ _~        _ _ _ _<" ,
            "<_ _ _ ___ ___ _ _ _<" ,
            "<_ _ _    y    _ _ _<" ,
            "<_ _ _____ _ _ _ _ _<" ,
            "<_ _~        _  ~_ _<" ,
            "<_ _________ _ ___ _<" ,
            "<_           _     _<" ,
            "<_____R_____________<" ,
            "<<<<<<e<<<<<<<<<<<<<<" ,



    };





// theme 9: 31-35

/*

PACMAN = P

     WALLS -> =
     SPACEENTRY1 = .
     EXIT ENTRY1 = E
     SPACEENTRY2 = -
     EXIT ENTRY2 = e
     red ghost = u
     orange ghost = c
     PINK GHOST = w
     blue GHOST = H
     LAVA =  V
     SACRED = Z
     SLIME = L
     SPIDER = W

 */


    // no problem only ghost size small [fixed]
    private static final String[] tileMap31 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<===================<" ,
            "<=      Z      u   =<" ,
            "<= ======= ======= =<" ,
            "<= =  V  = =     = =<" ,
            "<= = === = = = = = =<" ,
            "<= = =   c   = = = =<" ,
            "<= = = ===== = = = =<" ,
            "<=   = Z     = =   =<" ,
            "<= = ===============<" ,
            ".   V    =w   Z     E" ,
            "<= = ===== === =====<" ,
            "<=   =   Z   =     =<" ,
            "<= = = ===== = === =<" ,
            "<= = =   H   = = = =<" ,
            "<= = === = === = = =<" ,
            "<= =   V   Z   = = =<" ,
            "<= ======= ===== = =<" ,
            "<=   Z   = P     = =<" ,
            "<= ===== === === = =<" ,
            "<=   V     Z     = =<" ,
            "<===================<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,




    };


    private static final String[] tileMap32 = {


            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<===================<" ,
            "<=        Z        =<" ,
            "<= === === === === =<" ,
            "<= = =     u   = = =<" ,
            "<= = === === === = =<" ,
            "<=   =    V    =   =<" ,
            "=== = ======= = ====<" ,
            "<=   =    H    =   =<" ,
            "<= === === === === =<" ,
            ".        =c=        E" ,
            "<= === === === === =<" ,
            "<=   =   Z     =   =<" ,
            "=== = ======= = ====<" ,
            "<=   =    w    =   =<" ,
            "<= = === === === = =<" ,
            "<= = =    V    = = =<" ,
            "<= = = ======= = = =<" ,
            "<= =      P      = =<" ,
            "<= = = = = = = = = =<" ,
            "<=                 =<" ,
            "<===================<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,




    };


    private static final String[] tileMap33 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<===================<" ,
            "<=                 =<" ,
            "<= ====L=====L==== =<" ,
            "<= =      =      = =<" ,
            "<= =====L=L == = = =<" ,
            "<= = =   H= =  = = =<" ,
            "<= = = ==== =  = = =<" ,
            "<=L= =  u     =  = =<" ,
            "<= = = == ==== =   =<" ,
            ".  = L         L    E" ,
            "<= = ======== ==   =<" ,
            "<= = =   c     = = =<" ,
            "<= = == ======== = =<" ,
            "<=   =         = = =<" ,
            "<= = =========== = =<" ,
            "<= =   L     L   = =<" ,
            "<= = ==L=== ==L= = =<" ,
            "<=  =      w       =<" ,
            "<= = ===== === === =<" ,
            "<=      P          =<" ,
            "<===================<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,




    };

    private static final String[] tileMap34 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<= = = = = = = = = =<" ,
            "<=       =          -" ,
            "<= ===== = ======= =<" ,
            "<= =       L     = =<" ,
            "<= = ======== ==== =<" ,
            "<=                 =<" ,
            "===== V===== V==== =<" ,
            "<=     = c = =   = =<" ,
            "<= === = = = = = = =<" ,
            "<= = H = = W = = u =<" ,
            "<= === === === === =<" ,
            ".                   E" ,
            "<= ===== === ===== =<" ,
            "<= =     = =     = =<" ,
            "<= = === = = === = =<" ,
            "<= = = P     = = = =<" ,
            "<= = ======= = = = =<" ,
            "<=       =       = =<" ,
            "===== == = = ===== =<" ,
            "e          =       =<" ,
            "<===================<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,







    };

    private static final String[] tileMap35 = {

            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<===================<" ,
            "<=       =    =     E" ,
            "<= === = ===  = = ==<" ,
            "<= = = = =      = ==<" ,
            "<=== = = = ====== ==<" ,
            "<=   = = =        ==<" ,
            "<= === = ======== ==<" ,
            "<= =    c     W =  =<" ,
            "<= ===== ===== ==  =<" ,
            "-      = =   V      e" ,
            "<==== === == =======<" ,
            "<=   H =   = P     L<" ,
            "<= === == == ===== =<" ,
            "<=   =       =     =<" ,
            "=== ======= = === ==<" ,
            "<=         = = =   =<" ,
            "<= ======= = = = ===<" ,
            "<=   u   =   =     =<" ,
            "===== === ======== =<" ,
            ".      =   L     = =<" ,
            "<===================<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,





    };



// GAME ENDING IN NEXT TWO MAPS

    private static final String[] tileMap36  = {


            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<===================<" ,
            "<=     r     =     =<" ,
            "<= ========= = === =<" ,
            "<= =Y      =   =   =<" ,
            "<= = ===== ===== = =<" ,
            "<=Y= =Y  =       = =<" ,
            "<=Y= === = ======= =<" ,
            "<=Y=     = =Y    = =<" ,
            "<=Y===== = ===== = =<" ,
            "<=Y=Y  = b       = =<" ,
            "<=Y= = ========= = =<" ,
            "<=Y= =       =YY=  =<" ,
            "<= = ===== = ==== ==<" ,
            "<=       = = P     =<" ,
            "<======= = = =======<" ,
            "<=Y    = = =     =Y=<" ,
            "<= === = = ===== = =<" ,
            "<= =   = p       = =<" ,
            "<= = =========== = =<" ,
            "<=      YY  o      =<" ,
            "<===================<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,




    };

    private static final String[] tileMap37 = {


            "<<<<<<<<<<<<<<<<<<<<<" ,
            "<= = = = = = = = = =<" ,
            "<=       =  r      =<" ,
            "<= ===== = ======= =<" ,
            "<= =   = = =     = =<" ,
            "<= = = = = = === = =<" ,
            "<= = = =c=   = =   =<" ,
            "<= = = === === =====<" ,
            "<= = =   = =   =   =<" ,
            "<= = === = = === = =<" ,
            "<=     =   =   = = =<" ,
            "<===== ===== = = = =<" ,
            "<=   = =  c= = = = =<" ,
            "<= = = = === = === =<" ,
            "<= = = =     =     =<" ,
            "<= = = ======= =====<" ,
            "<= = =  c    = b   =<" ,
            "<= = = ===== ===== =<" ,
            "<= =   P  =  p     =<" ,
            "<= ===== = ======= =<" ,
            "<=     = c    o    =<" ,
            "<= = = = = = = = = =<" ,
            "<<<<<<<<<<<<<<<<<<<<<" ,



    };



    private static final String[][] MAPS = {
            tileMap1,
            tileMap2,
            tileMap3,
            tileMap4,
            tileMap5,
            tileMap6,
            tileMap7,
            tileMap8,
            tileMap9,
            tileMap10,
            tileMap11,
            tileMap12,
            tileMap13,
            tileMap14,
            tileMap15,
            tileMap16,
            tileMap17,
            tileMap18,
            tileMap19,
            tileMap20,
            tileMap21,
            tileMap22,
            tileMap23,
            tileMap24,
            tileMap25,
            tileMap26,
            tileMap27,
            tileMap28,
            tileMap29,
            tileMap30,
            tileMap31,
            tileMap32,
            tileMap33,
            tileMap34,
            tileMap35,
            tileMap36,
            tileMap37

    };

    // GEETING THE MAP BY INDEX
    public static String[] getMap(int index)
    {
        if (index < 0 || index >= MAPS.length) {
            throw new IllegalArgumentException("INVALID MAP INDEX : " +index);
        }
        return MAPS[index];
    }

    // TO RETURN TOTAL MAPS
    public static int getMapCount() {
        return MAPS.length;
    }



}
