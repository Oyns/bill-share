package oyns.billshare.party.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
public class PartyDto {
    private UUID id;
    @NotBlank
    private String name;
    private Boolean isPaid;
    private UUID initiator;
}
