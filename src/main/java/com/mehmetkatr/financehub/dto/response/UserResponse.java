package com.mehmetkatr.financehub.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private String email;
    private String fullName;
    private String phone;
    private boolean isActive;
}
