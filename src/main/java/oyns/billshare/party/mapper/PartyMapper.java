package oyns.billshare.party.mapper;

import org.springframework.stereotype.Component;
import oyns.billshare.party.dto.PartyDto;
import oyns.billshare.party.model.Party;

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
}
