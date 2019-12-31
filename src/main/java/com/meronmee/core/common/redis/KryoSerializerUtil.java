package com.meronmee.core.common.redis;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import de.javakaffee.kryoserializers.KryoReflectionFactorySupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Kryo序列化/反序列化工具类
 * @author Meron 
 */
public class KryoSerializerUtil {
	private static final Logger log = LoggerFactory.getLogger(KryoSerializerUtil.class);
    private static final byte[] EMPTY_ARRAY = new byte[0];
    
    private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            Kryo kryo = new KryoReflectionFactorySupport();
            return kryo;
        }
    };   
      
    /**
     * 序列化对象
     * @param obj 
     */
    public static <T> byte[] serialize(T obj) {
    	if (obj == null) {
            return EMPTY_ARRAY;
        }
        Kryo kryo = kryos.get();
        Output output = new Output(64, -1);
  
        try {
            kryo.writeClassAndObject(output, obj);
            return output.toBytes();
        } catch(Exception e){
        	log.error("serialize object exception", e);
        	return EMPTY_ARRAY;
        } finally {
            closeOutputStream(output);
        }
    }
  
    /**
     * 反序列化对象
     * @param bytes
     * @return
     */ 
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] bytes){
    	if (isEmpty(bytes)) {
            return null;
        }
        Kryo kryo = kryos.get();
        Input input = null;
        try {
            input = new Input(bytes); 
                        
            return (T) kryo.readClassAndObject(input);
        } catch(Exception e){
        	log.error("deserialize object exception", e);
        	return null;
        } finally {
            closeInputStream(input);
        }

    }
	//------------------------------------- 
    
    public static byte[] getBytes(final String string) {
        if (string == null) {
            return null;
        }
		return string.getBytes(Charset.forName("UTF-8")); 
    }

      
	//-------------------------------------
     
    private static void closeInputStream(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (Exception e) {
            	log.error("serialize object close inputStream exception", e);
            }
        }
    }
    private static void closeOutputStream(OutputStream output) {
        if (output != null) {
            try {
                output.flush();
                output.close();
            } catch (Exception e) {
                log.error("serialize object close outputStream exception", e);
            }
        }
    }
    private static boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }

 
}

