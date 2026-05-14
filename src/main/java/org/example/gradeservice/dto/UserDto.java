package org.example.gradeservice.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto implements Serializable {
    static final long serialVersionUID = 1L;
    Long id;
    String firstName;
    String lastName;
    String email;
}