package jk.edges.database;
/**
 * Created by janne on 22.09.2015.
 */


        import java.io.UnsupportedEncodingException;
        import java.security.MessageDigest;
        import java.security.SecureRandom;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.EnumMap;
        import java.util.HashMap;
        import java.util.Hashtable;
        import java.util.Random;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.DatabaseUtils;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.database.sqlite.SQLiteDatabase;
        import android.util.Log;

public class DBConnection extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Edges.db";
    public static final String USER_TABLE_NAME = "user";
    public static final String USER_COLUMN_ID = "id";
    public static final String USER_COLUMN_NAME = "name";
    public static final String USER_COLUMN_PASSWORD = "password";
    public static final String USER_COLUMN_SALT = "salt";
    public static final String USER_COLUMN_HIGHSCORE = "highscore";
    public static final String USER_COLUMN_SUM = "sum";
    public static final String USER_COLUMN_WON = "won";
    private static DBConnection instance=null;
    private HashMap hp;

    private DBConnection(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    public static DBConnection getInstance(Context context){
        if(instance==null)instance=new DBConnection(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE USER " +
                        "(id INTEGER PRIMARY KEY," +
                        " name TEXT UNIQUE NOT NULL," +
                        "password TEXT NOT NULL," +
                        "salt TEXT NOT NULL," +
                        "highscore INTEGER DEFAULT 0," +
                        "sum INTEGER DEFAULT 0," +
                        "won INTEGER DEFAULT 0)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean newAccount  (String name, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //generate salt
        byte[] salt = new byte[32];
        new SecureRandom().nextBytes(salt);
        String saltString = new String(salt);

        //hash password
        password = hashPassword(password,saltString);

        contentValues.put("name", name);
        contentValues.put("password", password);
        contentValues.put("salt", saltString);
        return db.insert("user", null, contentValues) >= 0;
    }

    public int validatePassword(String name,String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT id,salt,password FROM user WHERE name = ?", new String[]{name} );
        res.moveToFirst();

        int id = -1;

        //if name exists
        if(!res.isAfterLast()){
            String salt = res.getString(res.getColumnIndex(USER_COLUMN_SALT));

            //if hashes match
            if(hashPassword(password,salt).contentEquals(res.getString(res.getColumnIndex(USER_COLUMN_PASSWORD)))){
                 id = res.getInt(res.getColumnIndex(USER_COLUMN_ID));
            }
        }
        res.close();
        return id;
    }

    public void updateScore(int id,int score, boolean won){
        Log.d(""+id,""+score);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("UPDATE user SET highscore = CASE WHEN highscore < ? THEN ? ELSE highscore END, sum = sum + ?, won = won + ? WHERE id = ?",
                new String[]{score + "", (won ? score : 0) + "", score + "", (won ? "1" : "0"), id + ""});
        c.close();
    }

    public HashMap<String,String> getScoreHistory(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT highscore,sum,won FROM user WHERE id = ?", new String[]{id+""} );
        res.moveToFirst();

        HashMap<String,String> map = new HashMap<>();

        //if name exists
        if(!res.isAfterLast()){
             map.put("highscore", res.getString(res.getColumnIndex(USER_COLUMN_HIGHSCORE)));
             map.put("sum", res.getString(res.getColumnIndex(USER_COLUMN_SUM)));
             map.put("won", res.getString(res.getColumnIndex(USER_COLUMN_WON)));
        }
        res.close();
        return map;
    }


    private String hashPassword(String password,String salt){
        password = password+salt;

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(password.getBytes());
            return new String(sha.digest());
        } catch (Exception e) {
            //sha 256 exists so there won't be an exception
            e.printStackTrace();
        }
        return password;
    }
}