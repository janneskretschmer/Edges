package jk.edges.database;
/**
 * Created by janne on 22.09.2015.
 */


        import java.io.UnsupportedEncodingException;
        import java.security.MessageDigest;
        import java.security.SecureRandom;
        import java.util.ArrayList;
        import java.util.Arrays;
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
    private HashMap hp;

    public DBConnection(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
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

    /*public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }*/

    /*public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }*/

    /*public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }*/

    /*public Integer deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }*/

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

    /*public ArrayList<String> getAllCotacts()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }*/

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