package oyns.billshare.party.service;

import oyns.billshare.party.dto.PartyCreationDto;
import oyns.billshare.user.dto.UserDto;

public interface PartyService {

    PartyCreationDto saveParty(UserDto userDto);

    void saveNewUserToParty(UserDto userDto, String partyId);

    PartyCreationDto getPartyById(String partyId);
}
