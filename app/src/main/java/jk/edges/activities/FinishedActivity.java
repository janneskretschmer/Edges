package jk.edges.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import jk.edges.R;

public class FinishedActivity extends Activity {
    private int id1,id2,score1,score2;
    private String name1,name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_finished);

        id1 = getIntent().getIntExtra("id1", -1);
        id2 = getIntent().getIntExtra("id2", -1);
        score1 = getIntent().getIntExtra("score1", 0);
        score2 = getIntent().getIntExtra("score2", 0);
        name1 = getIntent().getStringExtra("name1");
        name2 = getIntent().getStringExtra("name2");

        TextView winner = (TextView)findViewById(R.id.winner);
        if(score1>score2){
            winner.setText(getString(R.string.winner,name1));
            winner.setTextColor(getResources().getColor(R.color.blue));
        }
        else if(score1<score2){
            winner.setText(getString(R.string.winner,name2));
            winner.setTextColor(getResources().getColor(R.color.red));
        }
        else{
            winner.setText(R.string.tie);
            winner.setTextColor(getResources().getColor(R.color.black));
        }

        findViewById(R.id.new_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                intent.putExtra("id1",id1);
                intent.putExtra("id2",id2);
                intent.putExtra("name1",name1);
                intent.putExtra("name2",name2);
                startActivity(intent);
                finish();
            }
        });
    }
}
