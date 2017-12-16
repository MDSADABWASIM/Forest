package mdsadabwasimcom.forest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AnimalActivity extends Activity {

    public static final String EXTRA_ANIMALNO ="animalNo" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        // get the animal from the intent
        int animalNo = (int) getIntent().getExtras().get(EXTRA_ANIMALNO);


        try {
            SQLiteOpenHelper animalDatabaseHelper = new AnimalDatabaseHelper(this);
            SQLiteDatabase db = animalDatabaseHelper.getWritableDatabase();

            Cursor cursor = db.query("ANIMAL",
            new String[] {"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID1", "IMAGE_RESOURCE_ID2", "FAVORITE"},
            "_id=?", new String[] {Integer.toString(animalNo)},
            null,null,null);

            //move to the first record in cursor
            if (cursor.moveToFirst()){

                //Get the animal details from the cursor
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId1 = cursor.getInt(2);
                int photoId2 = cursor.getInt(3);
                boolean isFavorite = (cursor.getInt(4) ==1);

                //Populate the animal name
                TextView name = (TextView) findViewById(R.id.name);
                name.setText(nameText);

                //populate the animal description
                TextView description = (TextView) findViewById(R.id.description);
                description.setText(descriptionText);

                //Populate the animal first image
                ImageView photo1= (ImageView) findViewById(R.id.photo1);
                photo1.setImageResource(photoId1);


                //Populate the animal second image
                ImageView photo2= (ImageView) findViewById(R.id.photo2);
                photo2.setImageResource(photoId2);

                //Populate the favorite checkbox
                CheckBox favorite = (CheckBox) findViewById(R.id.check_favorite);
                favorite.setChecked(isFavorite);

            }
            cursor.close();
            db.close();
        }catch (SQLException e){
            Toast toast=Toast.makeText(this,"Database Unavailable",Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public void onFavoriteClicked(View view) {
        int animalNo = (int) getIntent().getExtras().get("animalNo");
        CheckBox favorite = (CheckBox) findViewById(R.id.check_favorite);
        ContentValues animalValues = new ContentValues();
        animalValues.put("FAVORITE", favorite.isChecked());
        SQLiteOpenHelper animalDatabaseHelper = new AnimalDatabaseHelper(AnimalActivity.this);

        try {
            SQLiteDatabase db = animalDatabaseHelper.getWritableDatabase();
            db.update("ANIMAL", animalValues, "_id = ?", new String[] {Integer.toString(animalNo)});
            db.close();
        }catch (SQLiteException e){
            Toast.makeText(this,"DATABASE UNAVAILABLE", Toast.LENGTH_LONG).show();
        }
    }
}


















