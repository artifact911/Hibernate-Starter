package org.art.dto;

import org.art.entity.PersonalInfo;
import org.art.entity.Role;

public record UserCreateDto(PersonalInfo personalInfo,
                            String username,
                            String info,
                            Role role,
                            Integer companyId) {
}
