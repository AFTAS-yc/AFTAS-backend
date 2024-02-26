package com.api.AFTAS.domains.member.DTOs;

import com.api.AFTAS.domains.hunting.DTOs.HuntingReqDTO;
import com.api.AFTAS.domains.hunting.Hunting;
import com.api.AFTAS.domains.member.IdentityDocumentType;
import com.api.AFTAS.security.User.Role;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class MemberRespDTO {
    private Integer id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String email;
    private Role role;
    private String familtyName;
    private LocalDate AccessionDate;
    private String nationality;
    private IdentityDocumentType identityDocument;
    private String identityNumber;
    private List<HuntingReqDTO> huntings;
}
