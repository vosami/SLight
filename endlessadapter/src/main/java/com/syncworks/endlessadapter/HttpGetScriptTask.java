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
        //�⺻ �Ķ���͸� �����
        HttpParams httpParams = new BasicHttpParams();
        //������ ����ǰ� ������ �޴� �ð��� �����ݴϴ�.
        HttpConnectionParams.setConnectionTimeout(httpParams, 2500);
        //������ ���� ���� �ð��� �����ݴϴ�.
        HttpConnectionParams.setSoTimeout(httpParams, 2500);
        // �Ķ���͸� ������ �ݴϴ�.
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
