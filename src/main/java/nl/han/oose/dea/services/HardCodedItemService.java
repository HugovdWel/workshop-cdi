package nl.han.oose.dea.services;

import nl.han.oose.dea.services.dto.ItemDTO;
import nl.han.oose.dea.services.exceptions.IdAlreadyInUseException;
import nl.han.oose.dea.services.exceptions.ItemNotAvailableException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The {@code ItemService} can be used for accessing a {@link List} of {@link ItemDTO} instances, but also
 * for adding instances to and deleting from the {@link List}.
 */
public class HardCodedItemService implements ItemService {

    private List<ItemDTO> items = new ArrayList<>();

    public HardCodedItemService() {
        items.add(new ItemDTO(1, "Bread", new String[]{"Breakfast, Lunch"}, "Delicious!"));
        items.add(new ItemDTO(2, "Butter", new String[]{"Breakfast, Lunch"}, "Use it with bread"));
        items.add(new ItemDTO(3, "Honey", new String[]{"Breakfast, Lunch"}, "Use it with bread"));
    }

    @Override
    public List<ItemDTO> getAll() {
        return items;
    }

    @Override
    public void addItem(ItemDTO itemDTO) {
        if (items.stream().anyMatch(item -> item.getId() == itemDTO.getId())) {
            throw new IdAlreadyInUseException();
        }

        items.add(itemDTO);
    }

    @Override
    public ItemDTO getItem(int id) {
        Optional<ItemDTO> requestedItem = items.stream().filter(item -> item.getId() == id).findFirst();

        if (requestedItem.isPresent()) {
            return requestedItem.get();
        } else {
            throw new ItemNotAvailableException();
        }
    }

    @Override
    public void deleteItem(int id) {
        List<ItemDTO> filteredItems = items.stream().filter(item -> item.getId() != id).collect(Collectors.toList());

        if (filteredItems.size() == items.size()) {
            throw new ItemNotAvailableException();
        }

        items = filteredItems;
    }
}
