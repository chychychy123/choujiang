package org.chy.award.service;



import org.chy.award.pojo.Award;

import java.util.List;

public interface AwardService {

    List<Award> showAward();


    String draw(Long userId);
}
