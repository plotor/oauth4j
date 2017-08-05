package org.zhenchao.oauth.common.util;

import org.apache.commons.lang3.ArrayUtils;

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
public abstract class CompressUtils {

    /**
     * compress
     *
     * @param data
     * @return
     * @throws IOException
     */
    public static byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        try {
            deflater.setLevel(Deflater.DEFAULT_COMPRESSION);
            deflater.setStrategy(Deflater.NO_COMPRESSION);
            deflater.setInput(data);
            deflater.finish();
            byte[] buffer = new byte[2048];
            int count = deflater.deflate(buffer);
            return ArrayUtils.subarray(buffer, 0, count);
        } finally {
            deflater.end();
        }
    }

    /**
     * decompress
     *
     * @param data
     * @return
     * @throws IOException
     * @throws DataFormatException
     */
    public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        try {
            inflater.setInput(data);
            byte[] buffer = new byte[2048];
            int count = inflater.inflate(buffer);
            return ArrayUtils.subarray(buffer, 0, count);
        } finally {
            inflater.end();
        }
    }
}