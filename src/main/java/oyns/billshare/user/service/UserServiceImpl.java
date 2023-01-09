package oyns.billshare.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oyns.billshare.user.dto.NewUserDto;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.webconfig.WebSocketConfig;

import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final WebSocketConfig webSocketConfig;

    @Override
    public UserDto saveUser(NewUserDto newUserDto) throws IOException {
        log.info("Save user {}", newUserDto);
        return webSocketConfig.saveUser(newUserDto);
    }
}
