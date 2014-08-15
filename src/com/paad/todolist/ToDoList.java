package com.paad.todolist;

import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gtdev.commons.FileUtils;
import com.gtdev.commons.KeyUtils;

public class ToDoList extends Activity {

	static final private int DELETE_ALL = Menu.FIRST;
	static final private int REMOVE_TODO = Menu.FIRST;

	private ArrayList<String> todoItems;
	private ArrayAdapter<String> aa;
	private EditText editText;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		listView = (ListView) findViewById(R.id.myListView);
		editText = (EditText) findViewById(R.id.myEditText);

		todoItems = FileUtils.getData(getString(R.string.data_file), this);
		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, todoItems);
		listView.setAdapter(aa);

		editText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyUtils.okPressed(event)) {
					saveItem();
					return true;
				}
				return false;
			}
		});

		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				}
			}
		});

		registerForContextMenu(listView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem itemAdd = menu.add(0, DELETE_ALL, Menu.NONE,
				R.string.delete_all);
		itemAdd.setIcon(R.drawable.add_new_item);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.selected_to_do_item);
		menu.add(Menu.NONE, REMOVE_TODO, Menu.FIRST, R.string.remove);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case (DELETE_ALL):
			deleteAll();
			return true;
		}
		return false;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		switch (item.getItemId()) {
		case (REMOVE_TODO): {
			AdapterView.AdapterContextMenuInfo menuInfo;
			menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			int index = menuInfo.position;
			removeItem(index);
			return true;
		}
		}
		return false;
	}

	private void deleteAll() {
		try {
			todoItems.clear();
		} catch (IndexOutOfBoundsException exc) {
			Log.e(DISPLAY_SERVICE, "Error al eliminar un elemento", exc);
		}
		aa.notifyDataSetChanged();

	}

	private void removeItem(int _index) {
		try {
			todoItems.remove(_index);
		} catch (IndexOutOfBoundsException exc) {
			Log.e(DISPLAY_SERVICE, "Error al eliminar un elemento", exc);
		}
		aa.notifyDataSetChanged();
	}

	// Called after onCreate has finished, use to restore UI state
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
	}

	// Called before subsequent visible lifetimes
	// for an activity process.
	@Override
	public void onRestart() {
		super.onRestart();
		// Load changes knowing that the activity has already
		// been visible within this process.
	}

	// Called at the start of the visible lifetime.
	@Override
	public void onStart() {
		super.onStart();
		// Apply any required UI change now that the Activity is visible.
	}

	// Called at the start of the active lifetime.
	@Override
	public void onResume() {
		super.onResume();
		// Resume any paused UI updates, threads, or processes required
		// by the activity but suspended when it was inactive.
	}

	// Called to save UI state changes at the
	// end of the active lifecycle.
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
		super.onSaveInstanceState(savedInstanceState);
	}

	// Called at the end of the active lifetime.
	@Override
	public void onPause() {
		// Suspend UI updates, threads, or CPU intensive processes
		// that don’t need to be updated when the Activity isn’t
		// the active foreground activity.
		super.onPause();
	}

	// Called at the end of the visible lifetime.
	@Override
	public void onStop() {
		// Suspend remaining UI updates, threads, or processing
		// that aren’t required when the Activity isn’t visible.
		// Persist all edits or state changes
		// as after this call the process is likely to be killed.
		super.onStop();
		grabar();
	}

	// Called at the end of the full lifetime.
	@Override
	public void onDestroy() {
		// Clean up any resources including ending threads,
		// closing database connections etc.
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void saveItem() {
		String text = editText.getText().toString();
		if (text != null && text.trim().length() > 0) {
			todoItems.add(0, text);
			editText.setText("");
			aa.notifyDataSetChanged();
		}
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	public void grabar() {
		try {
			OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(
					getString(R.string.data_file), Activity.MODE_PRIVATE));
			for (int i = 0; i < aa.getCount(); i++) {
				archivo.write(aa.getItem(i));
				archivo.write(System.getProperty("line.separator"));
			}
			archivo.flush();
			archivo.close();
			Toast.makeText(this, "Los datos fueron grabados",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e(SEARCH_SERVICE, "Error al salvar datos", e);
		}
	}

}
