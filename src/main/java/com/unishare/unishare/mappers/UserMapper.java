package com.unishare.unishare.mappers;

import com.unishare.unishare.dtos.user.PublicUserDto;
import com.unishare.unishare.dtos.user.UserDto;
import com.unishare.unishare.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    PublicUserDto toPublicDto(User user);
}
