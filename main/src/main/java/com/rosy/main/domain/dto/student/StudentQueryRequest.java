package com.rosy.main.domain.dto.student;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String studentName;

    private String studentNo;

    private String gender;

    private String className;

    private String major;

    private String email;

    private String phone;
}
