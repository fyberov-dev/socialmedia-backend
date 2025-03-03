package ee.taltech.iti0302project.mapper;

import ee.taltech.iti0302project.dto.role.RoleDto;
import ee.taltech.iti0302project.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RoleMapper {
    RoleDto toRoleDto(RoleEntity roleEntity);

    RoleEntity toRoleEntity(RoleDto roleDto);

    List<RoleDto> toRoleDtoList(List<RoleEntity> roleEntities);
}
