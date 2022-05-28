import java.util.ArrayList;
import java.util.List;

public class MyNode {
    private int x;
    private int y;
    private boolean isVisited;
    private List<MyNode> neighbors;

    public MyNode(int x,int y){
        this.x=x;
        this.y=y;
        neighbors=new ArrayList<>();

    }

    public void addNeighbor(MyNode neighbor){
        this.neighbors.add(neighbor);

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public List<MyNode> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<MyNode> neighbors) {
        this.neighbors = neighbors;
    }
}
