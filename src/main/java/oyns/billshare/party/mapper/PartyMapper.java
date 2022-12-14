package oyns.billshare.party.mapper;

import org.springframework.stereotype.Component;
import oyns.billshare.item.model.Item;
import oyns.billshare.party.dto.FullPartyDto;
import oyns.billshare.party.dto.NewPartyDto;
import oyns.billshare.party.model.Party;
import oyns.billshare.user.model.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PartyMapper {
    public static Party toParty(NewPartyDto newPartyDto) {
        return Party.builder()
                .id(newPartyDto.getId())
                .name(newPartyDto.getPartyName())
                .build();
    }

    public static NewPartyDto toPartyDto(Party party) {
        return NewPartyDto.builder()
                .id(party.getId())
                .partyName(party.getName())
                .build();
    }

    public static FullPartyDto toFullPartyDto(Party party, User user) {
        Set<FullPartyDto.ShortUserDto> shortUserDtos = new HashSet<>();
        if (party.getUsers() != null) {
            shortUserDtos = party.getUsers().stream()
                    .map(PartyMapper::toShortUserOfFullPartyDto)
                    .collect(Collectors.toSet());
        }
        Set<FullPartyDto.Item> items = new HashSet<>();
        if (party.getItems() != null) {
            items = party.getItems().stream()
                    .map(PartyMapper::toItemOfFullPartyDto)
                    .collect(Collectors.toSet());
        }
        return FullPartyDto.builder()
                .id(party.getId())
                .name(party.getName())
                .users(shortUserDtos)
                .items(items)
                .owner(FullPartyDto.ShortUserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build())
                .build();
    }

    public static Party toPartyFromFullPartyDto(FullPartyDto fullPartyDto) {
        return Party.builder()
                .id(fullPartyDto.getId())
                .name(fullPartyDto.getName())
                .isPaid(null)
                .initiator(fullPartyDto.getOwner().getId())
                .users(fullPartyDto.getUsers().stream()
                        .map(PartyMapper::toUserModelFromShortUser)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static User toUserModelFromShortUser(FullPartyDto.ShortUserDto shortUserDto) {
        return User.builder()
                .id(shortUserDto.getId())
                .name(shortUserDto.getName())
                .build();
    }

    public static User toUserModelFromFullUser(FullPartyDto.UserWithItemsValueDto fullUserDto) {
        return User.builder()
                .id(fullUserDto.getId())
                .build();
    }

    public static FullPartyDto.UserWithItemsValueDto toFullUserOfFullPartyDto(User user) {
        return FullPartyDto.UserWithItemsValueDto.builder()
                .id(user.getId())
                .build();
    }

    public static FullPartyDto.ShortUserDto toShortUserOfFullPartyDto(User user) {
        return FullPartyDto.ShortUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static Item toItemModel(FullPartyDto.Item item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .amount(item.getAmount())
                .equally(item.getEqually())
                .discount(item.getDiscount())
                .users(item.getUsers().stream()
                        .map(PartyMapper::toUserModelFromFullUser)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static FullPartyDto.Item toItemOfFullPartyDto(Item item) {
        return FullPartyDto.Item.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .amount(item.getAmount())
                .equally(item.getEqually())
                .discount(item.getDiscount())
                .users(item.getUsers().stream()
                        .map(PartyMapper::toFullUserOfFullPartyDto)
                        .collect(Collectors.toSet()))
                .build();
    }
}
