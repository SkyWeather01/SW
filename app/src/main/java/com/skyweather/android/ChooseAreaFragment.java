package com.skyweather.android;

// import android.content.Intent;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.skyweather.android.databinding.ChooseAreaBinding;
import com.skyweather.android.db.City;
import com.skyweather.android.db.County;
import com.skyweather.android.db.Province;
import com.skyweather.android.util.httpUtil;
import com.skyweather.android.util.Utility;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {
    // public static final String PROVINCE = "province";
    // public static final String CITY = "city";
    // public static final String COUNTY = "county";
    // ChooseAreaBinding binding;
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;

    // private int currentLevel=LEVEL_PROVINCE;
    private ProgressDialog progressDialog;

    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;
    ChooseAreaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=ChooseAreaBinding.inflate(inflater,container,false);
        adapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,dataList);
        binding.listView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        binding.listView.setOnItemClickListener((adapter, view, position, id)->{
            if (currentLevel==LEVEL_PROVINCE){
                selectedProvince=provinceList.get(position);
                queryCities();
            }else if (currentLevel==LEVEL_CITY){
                selectedCity=cityList.get(position);
                queryCounties();
            }
            // else if( currentLevel==LEVEL_COUNTY){
            //     Intent intent=new Intent(getActivity(),WeatherActivity.class);
            //     County county=countyList.get(position);
            //     intent.putExtra("weather_id",county.getWeatherId());
            //     intent.putExtra("current_province",selectedProvince.getProvinceName());
            //     intent.putExtra("current_city",selectedCity.getCityName());
            //     intent.putExtra("current_county",county.getCountyName());
            //     startActivity(intent);
            // }
        });
        binding.backButton.setOnClickListener(v->{
            if (currentLevel==LEVEL_CITY){
                queryProvinces();
            }else if (currentLevel==LEVEL_COUNTY){
                queryCities();
            }
        });
        queryProvinces();
    }


    private void queryProvinces() {

        binding.titleText.setText("中国(China)");

        binding.backButton.setVisibility(View.GONE);

        provinceList = LitePal.findAll(Province.class);

        if (provinceList.size()>0){
            dataList.clear();

            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
                // province.save();
            }
            adapter.notifyDataSetChanged();
            binding.listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;


        }else {
            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }


    private void queryFromServer(String address, String type) {
        showProgressDialog();
        httpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(()->{
                    Toast.makeText(getContext(),"获取数据失败！", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseText=response.body().string();
                boolean result=false;
                if ("province".equals(type)){
                    result= Utility.handleProvinceResp(responseText);
                }else if("city".equals(type)){
                    result=Utility.handleCityResp(responseText,selectedProvince.getProvinceCode());
                }else if ("county".equals(type)){
                    result=Utility.handleCountyResp(responseText, selectedCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(()->{
                        if ("province".equals(type)) {
                            queryProvinces();
                        }else if ("ciyt".equals(type)){
                            queryCities();
                        }else if ("county".equals(type)){
                            queryCounties();
                        }
                    });
                }
            }
        });
    }


    private void showProgressDialog() {
        if (progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }


    private void queryCities(){
        binding.titleText.setText(selectedProvince.getProvinceName());

        binding.backButton.setVisibility(View.VISIBLE);

        cityList = LitePal.where("provinceId=?",String.valueOf(selectedProvince.getId()))
        .find(City.class);

        if (cityList.size()>0){
            dataList.clear();

            for (City city : cityList) {
                dataList.add(city.getCityName());
                // city.save();
            }
            adapter.notifyDataSetChanged();
            binding.listView.setSelection(0);
            currentLevel=LEVEL_CITY;


        }else {
            int provinceCode=selectedProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+selectedProvince.getProvinceCode();
            queryFromServer(address, "city");
        }
    }
    //_____------------------------------------------

    private void queryCounties() {

        binding.titleText.setText(selectedCity.getCityName());

        binding.backButton.setVisibility(View.VISIBLE);

        countyList = LitePal.where("cityId=?",String.valueOf(selectedCity.getId()))
                .find(County.class);

        if (countyList.size()>0){
            dataList.clear();

            for (County county : countyList) {
                dataList.add(county.getCountyName());
                // county.save();
            }
            adapter.notifyDataSetChanged();
            binding.listView.setSelection(0);
            currentLevel=LEVEL_COUNTY;


        }else {
            int provinceCode=selectedProvince.getProvinceCode();
            int cityCode=selectedCity.getCityCode();
            String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address, "county");
        }


    }

    

    

    

}













































































