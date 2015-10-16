package jk.edges.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Random;

import jk.edges.R;
import jk.edges.model.Playground;
import jk.edges.model.ItemParent;
import jk.edges.model.Type;

/**
 * Created by janne on 13.10.2015.
 */
public class PlaygroundView extends LinearLayout{
    private ItemParent[][] items;
    private int id1,id2;
    private Context context;


    public PlaygroundView(Context context) {
        this(context, null);
    }

    public PlaygroundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlaygroundView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setOrientation(HORIZONTAL);
        items = null;
        id1 = -1;
        id2 = -1;
    }

    public void setIds(int id1,int id2){
        this.id1 = id1;
        this.id2 = id2;
    }

    public void setPlayground(Playground playground) {
        this.items = playground.getItems();
    }

    public void draw(){
        if(items == null || items.length<1)return;
        if(getChildCount()==0){//add new views to layout
            int edgeWidth = 100;//width of a horizontal edge
            int edgeHeight = 40;//height of a horizontal edge

            int[] widths = new int[items[0].length];
            int[] heights = new int[items.length];

            //add layouts and get measures of rows and columns
            for (int x = 0;x  < items[0].length; x++) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(VERTICAL);
                addView(layout);
                widths[x]=edgeHeight;
                for (int y = 0; y < items.length; y++) {
                    if(heights[y]==0)heights[y]=edgeHeight;
                    if(items[y][x]!=null&&items[y][x].getType()==Type.Box){
                        widths[x]=edgeWidth;
                        heights[y]=edgeWidth;
                    }
                }
            }
            //add views
            for (int x = 0;x  < items[0].length; x++) {
                for (int y = 0; y < items.length; y++) {
                    View item = new View(context);
                    ((LinearLayout)getChildAt(x)).addView(item);
                    ViewGroup.LayoutParams layoutParams = item.getLayoutParams();
                    layoutParams.width = widths[x];
                    layoutParams.height= heights[y];
                    item.setLayoutParams(layoutParams);

                    Random rnd = new Random();
                    item.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));

                    if(items[y][x] == null){
                        int surroundingItems = 0;
                        if(x>0&&items[y][x-1]!=null)surroundingItems++;//left item
                        if(y>0&&items[y-1][x]!=null)surroundingItems++;//upper item
                        if(x+1<items[y].length&&items[y][x+1]!=null)surroundingItems++;//right item
                        if(y+1<items.length&&items[y+1][x]!=null)surroundingItems++;//lower item
                        if(x>0&&y>0&&items[y-1][x-1]!=null)surroundingItems++;//upper left item
                        if(x>0&&y+1<items.length&&items[y+1][x-1]!=null)surroundingItems++;//lower left item
                        if(x+1<items[y].length&&y+1<items.length&&items[y+1][x+1]!=null)surroundingItems++;//lower right item
                        if(x+1<items[y].length&&y>0&&items[y-1][x+1]!=null)surroundingItems++;//upper right item

                        if(surroundingItems>7){//edge cross
                            item.setBackgroundColor(getResources().getColor(R.color.dark_gray));//I have to use the deprecated Method, because the new one is not available for Android L
                        }else if(surroundingItems>0){//border
                            item.setBackgroundColor(getResources().getColor(R.color.black));
                        }else{//outside
                            item.setBackgroundColor(getResources().getColor(R.color.lighter_gray));
                        }
                    }else if(items[y][x].getType()==Type.Box){
                        if(items[y][x].isClaimed()){
                            if (items[y][x].getOwner()==id1&&id1>=0)item.setBackgroundColor(getResources().getColor(R.color.blue));
                            else if (items[y][x].getOwner()==id2&&id2>=0)item.setBackgroundColor(getResources().getColor(R.color.red));
                        }else item.setBackgroundColor(getResources().getColor(R.color.light_gray));
                    }else if(items[y][x].getType()==Type.Edge){
                        if(items[y][x].isClaimed()){
                            if (items[y][x].getOwner()==id1&&id1>=0)item.setBackgroundColor(getResources().getColor(R.color.edge_blue));
                            else if (items[y][x].getOwner()==id2&&id2>=0)item.setBackgroundColor(getResources().getColor(R.color.edge_red));
                        }else item.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                }
            }
        }
    }
}
