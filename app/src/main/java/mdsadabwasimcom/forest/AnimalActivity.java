package mdsadabwasimcom.forest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AnimalActivity extends Activity {
    SQLiteDatabase db;
    Cursor cursor;

    public static final String EXTRA_ANIMALNO ="animalNo" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        // get the animal from the intent
        int animalNo = (int) getIntent().getExtras().get(EXTRA_ANIMALNO);


        try {
            //AsyncTask class for database handling.
           new UpdateTasker().execute(animalNo);
        }catch (SQLException e){
            Toast toast=Toast.makeText(this,"Database Unavailable",Toast.LENGTH_LONG);
            toast.show();
        }

    }
     //UPDATE THE DATABASE WHEN CHECKBOX IS CLICKED
    public void onFavoriteClicked(View view) {
        int animalNo = (int) getIntent().getExtras().get("animalNo");
        //another AsyncTask class for database handling.
       new UpdateAnimalTask().execute(animalNo);
    }
    //task to run in background for favorite animals cursor.
    private class UpdateAnimalTask extends AsyncTask<Integer, Void ,Boolean>{
        ContentValues animalValues;

        @Override
        //for setup of doInBackground method
        protected void onPreExecute() {
            CheckBox favorite = (CheckBox) findViewById(R.id.check_favorite);
            animalValues = new ContentValues();
            animalValues.put("FAVORITE", favorite.isChecked());
           }
 //Actual code that run in background.
        @Override
        protected Boolean doInBackground(Integer... animals) {
            int animalNo= animals[0];
            SQLiteOpenHelper animalDatabaseHelper = new AnimalDatabaseHelper(AnimalActivity.this);

            try {
                db = animalDatabaseHelper.getWritableDatabase();
                db.update("ANIMAL", animalValues, "_id = ?", new String[] {Integer.toString(animalNo)});
                return true;
            }catch (SQLiteException e){
             return false;
            }

        }
//use the result of doInBackground to do something.
        @Override
        protected void onPostExecute(Boolean success) {
            if(!success){
                Toast toast = Toast.makeText(AnimalActivity.this, "DATABASE UNAVAILABLE",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    //Task to run in background for animals list cursor.
    private class UpdateTasker extends AsyncTask<Integer,Void,Cursor>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Integer... animals) {
            int animalNo = animals[0];

            try {
                SQLiteOpenHelper animalDatabaseHelper = new AnimalDatabaseHelper(AnimalActivity.this);
                db = animalDatabaseHelper.getWritableDatabase();

                cursor = db.query("ANIMAL",
                        new String[] {"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID1", "IMAGE_RESOURCE_ID2", "FAVORITE"},
                        "_id=?", new String[] {Integer.toString(animalNo)},
                        null,null,null);
                return cursor;
            }catch (SQLException e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            String descriptionText = null;
            String nameText = null;
            int photoId1 = 0;
            int photoId2 = 0;
            boolean isFavorite = false;

            //move to the first record in cursor
            if (cursor.moveToFirst()) {

                //Get the animal details from the cursor
                nameText = cursor.getString(0);
                descriptionText = cursor.getString(1);
                photoId1 = cursor.getInt(2);
                photoId2 = cursor.getInt(3);
                isFavorite = (cursor.getInt(4) == 1);
            }

            //Populate the animal name
            TextView name = (TextView) findViewById(R.id.name);
            name.setText(nameText);


            //populate the animal description
            TextView description = (TextView) findViewById(R.id.description);
            description.setText(descriptionText);

            //Populate the animal first image
            ImageView photo1 = (ImageView) findViewById(R.id.photo1);
            photo1.setImageResource(photoId1);


            //Populate the animal second image
            ImageView photo2 = (ImageView) findViewById(R.id.photo2);
            photo2.setImageResource(photoId2);

            //Populate the favorite checkbox
            CheckBox favorite = (CheckBox) findViewById(R.id.check_favorite);
            favorite.setChecked(isFavorite);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();

    }
}





















