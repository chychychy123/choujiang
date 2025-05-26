package org.chy.crud.controller;

import lombok.extern.slf4j.Slf4j;
import org.chy.crud.pojo.Award;
import org.chy.crud.service.AwardService;
import org.chy.crud.utils.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/awards")
public class AwardController {
    @Autowired
    private AwardService awardService;
    @Autowired
    private JwtTool jwtTool;
    
    //展示所有奖项
    @GetMapping("/showAward")
    public List<Award> showAward() {
        return awardService.showAward();
    }
    
    //抽奖功能
    @PostMapping("/draw")
    public String draw(@RequestHeader("Authorization") String token) {
        try {
            log.info("开始抽奖，接收到的token: {}", token);
            Long userId = jwtTool.parseTokenToUserId(token);
            log.info("解析出的userId: {}", userId);
            String result = awardService.draw(userId);
            log.info("抽奖结果: {}", result);
            return result;
        } catch (Exception e) {
            log.error("抽奖过程发生错误", e);
            throw e;
        }
    }
}
