package com.example.cse622test1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import edu.buffalo.cse622.cloudbackuplib.CloudBackupConstants.CloudBackupServices;
import edu.buffalo.cse622.cloudbackuplib.CloudBackupFactory;
import edu.buffalo.cse622.cloudbackuplib.ICloudBackupService;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class CloudBackupActivity extends Activity {

    
    
    TextView t;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		t = (TextView)findViewById(R.id.textView1);
		
		storageOptions();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
            	Log.d("CSE622", "could login");
            } else {
                Log.d("CSE622", "could not login");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
	
	
	/*
	private void dropboxTest() {
        try {
        	Date date = new Date();
        	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        	String formattedDate = sdf.format(date);
            final String TEST_DATA = "Hello Dropbox " + formattedDate;
            final String TEST_FILE_NAME = "hello_dropbox.txt";
            DbxPath testPath = new DbxPath(DbxPath.ROOT, TEST_FILE_NAME);

            // Create DbxFileSystem for synchronized file access.
            DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());

            // Write file
            DbxFile testFile = dbxFs.create(testPath);
            try {
                testFile.writeString(TEST_DATA);
            } finally {
                testFile.close();
            }
            t.append("Wrote file to dropbox\n");
            
        } catch (IOException e) {
            t.setText("Dropbox test failed: " + e);
        }
    }*/
	
	private void storageOptions() {
		
		t.setText("");
		t.setText("/data/data/" + getPackageName() + "\n\n");
		
		// dropbox
		ICloudBackupService s = CloudBackupFactory.getCloudService(CloudBackupServices.DROPBOX, this);
		s.login();
		
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
