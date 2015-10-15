package jk.edges.view;

import android.content.Context;
import android.widget.LinearLayout;

import jk.edges.model.PlaygroundItem;

/**
 * Created by janne on 13.10.2015.
 */
public class PlaygroundView extends LinearLayout{
    private PlaygroundItem[][] items;

    public PlaygroundView(Context context,int width,int height) {
        super(context);
    }


}
