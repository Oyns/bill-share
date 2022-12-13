package oyns.billshare.party.service;

import oyns.billshare.party.dto.FullPartyDto;
import oyns.billshare.party.dto.NewPartyDto;
import oyns.billshare.user.dto.UserDto;

public interface PartyService {

    FullPartyDto saveParty(NewPartyDto newPartyDto);

    void addUserToParty(UserDto userDto, String partyId);

    FullPartyDto getPartyById(String partyId);

    void removeUserFromParty(String userId, String partyId);

    void removeItemFromParty(String itemId, String partyId);

    FullPartyDto addUserToItem(String userId, String partyId, String itemId, Integer value);

    FullPartyDto removeUserFromItem(String userId, String partyId, String itemId);

    FullPartyDto updateItemInParty(String userId, String partyId, String itemId,
                                   Double price, Integer amount, Double discount);
}
