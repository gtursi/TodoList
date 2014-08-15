package com.gtdev.commons;

import android.view.KeyEvent;

public abstract class KeyUtils {

	public static boolean okPressed(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.FLAG_EDITOR_ACTION:
			case KeyEvent.KEYCODE_TAB:
				return true;
			}
		}
		return false;
	}

}
