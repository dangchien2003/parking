package com.parking.ticket_service.utils;

import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressionUtils {
    public static byte[] compress(String data) {
        try {
            byte[] input = data.getBytes("UTF-8");
            Deflater deflater = new Deflater();
            deflater.setInput(input);
            deflater.finish();
            byte[] output = new byte[1024];
            int compressedDataLength = deflater.deflate(output);
            deflater.end();
            byte[] compressedData = new byte[compressedDataLength];
            System.arraycopy(output, 0, compressedData, 0, compressedDataLength);
            return compressedData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decompress(byte[] data) {
        try {
            Inflater inflater = new Inflater();
            inflater.setInput(data);
            byte[] output = new byte[1024];
            int resultLength = inflater.inflate(output);
            inflater.end();
            return new String(output, 0, resultLength, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}