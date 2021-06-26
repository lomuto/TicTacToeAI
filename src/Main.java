import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static char[][] MAP;
    public static char user;
    public static char ai;

    public static void main(String[] args) throws IOException, InterruptedException {
        init();

        Node TicTacToeAI;
        if (user == 'O') {
            TicTacToeAI = new Node(MAP, 0, user);
        } else {
            TicTacToeAI = new Node(MAP, 0, ai);
        }

        // 제너레이터가 O를 받으면 작동안함
        // user로 X를 받으면 잘 작동함. 해결할것
        Node.generatePossibleAction(TicTacToeAI, ai);

        char currTurn = user == 'O' ? user : ai;
        int turn = 1;
        while (turn < 10) {
            int r;
            int c;
            if (currTurn == user) {
                renderMap();
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.println("Choose coordinate. ex) `2 1`");
                    r = scanner.nextInt();
                    c = scanner.nextInt();
                    if (MAP[r][c] != 0) {
                        System.out.println("Invalid spot");
                        continue;
                    }
                    if (r > -1 && c > -1 && r < 3 && c < 3) {
                        currTurn = ai;
                        break;
                    }
                    System.out.println("Invalid input as r: " + r + " c: " + c);
                }
                MAP[r][c] = user;

                for (Node next : TicTacToeAI.possibleAction) {
                    if (next.map[r][c] != 0) {
                        TicTacToeAI = next;
                        break;
                    }
                }
            } else {
                int nextPoint = Node.getBestAction(TicTacToeAI, ai);
                for (Node next : TicTacToeAI.possibleAction) {
                    if (next.point == nextPoint) {
                        TicTacToeAI = next;
                        Node.deepCpyMap(MAP, TicTacToeAI.map);
                        currTurn = user;
                        break;
                    }
                }
                // ai
            }
            turn++;
            clearConsole();
        }
        renderMap();
        int result = TicTacToeAI.point;
        switch (result) {
            case -10 :
                System.out.println("Player Won!");
                break;
            case 0:
                System.out.println("Draw! (Guess I've made good ai)");
                break;
            case 10:
                System.out.println("AI Won!");
                break;
        }
    }

    public static void clearConsole() throws IOException, InterruptedException {
//        final String os = System.getProperty("os.name");
//        if (os.contains("Windows"))
//            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//        else
//            Runtime.getRuntime().exec("clear");
    }

    public static void init() throws IOException, InterruptedException {
        MAP = new char[3][3];
        String buffer;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose player: `O` or `X`");
            buffer = scanner.next();
            if (buffer.length() != 1) {
                System.out.println("Invalid input of size " + buffer.length());
                continue;
            }
            user = buffer.charAt(0);
            if (user == 'O' || user == 'X') {
                ai = user == 'O' ? 'X' : 'O';
                clearConsole();
                return;
            }
            System.out.println("Only one character 'O' or 'X' allowed");
        }
    }

    public static void renderMap() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.printf("%c ", MAP[i][j]);
            }
            System.out.println();
        }
    }
}