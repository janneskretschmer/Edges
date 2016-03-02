package jk.edges.src;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jk.edges.R;
import jk.edges.activities.GameActivity;
import jk.edges.activities.OnlineListActivity;

/**
 * Created by janne on 02.03.2016.
 */
public class GameArrayAdapter extends ArrayAdapter<String[]> {
    private int id1;
    private String name1;

    public GameArrayAdapter(Context context, ArrayList<String[]> users,int id1,String name1) {
        super(context, 0, users);
        this.id1 = id1;
        this.name1 = name1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final String[] game = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_game, parent, false);
        }

        TextView label = (TextView) convertView.findViewById(R.id.label);

        label.setText("Spiel "+game[0]+": Level "+game[1]);

        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        Intent intent;
                        if (OnlineListActivity.getWebsiteContent("http://janneskretschmer.bplaced.net/congregation/game/?is_game_open=" + game[0]).equals("1")) {
                            intent = new Intent(getContext(), GameActivity.class);
                            intent.putExtra("id2", -1);
                            intent.putExtra("online", true);
                            intent.putExtra("name2", getContext().getString(R.string.player_2));
                            intent.putExtra("level", Integer.parseInt(game[1]));
                        } else {
                            intent = new Intent(getContext(), OnlineListActivity.class);
                        }
                        intent.putExtra("id1", id1);
                        intent.putExtra("name1", name1);
                        ((Activity) getContext()).finish();
                        ((Activity) getContext()).startActivity(intent);
                    }
                }.start();
            }
        });

        return convertView;
    }
}
