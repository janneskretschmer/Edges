package jk.edges.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.HashMap;

import jk.edges.R;
import jk.edges.database.DBConnection;

public class FinishedActivity extends Activity {
    private int id1,id2,score1,score2;
    private String name1,name2;
    private TextView nameDisplay1,nameDisplay2,scoreDisplay1,scoreDisplay2,highscoreDisplay1,highscoreDisplay2,sumDisplay1,sumDisplay2;

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


        ((TextView)findViewById(R.id.name1)).setText(name1);
        ((TextView)findViewById(R.id.name2)).setText(name2);
        ((TextView)findViewById(R.id.score1_display)).setText(score1+"");
        ((TextView)findViewById(R.id.score2_display)).setText(score2+"");

        DBConnection dbConnection = new DBConnection(this);
        HashMap<String,String> scoreHistory1 = dbConnection.getScoreHistory(id1);
        ((TextView)findViewById(R.id.highscore1_display)).setText(scoreHistory1.get("highscore"));
        ((TextView)findViewById(R.id.sum1_display)).setText(scoreHistory1.get("sum"));
        ((TextView)findViewById(R.id.won1_display)).setText(scoreHistory1.get("won"));

        //change for multiplayer
        HashMap<String,String> scoreHistory2 = dbConnection.getScoreHistory(id2);
        ((TextView)findViewById(R.id.highscore2_display)).setText(scoreHistory2.get("highscore"));
        ((TextView)findViewById(R.id.sum2_display)).setText(scoreHistory2.get("sum"));
        ((TextView)findViewById(R.id.won2_display)).setText(scoreHistory2.get("won"));

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
                newGame();
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        newGame();
    }

    private void newGame(){
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("id1",id1);
        intent.putExtra("id2",id2);
        intent.putExtra("name1",name1);
        intent.putExtra("name2",name2);
        startActivity(intent);
        finish();
    }
}
