package com.syncworks.slight.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syncworks.endlessadapter.EndlessListView;
import com.syncworks.endlessadapter.HttpListAdapter;
import com.syncworks.endlessadapter.HttpListData;
import com.syncworks.slight.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vosami on 2015-05-06.
 */
public class FragmentHttpDataList extends Fragment implements EndlessListView.EndlessListener{

    EndlessListView endlessListView = null;
    List<HttpListData> httpListData = null;
    HttpListAdapter httpListAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_script_data3, container, false);

        endlessListView = (EndlessListView) v.findViewById(R.id.el_lv);
        httpListData = new ArrayList<>();
//        httpListData.add(new HttpListData());
//        httpListData.add(new HttpListData());
//        httpListAdapter = new HttpListAdapter(getActivity().getBaseContext(),0, httpListData);
//        endlessListView.setAdapter(httpListAdapter);

        endlessListView.setListener(this);

        new HttpPostTask().execute(Integer.toString(0));

        return v;
    }

    @Override
    public void loadData() {

    }

    private class HttpPostTask extends AsyncTask<String, Void, JSONArray> {


        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jsonArray = null;
            HttpResponse response = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = null;
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            httpPost = new HttpPost("http://dspblog.co.kr/slight/get_script_list.php");
            //기본 파라미터를 만들고
            HttpParams httpParams = new BasicHttpParams();
            //서버와 연결되고 응답을 받는 시간을 정해줍니다.
            HttpConnectionParams.setConnectionTimeout(httpParams, 2500);
            //서버의 소켓 연결 시간을 정해줍니다.
            HttpConnectionParams.setSoTimeout(httpParams, 2500);
            // 파라미터를 설정해 줍니다.
            httpPost.setParams(httpParams);

            nameValuePairs.add(new BasicNameValuePair("count", params[0]));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();
                JSONTokener jsonTokener = new JSONTokener(json);
                jsonArray = new JSONArray(jsonTokener);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
           /* BufferedReader reader = null;
            JSONArray jsonArray = null;

            try {
                reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();
                JSONTokener jsonTokener = new JSONTokener(json);
                jsonArray = new JSONArray(jsonTokener);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }*/

            if (jsonArray != null) {
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject tempJsonObj = null;
                    HttpListData tempData = new HttpListData();

                    try {
                        tempJsonObj = jsonArray.getJSONObject(i);
                        tempData.setWriteCount(tempJsonObj.getInt("wr_count"));
                        tempData.setWriter(tempJsonObj.getString("wr_name"));
                        tempData.setPatternName(tempJsonObj.getString("patternname"));
                        tempData.setPatternType(tempJsonObj.getInt("patterntype"));
                        tempData.setDescription(tempJsonObj.getString("description"));
                        Date tempDate = null;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            tempDate =dateFormat.parse(tempJsonObj.getString("wr_datetime"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tempData.setDate(tempDate);
                        httpListData.add(tempData);
                        httpListAdapter = new HttpListAdapter(getActivity().getBaseContext(),0, httpListData);
                        endlessListView.setAdapter(httpListAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
    }
}
