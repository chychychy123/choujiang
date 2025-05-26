package org.chy.crud.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("award_record") // 对应您的表名
public class AwardRecord {
    private Long id;
    private Long userId;
    private String userName;    // 用户名称（对应user_name）
    private Integer awardId;
    private String awardName;   // 奖项名称
    private String prize;       // 奖品内容
    private LocalDateTime createTime; // 创建时间

    // 其他字段可根据需要添加
}