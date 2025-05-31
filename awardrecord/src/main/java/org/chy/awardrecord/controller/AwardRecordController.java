package org.chy.awardrecord.controller;


import org.chy.awardrecord.pojo.AwardRecord;
import org.chy.awardrecord.service.AwardRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/award")
public class AwardRecordController {
    @Autowired
    private AwardRecordService awardRecordService;
    //根据用户userid展示中奖信息
    @GetMapping("/showUserAward")
    public List<AwardRecord> showUserAward(Long userId){
        List<AwardRecord> awardRecord=awardRecordService.showAward(userId);
        return awardRecord;
    }
}
