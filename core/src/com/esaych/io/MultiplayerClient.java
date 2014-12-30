package com.esaych.io;

import java.io.IOException;

import com.esaych.hoops.screen.GameScreen;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;

public class MultiplayerClient extends MP {
	
	private static boolean enabled = false;
	
	private static Client client;

	public static void connect(String ip, final int portNumber, GameScreen gs) {
		gameScreen = gs;
		
	    client = new Client();
	    listener = new NetworkListener();
		
		String address = Networking.getLocalAddress();
		final String hostName = address.substring(0, address.lastIndexOf('.', address.lastIndexOf('.')-1)+1) + ip;
		
		gs.print("Client Connecting to " + hostName + ":" + portNumber + "...");
		
	    try {
	    	client.start();
	    	client.connect(5000, hostName, portNumber);
	    	client.addListener(listener);
//	    	client.getKryo().register(String.class);
	    	enabled = true;
	    } catch (IOException exception) {
	    	exception.printStackTrace();
	    	enabled = false;
	    }
	}
	
	public static void send(String data) {
		if (!enabled)
			return;
		listener.send(data);
		if (data.equals("END_CONNECT")) {
			endGameConnection();
			client.stop();
		}
	}
	
	public static String read() {
		if (!enabled)
			return "DISABLED";
		String line = listener.read();
		if (line == null)
			return "null";
		if (line.startsWith("GAME ")) {
			gameType = line.split(" ")[1];
			startGame();
		}
		return "null";
	}
	
	public static void connected(Connection con) {}

	public static String getGameType() {
		return gameType;
	}

	public static boolean isEnabled() {
		return enabled;
	}
}

/*
public class MultiplayerClient {

	private static ArrayList<String> input = new ArrayList<String>();
	private static ArrayList<String> output = new ArrayList<String>();
	
	private static GameScreen gs;
	
	private static boolean enabled = false;
	
	private static String gameType;

	public static void connect(String ip, final int portNumber, GameScreen gameScreen) {
		gs = gameScreen;
		
		enabled = true;
		
		String address = getLocalAddress();
		final String hostName = address.substring(0, address.lastIndexOf('.', address.lastIndexOf('.')-1)+1) + ip;
		
		gs.print("Client Connecting to " + hostName + ":" + portNumber + "...");
		
		new Thread(new Runnable() {
		    public void run() {
				try (
						Socket echoSocket = new Socket(hostName, portNumber);
						PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
						) {
					while (true) {
						if (input.size() > 0) { //Have an input to ask server
							String message = input.remove(0);
							gs.print("Mess: " + message);
							out.println(message);
		    				if (message.equals("END_CONNECT"))
		    					break;
		    				if (message.equals("PING")) { //Slow or lost connection
		    					String r = in.readLine();
		    					if (r == null || !r.equals("PONG")) {
		    						out.println("END_CONNECT");
		    						((MultiplayerWorld) gs.getWorld()).endConnection();
		    					}
		    				}
						}
						if (in.ready()) {
							String response = in.readLine();
							gs.print("Resp: " + response);
							if (response != null) {
								if (response.startsWith("GAME ")) {
		    						gameType = response.split(" ")[1];
		    						gs.setState(GameState.PLAYING);
		    						gs.resetWorld(false);
								} else if (response.equals("PING")) {
									out.println("PONG");
								} else
									output.add(response);
							}
						}
					}
					enabled = false;
				} catch (UnknownHostException e) {
					System.err.println("Don't know about host " + hostName);
					enabled = false;
				} catch (IOException e) {
					System.err.println("Couldn't get I/O for the connection to " + hostName);
		    		if (gs.getWorld() instanceof MultiplayerWorld)
		    			((MultiplayerWorld) gs.getWorld()).endConnection();
					enabled = false;
				}
		    }
		}).start();
		try {
			send("ID '" + Inet4Address.getLocalHost().getHostAddress() + "'");
			System.out.println("CLIENT STATUS: " + enabled);
		} catch (UnknownHostException e) {
		}
	}
	
	private static String getLocalAddress() {
		try {
			String address = Inet4Address.getLocalHost().getHostAddress();
			if (!isLocalIP(address)) {
				try {
					Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
					outerloop:
						while (ni.hasMoreElements()) {
							NetworkInterface netInt = ni.nextElement();
							Enumeration <InetAddress> addresses = netInt.getInetAddresses();
							while (addresses.hasMoreElements()) {
								String finalAddress = addresses.nextElement().getHostAddress();
								if (isLocalIP(finalAddress)) {
									address = finalAddress;
									break outerloop;
								}
							}
						}
					return address;
				} catch (SocketException e) {
					return "ERROR";
				}
			}
			System.out.println(address);
			return address;
		} catch (UnknownHostException e) {
			return "ERROR";
		}
	}
	
	private static boolean isLocalIP(String ip) {
		if (ip.startsWith("192.168."))
			return true;
		if (ip.startsWith("172.")) {
			int num2 = Integer.parseInt(ip.substring(4, ip.indexOf('.', ip.indexOf('.')+1)));
			if (num2 >= 16 && num2 <= 31)
				return true;
		}
		if (ip.startsWith("10."))
			return true;
		return false;
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static String getGameType() {
		return gameType;
	}
	
//	public static String processInput(String input) {
//		if (input.startsWith("WELCOME")) {
//			return "GAMETYPE";
//		}
//		return "null response";
//	}
	
	public static void send(String data) {
		if (enabled)
			input.add(data);
	}
	
	public static void respond(String data) {
		if (enabled)
			input.add(data);
	}

	public static String read() {
		if (!enabled)
			return "DISABLED";
		if (output.size() > 0)
			return output.remove(0);
		else return "null";
	}
}
*/