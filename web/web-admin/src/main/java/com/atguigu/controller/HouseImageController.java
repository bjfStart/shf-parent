package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.HouseService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author feng
 * @create 2022-06-15 21:49
 */
@Controller
@RequestMapping("/houseImage")
public class HouseImageController extends BaseController {
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseService houseService;
    private static final String SHOW_ACTION = "redirect:/house/";
    private static final String PAGE_UPLOAD_SHOW = "house/upload";

    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable("houseId") Long houseId, @PathVariable("type") Integer type, Model model) {
        model.addAttribute("houseId", houseId);
        model.addAttribute("type", type);
        return PAGE_UPLOAD_SHOW;
    }

    @ResponseBody
    @PostMapping("/upload/{houseId}/{type}")
    public Result upload(@PathVariable("houseId") Long houseId,
                         @PathVariable("type") Integer type,
                         @RequestParam("file") MultipartFile[] multipartFiles) throws IOException {

        for (int i = 0; i < multipartFiles.length; i++) {
            //1.将文件上传到七牛云
            //1.1获取文件名
            String filename = multipartFiles[i].getOriginalFilename();
            //1.2生成唯一的文件名
            String uuidName = FileUtil.getUUIDName(filename);
            //1.3上传文件
            QiniuUtils.upload2Qiniu(multipartFiles[i].getBytes(), uuidName);
            //2.将文件的url保存到数据库中
            //2.1 拼接图片的url
            String url = QiniuUtils.getUrl(uuidName);
            //2.2 创建HouseImage对象
            HouseImage houseImage = new HouseImage();
            houseImage.setImageName(uuidName);
            houseImage.setImageUrl(url);
            houseImage.setHouseId(houseId);
            houseImage.setType(type);
            //2.3 保存到数据库中
            houseImageService.insert(houseImage);

            if (i == 0) {
                //判断房源是否有默认图片，如果没有将上传的第一张图片设置为默认图片
                //根据房源houseId查询房源信息
                House house = houseService.getById(houseId);
                //判断房源是否有默认图片属性
                if (house.getDefaultImageUrl() == null || "".equals(house.getDefaultImageUrl()) || "null".equals(house.getDefaultImageUrl())) {
                    house.setDefaultImageUrl(url);
                    houseService.update(house);
                }
            }

        }
        //返回Result.ok表示上传成功
        return Result.ok();
    }

    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId, @PathVariable("id") Long id, Model model) {
        //从七牛云中删除
        HouseImage houseImage = houseImageService.getById(id);
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageName());
        //后端中删除
        houseImageService.delete(id);
        //判断当前删除的图片是否为房源的默认图片呢，若是，
        House house = houseService.getById(houseId);
        if (houseImage.getImageUrl().equals(house.getDefaultImageUrl())) {
            //将房源的默认图片设置为null
            house.setDefaultImageUrl("null");
            //更新数据中的
            houseService.update(house);
        }
        return SHOW_ACTION + houseId;
    }


}
