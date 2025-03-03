package ee.taltech.iti0302project.controller;

import ee.taltech.iti0302project.dto.role.RoleDto;
import ee.taltech.iti0302project.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Create a new role", description = "Creates a new role in the system.")
    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        RoleDto createdRole = roleService.saveRole(roleDto);
        return ResponseEntity.ok(createdRole);
    }

    @Operation(summary = "Get all roles", description = "Retrieves all roles available in the system.")
    @GetMapping
    public List<RoleDto> getRoles() {
        return roleService.findAll();
    }

    @Operation(summary = "Get role by ID", description = "Retrieves a role by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Long id) {
        RoleDto role = roleService.findById(id);
        return role == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(role);
    }

    @Operation(summary = "Update a role", description = "Updates the details of an existing role.")
    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long id, @RequestBody RoleDto updatedRoleDto) {
        RoleDto role = roleService.updateRole(id, updatedRoleDto);
        return role == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(role);
    }

    @Operation(summary = "Delete a role", description = "Deletes an existing role by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        boolean deleted = roleService.deleteRole(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
