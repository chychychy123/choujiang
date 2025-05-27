package org.chy.crud.service;

import org.chy.crud.pojo.AwardRecord;

import java.util.List;

public interface AwardRecordService {

    List<AwardRecord> showAward(Long userId);
}
