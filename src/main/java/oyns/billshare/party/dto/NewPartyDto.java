package oyns.billshare.party.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Builder
public class NewPartyDto {
    UUID id;
    @NotBlank
    String name;
    @NotBlank
    String partyName;
}
