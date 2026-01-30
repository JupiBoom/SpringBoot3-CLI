package com.rosy.main.domain.dto.live;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LiveRoomStatusUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long roomId;

    private Byte status;
}
