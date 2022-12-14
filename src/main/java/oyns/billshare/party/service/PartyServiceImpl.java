package oyns.billshare.party.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oyns.billshare.exception.EntityNotFoundException;
import oyns.billshare.exception.ValidationException;
import oyns.billshare.item.model.Item;
import oyns.billshare.item.repository.ItemRepository;
import oyns.billshare.party.dto.FullPartyDto;
import oyns.billshare.party.dto.NewPartyDto;
import oyns.billshare.party.mapper.PartyMapper;
import oyns.billshare.party.model.Party;
import oyns.billshare.party.repository.PartyRepository;
import oyns.billshare.user.dto.UserDto;
import oyns.billshare.user.model.User;
import oyns.billshare.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

import static oyns.billshare.party.mapper.PartyMapper.*;
import static oyns.billshare.user.mapper.UserMapper.toUser;

@Service
@Slf4j
@AllArgsConstructor
public class PartyServiceImpl implements PartyService {
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public FullPartyDto saveParty(NewPartyDto newPartyDto) {
        log.info("Save party {}", newPartyDto);
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
    public void addUserToParty(UserDto userDto, String partyId) {
        log.info("Save user {} to party {}", userDto, partyId);
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
        log.info("Get party {}", partyId);
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пати с таким id"));
        User user = userRepository.findById(party.getInitiator())
                .orElseThrow(() -> new EntityNotFoundException("Нет инициатора с таким id"));
        FullPartyDto fullPartyDto = toFullPartyDto(party,
                new User(user.getId(), user.getName()));
        Set<FullPartyDto.Item> items = fullPartyDto.getItems();
        for (FullPartyDto.Item item : items) {
            item.getUsers().forEach(fullUserDto -> fullUserDto.setValue(userRepository
                    .findAmountOfItemsForUser(item.getId(), fullUserDto.getId())));
        }
        fullPartyDto.setItems(items.stream()
                .sorted(Comparator.comparing(FullPartyDto.Item::getCreatedOn))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        return fullPartyDto;
    }

    @Override
    public void removeUserFromParty(String userId, String partyId) {
        log.info("Delete user {} from party {}", userId, partyId);
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пати с таким id"));
        Set<User> users = party.getUsers();
        users.removeIf(user -> user.getId().equals(UUID.fromString(userId)));
        party.setUsers(users);
        partyRepository.save(party);
    }

    @Override
    public void removeItemFromParty(String itemId, String partyId) {
        log.info("Delete item {} from party {}", itemId, partyId);
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пати с таким id"));
        Set<Item> items = party.getItems();
        items.removeIf(item -> item.getId().equals(UUID.fromString(itemId)));
        party.setItems(items);
        partyRepository.save(party);
    }

    @Override
    public FullPartyDto addUserToItem(String userId, String partyId, String itemId, Integer value) {
        log.info("Add user {} with value {} to item {} in party {}", userId, value, itemId, partyId);
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пати с таким id"));
        Item item = itemRepository.findById(UUID.fromString(itemId))
                .orElseThrow(() -> new EntityNotFoundException("Нет вещи с таким id."));
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("Нет инициатора с таким id"));
        User owner = userRepository.findById(party.getInitiator())
                .orElseThrow(() -> new EntityNotFoundException("Нет инициатора с таким id"));
        Set<User> users = item.getUsers();
        Set<Item> items = party.getItems();
        users.add(user);
        item.setUsers(users);
        itemRepository.save(item);
        return buildFullPartyDto(itemId, party, item, owner, items);
    }

    @Override
    public FullPartyDto removeUserFromItem(String userId, String partyId, String itemId) {
        log.info("Add user {} with to item {} in party {}", userId, itemId, partyId);
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пати с таким id"));
        Item item = itemRepository.findById(UUID.fromString(itemId))
                .orElseThrow(() -> new EntityNotFoundException("Нет вещи с таким id."));
        User owner = userRepository.findById(party.getInitiator())
                .orElseThrow(() -> new EntityNotFoundException("Нет инициатора с таким id"));
        Set<User> users = item.getUsers();
        Set<Item> items = party.getItems();
        users.removeIf(user -> user.getId().equals(UUID.fromString(userId)));
        item.setUsers(users);
        itemRepository.save(item);
        return buildFullPartyDto(itemId, party, item, owner, items);
    }

    @Override
    public FullPartyDto updateItemInParty(String userId, String partyId, String itemId,
                                          Double price, Integer amount, Double discount,
                                          String name, Boolean equally) {
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пати с таким id"));
        Item item = itemRepository.findById(UUID.fromString(itemId))
                .orElseThrow(() -> new EntityNotFoundException("Нет вещи с таким id."));
        User owner = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("Нет инициатора с таким id"));
        updateItemFieldsIfPresent(price, amount, discount, name, equally, item);
        itemRepository.save(item);
        Set<Item> items = party.getItems();
        items.removeIf(item1 -> item1.getId().equals(UUID.fromString(itemId)));
        items.add(item);
        party.setItems(items);
        partyRepository.save(party);
        return buildFullPartyDto(itemId, party, item, owner, items);
    }

    private FullPartyDto buildFullPartyDto(String itemId, Party party, Item item, User owner, Set<Item> items) {
        items.removeIf(item1 -> item1.getId().equals(UUID.fromString(itemId)));
        items.add(item);
        party.setItems(items);
        partyRepository.save(party);
        return FullPartyDto.builder()
                .id(party.getId())
                .name(party.getName())
                .owner(FullPartyDto.ShortUserDto.builder()
                        .name(owner.getName())
                        .id(owner.getId())
                        .build())
                .type("add user to item")
                .items(party.getItems().stream()
                        .map(PartyMapper::toItemOfFullPartyDto)
                        .collect(Collectors.toSet()))
                .users(party.getUsers().stream()
                        .map(PartyMapper::toShortUserOfFullPartyDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    private void updateItemFieldsIfPresent(Double price, Integer amount, Double discount,
                                           String name, Boolean equally, Item item) {
        Optional.ofNullable(price).ifPresent(item::setPrice);
        Optional.ofNullable(amount).ifPresent(item::setAmount);
        Optional.ofNullable(discount).ifPresent(item::setDiscount);
        Optional.ofNullable(name).ifPresent(item::setName);
        Optional.ofNullable(equally).ifPresent(item::setEqually);
    }
}
