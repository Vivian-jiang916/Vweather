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
 *  VWeatherDB��һ�������࣬�����Ĺ��췽��˽�л�
 *  ��getInstance()��������ȡ VWeatherDB��ʵ����
 * ������֤��ȫ�ַ�Χ��ֻ��һ��VWeatherDB��ʵ��
 * ������VWeatherDB���ṩ�����鷽����saveProvince(),loadProvince(),saveCity(),
 * loadCities(),saveCounty(),loadCounties()�ֱ����ڶ�ȡ�ʹ洢ʡ��ʱ���ص����ݡ�
 *
 */
public class VWeatherDB
{
	/**
	 * Database name
	 */
    public static final String db_name ="v_weather";
    
    /**
     * Database version(�汾)
     */
    public static final int version = 1;
    private static VWeatherDB vWeatherDB;
    private SQLiteDatabase db;
    
    /**
     * �����췽��˽�л�
     */
    private VWeatherDB (Context context)
    {
    	VWeatherOpenHelper dbHelper = new VWeatherOpenHelper(context,db_name,
    			null,version);
    	db = dbHelper.getWritableDatabase();
    	
    }
    
    /**
     * ��ȡVWeatherDBʵ��
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
     * ��Provinceʵ���洢�����ݿ�
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
     * �����ݿ��ȡȫ������ʡ�ݵ���Ϣ
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
     * ��Cityʵ���洢�����ݿ⡣
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
     * �����ݿ��ж�ȡĳһ��ʡ�µ����г�����Ϣ
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
     * ��Countyʵ���洢�����ݿ���
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
     * �����ݿ��ȡĳ���������г������Ϣ
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
