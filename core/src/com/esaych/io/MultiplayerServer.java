package com.esaych.io;

import java.io.IOException;

import com.esaych.hoops.screen.GameScreen;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class MultiplayerServer extends MP {
	
	private static boolean enabled = false;
	
	private static Server server;
	
	public static void start(final int portNumber, GameScreen gs, String gt) {
	    server = new Server();
	    listener = new NetworkListener();
	    
		gameType = gt;
		gameScreen = gs;
	    
	    try {
	    	server.start();
			server.bind(portNumber);
			server.addListener(listener);
//			server.getKryo().register(String.class);
			enabled = true;
		} catch (IOException e) {
			e.printStackTrace();
			enabled = false;
		}
	}
	
	public static void send(String data) {
		if (!enabled)
			return;
		listener.send(data);
		if (data.equals("END_CONNECT")) {
			endGameConnection();
			server.stop();
		}
	}
	
	public static String read() {
		if (!enabled)
			return "DISABLED";
		String line = listener.read();
		if (line == null)
			return "null";
		return line;
	}

	public static void connected(Connection con) {
		send("GAME " + gameType);
		startGame();
	}
	
	public static String getGameType() {
		return gameType;
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
}

/*
class OldServer {
	private static ArrayList<String> input = new ArrayList<String>();
	private static ArrayList<String> output = new ArrayList<String>();
	
	private static String clientID = "";
	
	private static GameScreen gs;
	
	private static String gameType;
	
	public static void start(final int portNumber, GameScreen gameScreen, String gt) {
		
		gs = gameScreen;
		gameType = gt;
		enabled = true;
		gs.print("Starting Server on " + getLocalAddress() + ":" + portNumber + " Code: " + getCode());

		new Thread(new Runnable() {
		    public void run() {
		    	try (
		    			ServerSocket serverSocket =	new ServerSocket(portNumber);
		    			Socket clientSocket = serverSocket.accept();     
		    			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);                   
		    			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    		)
		    	{
		    		while (true) {
		    			if (input.size() > 0) { //Have an input to ask client
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
		    					if (response.startsWith("ID ")) {
		    						clientID = response.split(" ")[1];
		    						System.out.println("CLIENT ID: " + clientID);
		    						send("GAME " + gameType);
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
		    	} catch (IOException e) {
		    		enabled = false;
		    		if (gs.getWorld() instanceof MultiplayerWorld)
		    			((MultiplayerWorld) gs.getWorld()).endConnection();
		    	}
		    }
		}).start();
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
	
	public static String getCode() {
		String address = getLocalAddress();
		return address.substring(address.lastIndexOf('.', address.lastIndexOf('.')-1)+1, address.length()).replace('.', '-');
	}
	
	public static String getGameType() {
		return gameType;
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
	
	/* private static String processInput(String input) {
		if (input.equals("FROZEN")) {
			gs.setState(GameState.FROZEN);
			return "ACK";
		}
		if (input.equals("PLAYING")) {
			gs.setState(GameState.PLAYING);
			return "ACK";
		}
		if (input.startsWith("ID '")) {
			clientID = input.substring(4, input.length()-1);
			System.out.println("CLIENT ID: " + clientID);
			return "GAMETYPE IS " + gameType;
		}
		return "null response";
	} */

	/*
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