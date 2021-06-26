import java.util.ArrayList;

public class Node {
    public char[][] map;
    public int depth;
    public char turn;
    public ArrayList<Node> possibleAction;
    public int point;
    public static int dr[] = {0, -1, -1, -1, 0, 1, 1, 1};
    public static int dc[] = {1, 1, 0, -1, -1, -1, 0, 1};
    public int chooseR;
    public int chooseC;

    public Node(char[][] currMap, int currDepth, char currTurn) {
        map = new char[3][3];
        deepCpyMap(map, currMap);
        depth = currDepth;
        turn = currTurn;
        possibleAction = new ArrayList<>();
        point = 0;
    }

    public static void deepCpyMap(char[][] mapDest, char[][] mapSrc) {
        for (int i = 0; i < 3; i++) {
            System.arraycopy(mapSrc[i], 0, mapDest[i], 0, 3);
        }
    }

    public static void generatePossibleAction(Node node, char ai) {
        if (node.depth == 10) {
            return;
        }
        // 승부가 갈렸다면 그 이후의 수를 볼 필요가 없다. 종료
        if (node.point != 0) {
            return;
        }
        char currTurn = node.turn == 'O' ? 'X' : 'O';

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (node.map[r][c] == 0) {
                    Node newPossibleAction = new Node(node.map, node.depth + 1, currTurn);
                    node.chooseR = r;
                    node.chooseC = c;
                    newPossibleAction.map[r][c] = node.turn;
                    setPoint(newPossibleAction, ai);
                    node.possibleAction.add(newPossibleAction);
                }
            }
        }

        for (Node nextAction : node.possibleAction) {
            generatePossibleAction(nextAction, ai);
        }
    }

    public static char evaluateMap(char[][] map) {
        char ret = 0;
        // 맵 전체를 돕니다.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // 만약 수가 놓여진 공간일 때
                if (map[i][j] != 0) {
                    int r;
                    int c;
                    // 8방향으로 일치하는게 있는지 쭉 둘러보고
                    for (int currDiff = 0; currDiff < 8; currDiff++) {
                        int cnt = 0;
                        r = i + dr[currDiff];
                        c = j + dc[currDiff];
                        // 배열 안벗어나는지 검사 및 승리조건 만족 검사하면서
                        while (r > -1 && c > -1 && r < 3 && c < 3 && map[r][c] == map[i][j]) {
                            cnt++;
                            // 3줄로 동일한게 완성되면 빙고!
                            if (cnt == 2) {
                                return map[r][c];
                            }
                            // r = i + dr[currDiff]; 로 잘못썼었음
                            // 이럴경우 2개만 빙고여도 점수받음. 조심하자
                            r = r + dr[currDiff];
                            c = c + dc[currDiff];
                        }
                    }
                }
            }
        }
        // 만약 빙고가 안나오면 0을 리턴
        return ret;
    }

    public static void setPoint(Node node, char ai) {
        char evaluation = evaluateMap(node.map);
        if (evaluation == 0) {
            node.point = 0;
        } else if (evaluation == ai) {
            node.point = 10;
        } else {
            node.point = -10;
        }
    }

    public static int getBestAction(Node node, char turn) {
        // 재귀적인 구조에서 base 컨디션 설정 빼먹음
        // 그래서 leaf case의 경우 draw라 0점이 나와야 함에도 불구하고 초기값인 -10 or 10 으로만 나옴
        // 비교 variable의 초기값을 -10이나 10이 아닌 현재 노드의 그 point값으로 수정...
        if(node.turn == turn) {
            int bestPoint = node.point;
            for(Node nextAction : node.possibleAction) {
                bestPoint = Integer.max(bestPoint,getBestAction(nextAction,turn));
            }
            node.point = bestPoint;
            return bestPoint;
        }
        int worstPoint = node.point;
        for(Node nextAction : node.possibleAction) {
            worstPoint = Integer.min(worstPoint,getBestAction(nextAction,turn));
        }
        node.point = worstPoint;
        return worstPoint;
    }
}
