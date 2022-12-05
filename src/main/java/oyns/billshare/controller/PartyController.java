package oyns.billshare.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
    public PartyCreationDto saveParty(@RequestBody UserDto userDto) {
        log.info("Save party. user={}", userDto);
        return partyService.saveParty(userDto);
    }

    @MessageMapping // <-- не понимаю какой путь здесь нужно прописать
    @CrossOrigin(origins = "*")
    public void receiveMessage(String userDto) {
        log.info("It's ALIVE. user={}", userDto);
    }

    @GetMapping("/party/{id}")
    @CrossOrigin(origins = "*")
    public PartyCreationDto getPartyById(@PathVariable(value = "id") String partyId) {
        return partyService.getPartyById(partyId);
    }
}