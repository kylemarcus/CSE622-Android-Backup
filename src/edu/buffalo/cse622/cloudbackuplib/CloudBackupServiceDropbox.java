package edu.buffalo.cse622.cloudbackuplib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.dropbox.sync.android.DbxAccountManager;
import com.example.cse622test1.R;

public class CloudBackupServiceDropbox implements ICloudBackupService {
	
	private static final String appKey = "k22wkd6981hxmil";
    private static final String appSecret = "sjmja3vqx34nubp";
    
    private static final int REQUEST_LINK_TO_DBX = 0;
    private DbxAccountManager mDbxAcctMgr;
    
    private Activity a;
    
    CloudBackupServiceDropbox(Activity activity) {
    	a = activity;
    }

	@Override
	public boolean login() {
		
		mDbxAcctMgr = DbxAccountManager.getInstance(a.getApplicationContext(), appKey, appSecret);
		
		Intent intent = new Intent(a, DropboxLink.class);
		a.startActivity(intent);
		
		mDbxAcctMgr.startLink(a, REQUEST_LINK_TO_DBX);
		return false;
	}
	
	class DropboxLink extends Activity {
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			
			super.onCreate(savedInstanceState);
			
		}
		
		
		
	}

}
