package oyns.billshare.party.service;

import oyns.billshare.party.dto.FullPartyDto;
import oyns.billshare.party.dto.NewPartyDto;
import oyns.billshare.user.dto.UserDto;

public interface PartyService {

    FullPartyDto saveParty(NewPartyDto newPartyDto);

    void saveNewUserToParty(UserDto userDto, String partyId);

    FullPartyDto getPartyById(String partyId);

    void deleteUserFromParty(String userId, String partyId);

    void deleteItemFromParty(String itemId, String partyId);
}
