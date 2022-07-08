package second.education.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import second.education.domain.classificator.Role;
import second.education.model.response.ResponseMessage;
import second.education.model.response.RoleResponse;
import second.education.repository.RoleRepository;
import second.education.security.SecurityConstant;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role create() {
        Optional<Role> roleOptional = roleRepository.findByName(SecurityConstant.DEFAULT_ROLE);
        if (roleOptional.isEmpty()) {
            Role role = new Role();
            role.setName(SecurityConstant.DEFAULT_ROLE);
            roleRepository.save(role);
            return role;
        }
        return roleOptional.get();
    }

    public List<RoleResponse> getAllRole() {
        return roleRepository.findAll().stream().map(RoleResponse::new).toList();
    }

    public RoleResponse getRoleById(int roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role " + ResponseMessage.NOT_FOUND.getMessage()));
        return new RoleResponse(role);
    }
}
