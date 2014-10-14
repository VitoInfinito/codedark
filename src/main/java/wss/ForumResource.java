package wss;


import wss.core.Course;
import wss.core.CourseGroup;
import wss.core.Forum;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
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
import wss.core.GroupUser;

/**
 * REST Web Service
 *
 *
 * @author 
 */
@Path("forum") // Leading trailing slash doesn't matter, see web.xml
public class ForumResource {
      
   // private final IForum forum = Forum.newInstance();
    @Inject Forum forum;
            
    @Context
    private UriInfo uriInfo;
    
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAll() {
        Collection<CourseGroup> groups = new ArrayList<>();
        for (CourseGroup g : forum.getGroupList().findAll()) {
            groups.add(g);
        }
        
        GenericEntity<Collection<CourseGroup>> ge = new GenericEntity<Collection<CourseGroup>>(groups) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "{id}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response find( @PathParam(value = "id") Long id) {
        CourseGroup cg = forum.getGroupList().find(id);
        if (cg != null) {
            return Response.ok(cg).build();
        } else {
            return Response.noContent().build();
        }
    }

    @GET
    @Path(value = "count")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response count() {
        int c = forum.getGroupList().count();
        
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }

    @DELETE
    @Path(value = "{id}")
    public Response delete(@PathParam(value = "id") final Long id) {
        try {
            forum.getGroupList().delete(id);
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
        try {
            String name = j.getString("gName");
            Course c = forum.getGroupList().find(id).getCourse();
            CourseGroup cg = new CourseGroup(id, c, name);
            forum.getGroupList().update(cg);
            // Convert old to HTTP response
            return Response.ok(cg).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
 
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response create(JsonObject j) {
        switch(j.getString("type")){
            case "group": 
                return createGroup(j);
            case "course":
                return createCourse(j);
            case "user":
                return createUser(j);
            default:
                return null;
        }
    }
    
    private Response createGroup(JsonObject j){
        Course c = forum.getCourseList().getByCC(j.getString("course"));
        CourseGroup cg = new CourseGroup(c, j.getString("name"));
        List<GroupUser> gU = new ArrayList<>();
        
        Iterator it = j.getJsonArray("members").iterator();
        while(it.hasNext()){
            gU.add((GroupUser) it.next());
        }
        try {  
            forum.getGroupList().create(cg);
            URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(cg.getId())).build(cg);
            return Response.created(uri).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private Response createCourse(JsonObject j){
        Course c = new Course(j.getString("cc"), j.getString("name"));
        try{
            forum.getCourseList().create(c);
        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(c.getId())).build(c);
            return Response.created(uri).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private Response createUser(JsonObject j){
        GroupUser gu = new GroupUser((long) j.getInt("ssnbr"), j.getString("email"), j.getString("password"));
        try{
            forum.getUserList().create(gu);  
            URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(gu.getId())).build(gu);
            return Response.created(uri).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GET
    @Path(value = "range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findRange(@QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
        Collection<CourseGroup> groups = new ArrayList<>();
        for(CourseGroup g : forum.getGroupList().findRange(fst, count)){
            groups.add(g);
        }
        
        GenericEntity<Collection<CourseGroup>> ge = new GenericEntity<Collection<CourseGroup>>(groups) {
        };
        return Response.ok(ge).build();
    }

}

    
