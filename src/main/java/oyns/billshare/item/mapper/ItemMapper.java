package oyns.billshare.item.mapper;

import org.springframework.stereotype.Component;
import oyns.billshare.item.dto.ItemDto;
import oyns.billshare.item.dto.ItemWithUsersDto;
import oyns.billshare.item.model.Item;

import java.util.Set;

@Component
public class ItemMapper {
    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .price(itemDto.getPrice())
                .amount(itemDto.getAmount())
                .isEqually(itemDto.getIsEqually())
                .discount(itemDto.getDiscount())
                .build();
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .amount(item.getAmount())
                .isEqually(item.getIsEqually())
                .discount(item.getDiscount())
                .build();
    }

    public static ItemWithUsersDto toItemWithUsersDto(Item item, Set<ItemWithUsersDto.User> users) {
        return ItemWithUsersDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .amount(item.getAmount())
                .isEqually(item.getIsEqually())
                .discount(item.getDiscount())
                .users(users)
                .build();
    }
}
