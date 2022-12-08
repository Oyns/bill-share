package oyns.billshare.party.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import oyns.billshare.item.model.Item;
import oyns.billshare.user.model.User;

import java.util.Set;
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
    Set<User> users;
    Set<Item> items;
    String type; // <---- event type
}
