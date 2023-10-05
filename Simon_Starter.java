import java.awt.Color;
import java.util.Random;

import tester.Tester;
import javalib.funworld.*;
import javalib.worldimages.*;

// To represent the animation of the game Simon Says
class PlayWorld extends World {
  static int SCENE_SIZE = 500;
  ILoButton sequence;
  ILoInt list;
  ILoInt olist;

  PlayWorld(ILoButton sequence, ILoInt list, ILoInt olist) {
    this.sequence = sequence;
    this.list = list;
    this.olist = olist;
  }

  /* TEMPLATE:
  Fields:
  ... this.sequence ...                      -- ILoButton
  ... this.list ...                          -- ILoInt
  ... this.olist ...                         -- ILoInt
  Methods:
  ... this.makeScene ...                     -- WorldScene
  ... this.onTick ...                        -- World
  Fields on Methods:
  ... this.sequence.drawButton ...           -- WorldImage
  ... this.sequence.getFirst ...             -- Button
  ... this.sequence.getRest ...              -- ILoButton
  ... this.sequence.getLength ...            -- int
   */

  // Draw the current state of the world
  public WorldScene makeScene() {
    WorldScene background = new WorldScene(SCENE_SIZE, SCENE_SIZE);
    background = background.placeImageXY(sequence.drawButton(Color.red), 125, 125);

    if (list.getFirst() == 0) {
      // red
      background = background.placeImageXY(sequence.getFirst().drawLit(), 125, 125);
    }
    else if (list.getFirst() == 1) {
      // blue
      background = background.placeImageXY(sequence.getRest().getFirst().drawLit(), 125, 125);
    }
    else if (list.getFirst() == 2) {
      // green
      background = background.placeImageXY(
          sequence.getRest().getRest().getFirst().drawLit(), 125, 125);
    }
    else if (list.getFirst() == 3) {
      // yellow
      background = background.placeImageXY(
          sequence.getRest().getRest().getRest().getFirst().drawLit(), 125, 125);
    }
    return background;
  }

  // handles ticking of the clock and updating the world if needed
  public World onTick() {
    if (list.getFirst() == -1) {
      return new SimonWorld(sequence, olist, olist, -1, 0);
    }
    else {
      return new PlayWorld(sequence, list.getRest(), olist);
    }
  }
}

// to represent the playing world of the game Simon Says
class SimonWorld extends World {
  //add fields needed to keep track of the state of the world
  static int SCENE_SIZE = 500;
  ILoButton sequence;
  ILoInt list; 
  ILoInt olist;
  int x;
  int count;

  SimonWorld(ILoButton sequence, ILoInt list, ILoInt olist, int x, int count) {
    this.sequence = sequence;
    this.list = list;
    this.olist = olist;
    this.x = x;
    this.count = count;
  }

  /* TEMPLATE:
  Fields:
  ... this.sequence ...                      -- ILoButton
  ... this.list ...                          -- ILoInt
  ... this.olist ...                         -- ILoInt
  ... this.x ...                             -- int
  ... this.count ...                         -- int
  Methods:
  ... this.makeScene ...                     -- WorldScene
  ... this.onTick ...                        -- World
  ... this.lastScene ...                     -- WorldScene
  ... this.onMouseClicked                    -- SimonWorld
  Fields on Methods:
  ... this.sequence.drawButton ...           -- WorldImage
  ... this.sequence.getFirst ...             -- Button
  ... this.sequence.getRest ...              -- ILoButton
  ... this.sequence.getLength ...            -- int
   */

  // Draw the current state of the world
  public WorldScene makeScene() {
    //stub
    WorldScene background = new WorldScene(SCENE_SIZE, SCENE_SIZE);
    background = background.placeImageXY(sequence.drawButton(Color.red), 125, 125);
    if (x == -1) {
      return background;
    }
    else if (x == 0) {
      return background = background.placeImageXY(
          sequence.getFirst().drawLit(), 125, 125);
    }
    else if (x == 1) {
      return background = background.placeImageXY(
          sequence.getRest().getFirst().drawLit(), 125, 125);
    }
    else if (x == 2) {
      return background = background.placeImageXY(
          sequence.getRest().getRest().getFirst().drawLit(), 125, 125);
    }
    else if (x == 3) {
      return background = background.placeImageXY(
          sequence.getRest().getRest().getRest().getFirst().drawLit(), 125, 125);
    }
    else {
      return lastScene("You have Failed");
    }
  }

  // handles ticking of the clock and updating the world if needed
  public World onTick() {
    //stub
    if (count == this.olist.getLength()) {
      ILoInt list1 = this.olist.createList();
      return new PlayWorld(sequence, list1, list1);
    }
    else {
      return this;
    }
  }

  // Returns the final scene with the given message displayed
  public WorldScene lastScene(String msg) {
    WorldScene background = new WorldScene(SCENE_SIZE, SCENE_SIZE);
    WorldImage txt = new TextImage(msg, 24, FontStyle.BOLD, Color.RED);
    return background.placeImageXY(txt, 250, 250);
  }

  // handles mouse clicks and is given the mouse location
  public SimonWorld onMouseClicked(Posn pos) {
    ILoInt olist = list;
    if (pos.x <= 250 && pos.y <= 250 && list.getFirst() == 0) {
      return new SimonWorld(this.sequence, this.list.getRest(), this.olist,  0, this.count + 1);
    }
    else if (pos.x <= 500 && pos.y < 250 && list.getFirst() == 1) {
      return new SimonWorld(this.sequence, this.list.getRest(), this.olist, 1, this.count + 1);
    }
    else if (pos.x < 250 && pos.y <= 500 && list.getFirst() == 2) {
      return new SimonWorld(this.sequence, this.list.getRest(), this.olist, 2, this.count + 1);
    }
    else if (pos.x <= 500 && pos.y <= 500 && list.getFirst() == 3) {
      return new SimonWorld(this.sequence, this.list.getRest(), this.olist, 3, this.count + 1);
    }
    else {
      return new SimonWorld(this.sequence, this.list, this.olist, -2, this.count + 1);
    } 
  }
}

// to represent a list of integers
interface ILoInt {
  // creates this list and adds a new random integer at the end
  ILoInt createList();

  // gets the first element of the list
  int getFirst();

  // gets the rest of the elements in the list
  ILoInt getRest();

  // gets the number of elements in the list
  int getLength();
}

// to represent an empty list of integers
class MtLoInt implements ILoInt {
  // creates this list and adds a new random integer at the end
  public ILoInt createList() {
    Random rand = new Random();
    return new ConsLoInt(rand.nextInt(4), new MtLoInt());
  }

  // gets the first element of the list
  public int getFirst() {
    return -1;
  }

  // gets the rest of the elements in the list
  public ILoInt getRest() {
    return new MtLoInt();
  }

  // gets the number of elements in the list
  public int getLength() {
    return 0;
  }
}

// to represent a non-empty list of integers
class ConsLoInt implements ILoInt {
  int first;
  ILoInt rest;

  ConsLoInt(int first, ILoInt rest)  {
    this.first = first;
    this.rest = rest;
  }

  /* TEMPLATE:
  Fields:
  ... this.first ...                         -- ILoButton
  ... this.rest ...                          -- ILoInt
  Methods:
  ... this.drawButton ...                    -- WorldImage
  ... this.getFirst ...                      -- Button
  ... this.getRest ...                       -- ILoButton
  ... this.getLength ...                     -- int
  Fields on Methods:
  ... this.rest.drawButton ...               -- WorldImage
  ... this.rest.getFirst ...                 -- Button
  ... this.rest.getRest ...                  -- ILoButton
  ... this.rest.getLength ...                -- int
   */

  // creates this list and adds a new random integer at the end
  public ILoInt createList() {
    return new ConsLoInt(this.first, this.rest.createList());
  }

  // gets the first element of the list
  public int getFirst() {
    return this.first;
  }

  // gets the rest of the elements in the list
  public ILoInt getRest() {
    return this.rest;
  }

  // gets the number of elements in the list
  public int getLength() {
    return 1 + this.rest.getLength();
  }
}

// Represents a list of buttons
interface ILoButton {
  // draws the buttons
  WorldImage drawButton(Color originalcolor);

  // gets the first element of the list
  Button getFirst();

  // gets the rest of the elements in the list  
  ILoButton getRest();

  // gets the number of elements in the list
  int getLength();
} 

// Represents an empty list of buttons
class MtLoButton implements ILoButton {

  // draws the buttons
  public WorldImage drawButton(Color originalcolor) {
    return new RectangleImage(0, 0, OutlineMode.SOLID, originalcolor); 
  }

  // gets the first element of the list
  public Button getFirst() {
    return new Button(Color.white, 0, 0); 
  }

  // gets the rest of the elements in the list
  public ILoButton getRest() {
    return this;
  }

  // gets the number of elements in the list
  public int getLength() {
    return 0;
  }
} 

// Represents a non-empty list of buttons
class ConsLoButton implements ILoButton {
  Button first;
  ILoButton rest;

  ConsLoButton(Button first, ILoButton rest) {
    this.first = first;
    this.rest = rest;
  }

  /* TEMPLATE:
  Fields:
  ... this.first ...                         -- ILoButton
  ... this.rest ...                          -- ILoInt
  Methods:
  ... this.drawButton ...                    -- WorldImage
  ... this.getFirst ...                      -- Button
  ... this.getRest ...                       -- ILoButton
  ... this.getLength ...                     -- int
  Fields on Methods:
  ... this.rest.drawButton ...               -- WorldImage
  ... this.rest.getFirst ...                 -- Button
  ... this.rest.getRest ...                  -- ILoButton
  ... this.rest.getLength ...                -- int
   */

  // draws the given buttons
  public WorldImage drawButton(Color originalcolor) {
    WorldImage first1 = first.drawButton(originalcolor);
    WorldImage rest1 = rest.drawButton(originalcolor);

    return new OverlayImage(rest1, first1);
  }

  // gets the first element of the list
  public Button getFirst() {
    return this.first;
  }

  // gets the rest of the elements in the list
  public ILoButton getRest() {
    return this.rest;
  }

  // gets the number of elements in the list
  public int getLength() {
    return 1 + this.rest.getLength();
  }
} 

// Represents one of the four buttons you can click
class Button {
  Color color;
  int x;
  int y;

  Button(Color color, int x, int y) {
    this.color = color;
    this.x = x;
    this.y = y;
  }

  // Draw this button dark
  WorldImage drawDark() {
    return new Button(this.color.darker().darker(), this.x, this.y).drawButton(color.red);
  }

  // Draw this button lit
  WorldImage drawLit() {
    return new Button(this.color.brighter().brighter(), this.x, this.y).drawButton(color.red);
  }

  // draws the button
  public WorldImage drawButton(Color originalcolor) {
    return 
        new RectangleImage(250, 250, OutlineMode.SOLID, this.color).movePinholeTo(new Posn(x, y));
  }
}

// Examples
class ExamplesSimon {
  //put all of your examples and tests here
  Button buttontest = new Button(Color.red, 0, 0);
  Button button = new Button(Color.red.darker().darker(), 0, 0);
  Button button1 = new Button(Color.blue.darker().darker(), -250, 0);
  Button button2 = new Button(Color.green.darker().darker(), 0, -250);
  Button button3 = new Button(Color.yellow.darker().darker(), -250, -250);

  ILoButton empty = new MtLoButton();
  ILoButton sequence = 
      new ConsLoButton(button, 
          new ConsLoButton(button1, 
              new ConsLoButton(button2, 
                  new ConsLoButton(button3, 
                      new MtLoButton()))));
  ILoButton sequence1 = 
      new ConsLoButton(button, 
          new ConsLoButton(button1, 
              new MtLoButton()));

  ILoInt intlist = 
      new ConsLoInt(1, new ConsLoInt(2, new ConsLoInt(3, new ConsLoInt(4, new MtLoInt()))));

  SimonWorld testworld = new SimonWorld(sequence, new MtLoInt(), new MtLoInt(), -1, 0);
  SimonWorld testworld1 = new SimonWorld(sequence, intlist, intlist, -1, 0);


  // to test the drawButton function
  boolean testDrawButton(Tester t) {
    return t.checkExpect(
        empty.drawButton(Color.red), new RectangleImage(0, 0, OutlineMode.SOLID, Color.red))
        && t.checkExpect(
            button.drawButton(Color.red), 
            new RectangleImage(250, 250, OutlineMode.SOLID, 
                Color.red.darker().darker()).movePinholeTo(new Posn(0, 0)));
    
  }

  // to test the drawDark function
  boolean testDrawDark(Tester t) {
    return t.checkExpect(buttontest.drawDark(), 
        new RectangleImage(250, 250, OutlineMode.SOLID, 
            Color.red.darker().darker()).movePinholeTo(new Posn(0, 0)));
  }

  // to test the drawLight function
  boolean testDrawLight(Tester t) {
    return t.checkExpect(buttontest.drawLit(), 
        new RectangleImage(250, 250, OutlineMode.SOLID, 
            Color.red.brighter().brighter()).movePinholeTo(new Posn(0, 0)));
  }

  // to test getLength method
  boolean testGetLength(Tester t) {
    return t.checkExpect(sequence.getLength(), 4)
        && t.checkExpect(empty.getLength(), 0);
  }

  // to test the getFirst method
  boolean testGetFirst(Tester t) {
    return t.checkExpect(sequence.getFirst(), button)
        && t.checkExpect(empty.getFirst(), new Button(Color.white, 0, 0));
  }

  // to test the getRest method
  boolean testGetRest(Tester t) {
    return t.checkExpect(sequence.getRest(),         
        new ConsLoButton(button1, 
            new ConsLoButton(button2, 
                new ConsLoButton(button3, 
                    new MtLoButton()))))
        && t.checkExpect(empty.getRest(), new MtLoButton());
  }

  // to test the onMouseClicked method
  boolean testOnMouseClicked(Tester t) {
    return t.checkExpect(testworld.onMouseClicked(new Posn(10, 10)), 
        new SimonWorld(sequence, new MtLoInt(), new MtLoInt(), -2, 1))
        && t.checkExpect(testworld.onMouseClicked(new Posn(250, 250)), 
            new SimonWorld(sequence, new MtLoInt(), new MtLoInt(),  -2, 1));
  }  

  // to test makeScene function
  boolean testMakeScene(Tester t) {
    WorldScene background = new WorldScene(500, 500);
    background = background.placeImageXY(sequence.drawButton(Color.red), 125, 125);
    return t.checkExpect(testworld1.makeScene() , 
        background = background.placeImageXY(sequence.getRest().getFirst().drawLit(), 125, 125));
  }

  //runs the game by creating a world and calling bigBang
  boolean testBigBang(Tester t) {
    SimonWorld sWorld = new SimonWorld(sequence, new MtLoInt(), new MtLoInt(), -1, 0);
    return sWorld.bigBang(500, 500, 0.5);
  }
}

