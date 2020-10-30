package com.meronmee.core.common.util;

import org.apache.commons.io.FileUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件及文件夹常用操作工具类
 * @author Meron
 *
 */
public class FileUtil extends FileUtils{

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
                closeable = null;
            }
        } catch (Exception ioe) {
            // ignore
        }
    }

    public static void closeStream(InputStream in){
        if(in!=null){
            try{
                in.close();
            }catch(Exception e){ }
            in = null;
        }
    }
    public static void closeStream(OutputStream out){
        if(out!=null){
            try{
                out.close();
            }catch(Exception e){ }
            out = null;
        }
    }

    public static InputStream getUrlInputStream(String url) throws Exception{
        InputStream inputStream = null;
        if(url.toLowerCase().startsWith("http://")) {
            inputStream = new URL(url).openStream();
        } else {//https
            SSLContext sc = SSLContext.getInstance( "TLS");
            TrustManager[] tmArr = {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                @Override
                public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
            }};
            sc.init(null, tmArr, new SecureRandom());
            URL fileUrl = new URL(url);
            HttpsURLConnection huc = (HttpsURLConnection) fileUrl.openConnection();
            //java.security.cert.CertificateException: No subject alternative DNS
            huc.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            inputStream = huc.getInputStream();
        }

        return inputStream;
    }

    /**
     * 将目标文本文件按行平均分割为多个文件
     * @param filePath 文件路径
     * @param fileCount 分割后的个数
     * @throws IOException
     */
    public static List<String> splitTextFile(String filePath, int fileCount) throws IOException {
        List<String> splitedFiles = new ArrayList<>();

        FileInputStream fis = new FileInputStream(filePath);
        FileChannel inputChannel = fis.getChannel();
        final long fileSize = inputChannel.size();
        long average = fileSize / fileCount;//平均值
        long bufferSize = 1024; //缓存块大小，自行调整
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.valueOf(bufferSize + "")); // 申请一个缓存区
        long startPosition = 0; //子文件开始位置
        long endPosition = average < bufferSize ? 0 : average - bufferSize;//子文件结束位置
        for (int i = 0; i < fileCount; i++) {
            if (i + 1 != fileCount) {
                int read = inputChannel.read(byteBuffer, endPosition);// 读取数据
                readW:
                while (read != -1) {
                    byteBuffer.flip();//切换读模式
                    byte[] array = byteBuffer.array();
                    for (int j = 0; j < array.length; j++) {
                        byte b = array[j];
                        if (b == 10 || b == 13) { //判断\n\r
                            endPosition += j;
                            break readW;
                        }
                    }
                    endPosition += bufferSize;
                    byteBuffer.clear(); //重置缓存块指针
                    read = inputChannel.read(byteBuffer, endPosition);
                }
            }else{
                endPosition = fileSize; //最后一个文件直接指向文件末尾
            }
            String destFile = filePath.replace(".", "."+(i + 1)+".");
            FileOutputStream fos = new FileOutputStream(destFile);
            FileChannel outputChannel = fos.getChannel();
            inputChannel.transferTo(startPosition, endPosition - startPosition, outputChannel);//通道传输文件数据
            outputChannel.close();
            fos.close();
            startPosition = endPosition + 1;
            endPosition += average;

            splitedFiles.add(destFile);
        }//for
        inputChannel.close();
        fis.close();

        return splitedFiles;
    }
}
