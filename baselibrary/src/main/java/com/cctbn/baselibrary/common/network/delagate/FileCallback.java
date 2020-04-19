package com.cctbn.baselibrary.common.network.delagate;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.ResponseBody;

/**
 * @createDate: 2019/1/9
 * @author: mayz
 * @version: 1.0
 */
public abstract class FileCallback implements Callback {
    private String filePath;
    private String fileName;

    public FileCallback(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void saveFile(long range, ResponseBody responseBody) {

        RandomAccessFile randomAccessFile = null;
        InputStream inputStream = null;
        long total = range;
        long responseLength = 0;
        try {
            byte[] buf = new byte[2048];
            int len = 0;
            responseLength = responseBody.contentLength();
            inputStream = responseBody.byteStream();
            File file = new File(filePath, fileName);
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            onStart();
            randomAccessFile = new RandomAccessFile(file, "rwd");
            if (range == 0) {
                randomAccessFile.setLength(responseLength);
            }
            randomAccessFile.seek(range);
            int progress = 0;
            int lastProgress = 0;
            while ((len = inputStream.read(buf)) != -1) {
                randomAccessFile.write(buf, 0, len);
                total += len;
                lastProgress = progress;
                progress = (int) (total * 100 / randomAccessFile.length());
                if (progress > 0 && progress != lastProgress) {
                    onProgress(progress);
                }
            }

            onCompleted(file);

        } catch (Exception e) {
            onError(e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                //SPDownloadUtil.getInstance().save(url, total);
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void onStart();

    public abstract void onProgress(int progress);

    public abstract void onCompleted(File file);
}
