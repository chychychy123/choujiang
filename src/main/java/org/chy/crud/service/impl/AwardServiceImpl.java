package org.chy.crud.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.chy.crud.mapper.AwardMapper;

import org.chy.crud.pojo.Award;
import org.chy.crud.service.AwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AwardServiceImpl extends ServiceImpl<AwardMapper, Award> implements AwardService {

    private final Random random = new Random();

    @Override
    public List<Award> showAward() {
        return baseMapper.selectList(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String draw() {
        // 1. 查询所有还有库存的奖项
        List<Award> availableAwards = lambdaQuery()
                .gt(Award::getQuantity, 0)
                .list();

        if (CollectionUtils.isEmpty(availableAwards)) {
            return "奖品已抽完";
        }

        // 2. 计算总权重（剩余数量总和）
        int totalWeight = availableAwards.stream()
                .mapToInt(Award::getQuantity)
                .sum();

        // 3. 生成随机数并确定中奖项
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

        // 4. 扣减库存
        if (selectedAward != null) {
            boolean success = lambdaUpdate()
                    .eq(Award::getId, selectedAward.getId())
                    .gt(Award::getQuantity, 0)
                    .setSql("quantity = quantity - 1")
                    .update();

            if (success) {
                return "恭喜抽到"+selectedAward.getName() + ":" + selectedAward.getPrize()+"!";
            }
        }

        return "抽奖失败，请重试";
    }
}
