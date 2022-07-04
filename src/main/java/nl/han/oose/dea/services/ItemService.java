package nl.han.oose.dea.services;

import nl.han.oose.dea.services.dto.ItemDTO;
import nl.han.oose.dea.services.exceptions.IdAlreadyInUseException;
import nl.han.oose.dea.services.exceptions.ItemNotAvailableException;

import java.util.List;

public interface ItemService {

    /**
     * Return the full {@link List} of {@link ItemDTO} instances.
     *
     * @return The full {@link List} of {@link ItemDTO} instances.
     */
    List<ItemDTO> getAll();

    /**
     * Add an item to the {@link List} of items.
     * <p>
     * Note that the newly added item should have an unique Id.
     *
     * @param itemDTO The {@link ItemDTO} to be added
     * @throws IdAlreadyInUseException Thrown if the Id is not unique
     */
    void addItem(ItemDTO itemDTO);

    /**
     * Return a specific {@link ItemDTO} with the given Id.
     *
     * @param id The Id of the {@link ItemDTO} to be returned
     * @throws ItemNotAvailableException Thrown if there is no {@link ItemDTO} for the given Id
     */
    ItemDTO getItem(int id);

    /**
     * Delete a specific {@link ItemDTO} with the given Id.
     *
     * @throws ItemNotAvailableException Thrown if there is no {@link ItemDTO} for the given Id
     */
    void deleteItem(int id);
}
