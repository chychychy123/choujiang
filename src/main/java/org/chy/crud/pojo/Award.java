package org.chy.crud.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("award")
public class Award {
    private Integer id;
    private String name;
    private Integer quantity;
    private String prize;
}
