package oyns.billshare.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class NewUserDto {
    UUID id;
    UUID partyId;
    String userName;
}
