package ee.taltech.iti0302project;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ee.taltech.iti0302project.dto.role.RoleDto;
import ee.taltech.iti0302project.entity.RoleEntity;
import ee.taltech.iti0302project.mapper.RoleMapper;
import ee.taltech.iti0302project.repository.RoleRepository;
import ee.taltech.iti0302project.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoleServiceTests {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    private RoleDto roleDto;
    private RoleEntity roleEntity;
    private Long roleId = 1L;

    @BeforeEach
    void setUp() {
        roleDto = new RoleDto();
        roleDto.setId(roleId);
        roleDto.setName("ROLE_ADMIN");

        roleEntity = new RoleEntity();
        roleEntity.setId(roleId);
        roleEntity.setName("ROLE_ADMIN");
    }

    @Test
    void testFindAll() {
        List<RoleEntity> roleEntities = List.of(roleEntity);
        when(roleRepository.findAll()).thenReturn(roleEntities);
        when(roleMapper.toRoleDtoList(roleEntities)).thenReturn(List.of(roleDto));

        List<RoleDto> result = roleService.findAll();

        verify(roleRepository).findAll();
        verify(roleMapper).toRoleDtoList(roleEntities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(roleDto.getName(), result.get(0).getName());
    }

    @Test
    void testFindById_Found() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(roleEntity));
        when(roleMapper.toRoleDto(roleEntity)).thenReturn(roleDto);

        RoleDto result = roleService.findById(roleId);

        verify(roleRepository).findById(roleId);
        verify(roleMapper).toRoleDto(roleEntity);

        assertNotNull(result);
        assertEquals(roleDto.getName(), result.getName());
    }

    @Test
    void testFindById_NotFound() {
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        RoleDto result = roleService.findById(roleId);

        verify(roleRepository).findById(roleId);
        assertNull(result);
    }

    @Test
    void testSaveRole() {
        when(roleMapper.toRoleEntity(roleDto)).thenReturn(roleEntity);
        when(roleRepository.save(roleEntity)).thenReturn(roleEntity);
        when(roleMapper.toRoleDto(roleEntity)).thenReturn(roleDto);

        RoleDto result = roleService.saveRole(roleDto);

        verify(roleRepository).save(roleEntity);
        verify(roleMapper).toRoleEntity(roleDto);
        verify(roleMapper).toRoleDto(roleEntity);

        assertNotNull(result);
        assertEquals(roleDto.getName(), result.getName());
    }

    @Test
    void testUpdateRole_Success() {
        when(roleRepository.existsById(roleId)).thenReturn(true);
        when(roleMapper.toRoleEntity(roleDto)).thenReturn(roleEntity);
        when(roleRepository.save(roleEntity)).thenReturn(roleEntity);
        when(roleMapper.toRoleDto(roleEntity)).thenReturn(roleDto);

        RoleDto result = roleService.updateRole(roleId, roleDto);

        verify(roleRepository).existsById(roleId);
        verify(roleRepository).save(roleEntity);
        verify(roleMapper).toRoleEntity(roleDto);
        verify(roleMapper).toRoleDto(roleEntity);

        assertNotNull(result);
        assertEquals(roleDto.getName(), result.getName());
    }

    @Test
    void testUpdateRole_NotFound() {
        when(roleRepository.existsById(roleId)).thenReturn(false);

        RoleDto result = roleService.updateRole(roleId, roleDto);

        verify(roleRepository).existsById(roleId);
        assertNull(result);
    }

    @Test
    void testDeleteRole_Success() {
        when(roleRepository.existsById(roleId)).thenReturn(true);

        boolean result = roleService.deleteRole(roleId);

        verify(roleRepository).existsById(roleId);
        verify(roleRepository).deleteById(roleId);

        assertTrue(result);
    }

    @Test
    void testDeleteRole_NotFound() {
        when(roleRepository.existsById(roleId)).thenReturn(false);

        boolean result = roleService.deleteRole(roleId);

        verify(roleRepository).existsById(roleId);
        assertFalse(result);
    }
}
