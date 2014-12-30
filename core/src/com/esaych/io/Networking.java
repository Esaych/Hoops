package com.esaych.io;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Networking {
	public static String getCode() {
		String address = getLocalAddress();
		return address.substring(address.lastIndexOf('.', address.lastIndexOf('.')-1)+1, address.length()).replace('.', '-');
	}
	
	public static String getLocalAddress() {
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
}
