package com.esaych.io;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkListener extends Listener {

	Connection con;
	private static ArrayList<String> output = new ArrayList<String>();
	
	@Override
	public void connected(Connection connection) {
		con = connection;
		MP.connected(connection);
	}
	
	@Override
	public void disconnected(Connection connection) {
		MP.disconnected(connection);
	}
	
	@Override
	public void received (Connection connection, Object object) {
        if (object instanceof String) {
        	String msg = (String)object;
        	System.out.println("Received: " + msg);
        	output.add(msg);
        }
	}
	
	public void send(String data) {
		System.out.println("Sending: " + data);
		if (con != null)
			con.sendTCP(data);
	}
	
	public String read() {
		if (output.size() > 0)
			return output.remove(0);
		else
			return null;
	}
}
