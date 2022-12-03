package oyns.billshare.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import oyns.billshare.party.dto.PartyCreationDto;
import oyns.billshare.party.service.PartyServiceImpl;
import oyns.billshare.user.dto.UserDto;

@RestController
@Slf4j
public class PartyController {
    private final PartyServiceImpl partyService;

    public PartyController(PartyServiceImpl partyService) {
        this.partyService = partyService;
    }

    @PostMapping(value = "/party")
    @CrossOrigin(origins = "*")
    public PartyCreationDto saveParty(@RequestBody UserDto userDto) {
        log.info("Save party. user={}", userDto);
        return partyService.saveParty(userDto);
    }
}
