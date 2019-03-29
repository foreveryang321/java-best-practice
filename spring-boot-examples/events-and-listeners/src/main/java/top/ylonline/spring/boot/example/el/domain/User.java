package top.ylonline.spring.boot.example.el.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YL
 */
@Data
@Builder
// @NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private int age;
}
