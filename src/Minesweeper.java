import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

    public static int[][] initializeMap(int howManyMines, int[][] emptyMap) {
        Random random = new Random();

        for (int i = 0; i < emptyMap.length; i++) {
            emptyMap[i][0] = -2;
            emptyMap[i][emptyMap[0].length - 1] = -2;
        }
        for (int i = 0; i < emptyMap[0].length; i++) {
            emptyMap[0][i] = -2;
            emptyMap[emptyMap.length - 1][i] = -2;
        }
        for (int i = 0; i <= howManyMines; i++) {
            int randX = random.nextInt(emptyMap.length);
            int randY = random.nextInt(emptyMap[0].length);
            while (emptyMap[randX][randY] == -1 || emptyMap[randX][randY] == -2) {
                randX = random.nextInt(emptyMap.length);
                randY = random.nextInt(emptyMap[0].length);
            }
            emptyMap[randX][randY] = -1;
        }
        for (int i = 1; i < emptyMap.length - 1; i++) {
            for (int j = 1; j < emptyMap[0].length - 1; j++) {
                if (emptyMap[i][j] != -1)
                    emptyMap[i][j] = minesAround(i, j, emptyMap);
            }
        }
        return emptyMap;
    }

    public static int minesAround(int x, int y, int[][] map) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (map[x + i][y + j] == -1) {    //Обикаля околните клетки на сегашната и проверява ако има бомба (-1 е код за бомба)
                    count++;
                }
            }
        }
        return count;
    }

    public static void showMap(int[][] cover, int[][] map) {
        System.out.print("  ");
        for (int i = 0; i < map[0].length - 1; i++) {
            if (i > 0 && i < 10) {
                System.out.print("  " + i + "");
            }
            if (i == 9) {
                System.out.print("  ");
            }
            if (i >= 10 && i < 100) {
                System.out.print(i + " ");
            }
        }
        for (int i = 0; i < map.length - 1; i++) {
            if (i < 10 && i > 0) {
                System.out.print(" " + i);
            }
            if (i >= 10 && i < 100) {
                System.out.print(i);
            }
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == -2) {
                    if (i < 10 && j == 0) {
                        System.out.print(" ");
                    } else if (j == 0) {
                        System.out.print(" ");
                    } else {
                        System.out.print(" ");
                    }
                } else if (cover[i][j] == 1) {
                    if (map[i][j] == -1) {
                        System.out.print("[X]");
                    } else {
                        System.out.print("[" + minesAround(i, j, map) + "]");
                    }
                } else if (cover[i][j] == 0) {
                    System.out.print("[ ]");
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
                } else {
                    continue;
                }
            }
        }
        return cover;
    }

    public static int[][] openCell(int x, int y, int[][] map, int[][] cover) {
        int[][] newCover = cover;

        if (map[x][y] == -2) {
            System.out.println("You can't open a border");
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

    public static void main(String[] arg) {
        Scanner input = new Scanner(System.in);
        boolean isNumber;

        do {
            System.out.print("Choose field width. Enter only whole numbers between 5 and 99!");

            if (input.hasNextInt()) {
                isNumber = true;
            } else {
                System.out.println("Try again! Enter only whole numbers between 5 and 99!");
                isNumber = false;
                input.next();
            }
        } while (!(isNumber));
        int rangeX = input.nextInt();

        do {
            System.out.print("Choose field width. Enter only whole numbers between 2 and 99!");
            if (input.hasNextInt()) {
                isNumber = true;
            } else {
                System.out.println("Try again! Enter only whole numbers between 2 and 99!");
                isNumber = false;
                input.next();
            }
        } while (!(isNumber));
        int rangeY = input.nextInt();

        int[][] cover = new int[rangeY + 2][rangeX + 2];
        int[][] map = new int[rangeY + 2][rangeX + 2];

        int countMines = Math.round(rangeX * rangeY / 4);
        boolean boom = false;
        int counterMarks = 0;
        int markedMines = 0;

        System.out.println("There are " + countMines + " mines which are 1/4 of the field.");
        map = initializeMap(countMines, map);

        for (int i = 0; i < cover.length; i++) {
            for (int j = 0; j < cover[0].length; j++) {
                cover[i][j] = 0;
            }
        }

        while (!boom) {
            showMap(cover, map);

            do {
                System.out.println("You have " + (countMines + 1) + " total marks");
                System.out.println("You have " + ((countMines + 1) - counterMarks) + "  marks left");
                System.out.print("In which row is the cell?");

                if (input.hasNextInt()) {
                    isNumber = true;
                } else {
                    System.out.println("Try again! Enter only whole numbers between 2 and 99!");
                    isNumber = false;
                    input.next();
                }
            } while (!(isNumber));
            int x = input.nextInt();

            do {
                System.out.print("In which column is the cell?");

                if (input.hasNextInt()) {
                    isNumber = true;
                } else {
                    System.out.println("Try again! Enter only whole numbers between 2 and 99!");
                    isNumber = false;
                    input.next();
                }
            } while (!(isNumber));
            int y = input.nextInt();

            do {
                System.out.println("Choose an action!");
                System.out.println("[1]Open");
                System.out.println("[2]Mark/Unmark");
                System.out.println("[3]Surrender. All cells will be shown!");

                if (input.hasNextInt()) {
                    isNumber = true;
                } else {
                    System.out.println("Try again! Enter only whole numbers between 2 and 99!");
                    isNumber = false;
                    input.next();
                }
            } while (!(isNumber));
            int action = input.nextInt();

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
                        System.out.println("You can't mark a border");
                    }
                } else {
                    System.out.println("You can't mark it if it's open");
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