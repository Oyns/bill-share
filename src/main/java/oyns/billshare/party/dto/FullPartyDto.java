package oyns.billshare.party.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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
    ShortUserDto owner;
    @NotBlank
    String name;
    UUID id;
    String type;
    Set<Item> items;
    Set<ShortUserDto> users;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @EqualsAndHashCode(of = "id")
    @Builder
    public static class UserWithItemsValueDto {
        UUID id;
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
        @NotBlank
        String name;
        UUID id;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @EqualsAndHashCode(of = "id")
    @Builder
    public static class Item {
        @NotBlank
        String name;
        UUID id;
        Double price;
        Integer amount;
        Boolean equally;
        Double discount;
        LocalDateTime createdOn;
        Set<UserWithItemsValueDto> users;
    }
}
