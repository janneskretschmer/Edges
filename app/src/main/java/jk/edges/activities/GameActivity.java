package jk.edges.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import jk.edges.R;
import jk.edges.database.DBConnection;
import jk.edges.model.ItemParent;
import jk.edges.model.Playground;
import jk.edges.view.PlaygroundView;

public class GameActivity extends Activity {
    private TextView nameDisplay1,nameDisplay2,scoreDisplay1,scoreDisplay2;
    private Playground playground;
    private int id1,id2,currentPlayer,claimedEdges,numberOfEdges;
    private String name1,name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        id1 = getIntent().getIntExtra("id1", -1);
        id2 = getIntent().getIntExtra("id2", -1);
        name1 = getIntent().getStringExtra("name1");
        name2 = getIntent().getStringExtra("name2");

        playground = new Playground(id1,id2);

        String playgroundString = getIntent().getStringExtra("playground");
        if(playgroundString==null){
            //playgroundString = getString(R.string.level3);
            final String[] levels = getResources().getStringArray(R.array.levels);

            //level chooser
            View layout = getLayoutInflater().inflate(R.layout.dialog_levels, null);
            final PlaygroundView preview = (PlaygroundView)layout.findViewById(R.id.preview);
            preview.setTag(0); //save index of chosen level in tag
            drawPreview(preview, levels[0]);

            layout.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int chosen = (Integer) preview.getTag();
                    if (--chosen < 0) chosen = levels.length - 1;
                    preview.setTag(chosen);
                    drawPreview(preview, levels[chosen]);
                }
            });
            layout.findViewById(R.id.forward).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int chosen = (Integer)preview.getTag();
                    if(++chosen>=levels.length)chosen=0;
                    preview.setTag(chosen);
                    drawPreview(preview, levels[chosen]);
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setView(layout)
                    .setCancelable(false);
            builder.create().show();
            //initPlayground(playgroundString);
        }else initPlayground(playgroundString);
    }

    private void drawPreview(PlaygroundView preview, String pgString){
        Playground pg = new Playground();
        pg.parse(pgString);
        preview.setPlayground(pg);
        preview.setEdgeMeasures(80,20);
        preview.reset();
        preview.draw();
    }

    private void initPlayground(String playgroundString){
        playground.parse(playgroundString);

        nameDisplay1 = (TextView)findViewById(R.id.player1);
        nameDisplay1.setText(name1);
        nameDisplay2 = (TextView)findViewById(R.id.player2);
        nameDisplay2.setText(name2);
        scoreDisplay1 = (TextView)findViewById(R.id.score1);
        scoreDisplay1.setText(playground.getScore1()+"");
        scoreDisplay2 = (TextView)findViewById(R.id.score2);
        scoreDisplay2.setText(playground.getScore2()+"");

        final PlaygroundView playgroundView = (PlaygroundView)findViewById(R.id.playground);
        playgroundView.setPlayground(playground);
        playgroundView.setIds(id1,id2);
        playgroundView.draw();

        //set Onclick listener
        View[] edges = playgroundView.getEdges();
        if(edges!=null) {
            numberOfEdges=edges.length;
            for (int i = 0; i < edges.length; i++) {
                edges[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //return if already claimed
                        if(v.getTag(R.dimen.claimed)!=null&&v.getTag(R.dimen.claimed).equals(true))return;

                        playgroundView.updateViews(playground.claim((int) v.getTag(R.dimen.x), (int) v.getTag(R.dimen.y), currentPlayer));
                        updateScore();
                        claimedEdges++;
                        v.setTag(R.dimen.claimed,true);
                        if(claimedEdges>=numberOfEdges){
                            finishGame();
                        }
                        if(!playground.getAgain())switchPlayer();
                    }
                });
            }
        }else numberOfEdges = 0;

        //random start player
        currentPlayer=(Math.random()>0.5)?id1:id2;
        switchPlayer();
    }

    private void finishGame(){
        int score1 = playground.getScore1();
        int score2 = playground.getScore2();

        DBConnection dbConnection = new DBConnection(getApplicationContext());
        dbConnection.updateScore(id1, score1, score1 > score2);
        dbConnection.updateScore(id2, score2, score1 < score2);

        Intent intent = new Intent(getApplicationContext(),FinishedActivity.class);
        intent.putExtra("id1",id1);
        intent.putExtra("id2",id2);
        intent.putExtra("name1",name1);
        intent.putExtra("name2",name2);
        intent.putExtra("score1",score1);
        intent.putExtra("score2", score2);
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        View layout = getLayoutInflater().inflate(R.layout.dialog_pause, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(layout)
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        layout.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        layout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishGame();
            }
        });

        layout.findViewById(R.id.new_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    private void updateScore(){
        scoreDisplay1.setText(playground.getScore1()+"");
        scoreDisplay2.setText(playground.getScore2()+"");
    }
    private void switchPlayer(){
        if(currentPlayer == id1){
            currentPlayer=id2;
            nameDisplay2.setTypeface(null, Typeface.BOLD);
            nameDisplay1.setTypeface(null, Typeface.NORMAL);
        }else{
            currentPlayer=id1;
            nameDisplay2.setTypeface(null, Typeface.NORMAL);
            nameDisplay1.setTypeface(null, Typeface.BOLD);
        }
    }
}
