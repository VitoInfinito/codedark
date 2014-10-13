package cd;


import core.Forum;
import core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 *
 * @author 
 */
@Path("forum") // Leading trailing slash doesn't matter, see web.xml
public class ForumResource {
      
    private final IForum forum = Forum.newInstance();
    
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAll() {
        Collection<Group> groups = new ArrayList<>();
        for(Group g : forum.getGroupList().findAll()){
            groups.add(g);
        }
        
        GenericEntity<Collection<ProductWrapper>> ge = new GenericEntity<Collection<ProductWrapper>>(pws) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response find( @PathParam(value = "id") Long id) {
        log.log(Level.INFO, "Find");
        ProductWrapper p = new ProductWrapper(shop.getProductCatalogue().find(id));
        if (p != null) {
            log.log(Level.INFO, "Ok FIND");
            log.log(Level.INFO, p.getName());
            return Response.ok(p).build();
        } else {
            log.log(Level.INFO, "No content FIND");
            return Response.noContent().build();
        }
    }

    @GET
    @Path(value = "count")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response count() {
        log.log(Level.INFO, "Count");
        int c = shop.getProductCatalogue().count();
        
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }

    @DELETE
    @Path(value = "{id}")
    public Response delete(@PathParam(value = "id") final Long id) {
        log.log(Level.INFO, "Delete" + id);
        try {
            shop.getProductCatalogue().delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path(value = "{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response update(@PathParam(value = "id") Long id, JsonObject j) {
        log.log(Level.INFO, "Update");
        try {
            String name = j.getString("name");
            int price = j.getInt("price");
            ProductWrapper old = new ProductWrapper(shop.getProductCatalogue().find(id));
            shop.getProductCatalogue().update(new Product(id, name, price));
            // Convert old to HTTP response
            return Response.ok(old).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
 
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response create(JsonObject j) {
        log.log(Level.INFO, "Create");
        Product p = new Product(j.getString("name"), j.getInt("price"));
        try {  
            shop.getProductCatalogue().create(p);
            log.log(Level.INFO, p.getId().toString());
            URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(p.getId())).build(p);
            return Response.created(uri).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GET
    @Path(value = "range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findRange(@QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
        log.log(Level.INFO, "findRange");
        Collection<ProductWrapper> pws = new ArrayList<>();
        for(Product item : shop.getProductCatalogue().findRange(fst, count)){
            pws.add(new ProductWrapper(item));
        }
        
        GenericEntity<Collection<ProductWrapper>> ge = new GenericEntity<Collection<ProductWrapper>>(pws) {
        };
        return Response.ok(ge).build();
    }

}

    
