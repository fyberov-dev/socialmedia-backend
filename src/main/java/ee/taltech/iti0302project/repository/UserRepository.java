package ee.taltech.iti0302project.repository;

import ee.taltech.iti0302project.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
