package se.iths.java24.speeed;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@Setter
@Getter
@Table("users")
public class User {

    @Id
    private Long id;
    private String name;
    private String ip;
    private String token;
    private long count;
    @Version
    Integer version;

    // Constructors, getters, setters
}
