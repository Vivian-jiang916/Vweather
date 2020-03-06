package com.vweather.app.util;

import com.vweather.app.db.VWeatherDB;
import com.vweather.app.model.City;
import com.vweather.app.model.County;
import com.vweather.app.model.Province;

import android.text.TextUtils;


/**
 * 因为服务器在返回省市县数据都是“代号|城市”这种格式
 * 所以我们要用一个工具类解析、处理这种数据
 *
 */
public class Utility 
{
 /**
  * 解析和处理服务器返回的省级数据
  */
	
	public synchronized static boolean handleProvincesResponse(VWeatherDB
			vweatherDB, String response)
	{
		if(!TextUtils.isEmpty(response))
		{
			String[] allProvinces = response.split(",");
			if(allProvinces !=null && allProvinces.length >0)
			{
				for(String p: allProvinces)
				{
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//将解析出来的数据存储到Province()
					vweatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 解析和处理服务器返回的市级数据
	 */
	public static boolean handleCitiesResponse(VWeatherDB vweatherDB,
			String response, int provinceId)
	{
		if(!TextUtils.isEmpty(response))
		{
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length >0)
			{
				for(String c : allCities)
				{
					String[]  array =c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//将解析粗来的数据存储到City表
					vweatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 将解析和处理服务器返回的县级数据
	 */
	public static boolean handleCountiesResponse(VWeatherDB vweatherDB,String
			response,int cityId)
	{
		if(!TextUtils.isEmpty(response))
		{
			String[] allCounties = response.split(",");
			if(allCounties !=null && allCounties.length >0)
			{
				for(String c:allCounties)
				{
					String[] array = c.split("\\|");
					County county =new County();
					county.setCountyCode(array[0]);
					county.setConutyName(array[1]);
					county.setCityId(cityId);
					//将解析出来的数据存储到County表
					vweatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
}
