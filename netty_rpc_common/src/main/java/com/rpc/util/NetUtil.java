package com.rpc.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @Title：
 * @Author：wangchenggong
 * @Date 2021/4/5 11:05
 * @Description
 * @Version
 */
public class NetUtil {


    /**
     * 获取本机IP地址
     * @return
     */
    public static List<String> getLocalIP() {
        List<String> hosts = new ArrayList<>();
        try{
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()){
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                String displayName = netInterface.getDisplayName().toLowerCase();
                String name = netInterface.getName();
                boolean isEth = (name.startsWith("eth") || name.startsWith("wlan")) && !displayName.contains("virtual") && !name.contains("docker");
                if(isEth) {
                    Enumeration addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress ip = (InetAddress) addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            String localHost = ip.getHostAddress();
                            if (!localHost.trim().startsWith("127")) {
                                hosts.add(localHost);
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return hosts;
    }
}
