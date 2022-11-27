package oyns.billshare.party.service;

import org.springframework.stereotype.Service;
import oyns.billshare.party.repository.PartyRepository;

@Service
public class PartyServiceImpl implements PartyService{
    private final PartyRepository partyRepository;

    public PartyServiceImpl(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }
}
