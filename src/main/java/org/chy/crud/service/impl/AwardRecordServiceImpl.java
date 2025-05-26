package org.chy.crud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.chy.crud.mapper.AwardRecordMapper;
import org.chy.crud.pojo.AwardRecord;
import org.chy.crud.service.AwardRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AwardRecordServiceImpl extends ServiceImpl<AwardRecordMapper, AwardRecord> implements AwardRecordService {
    //根据用户userid展示中奖信息
    @Override
    public List<AwardRecord> showAward(Integer userId) {
        return baseMapper.selectAwardRecord(userId);
    }


}
