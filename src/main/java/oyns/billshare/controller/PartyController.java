package oyns.billshare.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import oyns.billshare.party.dto.FullPartyDto;
import oyns.billshare.party.dto.NewPartyDto;
import oyns.billshare.party.service.PartyServiceImpl;
import oyns.billshare.webconfig.BinaryEventLauncher;

import java.net.URISyntaxException;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PartyController {

    private final PartyServiceImpl partyService;
    private final BinaryEventLauncher binaryEventLauncher;

    @PostMapping(value = "/party")
    @CrossOrigin(origins = "*")
    public FullPartyDto saveParty(@RequestBody @Valid NewPartyDto partyDto) {
        return partyService.saveParty(partyDto);
    }

    @GetMapping("/party/{id}")
    @CrossOrigin(origins = "*")
    public FullPartyDto getPartyById(@PathVariable(value = "id") String partyId) {
        return partyService.getPartyById(partyId);
    }

    @GetMapping("/socket.io/")
    @CrossOrigin(origins = "*")
    public void test() throws URISyntaxException {
        binaryEventLauncher.setupSocket();
    }
}