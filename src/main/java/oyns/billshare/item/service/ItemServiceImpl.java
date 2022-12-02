package oyns.billshare.item.service;

import org.springframework.stereotype.Service;
import oyns.billshare.item.dto.ItemDto;
import oyns.billshare.item.repository.ItemRepository;

import static oyns.billshare.item.mapper.ItemMapper.toItem;
import static oyns.billshare.item.mapper.ItemMapper.toItemDto;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDto saveItem(ItemDto itemDto) {
        return toItemDto(itemRepository.save(toItem(itemDto)));
    }
}
