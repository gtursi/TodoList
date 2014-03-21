package com.paad.todolist;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class ToDoList extends Activity {

	static final private int ADD_NEW_TODO = Menu.FIRST;
	static final private int REMOVE_TODO = Menu.FIRST + 1;

	// Create the array list of to do items
	private ArrayList<String> todoItems;
	// Create the array adapter to bind the array to the listview
	private ArrayAdapter<String> aa;
	private EditText myEditText;
	private boolean addingNew = false;
	private ListView myListView;

	// Called at the start of the full lifetime.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Initialize activity.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// Get references to UI widgets
		myListView = (ListView) findViewById(R.id.myListView);
		myEditText = (EditText) findViewById(R.id.myEditText);

		// Create the array list of to do items
		todoItems = new ArrayList<String>();
		// Create the array adapter to bind the array to the listview
		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, todoItems);
		// Bind the array adapter to the listview.
		myListView.setAdapter(aa);

		myEditText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN)
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_ENTER:
					case KeyEvent.FLAG_EDITOR_ACTION:
						todoItems.add(0, myEditText.getText().toString());
						myEditText.setText("");
						aa.notifyDataSetChanged();
						cancelAdd();
						return true;
					default:
						break;
					}
				return false;
			}
		});
		hookupButtons();
		registerForContextMenu(myListView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemAdd = menu.add(0, ADD_NEW_TODO, Menu.NONE,
				R.string.add_new);
		MenuItem itemRem = menu.add(0, REMOVE_TODO, Menu.NONE, R.string.remove);
		// Assign icons
		itemAdd.setIcon(R.drawable.add_new_item);
		itemRem.setIcon(R.drawable.remove_item);
		// Allocate shortcuts to each of them.
		itemAdd.setShortcut('0', 'a');
		itemRem.setShortcut('1', 'r');
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Selected To Do Item");
		menu.add(0, REMOVE_TODO, Menu.NONE, R.string.remove);
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		int idx = myListView.getSelectedItemPosition();
		String removeTitle = getString(addingNew ? R.string.cancel
				: R.string.remove);
		MenuItem removeItem = menu.findItem(REMOVE_TODO);
		removeItem.setTitle(removeTitle);
		removeItem.setVisible(addingNew || idx > -1);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		myEditText.setVisibility(View.GONE);
		int index = myListView.getSelectedItemPosition();
		switch (item.getItemId()) {
		case (REMOVE_TODO): {
			if (addingNew) {
				cancelAdd();
			} else {
				removeItem(index);
			}
			return true;
		}
		case (ADD_NEW_TODO): {
			addNewItem();
			return true;
		}
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

	private void cancelAdd() {
		addingNew = false;
		myEditText.setVisibility(View.GONE);
	}

	private void addNewItem() {
		addingNew = true;
		myEditText.setVisibility(View.VISIBLE);
		myEditText.requestFocus();
	}

	private void removeItem(int _index) {
		addingNew = false;
		todoItems.remove(_index);
		aa.notifyDataSetChanged();
	}

	private void hookupButtons() {
		hookupAddNewButton();
		hookupRemoveButton();
	}

	private void hookupAddNewButton() {
		ImageButton addNewButton = (ImageButton) findViewById(R.id.addNewButton);

		addNewButton.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				addNewItem();
			}
		});
	}

	private void hookupRemoveButton() {
		ImageButton removeButton = (ImageButton) findViewById(R.id.removeButton);

		removeButton.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				removeItem(myListView.getSelectedItemPosition());
			}
		});
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
	}

	// Called at the end of the full lifetime.
	@Override
	public void onDestroy() {
		// Clean up any resources including ending threads,
		// closing database connections etc.
		super.onDestroy();
	}

}
