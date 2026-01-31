package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程资料视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class CourseMaterialVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 资料ID
     */
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

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
     * 资料类型描述
     */
    private String materialTypeDesc;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件大小描述
     */
    private String fileSizeDesc;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 上传者ID
     */
    private Long uploaderId;

    /**
     * 上传者姓名
     */
    private String uploaderName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}