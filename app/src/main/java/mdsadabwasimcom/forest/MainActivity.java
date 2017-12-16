package mdsadabwasimcom.forest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

    private SQLiteDatabase db;
    private Cursor favoritesCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create an OnItemClickListener
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView , View view, int position, long id) {
                if(position==0 ){
                    Intent intent = new Intent(MainActivity.this,AnimalCategoryActivity.class);
                    startActivity(intent);
                }
            }
        };
        //Add the listener to the options Listview
        ListView listView = (ListView) findViewById(R.id.list_item);
        listView.setOnItemClickListener(itemClickListener);

        //Populate the list_favorites Listview from a cursor
        ListView listFavorites = (ListView) findViewById(R.id.list_favorites);

        try {
            SQLiteOpenHelper animalDatabaseHelper = new AnimalDatabaseHelper(this);
            db = animalDatabaseHelper.getReadableDatabase();
            favoritesCursor = db.query("ANIMAL",
                    new String[] {"_id", "NAME"},
                    "FAVORITE = 1",
                    null,null,null,null);
            CursorAdapter favoriteAdapter =
                    new SimpleCursorAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            favoritesCursor,
                            new String[] {"NAME"},
                            new int[] {android.R.id.text1}, 0);
            listFavorites.setAdapter(favoriteAdapter);

        }catch (SQLiteException e){

            Toast.makeText(this,"DATABASE UNAVAILABLE",Toast.LENGTH_LONG).show();
        }
        //Navigate to the AnimalActivity if any favorite animal is clicked

        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,AnimalActivity.class);
                intent.putExtra(AnimalActivity.EXTRA_ANIMALNO, (int)id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            AnimalDatabaseHelper animalDatabaseHelper = new AnimalDatabaseHelper(this);
            db=animalDatabaseHelper.getReadableDatabase();
            Cursor newCursor = db.query("ANIMAL",
                    new String[] {"_id", "NAME"},
                    "FAVORITE=1",
                    null,null,null,null);
            ListView listFavorites = (ListView) findViewById(R.id.list_favorites);
            CursorAdapter adapter = (CursorAdapter) listFavorites.getAdapter();
            adapter.changeCursor(newCursor);
            favoritesCursor= newCursor;
        }catch (SQLiteException e){
            Toast.makeText(this,"DATABASE UNAVAILABLE",Toast.LENGTH_LONG).show();
        }
    }
}






















