package org.chy.crud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.chy.crud.pojo.Award;
import org.chy.crud.pojo.AwardRecord;

import java.util.List;

public interface AwardRecordMapper extends BaseMapper<AwardRecord> {
    List<AwardRecord> selectAwardRecord(Long userId);
}
