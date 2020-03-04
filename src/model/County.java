package model;

public class County
{
   private int id;
   private String conutyName;
   private String countyCode;
   private int cityId;
	public int getId() 
	{
		return id;
	}
	public void setId(int id) 
	{
		this.id = id;
	}
	public String getConutyName() 
	{
		return conutyName;
	}
	public void setConutyName(String conutyName) 
	{
		this.conutyName = conutyName;
	}
	public String getCountyCode() 
	{
		return countyCode;
	}
	public void setCountyCode(String countyCode) 
	{
		this.countyCode = countyCode;
	}
	public int getCityId() 
	{
		return cityId;
	}
	public void setCityId(int cityId) 
	{
		this.cityId = cityId;
	}
}
