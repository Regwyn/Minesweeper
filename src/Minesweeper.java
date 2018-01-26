import java.util.Random;
import java.util.Scanner;

public class Minesweeper {


    public static int[][] initializeMap(int howManyMines, int[][] emptyMap) {    // какво са howManyMines и emptyMap. Не ги виждам на друго място.
        Random random = new Random();

        for (int i = 0; i < emptyMap.length; i++) {
            emptyMap[i][0] = -2;                         //Приписва на първата колона код -2 за граница.
            emptyMap[i][emptyMap[0].length - 1] = -2;    //Приписва на последната колона код -2 за граница.
        }
        for (int i = 0; i < emptyMap[0].length; i++) {
            emptyMap[0][i] = -2;                         //Приписва на първият ред код -2 за граница.
            emptyMap[emptyMap.length - 1][i] = -2;       //Приписва на последният ред код -2 за граница.
        }
        for (int i = 0; i <= howManyMines; i++) {
            int randX = random.nextInt(emptyMap.length);         //emptyMap.length е максималната стойност
            int randY = random.nextInt(emptyMap[0].length);      //emptyMap[0].length е максималната стойност
            while (emptyMap[randX][randY] == -1 || emptyMap[randX][randY] == -2) {  //не го разбирам
                randX = random.nextInt(emptyMap.length);
                randY = random.nextInt(emptyMap[0].length);
            }
            emptyMap[randX][randY] = -1;   //записва на съответните рандом избрани координати кода за мина -1
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
            if (i > 0 && i < 10) {                   //Номерира и оформя едноцифрени колони
                System.out.print("  " + i + "");
            }
            if (i == 9) {                            //Намества и определя началото на двуцифрените колони
                System.out.print("  ");
            }
            if (i >= 10 && i < 100) {                //Номерира и оформя двуцифрените колони
                System.out.print(i + " ");
            }
        }
        for (int i = 0; i < map.length - 1; i++) {
            if (i < 10 && i > 0) {                   //Номерира и оформя едноцифрени редове
                System.out.print(" " + i);
            }
            if (i >= 10 && i < 100) {                //Номерира двуцифрени редове
                System.out.print(i);
            }
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == -2) {           //Това е нулев ред който избутва със еднопразно място клетките от цифрите отговарящи за дадения ред. ????? Не може ли просто с един sout " "
                    if (i < 10 && j == 0) {
                        System.out.print(" ");
                    } else if (j == 0) {
                        System.out.print(" ");
                    } else {
                        System.out.print(" ");
                    }
                } else if (cover[i][j] == 1) {    //Ако клетката е отворена...
                    if (map[i][j] == -1) {
                        System.out.print("[X]");                                //...поставя знак за открита бомба след отваряне на клетката.
                    } else {
                        System.out.print("[" + minesAround(i, j, map) + "]");   //...поставя скобии визуализира с цифра колко мини има около тази клетка.
                    }
                } else if (cover[i][j] == 0) {        //Ако не е отворена...
                    System.out.print("[ ]");          //...поставя скоби(действието се извършва веднага след избирането на размера на рамката).
                } else {
                    System.out.print("[M]");          //Поставя флагче
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
            System.out.println("You can't open a border");   //При въвеждане на първа или втора координатна цифра 0 се избира първия ред или колона, които са граници с празно поле.
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

    public static int getX(Scanner input) {
        boolean isNumber;

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


        return input.nextInt();
    }

    public static int getY(Scanner input) {
        boolean isNumber;

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
        return input.nextInt();
    }

    public static void main(String[] arg) {
        Scanner input = new Scanner(System.in);
        boolean isNumber;

        int rangeX = getX(input);
        int rangeY = getY(input);

        int[][] cover = new int[rangeY + 2][rangeX + 2];
        int[][] map = new int[rangeY + 2][rangeX + 2];

        int countMines = Math.round(rangeX * rangeY / 16);
        boolean boom = false;
        int counterMarks = 0;
        int markedMines = 0;

        System.out.println("There are " + countMines + " mines which are 1/4 of the field.");
        map = initializeMap(countMines, map);

        //for map
        //-2 = border
        //-1 = mine
        //0 = empty

        //for cover
        //-1 = marked
        //0 = hidden
        //1 = open

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