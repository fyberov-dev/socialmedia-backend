package ee.taltech.iti0302project.service;

import ee.taltech.iti0302project.dto.role.RoleDto;
import ee.taltech.iti0302project.entity.RoleEntity;
import ee.taltech.iti0302project.mapper.RoleMapper;
import ee.taltech.iti0302project.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public List<RoleDto> findAll() {
        return roleMapper.toRoleDtoList(roleRepository.findAll());
    }

    public RoleDto findById(Long id) {
        return roleRepository.findById(id)
                .map(roleMapper::toRoleDto)
                .orElse(null);
    }

    public RoleDto saveRole(RoleDto roleDto) {
        RoleEntity roleEntity = roleMapper.toRoleEntity(roleDto);
        RoleEntity savedRole = roleRepository.save(roleEntity);
        return roleMapper.toRoleDto(savedRole);
    }

    public RoleDto updateRole(Long id, RoleDto updatedRoleDto) {
        if (roleRepository.existsById(id)) {
            RoleEntity roleEntity = roleMapper.toRoleEntity(updatedRoleDto);
            roleEntity.setId(id);
            RoleEntity updatedRole = roleRepository.save(roleEntity);
            return roleMapper.toRoleDto(updatedRole);
        }
        return null;
    }

    public boolean deleteRole(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
