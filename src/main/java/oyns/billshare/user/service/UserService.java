package oyns.billshare.user.service;

import oyns.billshare.user.dto.NewUserDto;
import oyns.billshare.user.dto.UserDto;

import java.io.IOException;

public interface UserService {
    UserDto saveUser(NewUserDto newUserDto) throws IOException;
}
