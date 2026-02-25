package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LiveAudienceRetentionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer minuteMark;

    private Integer onlineCount;

    private Integer newViewer;
}
