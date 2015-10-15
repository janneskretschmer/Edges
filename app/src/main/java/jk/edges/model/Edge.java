package jk.edges.model;

/**
 * Created by janne on 24.09.2015.
 */
public class Edge extends PlaygroundItem {
    public Edge(int x,int y){
        super(x,y,Type.Edge);
    }

    @Override
    public boolean claim(int id){
        owner = id;
        return true;
    }
}
