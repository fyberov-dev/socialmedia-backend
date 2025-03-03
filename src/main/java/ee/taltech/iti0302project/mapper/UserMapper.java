package ee.taltech.iti0302project.mapper;

import ee.taltech.iti0302project.dto.auth.RegisterDto;
import ee.taltech.iti0302project.dto.user.UserDto;
import ee.taltech.iti0302project.dto.user.UserProfileDto;
import ee.taltech.iti0302project.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDto toDto(UserEntity userEntity);

    UserEntity fromRegisterDto(RegisterDto registerDto);

    UserProfileDto toUserProfileDto(UserEntity userEntity);

    UserEntity fromDto(UserDto userDto);
}
