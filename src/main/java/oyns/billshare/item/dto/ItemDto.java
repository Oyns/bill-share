package oyns.billshare.item.dto;

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
public class ItemDto {
    UUID id;
    @NotBlank
    String name;
    Double price;
    Integer amount;
    Boolean isEqually;
    Double discount;

    UUID user;
}
