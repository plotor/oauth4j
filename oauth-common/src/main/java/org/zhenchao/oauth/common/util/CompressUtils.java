package org.zhenchao.oauth.common.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * compress util
 *
 * @author zhenchao.wang 2017-02-11 14:29
 * @version 1.0.0
 */
public class CompressUtils {

    private static final Logger log = LoggerFactory.getLogger(CompressUtils.class);

    private CompressUtils() {
    }

    public static byte[] compress(byte[] data) throws IOException {
        return compress(data, Deflater.DEFAULT_COMPRESSION);
    }

    public static byte[] compress(byte[] data, int level) throws IOException {
        return compress(data, level, Deflater.DEFAULT_STRATEGY);
    }

    public static byte[] compress(byte[] data, int level, int strategy) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setLevel(level);
        deflater.setStrategy(strategy);
        deflater.setInput(data);
        deflater.finish();
        byte[] buffer = new byte[1024];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length)) {
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                bos.write(buffer, 0, count);
            }
            byte[] output = bos.toByteArray();
            log.debug("Compress result : original[length={}], result[length={}]", data.length, output.length);
            return output;
        } finally {
            deflater.end();
        }
    }

    public static byte[] decompress(byte[] data) throws IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length)) {
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                bos.write(buffer, 0, count);
            }
            byte[] output = bos.toByteArray();
            log.debug("Decompress result : original[length={}], result[length={}]", data.length, output.length);
            return output;
        } catch (DataFormatException e) {
            log.error("Decompress data error, base64 data info [{}]", Base64.encodeBase64URLSafeString(data));
            throw new IOException("data format exception when decompress", e);
        } finally {
            inflater.end();
        }
    }
}