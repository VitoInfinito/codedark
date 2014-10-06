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

    
