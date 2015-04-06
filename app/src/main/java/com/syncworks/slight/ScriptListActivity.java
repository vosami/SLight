package com.syncworks.slight;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.syncworks.define.Define;
import com.syncworks.dragndroplist.DragSortController;
import com.syncworks.dragndroplist.DragSortListView;
import com.syncworks.scriptdata.DialogAddScriptData;
import com.syncworks.scriptdata.ScriptAdapter;
import com.syncworks.scriptdata.ScriptData;
import com.syncworks.scriptdata.ScriptDataList;


public class ScriptListActivity extends ActionBarActivity {
    private final static String TAG = ScriptListActivity.class.getSimpleName();

	private DragSortListView lvScript;
	private DragSortController lvController;

	private ScriptDataList scriptDataList;
	private ScriptAdapter scriptAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_script_list);
		// 테스트용도로 리스트 생성
		testMakeList();

		lvScript = (DragSortListView) findViewById(R.id.lv_script_list);
		lvController = buildController(lvScript);
		lvScript.setFloatViewManager(lvController);
		lvScript.setOnTouchListener(lvController);
		lvScript.setDragEnabled(true);

		scriptAdapter = new ScriptAdapter(this,0,scriptDataList);

		lvScript.setAdapter(scriptAdapter);

		lvScript.setDropListener(onDrop);
		lvScript.setRemoveListener(onRemove);

	}

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			if (from != to) {
				ScriptData item = scriptAdapter.getItem(from);
				scriptAdapter.remove(item);
				scriptAdapter.insert(item, to);
			}
		}
	};

	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			scriptAdapter.remove(scriptAdapter.getItem(which));
		}
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_script_list, menu);
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

	// 테스트 용도
	private void testMakeList() {
		scriptDataList = new ScriptDataList(0);
		scriptDataList.add(new ScriptData(Define.OP_START,0));
		scriptDataList.add(new ScriptData(60,10));
        scriptDataList.add(new ScriptData(70,20));
        scriptDataList.add(new ScriptData(80,30));
        scriptDataList.add(new ScriptData(90,40));
        scriptDataList.add(new ScriptData(100,50));
        scriptDataList.add(new ScriptData(110,60));
        scriptDataList.add(new ScriptData(120,70));
        scriptDataList.add(new ScriptData(130,10));
        scriptDataList.add(new ScriptData(140,20));
        scriptDataList.add(new ScriptData(150,30));
        scriptDataList.add(new ScriptData(160,40));
        scriptDataList.add(new ScriptData(170,255));
		scriptDataList.add(new ScriptData(Define.OP_END,0));
	}

    public void scriptClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_script:
                DialogAddScriptData mDialog = new DialogAddScriptData(this);
                mDialog.show();
//                scriptDataList.add(new ScriptData(100,100));
//                scriptAdapter.notifyDataSetChanged();
//                lvScript.invalidate();
                break;
            case R.id.btn_test_script:
                int size = scriptDataList.size();
                int val, dur;
                for (int i=0;i<size;i++) {
                    val = scriptDataList.get(i).getVal();
                    dur = scriptDataList.get(i).getDuration();
                    Log.d(TAG,"The "+i+"th Data:"+val+","+ dur);
                }
                break;
        }
    }

	/**
	 * Called in onCreateView. Override this to provide a custom
	 * DragSortController.
	 */
	public DragSortController buildController(DragSortListView dslv) {
		// defaults are
		//   dragStartMode = onDown
		//   removeMode = flingRight
		DragSortController controller = new DragSortController(dslv);
		controller.setDragHandleId(R.id.drag_handle);
		controller.setRemoveEnabled(true);
		controller.setSortEnabled(true);
		controller.setDragInitMode(DragSortController.ON_LONG_PRESS);
		controller.setRemoveMode(DragSortController.FLING_REMOVE);
		return controller;
	}
}
