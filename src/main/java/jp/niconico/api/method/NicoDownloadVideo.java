package jp.niconico.api.method;

import jp.niconico.api.entity.FlvInfo;
import jp.niconico.api.entity.LoginInfo;
import jp.niconico.api.entity.ThumbInfo;
import jp.niconico.api.exception.NiconicoException;
import jp.niconico.api.http.HttpClientSetting;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;

public class NicoDownloadVideo {
    Logger logger = LoggerFactory.getLogger(NicoDownloadVideo.class);

    private String videoUrl = "http://www.nicovideo.jp/watch/";

    private LoginInfo loginInfo;

    private FlvInfo flvInfo;

    private ThumbInfo thumbInfo;

    private String videoId;

    public NicoDownloadVideo(String videoId, LoginInfo loginInfo, FlvInfo flvInfo, ThumbInfo thumbInfo) {
        this.videoId = videoId;
        this.loginInfo = loginInfo;
        this.flvInfo = flvInfo;
        this.thumbInfo = thumbInfo;
    }

    public File execute(String destDir) throws NiconicoException {
        File dir = new File(destDir);
        if (!dir.isDirectory()) {
            throw new NiconicoException(destDir + " isnt Directory.");
        }
        String fileName = thumbInfo.title;
        String fileType = thumbInfo.movieType;
        File destFile = null;

        //TODO mac（など）で日本語ファイル名文字化け問題暫定
        fileName = "nicovideo-" + thumbInfo.id;

        DefaultHttpClient httpClient = null;
        try {
            httpClient = HttpClientSetting.createHttpClient();
            httpClient.setCookieStore(loginInfo.cookie);
            HttpHead httpHead = new HttpHead(videoUrl + videoId);
            HttpResponse response = httpClient.execute(httpHead);
            //release entity
            EntityUtils.consumeQuietly(response.getEntity());

            HttpGet httpGet = new HttpGet(flvInfo.url);
            response = httpClient.execute(httpGet);

            logger.info("download video:" + videoId + " title: " + thumbInfo.title + " filesize:"
                    + String.format("%1$,3d", thumbInfo.sizeHigh / 1024) + "KByte");
            logger.info("download start --> " + flvInfo.url);
            long startTime = (new Date()).getTime();
            byte[] buf = EntityUtils.toByteArray(response.getEntity());
            long execTime = (new Date()).getTime() - startTime;
            logger.info("download end.  exec time: " + execTime + "ms");

            destFile = new File(dir.getAbsolutePath() + "/" + fileName + "." + fileType);
            if (destFile.exists()) {
                Date date = new Date();
                long time = date.getTime();
                destFile = new File(dir.getAbsolutePath() + "/" + fileName + "-" + time + "." + fileType);
            }

            FileUtils.writeByteArrayToFile(destFile, buf);
            logger.info("Create video file --> " + destFile.getAbsolutePath());


        } catch (Exception e) {
            throw new NiconicoException(e);
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }


        return destFile;
    }
}
