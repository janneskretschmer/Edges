package jk.edges.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import jk.edges.R;
import jk.edges.database.DBConnection;

public class NewAccountActivity extends Activity {
    private EditText name,password,confirm;
    private Button create;
    private TextView back,error;
    private DBConnection dbConnection;
    private int player1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        player1= getIntent().getIntExtra("player_1",-1);

        //init views
        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        confirm = (EditText)findViewById(R.id.confirm_password);

        create = (Button)findViewById(R.id.create_account);

        back = (TextView)findViewById(R.id.back);
        error = (TextView)findViewById(R.id.error);

        dbConnection = new DBConnection(this);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = name.getText().toString();
                String newPassword = password.getText().toString();
                String conPassword = confirm.getText().toString();

                //check data
                if(newName.length()<=0)error.setText(R.string.error_no_name);
                else if(newPassword.length()<=0)error.setText(R.string.error_no_password);
                else if(conPassword.length()<=0)error.setText(R.string.error_no_confirm);
                else if(!newPassword.contentEquals(conPassword))error.setText(R.string.error_passwords_no_match);
                else if(dbConnection.newAccount(newName,newPassword))goBack();
                else error.setText(R.string.error_name_exists);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    private void goBack(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("player_1",player1);
        startActivity(intent);
        finish();
    }
}
