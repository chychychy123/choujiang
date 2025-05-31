package org.chy.awardrecord.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.chy.awardrecord.pojo.AwardRecord;


import java.util.List;
@Mapper
public interface AwardRecordMapper extends BaseMapper<AwardRecord> {
    List<AwardRecord> selectAwardRecord(Long userId);
}
