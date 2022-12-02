package oyns.billshare.party.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Builder
public class PartyCreationDto {
    UUID id;
    @NotBlank
    String name;
    User owner;
    List<User> users;
    List<Item> items;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @EqualsAndHashCode(of = "id")
    @Builder
    public static class User {
        UUID id;
        String userName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @EqualsAndHashCode(of = "id")
    @Builder
    public static class Item {
        UUID id;
        @NotBlank
        String name;
        Double price;
        Integer amount;
        Boolean isEqually;
        Double discount;
    }
}
