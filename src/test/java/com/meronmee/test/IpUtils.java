package com.meronmee.test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class IpUtils {
	
	private static final String ANYHOST = "0.0.0.0";
	private static final String LOCALHOST = "127.0.0.1";
	private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    
	/**
	 * 本机地址
	 */
	public static String localIp;
	
	/**
	 * 本机公网地址
	 */
	public static String localPublicIp;
	
	/**
	 * 本机内网地址
	 */
	public static String localInternalIp;
	
	
    /**
     * 返回本机IP地址（优先使用外网地址，没有的话再使用内网地址）
     */
	public static String getLocalIpAddress(){		
		if(isNotBlank(localPublicIp)){
			return localPublicIp;
		}
		if(isNotBlank(localInternalIp)){
			return localInternalIp;
		}
		
		return getLocalPublicIp();
	}
	
	/**
     * 返回本机外网地址（没有的话使用内网地址）
     */
	public static String getLocalPublicIp(){
		//获取外网地址
		if(isNotBlank(localPublicIp)){
			return localPublicIp;
		}		
		localPublicIp = getFirstValidAddress("outer");
		if(isNotBlank(localPublicIp)){
			return localPublicIp;
		}
		
		//取不到外网地址，取内网地址
		if(isNotBlank(localInternalIp)){
			localPublicIp = localInternalIp;
			return localPublicIp;
		}		
		localInternalIp = getFirstValidAddress("inner");
		if(isNotBlank(localInternalIp)){
			localPublicIp = localInternalIp;
			 return localPublicIp;
		}
		
		//内外网都取不到，使用127.0.0.1		
		return "127.0.0.1";
	}
	

	/**
     * 返回本机内网地址（没有的话使用外网地址）
     */
	public static String getLocalInternalIp(){
		//获取内网地址
		if(isNotBlank(localInternalIp)){
			return localInternalIp;
		}		
		localInternalIp = getFirstValidAddress("inner");
		if(isNotBlank(localInternalIp)){
			 return localInternalIp;
		}

		//取不到内网地址，取外网地址
		if(isNotBlank(localPublicIp)){
			localInternalIp = localPublicIp;
			return localInternalIp;
		}		
		localPublicIp = getFirstValidAddress("outer");
		if(isNotBlank(localPublicIp)){
			localInternalIp = localPublicIp;
			return localInternalIp;
		}
				
		//内外网都取不到，使用127.0.0.1		
		return "127.0.0.1";
	}

	/**
	 * 获取第一个有效的地址
	 * @param flag outer:外网地址, inner:内网地址
	 */
	private static String getFirstValidAddress(String flag) {
		/*
		try {
			InetAddress localAddress = InetAddress.getLocalHost();
			if (isValidAddress(localAddress)) {
				return localAddress.getHostAddress();
			}
		} catch (Throwable e) {
			error("Failed to retriving ip address, " + e.getMessage(), e);
		}
		*/
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			if (interfaces != null) {
				while (interfaces.hasMoreElements()) {
					try {
						NetworkInterface network = interfaces.nextElement();
						Enumeration<InetAddress> addresses = network.getInetAddresses();
						if (addresses != null) {
							while (addresses.hasMoreElements()) {
								try {
									InetAddress address = addresses.nextElement();
									if (isValidAddress(address)) {
										String ip = address.getHostAddress();
										if("outer".equals(flag) && !isInternalIp(ip)){//外网地址
											return ip;
										}
										if("inner".equals(flag) && isInternalIp(ip)){//内网地址
											return ip;
										}
									}
								} catch (Throwable e) {
									error("Failed to retriving ip address, " + e.getMessage(), e);
								}
							}
						}
					} catch (Throwable e) {
						error("Failed to retriving ip address, " + e.getMessage(), e);
					}
				}
			}
		} catch (Throwable e) {
			error("Failed to retriving ip address, " + e.getMessage(), e);
		}
		error("Could not get local host "+flag+" ip address");
		return "";
	}

	
	/**
	 * 判断一个ipv4地址是不是内网地址
	 * 
	 * 原理：
	 * tcp/ip协议中，专门保留了三个IP地址区域作为私有地址，其地址范围如下：
	 * 10.0.0.0/8：10.0.0.0～10.255.255.255 
	 * 172.16.0.0/12：172.16.0.0～172.31.255.255 
	 * 192.168.0.0/16：192.168.0.0～192.168.255.255
	 * 
	 * @param ip要检测的ip地址
	 * @return true:
	 */
	public static boolean isInternalIp(String ip) {
	     byte[] addr = textToNumericFormatV4(ip);
	     
	     final byte b0 = addr[0];
	     final byte b1 = addr[1];
	     //10.x.x.x/8
	     final byte SECTION_1 = 0x0A;
	     //172.16.x.x/12
	     final byte SECTION_2 = (byte) 0xAC;
	     final byte SECTION_3 = (byte) 0x10;
	     final byte SECTION_4 = (byte) 0x1F;
	     //192.168.x.x/16
	     final byte SECTION_5 = (byte) 0xC0;
	     final byte SECTION_6 = (byte) 0xA8;
	     switch (b0) {
	         case SECTION_1:
	             return true;
	         case SECTION_2:
	             if (b1 >= SECTION_3 && b1 <= SECTION_4) {
	                 return true;
	             }
	         case SECTION_5:
	             switch (b1) {
	                 case SECTION_6:
	                     return true;
	             }
	         default:
	             return false;
	     }
	 }
	
	/**
	 * @see sun.net.util.IPAddressUtil#textToNumericFormatV4
	 * @param arg
	 * @return
	 */
	public static byte[] textToNumericFormatV4(String arg) {
		byte[] arg0 = new byte[4];
		long arg1 = 0L;
		int arg3 = 0;
		int arg4 = arg.length();
		if (arg4 != 0 && arg4 <= 15) {
			for (int arg5 = 0; arg5 < arg4; ++arg5) {
				char arg6 = arg.charAt(arg5);
				if (arg6 == 46) {
					if (arg1 < 0L || arg1 > 255L || arg3 == 3) {
						return null;
					}

					arg0[arg3++] = (byte) ((int) (arg1 & 255L));
					arg1 = 0L;
				} else {
					int arg7 = Character.digit(arg6, 10);
					if (arg7 < 0) {
						return null;
					}

					arg1 *= 10L;
					arg1 += (long) arg7;
				}
			}

			if (arg1 >= 0L && arg1 < 1L << (4 - arg3) * 8) {
				switch (arg3) {
				case 0:
					arg0[0] = (byte) ((int) (arg1 >> 24 & 255L));
				case 1:
					arg0[1] = (byte) ((int) (arg1 >> 16 & 255L));
				case 2:
					arg0[2] = (byte) ((int) (arg1 >> 8 & 255L));
				case 3:
					arg0[3] = (byte) ((int) (arg1 >> 0 & 255L));
				default:
					return arg0;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	//-----------------
	/**
	 * 是否是有效的ipv4地址
	 * @param address
	 * @return
	 */
	private static boolean isValidAddress(InetAddress address) {
		if (address == null || address.isLoopbackAddress())
			return false;
		String name = address.getHostAddress();
		return (name != null
				&& ! ANYHOST.equals(name)
				&& ! LOCALHOST.equals(name)
				//&& IP_PATTERN.matcher(name).matches()
				&& (address instanceof Inet4Address)
				);
	}
	private static boolean isNotBlank(String str){
		return  str!=null && str.trim().length()>0;
	}
	private static void error(String msg, Throwable e){
		System.out.println(msg+", " + e.getMessage());
	}
	private static void error(String msg){
		System.out.println(msg);
	}

	//-----------------
	public static void main(String[] args){
		String outer = getLocalPublicIp();
		String inner = getLocalInternalIp();
		String ip = getLocalIpAddress();
		System.out.println("outer:"+outer); 
		System.out.println("inner:"+inner); 
		System.out.println("ip:"+ip); 
	}
}
