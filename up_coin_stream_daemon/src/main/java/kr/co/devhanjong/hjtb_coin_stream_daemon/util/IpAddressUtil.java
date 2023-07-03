package kr.co.devhanjong.hjtb_coin_stream_daemon.util;

import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@Service
public class IpAddressUtil {


    public static String getHostIp(){
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();

                        if (!inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress()
                                && !inetAddress.isMulticastAddress()) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }

            return "127.0.0.1";
        } catch (SocketException e) {
            return "127.0.0.1";
        }
    }
}
