package top.ylonline.sb.redis.v2.test.domain;

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
    private static final long serialVersionUID = 858347007112924676L;
    
    private Long id;
    private String firstName;
    private String lastName;
}
