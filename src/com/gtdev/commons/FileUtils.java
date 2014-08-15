package com.gtdev.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

public abstract class FileUtils {

	public static ArrayList<String> getData(String filename,
			ContextWrapper context) {
		ArrayList<String> tareas = new ArrayList<String>();
		String[] archivos = context.fileList();
		if (exists(archivos, filename)) {
			try {
				InputStreamReader archivo = new InputStreamReader(
						context.openFileInput(filename));
				BufferedReader br = new BufferedReader(archivo);
				String linea = br.readLine();
				while (linea != null) {
					tareas.add(linea);
					linea = br.readLine();
				}
				br.close();
				archivo.close();
			} catch (IOException e) {
				Log.e(Context.SEARCH_SERVICE, "Error al recuperar notas", e);
			}
		}
		return tareas;
	}

	public static boolean exists(String[] files, String filename) {
		for (int f = 0; f < files.length; f++)
			if (filename.equals(files[f]))
				return true;
		return false;
	}

}
