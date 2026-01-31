package com.rosy.main.domain.dto.material;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 课程资料添加请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class MaterialAddRequest implements Serializable {

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
     * 资料描述
     */
    private String description;

    /**
     * 资料类型：1-文档，2-视频，3-图片，4-其他
     */
    private Byte materialType;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;
}