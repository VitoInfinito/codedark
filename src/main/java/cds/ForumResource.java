package cds;


import cds.core.*;
import cds.persistence.AbstractEntity;
import cds.persistence.IDAO;
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
    @Path(value = "allGroups")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAllGroups() {
        return findAll(new CourseGroup(), forum.getGroupList());
    }
    
    @GET
    @Path(value = "allUsers")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAllUsers() {
        return findAll(new GroupUser(), forum.getGroupList());
    }
    @GET
    @Path(value = "allCourses")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAllCourses() {
        return findAll(new Course(), forum.getGroupList());
    }
    
    private <T extends AbstractEntity, K extends IDAO> Response findAll(T l, K list){
        Collection<T> groups = new ArrayList<>();
        Iterator<T> it = list.findAll().iterator();
        while (it.hasNext()) {
            groups.add(it.next());
        }
        
        GenericEntity<Collection<T>> ge = new GenericEntity<Collection<T>>(groups) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "user/{id}")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response findUser(@PathParam(value= "id") Long id){
        GroupUser user = forum.getUserList().find(id);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.noContent().build();
        }
    
    }
    @GET
    @Path(value = "group/{id}")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response findGroup( @PathParam(value= "id")Long id) {
        CourseGroup cg = forum.getGroupList().find(id);
        if (cg != null) {
            return Response.ok(cg).build();
        } else {
            return Response.noContent().build();
        }
    }
    
    @GET
    @Path(value = "course/{id}")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response findCourse(@PathParam(value= "id") Long id) {
        Course c = forum.getCourseList().find(id);
        if (c != null) {
            return Response.ok(c).build();
        } else {
            return Response.noContent().build();
        }
    }    

    @GET
    @Path(value = "countGroups")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response countGroups() {
        int c = forum.getGroupList().count();
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }
    
    @GET
    @Path(value = "countCourses")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response countCourses(){
        int c = forum.getCourseList().count();
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }
    
    @GET
    @Path(value = "countUsers")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response countUsers(){
        int c = forum.getUserList().count();
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }
    
    public Response deleteGroup(final Long id){
        try {
            forum.getGroupList().delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    public Response deleteCourse(final Long id){
        try {
            forum.getCourseList().delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    public Response deleteUser(final Long id){
        try {
            forum.getUserList().delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path(value = "group/{id}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response updateGroup(JsonObject j, Long id){
        try {
            String name = j.getString("gName");
            Course c = forum.getGroupList().find(id).getCourse();
            CourseGroup cg1 = forum.getGroupList().find(id);
            List<GroupUser> list = cg1.getMembers();
     
            list.add(forum.getUserList().find((long)j.getInt("userId")));
            CourseGroup cg = new CourseGroup(id, c, name, list);
            forum.getGroupList().update(cg);
            // Convert old to HTTP response
            return Response.ok(cg).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PUT
    @Path(value = "course/{id}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response updateCourse(JsonObject j, Long id){
        try{
            String cc = j.getString("cc");
            String name = j.getString("name");
            Course updatedCourse = new Course(id, cc, name);
            forum.getCourseList().update(updatedCourse);
        
            return Response.ok(updatedCourse).build();
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
    }
    @PUT
    @Path(value = "user/{id}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response updateUser(JsonObject j, Long id){
        try{
            Integer ssnbrInt = j.getInt("ssnbr");
            Long ssnbr = ssnbrInt.longValue();
            String email = j.getString("email");
            String pwd = j.getString("pwd");
            String fname = j.getString("fname");
            String lname = j.getString("lname");

            GroupUser updatedUser = new GroupUser(id, ssnbr, email, pwd, fname, lname);
            return Response.ok(updatedUser).build();
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
 
    @POST
    @Path(value = "group")
    public Response createGroup(JsonObject j){
        Course c = forum.getCourseList().getByCC(j.getString("course"));
        
        List<GroupUser> gU = new ArrayList<>();

        gU.add(forum.getUserList().find((long) j.getInt("userId")));
        
        CourseGroup cg = new CourseGroup(c, j.getString("name"), gU);
        
        try {  
            forum.getGroupList().create(cg);
            URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(cg.getId())).build(cg);
            return Response.created(uri).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Path(value = "course")
    public Response createCourse(JsonObject j){
        Course c = new Course(j.getString("cc"), j.getString("name"));
        try{
            forum.getCourseList().create(c);
        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(c.getId())).build(c);
            return Response.created(uri).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Path(value = "user")
    public Response createUser(JsonObject j){
        GroupUser gu = new GroupUser((long) j.getInt("ssnbr"), j.getString("email"), j.getString("password"), 
            j.getString("fname"), j.getString("lname"));
        
        try{
            forum.getUserList().create(gu);  
            URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(gu.getId())).build(gu);
            return Response.created(uri).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    //TODO: Fix this implementation
    private static <T extends AbstractEntity, K extends IDAO> Response createHelpMethod(T c, K utilList){
        return null;
    }
    
    @GET
    @Path(value = "groups/range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findGroupRange(@QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
                return findRange(fst, count, forum.getGroupList());
    }
    
    @GET
    @Path(value = "courses/range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findCourseRange(@QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
                return findRange(fst, count, forum.getGroupList());
    }
    
    @GET
    @Path(value = "users/range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findUserRange(@QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
                return findRange(fst, count, forum.getGroupList());
    }
    

    private static <T extends IDAO> Response findRange(int fst, int count, T list){
        Collection<T> groups = new ArrayList<>();
        Iterator<T> it = list.findRange(fst, count).iterator();
        while(it.hasNext()) {
            T g = it.next();
            groups.add(g);
        }
        
        GenericEntity<Collection<T>> ge = new GenericEntity<Collection<T>>(groups) {
        };
        return Response.ok(ge).build();
    }
    

    
    

}

    
