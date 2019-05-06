package top.ylonline.sb.webflux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.ylonline.sb.webflux.entity.User;

/**
 * @author YL
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
