package org.example.gradeservice.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectDto implements Serializable {
    static final long serialVersionUID = 1L;
    Long id;
    String name;
    Integer credits;
}