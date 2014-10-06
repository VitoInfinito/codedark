package cd;

import java.util.Collection;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 *
 * @author 
 */
@Path("products") // Leading trailing slash doesn't matter, see web.xml
public class ForumResource {
      
//    private final IForum forum = SingletonForum.INSTANCE.getForum();
    
//    FROM WS2
//    private final IShop shop = SingletonShop.INSTANCE.getShop();
//    private final IProductCatalogue products = shop.getProductCatalogue();
//    
//    @POST
//    @Consumes(value = MediaType.APPLICATION_JSON)
//    @Produces(value = {MediaType.APPLICATION_JSON})
//    public Response create(JsonObject json) {
//        
//        String name = json.getString("name");
//        double price = (double) json.getInt("price");
//         
//        try{
//            Product p = new Product(name, price);    
//            products.create(p);
//            ProductWrapper pw = new ProductWrapper(p);
//            
//            return Response.ok(pw).build();
//        } catch (IllegalArgumentException e) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @DELETE
//    @Path(value = "{id}")
//    public Response delete(@PathParam(value = "id") final Long id) {
//        
//        try {
//            products.delete(id);
//            return Response.ok().build();
//        } catch (IllegalArgumentException e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @PUT
//    @Path(value = "{id}")
//    @Consumes(value = MediaType.APPLICATION_JSON)
//    @Produces(value = {MediaType.APPLICATION_JSON})
//    public Response update(@PathParam(value = "id") final Long id, JsonObject json) {
//        
//        String name = json.getString("name");
//        double price = (double) json.getInt("price");
//        Product p = new Product(id, name, price);
//        products.update(p);
//        
//        ProductWrapper pw = new ProductWrapper(p);
//        return Response.ok(pw).build();
//        
//    }
//
//    @GET
//    @Produces(value = {MediaType.APPLICATION_JSON})
//    @Path(value = "{id}")
//    public Response find(@PathParam(value = "id") Long id) {
//        
//        Product p = products.find(id);
//        ProductWrapper pw = new ProductWrapper(p); 
//        if (pw != null) {
//            return Response.ok(pw).build();
//        } else {
//            return Response.noContent().build();
//        }
//    }
//
//    @GET
//    @Produces(value = {MediaType.APPLICATION_JSON})
//    public Response findAll() {
//   
//        List<Product> old = products.findAll();
//        
//        ProductWrapper pw;
//        List<ProductWrapper> newList = new ArrayList<>();
//        for(Product p: old){
//            pw = new ProductWrapper(p);
//            newList.add(pw);
//            
//        }
//        
//        GenericEntity<List<ProductWrapper>> geAll = new GenericEntity<List<ProductWrapper>>(newList){};
//        return Response.ok(geAll).build();
//    }
//
//    @GET
//    @Path(value = "range")
//    @Produces(value = {MediaType.APPLICATION_JSON})
//    public Response findRange(@QueryParam(value = "first") int first, @QueryParam(value = "n") int n) {
//       
//        List<Product> range = products.findRange(first, n);
//        
//        ProductWrapper pw;
//        List<ProductWrapper> newList = new ArrayList<>();
//        for(Product p: range){
//            pw = new ProductWrapper(p);
//            newList.add(pw);
//        }
//        
//        GenericEntity<List<ProductWrapper>> geRange = new GenericEntity<List<ProductWrapper>>(newList) {};
//        return Response.ok(geRange).build();
//    }
//
//    @GET
//    @Path(value = "count")
//    @Produces(value = {MediaType.APPLICATION_JSON})
//    public Response count() {
//        
//        int c = shop.getProductCatalogue().count();
//        JsonObject value = Json.createObjectBuilder().add("value", c).build();
//        return Response.ok(value).build();
//    }
    
    //METHODS
    //Examples from WS2
    /*
    public Response create(T t){
        return null;
    }

    public Response delete(K id){
        return null;
    }

    public Response update(T t){
        return null;
    }

    public Response find(K id){
        return null;
    }
    
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAll() {
   
        Collection<Product> ps = shop.getProductCatalogue().findAll();
        
        
        GenericEntity<Collection<Product>> ge = new GenericEntity<Collection<Product>>(ps) {
        };

        return Response.ok(ge).build();
    }

    public Response findRange(int first, int n ){
        return null;
    }

    public Response count(){
        return null;
    }*/
}

    
