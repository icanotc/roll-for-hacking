package logic;

import graphics.CellLoader;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import main.Main;
import tile.Avatar;
import tile.Block;
import tile.Item;
import tile.Tile;

import javafx.scene.input.KeyEvent;

import tile.blocks.Entrance;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Updater  {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        public static Direction opposite(Direction d) {
            switch(d) {
                case UP:
                    return DOWN;
                case RIGHT:
                    return LEFT;
                case LEFT:
                    return RIGHT;
                case DOWN:
                    return UP;
                default:
                    throw new IllegalArgumentException("You didn't put in a direction");
            }
        }
    }

//    public static int[] currentRoomID = new int[]{0, 0};
//    public static Room[][] rooms = new Room[2][2];
    public static Room currentRoom;
    public static Avatar avatar = new Avatar();
    static Tile standingOn;
    static boolean isInventoryOpen = false;
    static boolean isAdvNotifOpen = false;
    public static GridPane inventory = new GridPane();
    public static StackPane advNotification;

    static {
        try {
            advNotification = FXMLLoader.load(new File("resources/advNotification.fxml").toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void update(KeyEvent event) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        System.out.println(event.getText());

        toggleInventory(event);
        move(event);
        standingOn = currentRoom.getTile(avatar.location[0], avatar.location[1]);
        pickupItem();
        interactWithEntrance(event);
        if (!event.isConsumed()) {
            event.consume();
        }

    }

    private static boolean interactWithEntrance(KeyEvent event) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (standingOn instanceof Entrance ) {
            if (event.getText().equals(" ")) {
                return moveRooms(((Entrance) standingOn).direction);
            } else {
                if (!isAdvNotifOpen) {

                    advNotification.setAlignment(Pos.CENTER);
                    Main.root.getChildren().add(advNotification);

                }
            }
        } else {
            isAdvNotifOpen = false;
            Main.root.getChildren().remove(advNotification);
        }
        return false;
    }

    public static boolean moveRooms(Direction direction) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
//        try {
//            switch (direction) {
//                case RIGHT:
//                    currentRoomID[0]++;
//                case LEFT:
//                    currentRoomID[0]--;
//                case UP:
//                    currentRoomID[1]--;
//                case DOWN:
//                    currentRoomID[1]++;
//            }
//            currentRoom = rooms[currentRoomID[0]][currentRoomID[1]];
//            CellLoader.loadAll();
//            return true;
//        } catch (ArrayIndexOutOfBoundsException e) {
//            return false;
//        }
        currentRoom = RoomGenerator.generateRoom(Room.RoomType.REGULAR_ROOM, new int[]{0, 0});
        CellLoader.loadAll();
        return true;

    }

    private static boolean toggleInventory(javafx.scene.input.KeyEvent e) {

        char invKeyChar = 'e';

        if (isInventoryOpen) {
            if(e.getText().charAt(0) == invKeyChar) {
                isInventoryOpen = false;
                closeInventory();
            }
            e.consume();
        } else { // inventory is NOT open
            if (e.getText().charAt(0) == invKeyChar) {
                isInventoryOpen = true;
                e.consume();
                viewInventory();
                return true;
            }
            // pass the event to movement if not 'e'
        }
        return false;

    }

    private static boolean move(KeyEvent keyInput) {

        Map<Character, Direction> characterDirectionMap = new HashMap<>();
        characterDirectionMap.put('w', Direction.UP);
        characterDirectionMap.put('s', Direction.DOWN);
        characterDirectionMap.put('a', Direction.LEFT);
        characterDirectionMap.put('d', Direction.RIGHT);

        Direction direction = characterDirectionMap.get(keyInput.getText().charAt(0));

        if (direction == null) return false;

        if (isObstacle(direction)) {
            int[] difference = new int[2];
            switch (direction) {
                case UP -> difference[1] = -1;
                case DOWN -> difference[1] = 1;
                case LEFT -> difference[0] = -1;
                case RIGHT -> difference[0] = 1;
            }

            avatar.location = new int[]{avatar.location[0] + difference[0], avatar.location[1] + difference[1]};
            CellLoader.loadCell(avatar.location[0], avatar.location[1]);
            CellLoader.loadCell(avatar.location[0] - difference[0], avatar.location[1] - difference[1]);

            return true;
        }
        return false;

    }

    private static boolean isObstacle(Direction direction) {
        boolean movingVertically = direction == Direction.UP || direction == Direction.DOWN;
        int[] difference = new int[2];

        if (movingVertically) {
            difference[1] = direction == Direction.UP ? -1 : 1;
        } else {
            difference[0] = direction == Direction.RIGHT ? 1 : -1;
        }

        Tile tileTravellingTo;
        try {
            tileTravellingTo = currentRoom.getTile(avatar.location[0] + difference[0], avatar.location[1] + difference[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }

        if (tileTravellingTo instanceof Block) {
            return ((Block) tileTravellingTo).canMoveThrough;
        }

        return true;

    }

    private static void pickupItem() {
        if(standingOn instanceof Item) {
            avatar.pocket.add((Item) standingOn);
            ((Item) standingOn).inWorld = false;
            currentRoom.tiles[avatar.location[0]][avatar.location[1]] = null;
            CellLoader.loadCell(avatar.location[0], avatar.location[1]);
            Counter.increment();
        }
    }

    private static void viewInventory() {
        int columnsPerRow = 5;
        int index = 0;
        for(Item item : avatar.pocket) {
            Text text= new Text(item.name);
            inventory.add(text, index%columnsPerRow, (int)Math.floor(index/columnsPerRow));
            index++;
        }
        inventory.setAlignment(Pos.CENTER);
        inventory.setStyle("-fx-background-color: rgba(100, 0, 0, 0.5);");
        Main.root.getChildren().add(inventory);
    }


    private static void closeInventory() {
        Main.root.getChildren().remove(inventory);
    }



}



