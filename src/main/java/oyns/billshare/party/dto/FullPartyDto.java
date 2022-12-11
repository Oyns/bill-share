package oyns.billshare.party.dto;

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
public class FullPartyDto {
    UUID id;
    @NotBlank
    String name;
    ShortUserDto owner;
    Set<ShortUserDto> users;
    Set<Item> items;
    String type; // <---- event type

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @EqualsAndHashCode(of = "id")
    @Builder
    public static class FullUserDto {
        UUID id;
        @NotBlank
        String name;
        Integer value;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @EqualsAndHashCode(of = "id")
    @Builder
    public static class ShortUserDto {
        UUID id;
        @NotBlank
        String name;
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
        UUID user;
        Set<FullUserDto> users;
    }
}
