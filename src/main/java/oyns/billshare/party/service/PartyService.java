package oyns.billshare.party.service;

import oyns.billshare.party.dto.PartyCreationDto;
import oyns.billshare.user.dto.UserDto;

public interface PartyService {

    PartyCreationDto saveParty(UserDto userDto);

    PartyCreationDto getPartyById(String partyId);
}
