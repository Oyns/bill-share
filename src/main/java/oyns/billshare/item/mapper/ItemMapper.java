package oyns.billshare.item.mapper;

import org.springframework.stereotype.Component;
import oyns.billshare.item.dto.ItemDto;
import oyns.billshare.item.model.Item;
import oyns.billshare.user.model.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public static Item toItem(ItemDto itemDto) {
        Set<User> users = new HashSet<>();
        if (itemDto.getUsers() != null) {
            users = itemDto.getUsers().stream()
                    .map(ItemMapper::toItemDtoUser)
                    .collect(Collectors.toSet());
        }
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .price(itemDto.getPrice())
                .amount(itemDto.getAmount())
                .equally(itemDto.getEqually())
                .discount(itemDto.getDiscount())
                .user(itemDto.getUser())
                .users(users)
                .build();
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .amount(item.getAmount())
                .equally(item.getEqually())
                .discount(item.getDiscount())
                .user(item.getUser())
                .build();
    }

    public static User toItemDtoUser(ItemDto.User user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
