package jk.edges.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import jk.edges.R;
import jk.edges.database.DBConnection;
import jk.edges.model.Playground;

public class LoginActivity extends Activity {
    private CheckBox ki;
    private TextView title, newAccount,error,header;
    private Button login;
    private EditText name,password;
    private DBConnection dbConnection;
    private int player1;
    private String name1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);


        dbConnection = new DBConnection(this);

        //init views
        header = (TextView)findViewById(R.id.header);
        title = (TextView)findViewById(R.id.title);
        newAccount = (TextView)findViewById(R.id.new_account);
        error = (TextView)findViewById(R.id.error);

        login = (Button)findViewById(R.id.login);
        ki = (CheckBox)findViewById(R.id.ki);

        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);

        //check if player was already set, e.g. if a new account was created
        player1 = getIntent().getIntExtra("player_1", -1);
        if(player1>=0){
            header.setText(R.string.player_2);
            header.setTextColor(getResources().getColor(R.color.red));
        }
        name1 = getIntent().getStringExtra("name1");

        ki.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(player1>=0) {
                    if (isChecked) {
                        name.setInputType(InputType.TYPE_NULL);
                        password.setInputType(InputType.TYPE_NULL);
                    }else{
                        name.setInputType(InputType.TYPE_CLASS_TEXT);
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                }
            }
        });


        //set actions
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userPassword = password.getText().toString();

                //check data
                boolean validatePlayer2=player1<0&&ki.isChecked()||!ki.isChecked();
                if(userName.length()<=0&&validatePlayer2)error.setText(R.string.error_no_name);
                else if(userPassword.length()<=0&&validatePlayer2)error.setText(R.string.error_no_password);
                else{//validate credentials
                    int id = dbConnection.validatePassword(userName,userPassword);
                    if (id<0&&validatePlayer2){
                        error.setText(R.string.error_false_credentials);
                        password.setText("");
                    }else{
                        if(player1<0&&!ki.isChecked()){
                            player1=id;
                            name1 = userName;
                            resetViews();
                            header.setText(R.string.player_2);
                            header.setTextColor(getResources().getColor(R.color.red));
                            Log.d("p1",player1+"");
                        }else{
                            if(player1==id&&!ki.isChecked())error.setText(R.string.error_account_logged_in);
                            else{
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                                intent.putExtra("id1",(id>0&&ki.isChecked())?id:player1);
                                intent.putExtra("id2",(ki.isChecked())?0:id);
                                intent.putExtra("name1",(userName.length()>0&&ki.isChecked())?userName:name1);
                                intent.putExtra("name2", (ki.isChecked()) ? getString(R.string.ki_name):userName);
                                finish();
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
        });

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NewAccountActivity.class);
                intent.putExtra("player_1",player1);
                intent.putExtra("name1",name1);
                startActivity(intent);
            }
        });

    }

    private void resetViews(){
        name.setText("");
        password.setText("");
        error.setText("");
    }
}
