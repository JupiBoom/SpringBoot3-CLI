package com.rosy.main.domain.dto.teacher;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeacherQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String teacherName;

    private String teacherNo;

    private String gender;

    private String department;

    private String title;

    private String email;

    private String phone;
}
