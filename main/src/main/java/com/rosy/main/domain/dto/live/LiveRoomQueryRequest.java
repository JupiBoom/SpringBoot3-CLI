package com.rosy.main.domain.dto.live;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class LiveRoomQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String roomName;

    private Long anchorId;

    private String anchorName;

    private Byte status;

    private String sortField;

    private String sortOrder;
}
