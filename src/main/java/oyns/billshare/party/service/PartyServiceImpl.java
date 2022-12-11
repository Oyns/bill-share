package oyns.billshare.party.service;

import org.springframework.stereotype.Service;
import oyns.billshare.exception.EntityNotFoundException;
import oyns.billshare.exception.ValidationException;
import oyns.billshare.item.model.Item;
import oyns.billshare.party.dto.FullPartyDto;
import oyns.billshare.party.dto.NewPartyDto;
import oyns.billshare.party.model.Party;
import oyns.billshare.party.repository.PartyRepository;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.model.User;
import oyns.billshare.user.repository.UserRepository;

import java.util.Set;
import java.util.UUID;

import static oyns.billshare.party.mapper.PartyMapper.toFullPartyDto;
import static oyns.billshare.party.mapper.PartyMapper.toPartyFromFullPartyDto;
import static oyns.billshare.user.mapper.UserMapper.toUser;
import static oyns.billshare.user.mapper.UserMapper.toUserDto;

@Service
public class PartyServiceImpl implements PartyService {
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;

    public PartyServiceImpl(PartyRepository partyRepository, UserRepository userRepository) {
        this.partyRepository = partyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FullPartyDto saveParty(NewPartyDto newPartyDto) {
        User user = userRepository.save(User.builder()
                .name(newPartyDto.getUserName())
                .build());
        FullPartyDto partyDto = FullPartyDto.builder()
                .name(newPartyDto.getPartyName())
                .owner(FullPartyDto.ShortUserDto.builder()
                        .id(user.getId())
                        .name(newPartyDto.getUserName())
                        .build())
                .users(Set.of(FullPartyDto.ShortUserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build()))
                .build();
        return toFullPartyDto(partyRepository
                .save(toPartyFromFullPartyDto(partyDto)), user);
    }

    @Override
    public void saveNewUserToParty(UserDto userDto, String partyId) {
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new ValidationException("Нет пати с таким id."));
        User user = userRepository.save(toUser(userDto));
        Set<User> users = party.getUsers();
        users.add(user);
        party.setUsers(users);
        partyRepository.save(party);
    }

    @Override
    public FullPartyDto getPartyById(String partyId) {
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пати с таким id"));
        UserDto userDto = toUserDto(userRepository.findById(party.getInitiator())
                .orElseThrow(() -> new EntityNotFoundException("Нет инициатора с таким id")));
        FullPartyDto fullPartyDto = toFullPartyDto(party,
                new User(userDto.getId(), userDto.getName()));

        Set<FullPartyDto.Item> items = fullPartyDto.getItems();
        for (FullPartyDto.Item item : items) {
            item.getUsers().forEach(fullUserDto -> fullUserDto.setValue(userRepository
                    .findAmountOfItemsForUser(item.getId(), fullUserDto.getId())));
        }
        fullPartyDto.setItems(items);
        return fullPartyDto;
    }

    @Override
    public void deleteUserFromParty(String userId, String partyId) {
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пати с таким id"));
        Set<User> users = party.getUsers();
        users.removeIf(user -> user.getId().equals(UUID.fromString(userId)));
        party.setUsers(users);
        partyRepository.save(party);
    }

    @Override
    public void deleteItemFromParty(String itemId, String partyId) {
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пати с таким id"));
        Set<Item> items = party.getItems();
        items.removeIf(item -> item.getId().equals(UUID.fromString(itemId)));
        party.setItems(items);
        partyRepository.save(party);
    }
}
