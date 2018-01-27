import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Minesweeper {

    public static int[][] initializeMap(int countMines, int[][] emptyMap) { //emptyMap is map from row 225
        Random random = new Random();

        for (int i = 0; i < emptyMap.length; i++) {
            emptyMap[i][0] = -2;                         //Adds border code -2 to the first column.
            emptyMap[i][emptyMap[0].length - 1] = -2;    //Adds border code -2 to the last column.
        }
        for (int i = 0; i < emptyMap[0].length; i++) {
            emptyMap[0][i] = -2;                         //Adds border code -2 to the first row.
            emptyMap[emptyMap.length - 1][i] = -2;       //Adds border code -2 to the last row.
        }
        for (int i = 0; i <= countMines; i++) { // Max random is the count of mines.
            int randX = random.nextInt(emptyMap.length); // Randomizes the X coordinates.
            int randY = random.nextInt(emptyMap[0].length); // Randomizes the Y coordinates.
            while (emptyMap[randX][randY] == -1 || emptyMap[randX][randY] == -2) {
                randX = random.nextInt(emptyMap.length);
                randY = random.nextInt(emptyMap[0].length);
            }
            emptyMap[randX][randY] = -1;   //Assigns code -1 for mine at the chosen coordinates
        }
        for (int i = 1; i < emptyMap.length - 1; i++) { //Adds the code - 1 without the borderlines starting to count from 1, not 0.
            for (int j = 1; j < emptyMap[0].length - 1; j++) {
                if (emptyMap[i][j] != -1)
                    emptyMap[i][j] = minesAround(i, j, emptyMap); //Assigns the count of mines around the current cell.
            }
        }
        return emptyMap;
    }

    public static int minesAround(int x, int y, int[][] map) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (map[x + i][y + j] == -1) {    //Counts the number of mines around the current cell.
                    count++;
                }
            }
        }
        return count;
    }

    public static void showMap(int[][] cover, int[][] map) {
        System.out.print("  ");
        for (int i = 0; i < map[0].length - 1; i++) {
            if (i > 0 && i < 10) {                   //Numerates and shapes the single digit columns.
                System.out.print("  " + i + "");
            }
            if (i == 9) {                            //Places and defines the start of the double digit columns.
                System.out.print("  ");
            }
            if (i >= 10 && i < 100) {                //Numerates and shapes the double digit columns.
                System.out.print(i + " ");
            }
        }
        for (int i = 0; i < map.length - 1; i++) {
            if (i < 10 && i > 0) {                   //Numerates and shapes the single digit rows.
                System.out.print(" " + i);
            }
            if (i >= 10 && i < 100) {                //Numerates the double digit rows.
                System.out.print(i);
            }
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == -2) {           //That's a border/zero row which separates/pushes with space the cells from the row numerations.
                    System.out.print(" ");
                } else if (cover[i][j] == 1) {   //If the cell is opened...
                    if (map[i][j] == -1) {
                        System.out.print("[X]");                                //...Puts sign for revealed bomb.
                    } else {
                        System.out.print("[" + minesAround(i, j, map) + "]");   //Puts brackets and visualizes with number how many mines are around the current cell.
                    }
                } else if (cover[i][j] == 0) {        //If the cell isn't opened...
                    System.out.print("[ ]");          //...puts brackets right after choosing the board size.
                } else {
                    System.out.print("[M]");
                }
            }
            System.out.println();
        }
    }

    public static int[][] openEmptyAround(int x, int y, int[][] map, int[][] cover) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (map[x + i][y + j] == 0 && cover[x + i][y + j] == 0) {
                    cover[x + i][y + j] = 1;
                    cover = openEmptyAround(x + i, y + j, map, cover);
                }
                if (map[x + i][y + j] > 0 && (cover[x + i][y + j] == 0) &&

                        (map[(x - 1) + i][(y - 1) + j] == 0 ||
                                map[(x - 1) + i][y + j] == 0 ||
                                map[(x - 1) + i][(y + 1) + j] == 0 ||
                                map[x + i][(y - 1) + j] == 0 ||
                                map[x + i][(y + 1) + j] == 0 ||
                                map[(x + 1) + i][(y - 1) + j] == 0 ||
                                map[(x + 1) + i][y + j] == 0 ||
                                map[(x + 1) + i][(y + 1) + j] == 0)) {
                    cover[x + i][y + j] = 1;
                    cover = openEmptyAround(x + i, y + j, map, cover);
                }
            }
        }
        return cover;
    }

    public static int[][] openCell(int x, int y, int[][] map, int[][] cover) {
        int[][] newCover = cover;

        if (map[x][y] == -2) {
            System.out.println("You can't open a border");   //If x or y are 0 then this is the borders with code -2.
        } else if (map[x][y] == -1) {
            newCover[x][y] = 1;
            System.out.println("You opened a mine!!!");
        } else if (cover[x][y] == -1) {
            System.out.println("You can't open a marked cell. Unmark it first.");
        } else {
            cover[x][y] = 1;
            System.out.println("Successfully opened");
            if (map[x][y] == 0) {
                cover = openEmptyAround(x, y, map, cover);
            }
        }
        return newCover;
    }

    public static int getRangeX(Scanner input) {
        boolean isNumber;

        do {
            System.out.print("Choose field width. Enter only whole numbers!");
            if (input.hasNextInt()) {
                isNumber = true;
            } else {
                System.out.println("Try again! Enter only whole numbers!");
                isNumber = false;
                input.next();
            }
        } while (!(isNumber));

        return input.nextInt();
    }

    public static int getRangeY(Scanner input) {
        boolean isNumber;

        do {
            System.out.print("Choose field height. Enter only whole numbers!");
            if (input.hasNextInt()) {
                isNumber = true;
            } else {
                System.out.println("Try again! Enter only whole numbers!");
                isNumber = false;
                input.next();
            }
        } while (!(isNumber));
        return input.nextInt();
    }

    public static int getX(Scanner input, int countMines, int counterMarks) {
        boolean isNumber;

        do {
            System.out.println("You have " + (countMines + 1) + " total marks");
            System.out.println("You have " + ((countMines + 1) - counterMarks) + "  marks left");
            System.out.print("In which row is the cell?");

            if (input.hasNextInt()) {
                isNumber = true;
            } else {
                System.out.println("Try again! Enter only whole numbers!");
                isNumber = false;
                input.next();
            }
        } while (!(isNumber));
        return input.nextInt();
    }

    public static int getY(Scanner input, int countMines, int counterMarks) {
        boolean isNumber;

        do {
            System.out.println("You have " + (countMines + 1) + " total marks");
            System.out.println("You have " + ((countMines + 1) - counterMarks) + "  marks left");
            System.out.print("In which column is the cell?");

            if (input.hasNextInt()) {
                isNumber = true;
            } else {
                System.out.println("Try again! Enter only whole numbers!");
                isNumber = false;
                input.next();
            }
        } while (!(isNumber));
        return input.nextInt();
    }

    public static int getAction(Scanner input) {
        boolean isNumber;

        do {
            System.out.println("Choose an action!" + "\n" + "[1]Open" + "\n" + "[2]Mark/Unmark" + "\n" + "[3]Surrender. All cells will be shown!");

            if (input.hasNextInt()) {
                isNumber = true;
            } else {
                System.out.println("Try again! Enter only whole numbers!");
                isNumber = false;
                input.next();
            }
        } while (!(isNumber));
        return input.nextInt();
    }

    public static void main(String[] arg) {
//        Timer tickTock = new Timer();
//        TimerTask tickTockTask = new TimerTask() {
//            int elapsedTime = 0;
//
//            public void run() {
//                elapsedTime++;
//                System.out.print(elapsedTime + " ");
//            }
//        };
//        tickTock.schedule(tickTockTask, 1000, 1000);
        Scanner input = new Scanner(System.in);

        int rangeX = getRangeX(input);
        int rangeY = getRangeY(input);

        int[][] cover = new int[rangeY + 2][rangeX + 2]; //for cover:   -1 = marked, 0 = hidden, 1 = open
        int[][] map = new int[rangeY + 2][rangeX + 2];   //for map: -2 = border, -1 = mine, 0 = empty

        int countMines = Math.round(rangeX * rangeY / 8);
        boolean boom = false;
        int counterMarks = 0;
        int markedMines = 0;

        System.out.println("There are " + countMines + " mines which are around 1/8 of the field.");
        map = initializeMap(countMines, map);

        while (!boom) {
            showMap(cover, map);

            int x = getX(input, countMines, counterMarks);
            int y = getY(input, countMines, counterMarks);
            int action = getAction(input);

            if (action == 1) {
                cover = openCell(x, y, map, cover);
            } else if (action == 2) {
                if (cover[x][y] != 1) {
                    if (map[x][y] != -2) {
                        if (counterMarks < countMines + 1) {
                            if (cover[x][y] == -1) {
                                cover[x][y] = 0;
                                counterMarks--;
                                if (map[x][y] == -1) {
                                    markedMines--;
                                }
                            } else {
                                cover[x][y] = -1;
                                if (map[x][y] == -1) {
                                    markedMines++;
                                }
                                counterMarks++;
                            }
                        } else {
                            System.out.println("You don't have marks left!");
                        }
                    } else {
                        System.out.println("There are no such coordinates!");
                    }
                } else {
                    System.out.println("You can't mark a cell that has already been opened!");
                }
            } else if (action == 3) {
                System.out.println("Game over!");
                for (int i = 0; i < cover.length; i++) {
                    for (int j = 0; j < cover[0].length; j++) {
                        cover[i][j] = 1;
                    }
                }
            } else {
                System.out.println("No such function");
            }
            if (markedMines == countMines) {
                System.out.println("You marked all the mines!!");
                boom = true;
            }
            if (cover[x][y] == 1 && map[x][y] == -1) {
                boom = true;
            }
        }
        showMap(cover, map);
    }
}