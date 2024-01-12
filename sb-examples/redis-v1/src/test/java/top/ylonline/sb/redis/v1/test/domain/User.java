package top.ylonline.sb.redis.v1.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author YL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 3776888683392400078L;
    
    private Long id;
    private String firstName;
    private String lastName;
}
