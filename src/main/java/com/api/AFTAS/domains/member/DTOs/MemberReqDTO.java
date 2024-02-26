package com.api.AFTAS.domains.member.DTOs;

import com.api.AFTAS.domains.member.IdentityDocumentType;
import com.api.AFTAS.security.User.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
@Data
public class MemberReqDTO {
    private Integer id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String email;
    private Role role;
    private String nationality;
    private IdentityDocumentType identityDocument;
    private String identityNumber;
}
