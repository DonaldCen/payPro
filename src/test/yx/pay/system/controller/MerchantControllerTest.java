package yx.pay.system.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import yx.pay.common.domain.FebsResponse;
import yx.pay.system.service.UploadService;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * Created by 0151717 on 2019/3/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MerchantControllerTest {
    @Autowired
    private UploadService uploadService;

    @Test
    public void testUpload() throws Exception {
        File file = new File("D:\\2018手机文件\\pic\\aa.jpg");
        MultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(file));
        FebsResponse s = uploadService.uploadFile(multipartFile);
        System.out.println(s);
    }
}