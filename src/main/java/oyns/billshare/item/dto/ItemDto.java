package oyns.billshare.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
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
    Boolean equally;
    Double discount;

    UUID user;
    Set<User> users;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @EqualsAndHashCode(of = "id")
    @Builder
    public static class User {
        UUID id;
        @NotBlank
        String name;
        Integer value;
    }
}
