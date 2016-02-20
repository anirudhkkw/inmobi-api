package com.inmobi.AWS;

import settings.Global;

import java.net.URL;
import java.util.Date;

public class GeneratePublicUrlAws {
    public static String generateUrl(String photoId, String imageName, String productName) {
    Date expiration = new Date();
    long msec = expiration.getTime();
    msec += 1000*60*60*24*24;
    expiration.setTime(msec);
    URL s = Global.getS3Client().generatePresignedUrl(Global.getAmazonBucket(),productName + "/" +photoId + "/" + imageName, expiration);
    return s.toString();
}
}
