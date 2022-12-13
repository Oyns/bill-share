package oyns.billshare.item.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oyns.billshare.exception.EntityNotFoundException;
import oyns.billshare.item.dto.ItemDto;
import oyns.billshare.item.model.Item;
import oyns.billshare.item.repository.ItemRepository;
import oyns.billshare.party.model.Party;
import oyns.billshare.party.repository.PartyRepository;
import oyns.billshare.user.model.User;
import oyns.billshare.user.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static oyns.billshare.item.mapper.ItemMapper.toItem;
import static oyns.billshare.item.mapper.ItemMapper.toItemDto;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto saveItem(ItemDto itemDto, String partyId, String userId) {
        log.info("Save item {}, partyId {}, userId {}", itemDto, partyId, userId);
        itemDto.setEqually(true);
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пользователя с таким id."));
        itemDto.setUsers(Set.of(ItemDto.User.builder()
                .id(user.getId())
                .name(user.getName())
                .build()));
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Пати с таким id не существует."));
        Set<Item> items = party.getItems();
        Item item = itemRepository.save(toItem(itemDto));
        items.add(item);
        party.setItems(items);
        partyRepository.save(party);
        return toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, String userId, String partyId, String itemId) {
        log.info("Update item {}, user {}, party {}", itemId, userId, partyId);
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пользователя с таким id."));
        Item item = itemRepository.findById(UUID.fromString(itemId))
                .orElseThrow(() -> new EntityNotFoundException("Нет вещи с таким id."));
        updateItemData(itemDto, item);
        Set<User> users = item.getUsers();
        users.add(user);
        item.setUsers(users);
        itemRepository.save(item);
        Party party = partyRepository.findById(UUID.fromString(partyId))
                .orElseThrow(() -> new EntityNotFoundException("Нет пати с таким id."));
        Set<Item> items = party.getItems();
        items.removeIf(item1 -> item1.getId().equals(UUID.fromString(itemId)));
        items.add(item);
        party.setItems(items);
        partyRepository.save(party);
        return toItemDto(item);
    }

    private void updateItemData(ItemDto itemDto, Item item) {
        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getPrice()).ifPresent(item::setPrice);
        Optional.ofNullable(itemDto.getAmount()).ifPresent(item::setAmount);
        Optional.ofNullable(itemDto.getDiscount()).ifPresent(item::setDiscount);
        Optional.ofNullable(itemDto.getEqually()).ifPresent(item::setEqually);
    }
}
