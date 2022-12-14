package oyns.billshare.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import oyns.billshare.party.dto.FullPartyDto;
import oyns.billshare.party.dto.NewPartyDto;
import oyns.billshare.party.service.PartyServiceImpl;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PartyController {

    private final PartyServiceImpl partyService;

    @PostMapping(value = "/party")
    public FullPartyDto saveParty(@RequestBody @Valid NewPartyDto partyDto) {
        return partyService.saveParty(partyDto);
    }

    @GetMapping("/party/{id}")
    public FullPartyDto getPartyById(@PathVariable(value = "id") String partyId) {
        return partyService.getPartyById(partyId);
    }
}