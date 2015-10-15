package jk.edges.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Random;

import jk.edges.model.Playground;
import jk.edges.model.ItemParent;
import jk.edges.model.Type;

/**
 * Created by janne on 13.10.2015.
 */
public class PlaygroundView extends LinearLayout{
    private ItemParent[][] items;
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
    }

    public void setPlayground(Playground playground) {
        this.items = playground.getItems();
    }

    public void draw(){
        if(items == null || items.length<1)return;
        if(getChildCount()==0){//add new views to layout
            int edgeWidth = 50;//width of a horizontal edge
            int edgeHeight = 10;//height of a horizontal edge

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
                        int width = 0;
                        int height = 0;
                        if(x>0&&items[y][x-1]!=null){//left item
                            surroundingItems++;
                            if (items[y][x - 1].getType() == Type.Edge)height=edgeHeight;
                            else height = edgeWidth;
                        }
                        if(y>0&&items[y-1][x]!=null){//upper item
                            surroundingItems++;
                            if (items[y-1][x].getType() == Type.Edge)width=edgeHeight;
                            else width = edgeWidth;
                        }
                        if(x+1<items[y].length&&items[y][x+1]!=null){//right item
                            surroundingItems++;
                            if (items[y][x+1].getType() == Type.Edge)height=edgeHeight;
                            else height = edgeWidth;
                        }
                        if(y+1<items.length&&items[y+1][x]!=null)if(x+1<items[y].length&&items[y][x+1]!=null){//lower item
                            surroundingItems++;
                            if (items[y+1][x].getType() == Type.Edge)width=edgeHeight;
                            else width = edgeWidth;
                        }
                    }
                }
            }
        }
    }
}
