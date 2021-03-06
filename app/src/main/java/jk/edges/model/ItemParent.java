package jk.edges.model;

/**
 * Created by janne on 24.09.2015.
 */
public abstract class ItemParent implements Cloneable{
    private int x,y;
    protected int owner;
    private Type type;


    public ItemParent(int x, int y, Type type){
        this.x = x;
        this.y = y;
        this.type = type;
        this.owner = -1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Type getType() {
        return type;
    }

    public abstract int claim(int id);

    public boolean isClaimed(){
        return owner > -1;
    }
    public int getOwner(){
        return owner;
    }

    @Override
    protected ItemParent clone() throws CloneNotSupportedException{
        return(ItemParent) super.clone();
    }
}
