package top.ylonline.spring.webflux.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.ylonline.spring.webflux.example.entity.User;

/**
 * @author Created by YL on 2019/1/2
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
