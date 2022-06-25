package com.atguigu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

/**
 * @author feng
 * @create 2022-06-15 21:09
 */
public class testUpload {
    /**
     * 上传
     */
    @Test
    public void testUpload(){
        //构造一个带指定 Region 对象的配置类 Zone指定的是区域地区，zone2表示华南地区
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "JGD_mlslyxas_rdqYC_Fl67r-u2yRMXdMbyMPFYi";
        String secretKey = "Cfe_TPawW5-XTKEvldrFomXUP1k-v90EAj6yQlDk";
        String bucket = "atguigu-pic";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "d:/qiniu/1.png";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

    }

    @Test
    public void deleteFile(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        String accessKey = "JGD_mlslyxas_rdqYC_Fl67r-u2yRMXdMbyMPFYi";
        String secretKey = "Cfe_TPawW5-XTKEvldrFomXUP1k-v90EAj6yQlDk";
        String bucket = "atguigu-pic";
        String key = "Fk8-T_v5bvqEyu8DmMgdbPqMDzP5";

        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
