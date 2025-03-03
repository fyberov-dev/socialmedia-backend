package ee.taltech.iti0302project.repository;

import ee.taltech.iti0302project.entity.PostEntity;
import ee.taltech.iti0302project.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findAllByUser(UserEntity userEntity, Pageable pageable);
}
