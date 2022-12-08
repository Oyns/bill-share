package oyns.billshare.party.mapper;

import org.springframework.stereotype.Component;
import oyns.billshare.party.dto.PartyCreationDto;
import oyns.billshare.party.dto.PartyDto;
import oyns.billshare.party.model.Party;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.model.User;

@Component
public class PartyMapper {
    public static Party toParty(PartyDto partyDto) {
        return Party.builder()
                .id(partyDto.getId())
                .name(partyDto.getName())
                .isPaid(partyDto.getIsPaid())
                .initiator(partyDto.getInitiator())
                .build();
    }

    public static PartyDto toPartyDto(Party party) {
        return PartyDto.builder()
                .id(party.getId())
                .name(party.getName())
                .isPaid(party.getIsPaid())
                .initiator(party.getInitiator())
                .build();
    }

    public static PartyCreationDto toPartyCreationDto(Party party, UserDto userDto) {
        return PartyCreationDto.builder()
                .id(party.getId())
                .name(party.getName())
                .owner(User.builder()
                        .id(userDto.getId())
                        .name(userDto.getUserName())
                        .build())
                .users(party.getUsers())
                .items(party.getItems())
                .build();
    }

    public static Party toPartyFromCreationDto(PartyCreationDto partyCreationDto) {
        return Party.builder()
                .id(partyCreationDto.getId())
                .name(partyCreationDto.getName())
                .isPaid(null)
                .initiator(partyCreationDto.getOwner().getId())
                .build();
    }
}
