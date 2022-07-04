package nl.han.oose.dea.services;

import jakarta.enterprise.inject.Alternative;
import nl.han.oose.dea.services.dto.ItemDTO;
import nl.han.oose.dea.services.exceptions.IdAlreadyInUseException;
import nl.han.oose.dea.services.exceptions.ItemNotAvailableException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Alternative
public class WoodWorkingItemService implements ItemService {

    private List<ItemDTO> items = new ArrayList<>();

    public WoodWorkingItemService() {
        items.add(new ItemDTO(1, "Saw", new String[]{"Carpentry"}, "Keep it sharp"));
        items.add(new ItemDTO(2, "Chisel", new String[]{"Carpentry"}, "Keep it sharp"));
        items.add(new ItemDTO(3, "Plane", new String[]{"Carpentry"}, "Keep it sharp"));
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
