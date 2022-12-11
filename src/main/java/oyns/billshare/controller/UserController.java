package oyns.billshare.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import oyns.billshare.user.dto.NewUserDto;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.service.UserServiceImpl;

@RestController
@Slf4j
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    @CrossOrigin(origins = "*")
    public UserDto addUserToParty(@RequestBody @Valid NewUserDto newUserDto) {
        log.info("Save user={}, partyId={}", newUserDto, newUserDto.getPartyId());
        return userService.saveUser(newUserDto);
    }
}
