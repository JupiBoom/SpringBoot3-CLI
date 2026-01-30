package com.rosy.main.domain.dto.live;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LiveRoomItemUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String sellingPoints;

    private Integer sortOrder;

    private Byte status;
}