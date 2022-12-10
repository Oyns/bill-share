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

    public static FullPartyDto toFullPartyDto(Party party, NewPartyDto newPartyDto) {
        Set<FullPartyDto.User> users = new HashSet<>();
        if (party.getUsers() != null) {
            users = party.getUsers().stream()
                    .map(PartyMapper::toUserOfFullPartyDto)
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
                .owner(FullPartyDto.User.builder()
                        .id(newPartyDto.getId())
                        .name(newPartyDto.getUserName())
                        .build())
                .users(users)
                .items(items)
                .build();
    }

    public static Party toPartyFromFullPartyDto(FullPartyDto fullPartyDto) {
        return Party.builder()
                .id(fullPartyDto.getId())
                .name(fullPartyDto.getName())
                .isPaid(null)
                .initiator(fullPartyDto.getOwner().getId())
                .users(fullPartyDto.getUsers().stream()
                        .map(PartyMapper::toUserModel)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static User toUserModel(FullPartyDto.User user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static FullPartyDto.User toUserOfFullPartyDto(User user) {
        return FullPartyDto.User.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static Item toItemModel(FullPartyDto.Item item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .amount(item.getAmount())
                .isEqually(item.getIsEqually())
                .discount(item.getDiscount())
                .user(item.getUser())
                .users(item.getUsers().stream()
                        .map(PartyMapper::toUserModel)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static FullPartyDto.Item toItemOfFullPartyDto(Item item) {
        return FullPartyDto.Item.builder()
                .id(item.getId())
                .name(item.getName())
                .amount(item.getAmount())
                .isEqually(item.getIsEqually())
                .discount(item.getDiscount())
                .user(item.getUser())
                .users(item.getUsers().stream()
                        .map(PartyMapper::toUserOfFullPartyDto)
                        .collect(Collectors.toSet()))
                .build();
    }
}
