package com.rosy.main.domain.dto.material;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 课程资料查询请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 资料标题
     */
    private String title;

    /**
     * 资料类型：1-文档，2-视频，3-图片，4-其他
     */
    private Byte materialType;
}