package org.chy.awardrecord.service;



import org.chy.awardrecord.pojo.AwardRecord;

import java.util.List;

public interface AwardRecordService {

    List<AwardRecord> showAward(Long userId);
}
