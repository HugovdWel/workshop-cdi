package nl.han.oose.dea.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.services.ItemService;
import nl.han.oose.dea.services.dto.ItemDTO;

@Path("/items")
public class ItemResource {

    private ItemService itemService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getTextItems() {
        return "bread, butter";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJsonItems() {
        return Response.ok().entity(itemService.getAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(ItemDTO itemDTO) {
        itemService.addItem(itemDTO);

        return Response.status(201).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("id") int id) {
        return Response.ok().entity(itemService.getItem(id)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItem(@PathParam("id") int id) {
        itemService.deleteItem(id);
        return Response.ok().build();
    }

    @Inject
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }
}
