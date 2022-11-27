package oyns.billshare.user.service;

import org.springframework.stereotype.Service;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.repository.UserRepository;

import static oyns.billshare.user.mapper.UserMapper.toUser;
import static oyns.billshare.user.mapper.UserMapper.toUserDto;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        return toUserDto(userRepository.save(toUser(userDto)));
    }
}
