package org.chy.award.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.chy.award.exception.BadRequestException;
import org.chy.award.mapper.AwardMapper;
import org.chy.award.pojo.Award;
import org.chy.award.service.AwardService;
import org.chy.awardrecord.mapper.AwardRecordMapper;
import org.chy.awardrecord.pojo.AwardRecord;
import org.chy.login.pojo.UserLogin;
import org.chy.login.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwardServiceImpl extends ServiceImpl<AwardMapper, Award> implements AwardService {

    private final Random random = new Random();
    @Autowired
    private  UserLoginService userLoginService; // 用于获取用户信息、
    @Autowired
    private  AwardRecordMapper awardRecordMapper;
    @Override
    public List<Award> showAward() {
        return baseMapper.selectList(null);
    }

    @Override
    public String draw(Long userId) {
        try {
            log.info("开始处理用户{}的抽奖请求", userId);
            
            // 1. 获取当前用户信息
            UserLogin user = userLoginService.query(userId);
            log.info("查询到的用户信息: {}", user);
            if (user == null) {
                log.warn("用户{}不存在", userId);
                throw new BadRequestException("用户不存在");
            }

            // 2. 查询可用奖品
            List<Award> availableAwards = lambdaQuery()
                    .gt(Award::getQuantity, 0)
                    .list();
            log.info("当前可用奖品列表: {}", availableAwards);

            if (CollectionUtils.isEmpty(availableAwards)) {
                log.warn("没有可用奖品");
                return "奖品已抽完";
            }

        int totalWeight = availableAwards.stream()
                .mapToInt(Award::getQuantity)
                .sum();

        int randomNum = random.nextInt(totalWeight) + 1;
        int accumulatedWeight = 0;
        Award selectedAward = null;

        for (Award award : availableAwards) {
            accumulatedWeight += award.getQuantity();
            if (randomNum <= accumulatedWeight) {
                selectedAward = award;
                break;
            }
        }

        // 3. 扣减库存
        if (selectedAward != null) {
            boolean success = lambdaUpdate()
                    .eq(Award::getId, selectedAward.getId())
                    .gt(Award::getQuantity, 0)
                    .setSql("quantity = quantity - 1")
                    .update();

            if (success) {
                // 4. 插入中奖记录
                AwardRecord awardRecord = new AwardRecord();
                awardRecord.setUserId(userId);
                awardRecord.setUserName(user.getUsername());
                awardRecord.setAwardId(selectedAward.getId().intValue());
                awardRecord.setAwardName(selectedAward.getName());
                awardRecord.setPrize(selectedAward.getPrize());
                awardRecord.setCreateTime(LocalDateTime.now());

                awardRecordMapper.insert(awardRecord);

                return "恭喜" + user.getUsername() + "抽到" +
                        selectedAward.getName() + ":" + selectedAward.getPrize() + "!";
            }
        }
        return "抽奖失败，请重试";
        } catch (Exception e) {
            log.error("抽奖过程发生错误: {}", e.getMessage(), e);
            throw e;
        }
    }
}
