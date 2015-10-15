package jk.edges.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import jk.edges.R;
import jk.edges.model.Playground;
import jk.edges.view.PlaygroundView;

public class GameActivity extends Activity {
    private Playground playground;
    private int id1,id2,score1,score2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        id1 = getIntent().getIntExtra("id1",-1);
        id2 = getIntent().getIntExtra("id2",-1);
        score1 = 0;
        score2 = 0;

        playground = new Playground(id1,id2);

        String playgroundString = "BEBNN\nENENE\nBEBEB\nENENE\nNNBEB";
        playground.parse(playgroundString);

        /*playground.claim(1, 0, id1);
        playground.claim(0, 1, id2);
        playground.claim(4, 1, id1);
        playground.claim(1, 2, id1);
        playground.claim(2, 1, id1);
        playground.claim(2, 3, id1);
        playground.claim(3, 2, id1);

        Log.d("pg","\n"+playground.stringify());*/

        PlaygroundView playgroundView = (PlaygroundView)findViewById(R.id.playground);
        playgroundView.setPlayground(playground);
        playgroundView.draw();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
