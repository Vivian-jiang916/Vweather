package com.vweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.vweather.app.model.City;
import com.vweather.app.model.County;
import com.vweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 *  VWeatherDB是一个单例类，将它的构造方法私有化
 *  用getInstance()方法来获取 VWeatherDB的实例。
 * 这样保证了全局范围内只有一个VWeatherDB的实例
 * 接着在VWeatherDB中提供了六组方法，saveProvince(),loadProvince(),saveCity(),
 * loadCities(),saveCounty(),loadCounties()分别用于读取和存储省、时、县的数据。
 *
 */
public class VWeatherDB
{
	/**
	 * Database name
	 */
    public static final String db_name ="v_weather";
    
    /**
     * Database version(版本)
     */
    public static final int version = 1;
    private static VWeatherDB vWeatherDB;
    private SQLiteDatabase db;
    
    /**
     * 将构造方法私有化
     */
    private VWeatherDB (Context context)
    {
    	VWeatherOpenHelper dbHelper = new VWeatherOpenHelper(context,db_name,
    			null,version);
    	db = dbHelper.getWritableDatabase();
    	
    }
    
    /**
     * 获取VWeatherDB实例
     */
    public synchronized static VWeatherDB getInstance(Context context)
    {
    	if(vWeatherDB ==null)
    	{
    		vWeatherDB = new VWeatherDB(context);
    	}
    	return vWeatherDB;
    }
    
    /**
     * 将Province实例存储到数据库
     */
    public void saveProvince(Province province)
    {
    	if(province !=null)
    	{
    		ContentValues values =new ContentValues();
    		values.put("province_name", province.getProvinceName());
    		values.put("province_code", province.getProvinceCode());
    		db.insert("Province", null, values);
    	}
    }
    
    /**
     * 从数据库读取全国所有省份的信息
     */
    public List<Province> loadProvinces()
    {
    	List<Province> list = new ArrayList<Province>();
    	Cursor cursor =db.query("province", null, null, null, null, null,null);
		if(cursor.moveToFirst())
		{
			do
			{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("provinxe_name")));
			    province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			    list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor !=null)
		{
			cursor.close();
		}
    	return list;  	
    }
    /**
     * 将City实例存储到数据库。
     */
    
    
    public void saveCity(City city)
    {
    	if(city !=null)
    	{
    		ContentValues values = new ContentValues();
    		values.put("city_name", city.getCityName());
    		values.put("city_code",city.getCityCode());
    		values.put("province_id", city.getProvinceId());
    		db.insert("City", null, values);
    	}
    }
    
    /**
     * 从数据库中读取某一个省下的所有城市信息
     */
    public List<City> loadCities(int provinceId)
    {
    	List<City> list = new ArrayList<City>();
    	Cursor cursor = db.query("City", null, "province_id = ?", 
    			new String[] {String.valueOf(provinceId)}, null, null, null);
    	if(cursor.moveToFirst())
    	{
    		do
    		{
    			City city =new City();
    			city.setId(cursor.getInt(cursor.getColumnIndex("id")));
    			city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
    			city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
    			city.setProvinceId(provinceId);
    			list.add(city);
    		}while (cursor.moveToNext());
    	}
    	if(cursor != null)
    	{
    		cursor.close();
    	}
		return list; 	
    }
    /**
     * 将County实例存储到数据库中
     */
    
    public void saveCounty (County county)
    {
    	if(county != null)
    	{
    		ContentValues values = new ContentValues();
    		values.put("county_name", county.getConutyName());
    		values.put("county_code", county.getCountyCode());
    		values.put("city_id", county.getCityId());
    		db.insert("County", null, values);
    	}
    }
    
    /**
     * 从数据库读取某城市下所有城镇的信息
     */
    
    
    public List<County> loadCounties(int cityId)
    {
    	List<County> list =new ArrayList<County>();
    	Cursor cursor = db.query("County", null, "city_id = ?", 
    			new String[]{String.valueOf(cityId)}, null, null, null);
    	if(cursor.moveToFirst())
    	{
    		do
    		{
    			County county =new County();
    			county.setId(cursor.getInt(cursor.getColumnIndex("id")));
    			county.setConutyName(cursor.getString(cursor.getColumnIndex("county_name")));
    			county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
    			county.setCityId(cityId);
    			list.add(county);
    		}while(cursor.moveToNext());
    	}
    	if(cursor != null)
    	{
    		cursor.close();
    	}
    	return list;
    }
}
