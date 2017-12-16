package mdsadabwasimcom.forest;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AnimalDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "animal";
    private static final int DB_VERSION=2;


    public AnimalDatabaseHelper(Context context ) {
        super(context, DB_NAME , null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db,0,DB_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db , oldVersion,  newVersion);

    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion < 1){
            db.execSQL("CREATE TABLE ANIMAL (_id INTEGER PRIMARY KEY AUTOINCREMENT , "
            +"NAME TEXT , "
            +"DESCRIPTION TEXT , "
            +"IMAGE_RESOURCE_ID1 INTEGER  , "
            +"IMAGE_RESOURCE_ID2 INTEGER); ");

            insertAnimal(db,"Lion", "lion is very dengerous animal", R.drawable.lion1,R.drawable.lion2);
            insertAnimal(db,"Elephant", "Elephant is the largest animal in the planet", R.drawable.elephant1,R.drawable.elephant2);
            insertAnimal(db,"Dog" , "dogs are really cute animals", R.drawable.dog1,R.drawable.dog2);

        }
        if (oldVersion<2){
            db.execSQL("ALTER TABLE ANIMAL ADD COLUMN FAVORITE NUMERIC");
        }
    }

    private void insertAnimal(SQLiteDatabase db, String name, String description, int resourceId1, int resourceId2) {
        ContentValues animalValues = new ContentValues();
        animalValues.put("NAME" , name);
        animalValues.put("DESCRIPTION" , description);
        animalValues.put("IMAGE_RESOURCE_ID1" , resourceId1);
        animalValues.put("IMAGE_RESOURCE_ID2" , resourceId2);
        db.insert("ANIMAL" , null, animalValues);
    }
}
