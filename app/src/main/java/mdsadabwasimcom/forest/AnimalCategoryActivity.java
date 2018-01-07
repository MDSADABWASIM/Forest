package mdsadabwasimcom.forest;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class AnimalCategoryActivity extends ListActivity {
    public SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
           new MyTask().execute();



        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database Unavailable",Toast.LENGTH_LONG);
            toast.show();
        }
    }
    @Override
    protected void onListItemClick(ListView l,
                                   View v,
                                   int position,
                                   long id) {
        Intent intent =new Intent(AnimalCategoryActivity.this, AnimalActivity.class);
        intent.putExtra(AnimalActivity.EXTRA_ANIMALNO , (int) id);
        startActivity(intent);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

   private class  MyTask extends AsyncTask<Void,Void,Cursor>{
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
       }

       @Override
        protected Cursor doInBackground(Void... params) {
           SQLiteOpenHelper animalDatabaseHelper = new AnimalDatabaseHelper(AnimalCategoryActivity.this);
            try{

                db= animalDatabaseHelper.getReadableDatabase();
                cursor= db.query("ANIMAL",
                        new String[] {"_id", "NAME"},
                        null,null,null,null,null);


                return cursor;

            }catch (SQLiteException e){
               return  null;
            }
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            CursorAdapter listAdapter = new SimpleCursorAdapter(AnimalCategoryActivity.this,
                    android.R.layout.simple_list_item_1,cursor,
                    new String[] {"NAME"},
                    new int[] {android.R.id.text1},
                    0);
            ListView listView = getListView();
            listView.setAdapter(listAdapter);

        }
    }


}
