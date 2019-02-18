package top.ylonline.spring.webflux.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.ylonline.spring.webflux.example.entity.User;

/**
 * @author YL
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
