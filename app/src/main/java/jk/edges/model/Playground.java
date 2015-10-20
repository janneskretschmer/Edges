package jk.edges.model;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by janne on 24.09.2015.
 */
public class Playground {
    //every second row contains boxes.
    //edges are vertical in rows with boxes
    //edges are horizontal in rows without boxes
    //in horizontal rows only every second item is a edge
    private ItemParent[][] items;
    private int id1,id2;

    public Playground(int boxWidth,int boxHeight,int id1,int id2){
        items=new ItemParent[boxWidth*2+1][boxHeight*2+1];
        this.id1=id1;
        this.id2=id2;
    }
    public Playground(int id1,int id2){
        items=null;
        this.id1=id1;
        this.id2=id2;
    }
    public Playground(){
        items=null;
        id1=-1;
        id2=-1;
    }

    // E:edge F:Player1 G:Player2
    // B:box C:Player1 D:Player2
    // N:null
    // it has to be a rectangle
    public void parse(String map){
        String[] temp = map.split("\\r?\\n");
        items = new ItemParent[temp.length][temp[0].length()];
        for (int y = 0; y < temp.length; y++) {
            for (int x = 0; x < temp[0].length(); x++) {
                char c = temp[y].charAt(x);
                if(c=='E'||c=='F'||c=='G'){
                    items[y][x]=new Edge(x,y);
                    //wegen der folgenden Fallunterscheidung habe ich kein switch genommen
                    if(c=='F'&&id1>-1)((Edge)items[y][x]).claim(id1);
                    else if(c=='G'&&id2>-1)((Edge)items[y][x]).claim(id2);
                }else if(c=='B'||c=='C'||c=='D'){
                    items[y][x]=new Box(x,y);
                    if(c=='D'&&id1>-1)((Box)items[y][x]).setOwner(id1);
                    else if(c=='D'&&id2>-1)((Box)items[y][x]).setOwner(id2);
                }
            }
        }
        adaptBoxEdgeCount();
    }

    private void adaptBoxEdgeCount(){
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[i].length; j++) {
                if(items[i][j]!=null && items[i][j].getType()==Type.Box){
                    //count surrounding edges
                    int edgeCount = 0;
                    if(i>0&&items[i-1][j]!=null)edgeCount++;//upper edge
                    if(j>0&&items[i][j-1]!=null)edgeCount++;//left edge
                    if(i+1<items.length&&items[i+1][j]!=null)edgeCount++;//lower edge
                    if(j+1<items[i].length&&items[i][j+1]!=null)edgeCount++;//right edge
                    ((Box)items[i][j]).setEdgeCount(edgeCount);
                }
            }
        }
    }

    public String stringify(){
        StringBuffer sb = new StringBuffer();
        for (int y = 0; y < items.length; y++) {
            for (int x = 0; x < items[0].length; x++) {
                if(items[y][x] == null){
                    sb.append("N");
                }else if(items[y][x].getType() == Type.Box){
                    if(id1>-1&&items[y][x].getOwner()==id1)sb.append("C"); //Player 1 owns the box
                    else if(id2>-1&&items[y][x].getOwner()==id2)sb.append("D"); //Player 2 owns the box
                    else sb.append("B"); //Nobody owns it
                }else if(items[y][x].getType() == Type.Edge){
                    if(id1>-1&&items[y][x].getOwner()==id1)sb.append("F"); //Player 1 owns the edge
                    else if(id2>-1&&items[y][x].getOwner()==id2)sb.append("G"); //Player 2 owns the edge
                    else sb.append("E"); //Nobody owns it
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public ItemParent[][] getItems() {
        return items;
    }

    /**
     * claim an edge
     * @param x
     * @param y
     * @param id
     * @return affected items, if null or length 0 nothing changed
     */
    public ArrayList<Point> claim(int x,int y,int id){
        if(items[y][x]==null)return null;

        // a player can only claim an unclaimed edge directly
        ArrayList<Point> affectedItems = new ArrayList<>();
        if(items[y][x].getType()==Type.Edge&&!items[y][x].isClaimed()){
            items[y][x].claim(id);
            affectedItems.add(new Point(x,y));
            //increment claim count for surrounding boxes
            if(x>0&&items[y][x-1]!=null){
                items[y][x-1].claim(id);
                affectedItems.add(new Point(x-1, y));
            }
            if(y>0&&items[y-1][x]!=null){
                items[y-1][x].claim(id);
                affectedItems.add(new Point(x, y-1));
            }
            if(x+1<items[y].length&&items[y][x+1]!=null){
                items[y][x+1].claim(id);
                affectedItems.add(new Point(x+1, y));
            }
            if(y+1<items.length&&items[y+1][x]!=null){
                items[y+1][x].claim(id);
                affectedItems.add(new Point(x, y+1));
            }
        }
        return affectedItems;
    }
}
