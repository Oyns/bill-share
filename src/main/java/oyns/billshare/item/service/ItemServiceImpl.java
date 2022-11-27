package oyns.billshare.item.service;

import org.springframework.stereotype.Service;
import oyns.billshare.item.repository.ItemRepository;

@Service
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
}
