package oyns.billshare.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import oyns.billshare.party.dto.PartyCreationDto;
import oyns.billshare.party.service.PartyServiceImpl;
import oyns.billshare.user.dto.UserDto;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PartyController {

    private final PartyServiceImpl partyService;

    @PostMapping(value = "/party")
    @CrossOrigin(origins = "*")
    public PartyCreationDto saveParty(@RequestBody @Valid UserDto userDto) {
        log.info("Save party. user={}", userDto);
        return partyService.saveParty(userDto);
    }

    @GetMapping("/party/{id}")
    @CrossOrigin(origins = "*")
    public PartyCreationDto getPartyById(@PathVariable(value = "id") String partyId) {
        return partyService.getPartyById(partyId);
    }
}