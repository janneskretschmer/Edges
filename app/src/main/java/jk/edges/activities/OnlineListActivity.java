package jk.edges.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

import jk.edges.R;
import jk.edges.src.GameArrayAdapter;

public class OnlineListActivity extends Activity {
    private int id1;
    private String name1;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_online_list);

        id1 = getIntent().getIntExtra("id1", -1);
        name1 = getIntent().getStringExtra("name1");
        list = (ListView) findViewById(R.id.game_list);
        list.setTag(true);

        new Thread(){
            @Override
            public void run() {
                try {
                    final ArrayList<String[]> games= new ArrayList<>();
                    Log.d("web",getWebsiteContent("http://janneskretschmer.bplaced.net/game/?list_games"));
                    JSONObject json = new JSONObject(getWebsiteContent("http://janneskretschmer.bplaced.net/game/?list_games"));
                    Iterator<?> keys = json.keys();
                    while( keys.hasNext() ) {
                        String key = (String)keys.next();
                        if ( json.get(key) instanceof String ) {
                            String[] game = new String[2];
                            game[0] = key;
                            game[1] = (String)json.get(key);
                            games.add(game);
                        }
                    }

                    runOnUiThread(new Thread() {
                        @Override
                        public void run() {
                            GameArrayAdapter adapter = new GameArrayAdapter(OnlineListActivity.this, games,id1,name1);
                            list.setAdapter(adapter);
                        }
                    });
                    Log.d("t",":)");
                    Thread.sleep(500);
                    if(list!=null && list.getTag().equals(true))run();
                } catch (Exception e) {
                    e.printStackTrace();
                    goBack();
                }
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        list.setTag(false);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("id1", id1);
        intent.putExtra("name1", name1);
        finish();
        startActivity(intent);
    }

    public static String getWebsiteContent(String urlString){
        StringBuffer sb = new StringBuffer();
        InputStream is = null;
        BufferedReader reader = null;
        try{
            //get url object
            URL url = new URL(urlString);

            //open stream
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
            is = connection.getInputStream();
            //read it line for line
            reader = new BufferedReader( new InputStreamReader( is )  );
            String line = null;
            while( ( line = reader.readLine() ) != null )  {
                sb.append(line);
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if(is!=null)is.close();
                if(reader!=null)reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //check if StringBuffer contains text
        return sb.toString().trim();
    }
}
