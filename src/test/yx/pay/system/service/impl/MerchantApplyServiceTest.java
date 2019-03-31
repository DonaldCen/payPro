package yx.pay.system.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import yx.pay.common.domain.QueryRequest;
import yx.pay.system.domain.wx.MerchantApply;
import yx.pay.system.service.MerchantApplyService;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MerchantApplyServiceTest {
    @Autowired
    private MerchantApplyService merchantApplyService;

    @Test
    public void test(){
        QueryRequest request = new QueryRequest();
        MerchantApply apply = new MerchantApply();
        List<MerchantApply> list = merchantApplyService.findMerchantApplyList(request,apply);
        log.info(JSON.toJSONString(list));
    }

}
