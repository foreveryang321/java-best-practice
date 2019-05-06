package top.ylonline.sc.sleuth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.ylonline.sc.sleuth.entity.User;

/**
 * @author YL
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
