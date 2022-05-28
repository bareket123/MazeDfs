import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Maze extends JFrame {

    private int[][] values;
    private boolean[][] visited;
    private int startRow;
    private int startColumn;
    private ArrayList<JButton> buttonList;
    private int rows;
    private int columns;
    private boolean backtracking;
    private int algorithm;

    public Maze(int algorithm, int size, int startRow, int startColumn) {
        this.algorithm = algorithm;
        Random random = new Random();
        this.values = new int[size][];
        for (int i = 0; i < values.length; i++) {
            int[] row = new int[size];
            for (int j = 0; j < row.length; j++) {
                if (i > 1 || j > 1) {
                    row[j] = random.nextInt(8) % 7 == 0 ? Definitions.OBSTACLE : Definitions.EMPTY;
                } else {
                    row[j] = Definitions.EMPTY;
                }
            }
            values[i] = row;
        }
        values[0][0] = Definitions.EMPTY;
        values[size - 1][size - 1] = Definitions.EMPTY;
        this.visited = new boolean[this.values.length][this.values.length];
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.buttonList = new ArrayList<>();
        this.rows = values.length;
        this.columns = values.length;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setLocationRelativeTo(null);
        GridLayout gridLayout = new GridLayout(rows, columns);
        this.setLayout(gridLayout);
        for (int i = 0; i < rows * columns; i++) {
            int value = values[i / rows][i % columns];
            JButton jButton = new JButton(String.valueOf(i));
            if (value == Definitions.OBSTACLE) {
                jButton.setBackground(Color.BLACK);
            } else {
                jButton.setBackground(Color.WHITE);
            }
            this.buttonList.add(jButton);
            this.add(jButton);
        }
        this.setVisible(true);
        this.setSize(Definitions.WINDOW_WIDTH, Definitions.WINDOW_HEIGHT);
        this.setResizable(false);
    }

    public void checkWayOut() {
        new Thread(() -> {
            boolean result = false;
            switch (this.algorithm) {
                case Definitions.ALGORITHM_BRUTE_FORCE:
                    break;


                case Definitions.ALGORITHM_DFS:

                    Stack<MyNode> stack = new Stack<>();
                    MyNode[][] nodesMatrix = initNodesMatrix();
                    initNeighbors(nodesMatrix);
                    stack.add(nodesMatrix[0][0]);
                    while (!stack.empty() && !result) {
                        MyNode currentNode = stack.pop();
                        if (!currentNode.isVisited()) {
                                currentNode.setVisited(true);
                                setSquareAsVisited(currentNode.getX(), currentNode.getY(), currentNode.isVisited());
                            if (isTheTarget(currentNode)){
                                result=true;
                            }else {
                                for (MyNode neighbor :  currentNode.getNeighbors()) {
                                    if (!neighbor.isVisited() && isEmpty(neighbor))
                                        stack.add(neighbor);

                                }
                            }


                        }
                    }
                    break;

                case Definitions.ALGORITHM_BFS:
                    break;
            }

            JOptionPane.showMessageDialog(null, result ? "FOUND SOLUTION" : "NO SOLUTION FOR THIS MAZE");

        }).start();
    }

    public void setSquareAsVisited(int x, int y, boolean visited) {
        try {
            if (visited) {
                if (this.backtracking) {
                    Thread.sleep(Definitions.PAUSE_BEFORE_NEXT_SQUARE * 5);
                    this.backtracking = false;
                }
                this.visited[x][y] = true;
                for (int i = 0; i < this.visited.length; i++) {
                    for (int j = 0; j < this.visited[i].length; j++) {
                        if (this.visited[i][j]) {
                            if (i == x && y == j) {
                                this.buttonList.get(i * this.rows + j).setBackground(Color.RED);
                            } else {
                                this.buttonList.get(i * this.rows + j).setBackground(Color.BLUE);
                            }
                        }
                    }
                }
            } else {
                this.visited[x][y] = false;
                this.buttonList.get(x * this.columns + y).setBackground(Color.WHITE);
                Thread.sleep(Definitions.PAUSE_BEFORE_BACKTRACK);
                this.backtracking = true;
            }
            if (!visited) {
                Thread.sleep(Definitions.PAUSE_BEFORE_NEXT_SQUARE / 4);
            } else {
                Thread.sleep(Definitions.PAUSE_BEFORE_NEXT_SQUARE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isTheTarget(MyNode nodeToCheck) {
        boolean isTarget = false;
        if (nodeToCheck.getX()==rows-1 && nodeToCheck.getY()==columns-1) {
           isTarget=true;
        }
        return isTarget;
    }

    public MyNode[][] initNodesMatrix() {
        MyNode[][] nodes = new MyNode[rows][columns];
        for (int i = 0; i < rows; i++) {
            nodes[i] = new MyNode[rows];
            for (int j = 0; j < columns; j++) {
                    nodes[i][j] = new MyNode(i, j);
            }
        }

        return nodes;
    }

    private void initNeighbors(MyNode[][] nodesList) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                MyNode currentNode = nodesList[i][j];

                if (isValidIndex(i - 1) != Definitions.INVALID_INDEX) {
                    currentNode.addNeighbor(nodesList[i - 1][j]);
                }
                if (isValidIndex(j - 1) !=  Definitions.INVALID_INDEX) {
                    currentNode.addNeighbor(nodesList[i][j - 1]);
                }
                if (isValidIndex(i + 1) !=  Definitions.INVALID_INDEX) {
                    nodesList[i][j].addNeighbor(nodesList[i + 1][j]);
                }
                if (isValidIndex(j + 1) !=  Definitions.INVALID_INDEX) {
                    currentNode.addNeighbor(nodesList[i][j + 1]);
                }

            }
        }
    }
    private int isValidIndex(int currentIndex){
        int valid= Definitions.INVALID_INDEX;

        if (currentIndex<rows  && currentIndex>=0 ){
            valid=currentIndex;
        }
        return valid;
    }



    private boolean isEmpty (MyNode nodeToCheck) {
        boolean isEmpty=false;

        if (values[nodeToCheck.getX()][nodeToCheck.getY()] == Definitions.EMPTY){
            isEmpty=true;
        }
        return isEmpty ;
        }



}

