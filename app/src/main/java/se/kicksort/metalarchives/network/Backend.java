package se.kicksort.metalarchives.network;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Backend {
    private final static String baseUrl = "http://192.168.1.62:3001/";

    public static String getBaseUrl() {
        return baseUrl;
    }

    private static String getLocalIpAddress() throws SocketException, UnknownHostException {
        InetAddress result = null;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            Enumeration<InetAddress> addresses = interfaces.nextElement().getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (!address.isLoopbackAddress()) {
                    if (address.isSiteLocalAddress()) {
                        return address.getHostAddress();
                    } else if (result == null) {
                        result = address;
                    }
                }
            }
        }

        return (result != null ? result : InetAddress.getLocalHost()).getHostAddress();
    }
}
