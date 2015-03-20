package com.syncworks.slight;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.syncworks.scriptdata.ScriptExecuteService;
import com.syncworks.scriptdata.ScriptXmlParser;

import java.io.IOException;
import java.io.InputStream;


public class TestActivity extends ActionBarActivity {
    private final static String TAG = TestActivity.class.getSimpleName();

    // LED 실행 서비스
    ScriptExecuteService scriptExecuteService;

    private final ServiceConnection scriptExecuteServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            scriptExecuteService = ()
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onTest(View v) {
        switch (v.getId()) {
            case R.id.btn_test:
                parseXml();
                break;
        }
    }

    private void parseXml() {
        AssetManager assetManager = getBaseContext().getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
            for (int i=0;i<files.length;i++) {
                if (files[i].contains(".xml")) {
                    InputStream is = assetManager.open(files[i]);
                    Log.d(TAG,"File Name is "+files[i] );
                    ScriptXmlParser.parse(is);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            InputStream is = assetManager.open("test.xml");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(is,"UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if(startTag.equals("item")) {

                        }

                        //방법 1. 속성값일때 얻기
                        p.setName(parser.getAttributeValue(0));
                        p.setAge(parser.getAttributeValue(1));
                        p.setAddress(parser.getAttributeValue(2));

                        //방법 2. 태그값일때 얻기
                        if(search != null) {
                            if(startTag.equals("title")) {
                                String temp = parser.nextText();
                                temp=temp.replace("<b>", "");
                                temp=temp.replace("</b>", "");
                                search.setTitle(temp);
                            }
                            if(startTag.equals("link")) { search.setLink(parser.nextText()); }
                            if(startTag.equals("description")) { search.setDescription(parser.nextText()); }
                            if(startTag.equals("telephone")) { search.setTelephone(parser.nextText()); }
                            if(startTag.equals("address")) { search.setAddress(parser.nextText()); }
                            if(startTag.equals("mapx")) { search.setMapx(parser.nextText()); }
                            if(startTag.equals("mapy")) { search.setMapy(parser.nextText()); }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        break;
                } // end switch
                eventType = parser.next();
            } // end while
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }*/


    }
}
