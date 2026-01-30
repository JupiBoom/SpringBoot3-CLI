package com.rosy.main.domain.dto.live;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LiveRoomUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String description;

    private String coverImage;

    private Byte status;

    private Long currentItemId;
}