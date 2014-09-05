package com.example.cse622test1;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		storageOptions();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void storageOptions() {
		
		TextView t= (TextView)findViewById(R.id.textView1);
		t.setText("");
		t.setText("/data/data/" + getPackageName() + "\n\n");
		
		// Saving Key-Value Sets
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("Hello", "World");
		editor.commit();
		t.append("Wrote to shared prefs file\n");
		
		// save file to internal storage
		String filename = "internalFile.txt";
		String contents = "Hello world";
		FileOutputStream outputStream;
		try {
		  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
		  outputStream.write(contents.getBytes());
		  outputStream.close();
		  t.append("Wrote to internal file\n");
		} catch (Exception e) {
		  e.printStackTrace();
		}
		
		// save file to cache storage
		filename = "cacheFile.txt";
		contents = "Hello world cached";
		try {
			File file = File.createTempFile(filename, null);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(contents.getBytes());
			fos.close();
			t.append("Wrote to cache file\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// save file to external storage
		String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	    	
	    	// private
	    	File file = new File(getExternalFilesDir(null), "DemoFile.jpg");
	    	t.append("Wrote to external private file\n");
	    	
	    	// public
	    	File path = Environment.getExternalStoragePublicDirectory(
	                Environment.DIRECTORY_PICTURES);
	        file = new File(path, "DemoPicture.jpg");
	        t.append("Wrote to external public file\n");
	        
	    } else {
	    	t.append("Could not write to external storage\n");
	    }
	    
	    // write to database
	    class FeedReaderDbHelper extends SQLiteOpenHelper {
		    
		    public static final int DATABASE_VERSION = 1;
		    public static final String DATABASE_NAME = "mydatabase.db";
		    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE foo (id INTEGER, name TEXT)";
		    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS foo";

		    public FeedReaderDbHelper(Context context) {
		        super(context, DATABASE_NAME, null, DATABASE_VERSION);
		    }
		    public void onCreate(SQLiteDatabase db) {
		        db.execSQL(SQL_CREATE_ENTRIES);
		    }
		    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		        db.execSQL(SQL_DELETE_ENTRIES);
		        onCreate(db);
		    }
		    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		        onUpgrade(db, oldVersion, newVersion);
		    }
		}
	    
	    FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
	    SQLiteDatabase db = mDbHelper.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    values.put("id", 1);
	    values.put("name", "name1");
	    db.insert("foo", "null", values);
	    t.append("Wrote to database file\n");   
		
	}

}
