import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static char[][] MAP;
    public static char user;
    public static char ai;

    public static void main(String[] args) throws IOException {
        init();

        Node TicTacToeAI = new Node(MAP, 0, ai);

        // 제너레이터가 O를 받으면 작동안함
        // user로 X를 받으면 잘 작동함. 해결할것
        Node.generatePossibleAction(TicTacToeAI,ai);

//        for(Node node : TicTacToeAI.possibleAction) {
//            for (int i = 0; i < 3; i++) {
//                for (int j = 0; j < 3; j++) {
//                    System.out.printf("%c ",node.map[i][j]);
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }

        char currTurn = user == 'O' ? user : ai;
        int turn = 1;
        while(turn < 9){
            renderMap();
            int r;
            int c;
            if(currTurn == user) {
                byte[] input = new byte[3];
                while(true) {
                    System.out.println("Choose coordinate. ex) `2 1`");
                    System.in.read(input,0,3);
                    r = new String(input).charAt(0) - '0';
                    c = new String(input).charAt(2)- '0';
                    if(r > -1 && c > -1 && r < 3 && c < 3) {
                        currTurn = ai;
                        break;
                    }
                    System.out.println("Invalid input as r: "+ r + " c: " + c);
                }
                MAP[r][c] = user;
                // ai 업데이트안됨
                for(Node next : TicTacToeAI.possibleAction) {
                    System.out.println("DEBUG: "+next.map[r][c]);
                    if(next.map[r][c] != 0) {
                        System.out.println("DEBUG");
                        TicTacToeAI = next;
                        break;
                    }
                }
            }else {
                int nextPoint = Node.getBestAction(TicTacToeAI,ai);
                for(Node next : TicTacToeAI.possibleAction) {
                    if(next.point == nextPoint) {
                        TicTacToeAI = next;
                        Node.deepCpyMap(MAP,TicTacToeAI.map);
                        currTurn = user;
                        break;
                    }
                }
                // ai
            }
            turn++;
            clearConsole();
        }
        // 이 인자대로라면 첫번쨰 수는 'O' 이어야 하는데 재귀때문에 한번 바뀌면서 `X`가 첫수
        // 사용자에게 첫수 입력받으면 한번 돌려서 인자로 줘야할듯

//        Node curr = TicTacToeAI;
//        while(true) {
//            System.out.println("Curr depth: "+curr.depth);
//            System.out.println("Curr Point: "+curr.point);
//            for(int i=0; i<3; i++) {
//                for(int j=0; j<3; j++) {
//                    System.out.printf("%c ", curr.map[i][j]);
//                }
//                System.out.println();
//            }
//            System.out.println();
//            if(curr.possibleAction.size() == 0) {
//                break;
//            }
//            curr = curr.possibleAction.get(0);
//        }
    }

    public static void clearConsole() throws IOException {
//        System.out.print("cls");
//        System.out.flush();
    }

    public static void init() throws IOException {
        MAP = new char[3][3];
        byte[] input = new byte[1];
        while(true) {
            System.out.println("Choose player: `O` or `X`");
            System.in.read(input,0,1);
            user = new String(input).charAt(0);
            if(user == 'O' || user == 'X') {
                ai = user == 'O' ? 'X' : 'O';
                clearConsole();
                return;
            }
        }
    }

    public static void renderMap() {
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                System.out.printf("%c ", MAP[i][j]);
            }
            System.out.println();
        }
    }
}