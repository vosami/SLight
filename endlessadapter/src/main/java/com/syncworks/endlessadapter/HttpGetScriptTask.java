package com.syncworks.endlessadapter;

import android.os.AsyncTask;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by vosami on 2015-05-11.
 */
public class HttpGetScriptTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        HttpResponse response = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = null;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

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
        String json = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpClient.execute(httpPost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            json = reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}
