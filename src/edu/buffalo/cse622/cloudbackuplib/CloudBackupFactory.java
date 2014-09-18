package edu.buffalo.cse622.cloudbackuplib;

import android.app.Activity;
import edu.buffalo.cse622.cloudbackuplib.CloudBackupConstants.CloudBackupServices;

public class CloudBackupFactory {
	
	public static ICloudBackupService getCloudService (CloudBackupServices s, Activity a) {
		
		if (s == CloudBackupServices.DROPBOX) {
			return new CloudBackupServiceDropbox(a);
		}
		
		return null;
		
	}

}
