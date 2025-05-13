package se.iths.java24.speeed;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

@NoArgsConstructor
@Setter
@Getter
public class GlobalStats {

    @Id
    private Long id;
    private long totalCount;
    @Version
    Integer version;
    // Constructors, getters, setters
}
