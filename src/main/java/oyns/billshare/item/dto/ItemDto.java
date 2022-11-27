package oyns.billshare.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
public class ItemDto {
    private UUID id;
    @NotBlank
    private String name;
    private Double price;
    private Integer amount;
    private Boolean isEqually;
    private Double discount;
}
