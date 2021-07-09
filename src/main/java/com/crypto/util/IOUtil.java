package com.crypto.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class IOUtil {
	
	public static void read(List<String> data, String fileName) {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"))){
			String line;
			while((line = reader.readLine()) != null) {
				data.add(line);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String read(InputStream in) {
		StringBuilder text = new StringBuilder();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"))){
			String line;
			while((line = reader.readLine()) != null) {
				text.append(line).append("\n");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text.toString();
	}

	public static void write(String webPage, long id) {
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\files\\Dev Roadmap\\crypto\\pages\\"+ String.valueOf(id)+".html")))){
			writer.write(webPage);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

}
