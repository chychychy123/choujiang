package org.chy.crud.controller;

import org.chy.crud.pojo.Award;
import org.chy.crud.service.AwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/awards")
public class AwardController {
    @Autowired
    private AwardService awardService;
    //展示所有奖项
    @GetMapping("/showAward")
    public List<Award> showAward() {
        return awardService.showAward();
    }
    //抽奖功能
    @GetMapping("/draw")
    public String draw() {
        return awardService.draw();
    }
}
