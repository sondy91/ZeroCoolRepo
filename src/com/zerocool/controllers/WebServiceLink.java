package com.zerocool.controllers;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.zerocool.entities.ParticipantView;
import com.zerocool.gui.Observer;

public class WebServiceLink {

	public static final String SERVICE_URL = "http://localhost:8888/";

	private Gson g;
	
	public WebServiceLink() {
		g = new Gson();
	}
	
	public void postToServer(ArrayList<ParticipantView> list) {
		System.out.println(g.toJson(list));
		try {
			URL site = new URL(SERVICE_URL);
			HttpURLConnection conn = (HttpURLConnection) site.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			String json = "data=" + g.toJson(list);
			out.writeBytes(json);
			out.flush();
			out.close();
			System.out.println("Sent to server");
			new InputStreamReader(conn.getInputStream());
		} catch (Exception e) {

		}
	}

}
