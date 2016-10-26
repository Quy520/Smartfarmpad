package com.smartfarm.db.access;

import java.io.File;
import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.smartfarm.db.bean.CmdRecord;
import com.smartfarm.db.bean.ControlRecord;
import com.smartfarm.db.bean.HuanxinMsgRecord;
import com.smartfarm.db.bean.SerialMsgRecord;
import com.smartfarm.db.bean.TempConfig;
import com.smartfarm.db.bean.TempRecord;
import com.smartfarm.db.bean.User;
import com.smartfarm.tools.ExceptionUtils;
import com.smartfarm.tools.FileUtils;
import com.smartfarm.view.AppContext;

@SuppressLint("SdCardPath")
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	public static final String DATABASE_TEMP = "/data/data/com.smartfarm.view/databases/main";
	public static final String DATABASE_DIR = Environment.getExternalStorageDirectory() + "/smartfarm_pad/";
	public static final String DATABASE_PATH = Environment.getExternalStorageDirectory() 
			+ "/smartfarm_pad/smartfarm_pad_db.db";
	private static final String DATABASE_NAME = "smartfarm_pad_db";
	private static final int DATABASE_VERSION = 7;

	private static ThreadLocal<DatabaseHelper> localHelper = new ThreadLocal<DatabaseHelper>();
	private boolean mainTmpDirSet = false;
	
	public static DatabaseHelper getDbHelper() {
		
		DatabaseHelper helper = localHelper.get();
		
		if(helper == null) {
			
			helper = new DatabaseHelper();
			localHelper.set(helper);
		}
			
		return helper;
	}
	
	public static void init() throws SQLiteCantOpenDatabaseException {
		
		File dbDir = new File(DATABASE_DIR);
		
		if(!dbDir.exists()) 
			dbDir.mkdirs();

		if(!dbDir.isDirectory()) {
			dbDir.delete();
			dbDir.mkdirs();
		}
		
		File dbFile = new File(DATABASE_PATH);
		if (!dbFile.exists()) {
			
			createDb();
		} else if(dbFile.isDirectory()) {
			
			FileUtils.deleteDir(dbFile);
			createDb();
		} else {

			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
					DatabaseHelper.DATABASE_PATH, null);

			try {
				
				DatabaseHelper dbHelper = getDbHelper();
				dbHelper.onUpgrade(db, db.getVersion(), DATABASE_VERSION);
				db.close();
			} catch (Exception e) {
				ExceptionUtils.report(e);
			}
		}
	}
	
	private static void createDb() throws SQLiteCantOpenDatabaseException {
		
		try {
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
					DatabaseHelper.DATABASE_PATH, null);
			
			DatabaseHelper dbHelper = getDbHelper();
			dbHelper.onCreate(db);
			db.close();
		} catch(SQLiteCantOpenDatabaseException e) {

			ExceptionUtils.report(e);
			throw e; 
		} catch (Exception e) {
			ExceptionUtils.report(e);
		}
	}
	
	private DatabaseHelper() {
		
		super(AppContext.context(), DATABASE_NAME, null, DATABASE_VERSION);
	}
	
    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
    	
    	SQLiteDatabase dbInstance = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    	
    	if (!mainTmpDirSet) {
    		
            new File(DATABASE_TEMP).mkdir();

            dbInstance.execSQL("PRAGMA temp_store_directory = '" + DATABASE_TEMP + "'");
            mainTmpDirSet = true;
        } 

        return dbInstance;
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
    	
    	SQLiteDatabase dbInstance = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
    	
    	if (!mainTmpDirSet) {
    		
            new File(DATABASE_TEMP).mkdir();

            dbInstance.execSQL("PRAGMA temp_store_directory = '" + DATABASE_TEMP + "'");
            mainTmpDirSet = true;
        } 

        return dbInstance;
    }

	@Override
	public void onUpgrade(SQLiteDatabase database, 
			ConnectionSource connectionSource, int oldVersion, int newVersion) {

		try { 

			if(newVersion > oldVersion) {
				TableUtils.dropTable(connectionSource, User.class, true);
		        TableUtils.createTable(connectionSource, User.class);
		        database.setVersion(newVersion);
			}
	        
		} catch (SQLException e) {

        	ExceptionUtils.report(e);
		}  
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		
		try {  
			
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, CmdRecord.class);  
            TableUtils.createTable(connectionSource, ControlRecord.class);  
            TableUtils.createTable(connectionSource, HuanxinMsgRecord.class);  
            TableUtils.createTable(connectionSource, SerialMsgRecord.class);  
            TableUtils.createTable(connectionSource, TempConfig.class);     
            TableUtils.createTable(connectionSource, TempRecord.class);  
            
        } catch (SQLException e) {

        	ExceptionUtils.report(e);
		}
	}
}
	