package oyns.billshare.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
public class UserDto {
    private UUID id;
    @NotBlank
    private String name;
}
