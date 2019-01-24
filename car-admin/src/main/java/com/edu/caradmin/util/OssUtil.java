package com.edu.caradmin.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.edu.car.uid.IdWorker;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


/**
 * carRental
 *
 * @author Administrator
 * @date 2019/1/23 16:39
 */
public class OssUtil {
    public static String getUrl(MultipartFile file) {
        String endpoint = "oss-cn-shanghai.aliyuncs.com";
        String accessKeyId = "LTAIKhwpYCuDStdz";
        String accessKeySecret = "4hwhm3bobkZKQQ51iQbRpjVqgQA4Rv";
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        StringBuilder name = new StringBuilder().append(IdWorker.getId()).append(".jpg");
        try {
            InputStream inputStream = file.getInputStream();
            String bucketName = "2019123";
            ossClient.putObject(bucketName, name.toString(),inputStream);
            StringBuilder url = new StringBuilder().append("https://")
                    .append(bucketName).append(".").append(endpoint).append("/").append(name);
            return url.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
    }
}
