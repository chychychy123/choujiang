package org.chy.crud.service;

import org.chy.crud.pojo.Award;

import java.util.List;

public interface AwardService {

    List<Award> showAward();


    String draw(Long userId);
}
