package ee.taltech.iti0302project.repository;

import ee.taltech.iti0302project.entity.ChatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    Page<ChatEntity> findByCreatorUsername(String username, Pageable pageable);
}

