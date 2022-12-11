package oyns.billshare.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import oyns.billshare.party.dto.FullPartyDto;
import oyns.billshare.party.dto.NewPartyDto;
import oyns.billshare.party.service.PartyServiceImpl;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PartyController {

    private final PartyServiceImpl partyService;

    @PostMapping(value = "/party")
    @CrossOrigin(origins = "*")
    public FullPartyDto saveParty(@RequestBody @Valid NewPartyDto partyDto) {
        log.info("Save party. user={}", partyDto);
        return partyService.saveParty(partyDto);
    }

    @GetMapping("/party/{id}")
    @CrossOrigin(origins = "*")
    public FullPartyDto getPartyById(@PathVariable(value = "id") String partyId) {
        return partyService.getPartyById(partyId);
    }
}