package com.rosy.main.domain.dto.live;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LiveRoomQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Byte status;

    private Long creatorId;
}