package yx.pay.system.service;


import org.springframework.web.multipart.MultipartFile;

import yx.pay.common.domain.FebsResponse;

/**
 * 图片上传
 * * Created by 0151717 on 2019/3/23.
 */
public interface UploadService {
    /**
     * 图片上传
     * @param multipartFile
     * @return
     */
    FebsResponse uploadFile(MultipartFile multipartFile);
}