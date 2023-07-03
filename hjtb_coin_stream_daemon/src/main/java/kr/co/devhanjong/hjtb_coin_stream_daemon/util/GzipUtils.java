package kr.co.devhanjong.hjtb_coin_stream_daemon.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GzipUtils {
    public static String decompress(byte[] compressedData) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
            GZIPInputStream gzipInputStream = new GZIPInputStream(bais);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            gzipInputStream.close();
            baos.close();

            return baos.toString("UTF-8");
        } catch (IOException e) {
            return "";
        }
    }
}
