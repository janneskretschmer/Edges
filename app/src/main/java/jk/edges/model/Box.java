package jk.edges.model;

/**
 * Created by janne on 24.09.2015.
 */
public class Box extends PlaygroundItem {
    private int claimedEdges = 0;
    private int edgeCount = 4;

    public Box(int x,int y){
        super(x,y,Type.Box);
    }

    /**
     *
     * @return if box is fully surrounded by claimed edges
     */
    @Override public boolean claim(int id){
        if(++claimedEdges == edgeCount) {
            owner = id;
            return true;
        }
        return false;
    }

    public boolean isClaimed(){
        return owner>-1;
    }

    public void setOwner(int newOwner){
        owner = newOwner;
    }

    public void setEdgeCount(int edgeCount){
        this.edgeCount = edgeCount;
    }
}