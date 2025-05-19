package az.edu.msauth.repository;

import az.edu.msauth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<User> findByEmailContainingOrFullNameContaining(String email, String fullName, Pageable pageable);
    long countByActiveTrue();
    long countByActiveFalse();
    long countByCreatedAtAfter(LocalDateTime date);
}