package com.vweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.vweather.app.R;
import com.vweather.app.db.VWeatherDB;
import com.vweather.app.model.City;
import com.vweather.app.model.County;
import com.vweather.app.model.Province;
import com.vweather.app.util.HttpCallbackListener;
import com.vweather.app.util.HttpUtil;
import com.vweather.app.util.Utility;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity 
{
	public static final int level_province= 0;
	public static final int level_city = 1;
	public static final int level_county = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private com.vweather.app.db.VWeatherDB vWeatherDB;
    private List<String> dataList = new ArrayList<String>();
    
    /**
     * ʡ�б�
     * 
     */
    private List<Province> provinceList;
    /**
     * ���б�
     */
    private List<City> cityList;
    
    /**
     * ���б�
     */
    private List<County> countyList;
    /**
     * ѡ�е�ʡ��
     */
    private Province selectedProvince;
    /**
     * ѡ�еĳ���
     */
    private City selectedCity;
    /**
     * ��ǰѡ�еļ���
     * 
     */
    private int currentLevel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.choose_area);//����Ŀ¼λ�ò���
    	listView = (ListView) findViewById(R.id.list_view);
    	titleText = (TextView) findViewById(R.id.title_text);
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
    			dataList);
    	listView.setAdapter(adapter);
    	vWeatherDB = VWeatherDB.getInstance(this);
    	listView.setOnItemClickListener(new OnItemClickListener()
    	{
    		@Override
    		public void onItemClick(AdapterView<?>arg0, View view,int index,
    				long arg3)
    		{
    			if(currentLevel == level_province)
    			{
    				selectedProvince = provinceList.get(index);
    				queryCities();   				
    			}else
    			{
    				selectedCity = cityList.get(index);
    				queryCounties();
    			}
    		}

    	});
    	queryProvinces();  //����ʡ������
    }
    /**
     * ��ѯȫ�����е�ʡ�����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
     */
    private void queryProvinces()
    {
    	provinceList = vWeatherDB.loadProvinces();
    	if(provinceList.size()>0)
    	{
    		dataList.clear();
    		for(Province province : provinceList)
    		{
    			dataList.add(province.getProvinceName());   			
    		}
    		adapter.notifyDataSetChanged();
    		titleText.setText("�й�");
    		currentLevel = level_province;
    	}else
    	{
    		queryFromServer(null,"province");
    	}
    }
    /**
     * ��ѯѡ��ʡ�����еĳ��У����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
     */
    private void queryCities()
    {
    	cityList = vWeatherDB.loadCities(selectedProvince.getId());
    	if(cityList.size()>0)
    	{
    		dataList.clear();
    		for(City city : cityList)
    		{
    			dataList.add(city.getCityName());						
    		}
    		adapter.notifyDataSetChanged();
    		listView.setSelection(0);
    		titleText.setText(selectedProvince.getProvinceName());
    		currentLevel = level_city;
    	}else
    	{
    		queryFromServer(selectedProvince.getProvinceCode(),"city");
    	}
    }
    /**
     * ��ѯѡ�е��������е��أ����ȴ����ݿ��в�ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
     */
    private void queryCounties()
    {
    	countyList = vWeatherDB.loadCounties(selectedCity.getId());
    	if(countyList.size()>0)
    	{
    		dataList.clear();
    		for(County county : countyList)
    		{
    			dataList.add(county.getConutyName());
    		}
    		adapter.notifyDataSetChanged();
    		listView.setSelection(0);
    		titleText.setText(selectedCity.getCityName());
    		currentLevel = level_county;
    	}else
    	{
    		queryFromServer(selectedCity.getCityCode(),"county");
    	}
    }
    /**
     * ���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯʡ�������ݡ�
     */
    private void queryFromServer(final String code,final String type)
    {
    	String address;
    	if (!TextUtils.isEmpty(code));
    	{
    		address = "http://www.weather.com.cn/data/list3/city" + code+".xml";
    	}
    	{
    		address = "http://www.weather.com.cn/data/list3/city.xml";
    	}
    	showProgressDialog();
    	HttpUtil.sendHttpRequest(address, new HttpCallbackListener()
    	{
    		@Override
    		public void onFinish(String response)
    		{
    			boolean result = false;
    			if("province".equals(type))
    			{
    				result = Utility.handleProvincesResponse(vWeatherDB,
    						response);
    			}else if("city" .equals(type))
    			{
    				result = Utility.handleCitiesResponse(vWeatherDB, response, 
    						selectedProvince.getId());					
    			}else if("county".equals(type))
    			{
    				result = Utility.handleCountiesResponse(vWeatherDB, response, 
    						selectedCity.getId());
    			}
    			if(result)
    			{
    				//ͨ��runOnUiThread()�����ص����߳̽��洦���߼�
    				runOnUiThread(new Runnable()
    				{
    					@Override
    					public void run()
    					{
    						closeProgressDialog();
    						if("province".equals(type))
    						{
    							queryProvinces();
    						}else if("city".equals(type))
    						{
    							queryCities();
    						}else if("county".equals(type))
    						{
    							queryCounties();
    						}
    					}
    				});
    			}
    		}
    		@Override
    		public void onError(Exception e)
    		{
    			//ͨ��runOnUiThread()�����ص����߳̽��洦���߼�
    			runOnUiThread(new Runnable()
    			{
    				@Override
					public void run()
					{
    					closeProgressDialog();
    					Toast.makeText(ChooseAreaActivity.this,
                         "����ʧ��", Toast.LENGTH_SHORT).show();
					}
    			});
    		}
    	});
    }
    /**
     * ��ʾ���ȶԻ���
     */
    private void showProgressDialog()
    {
    	if(progressDialog ==null)
    	{
    		progressDialog = new ProgressDialog(this);
    		progressDialog.setMessage("���ڼ���...");
    		progressDialog.setCanceledOnTouchOutside(false);
    	}
    	progressDialog.show();
    }
    /**
     * �رս��ȶԻ���
     */
    private void closeProgressDialog()
    {
    	if(progressDialog !=null)
    	{
    		progressDialog.dismiss();
    	}
    }
    /**
     * ����Back���������ݵ�ǰ�ļ������жϣ���ʱӦ�÷����С�ʡ�б�����ֱ���˳���
     */
    @Override
    public void onBackPressed()
    {
    	if(currentLevel == level_county)
    	{
    		queryCities();
    	}else if(currentLevel == level_city)
    	{
    		queryProvinces();
    	}else
    	{
    		finish(); 
    	}
    }
}
