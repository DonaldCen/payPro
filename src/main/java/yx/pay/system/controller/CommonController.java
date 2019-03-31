package yx.pay.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yx.pay.common.controller.BaseController;
import yx.pay.common.domain.FebsResponse;
import yx.pay.common.utils.ListUtil;
import yx.pay.system.domain.Area;
import yx.pay.system.domain.Bank;
import yx.pay.system.domain.ImageTypeEnum;
import yx.pay.system.domain.Rate;
import yx.pay.system.domain.vo.BankAreaVo;
import yx.pay.system.domain.wx.WxConfig;
import yx.pay.system.service.AreaService;
import yx.pay.system.service.BankService;
import yx.pay.system.service.RateService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author <a href="mailto:cenyingqiang@wxchina.com">yingqiang.Cen</a>
 * @Date 2019/3/21
 * @Version 1.0.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/sys/common")
public class CommonController extends BaseController {
    @Autowired
    private BankService bankService;
    @Autowired
    private RateService rateService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private WxConfig wxConfig;
    private static final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");


    @GetMapping("getParentBankList")
    public FebsResponse getParentBankList() {
        try {
            List<Bank> list = bankService.getParentBankList(0);
            return new FebsResponse().success(list);
        } catch (Exception e) {
            log.error("getParentBankList error..",e);
        }
        return new FebsResponse().fail("getParentBankList failed..");
    }

    @PostMapping("getBankListByName")
    public FebsResponse getBankListByName(Bank bank) {
        try {
            String bankName = bank.getBankName();
            Integer parentId = bank.getParentId();
            if (parentId == null) {
                parentId = 0;
            }
            List<Bank> list = bankService.selectByName(bankName, parentId);
            return new FebsResponse().success(list);
        } catch (Exception e) {
            log.error("getBankListByName error..",e);
        }
        return new FebsResponse().fail("getBankListByName failed..");
    }

    @GetMapping("getRateList")
    public FebsResponse getRateList() {
        try {
            List<Rate> list = rateService.selectAll();
            return new FebsResponse().success(list);
        } catch (Exception e) {
            log.error("getRateList error..",e);
        }
        return new FebsResponse().fail("getRateList failed..");
    }

    @GetMapping("getBankAreaList")
    public FebsResponse getBankAreaList(){
        try {
            Integer parentId = 0;
            Area area = new Area();
            area.setParentId(parentId);
            area.setLevel(0);
            List<BankAreaVo> voList = getBankAreaVoLists(area);
            return new FebsResponse().success(voList);
        } catch (Exception e) {
            log.error("getBankAreaList error..",e);
        }
        return new FebsResponse().fail("getBankAreaList failed..");
    }

    @PostMapping("getChildrenArea")
    public FebsResponse getChildrenArea(Area a){
        try {
            if(a == null){
                return new FebsResponse().fail("getChildrenArea failed..");
            }
            List<BankAreaVo> voList = getBankAreaVoLists(a);
            return new FebsResponse().success(voList);
        } catch (Exception e) {
            log.error("getChildrenArea error..",e);
        }
        return new FebsResponse().fail("getChildrenArea failed..");
    }

    @RequestMapping("/addImage")
    public FebsResponse addImage(@RequestParam(name = "file") MultipartFile file,@RequestParam(name = "fileType")String fileType){
        if(file != null && !file.isEmpty()){
            try {
                String fileName = getFileNameByFileType(fileType,file);
                log.info("fileName=[{}]",fileName);
                File newFile = new File(fileName);
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(newFile));
                out.write(file.getBytes());
                out.flush();
                out.close();
                return new FebsResponse().success();
            } catch (IOException e) {
                log.error("addImage error..",e);
            }
        }
        return new FebsResponse().fail("addImage failed..");
    }

    private String getFileNameByFileType(String fileType,MultipartFile file){
        String fileName = file.getOriginalFilename();
        //文件后缀名
        String[] arr = fileName.split("\\.");
        String fileSuffix = null;
        if(arr != null && arr.length >= 2){
            fileSuffix = arr[1];
        } else {
            fileSuffix = ".jpg";
        }
        String savePath = wxConfig.getQrCodeUrl();
        String type = ImageTypeEnum.getTypeName(Integer.parseInt(fileType));
        StringBuffer sb = new StringBuffer();
        sb.append(savePath);
        sb.append(type).append("_").append(dateFormat.format(new Date())).append(".").append(fileSuffix);
        return sb.toString();
    }

    private List<BankAreaVo> getBankAreaVoLists(Area a){
        List<Area> list = areaService.selectAllProvince(a.getParentId());
        List<BankAreaVo> voList = new ArrayList<>();
        if(ListUtil.isNotBlank(list)){
            for(Area area:list){
                BankAreaVo vo = new BankAreaVo(String.valueOf(area.getCode()),area.getAreaName(),a.getLevel()==3);
                vo.setLevel(area.getLevel());
                voList.add(vo);
            }
        }
        return voList;
    }



}
