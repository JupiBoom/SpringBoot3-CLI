package com.rosy.main.domain.vo;

import com.rosy.main.common.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ForumPostVO extends BaseVO {

    private Long id;
    
    private String title;
    
    private String content;
    
    private Long activityId;
    
    private Long userId;
    
    private String userName;
    
    private Long viewCount;
    
    private Long likeCount;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private List<String> images;
    
    private String coverImage;
}