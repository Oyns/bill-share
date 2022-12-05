package oyns.billshare.user.service;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import oyns.billshare.party.model.Party;
import oyns.billshare.party.repository.PartyRepository;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.model.User;
import oyns.billshare.user.repository.UserRepository;

import java.util.Set;
import java.util.UUID;

import static oyns.billshare.user.mapper.UserMapper.toUser;
import static oyns.billshare.user.mapper.UserMapper.toUserDto;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;

    @Override
    public UserDto saveUser(UserDto userDto, String partyId) {
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new ValidationException("Нет пати с таким id"));
        Set<User> users = party.getUsers();
        User user = userRepository.save(toUser(userDto));
        users.add(user);
        party.setUsers(users);
        partyRepository.save(party);
        return toUserDto(user);
    }
}
