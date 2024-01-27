package ec.service;

import ec.model.Role;
import ec.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ec.repository.RoleRepository;
import ec.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserAndRoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;



    @Transactional( rollbackFor = Exception.class )
    public void saveRole(Role role)
    {
        this.roleRepository.save(role);
    }

    @Transactional( rollbackFor = Exception.class )
    public User saveUser(User user)
    {
        return this.userRepository.save(user);
    }

    @Transactional( rollbackFor = Exception.class )
    public List<User> saveUsers(List<User> users)
    {
        return this.userRepository.saveAll(users);
    }

    public Optional<User> getUser(String username)
    {
        return this.userRepository.findByUsername(username);
    }

    @Transactional( rollbackFor = Exception.class )
    public int updatePassword(String username, String password)
    {
        return userRepository.updatePassword(username, password);
    }

}
