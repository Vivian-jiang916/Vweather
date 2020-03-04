package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class VWeatherOpenHelper extends SQLiteOpenHelper{

/**
 * 创建省Province表
 */
	public static final String Create_province = "create table Province ("
		+"id integer primary key autoincrement,"+"province_name text,"
			+"province_code text)";
/**
 * 创建城市City表	
 */
	
	public static final String Create_city = "create table City ("
			+"id integer primary key autoincrement,"+"city_name text,"
				+"city_code text"+"province_id integer)";
	
	/**
	 * 创建Country表
	 */
	public static final String Create_county = "create table County ("
			+"id integer primary key autoincrement,"+"county_name text,"
				+"county_code text"+"city_id integer)";
	public VWeatherOpenHelper(Context context, String name, CursorFactory 
			factory,  int version)
	{
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate (SQLiteDatabase db)
	{
		db.execSQL(Create_province);
		db.execSQL(Create_city);
		db.execSQL(Create_county);
	}
	
	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}
	
}
