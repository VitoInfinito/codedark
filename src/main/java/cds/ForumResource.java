package cds;


import cds.core.Course;
import cds.core.CourseGroup;
import cds.core.Forum;
import cds.core.GroupUser;
import cds.core.ICourseGroupList;
import cds.core.ICourseList;
import cds.core.IGroupUserList;
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
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response find(JsonObject j, @PathParam(value= "id") Long id){
        switch(j.getString("type")){
            case "group":
                return findGroup(j, id);
            case "course":
                return findCourse(j, id);
            case "user":
                return findUser(j, id);
            default:
                return null;
        }
    
    }

    private Response findGroup(JsonObject j, Long id) {
        CourseGroup cg = forum.getGroupList().find(id);
        if (cg != null) {
            return Response.ok(cg).build();
        } else {
            return Response.noContent().build();
        }
    }
    
    private Response findCourse(JsonObject j, Long id) {
        Course c = forum.getCourseList().find(id);
        if (c != null) {
            return Response.ok(c).build();
        } else {
            return Response.noContent().build();
        }
    }
    
    private Response findUser(JsonObject j, Long id) {
        GroupUser user = forum.getUserList().find(id);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.noContent().build();
        }
    }

    @GET
    @Path(value = "count")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response count(JsonObject j) {
        switch(j.getString("type")){
            case "group":
                return countGroups();
            case "course":
                return countCourses();
            case "user":
                return countUsers();
            default:
                return null;
        }
    }
    
    private Response countGroups(){
        int c = forum.getGroupList().count();
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }
    
    private Response countCourses(){
        int c = forum.getCourseList().count();
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }
    
    private Response countUsers(){
        int c = forum.getUserList().count();
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }

    @DELETE
    @Path(value = "{id}")
    public Response delete(JsonObject j, @PathParam(value = "id") final Long id) {
        switch(j.getString("type")){
            case "group":
                return deleteGroup(id);
            case "course":
                return deleteCourse(id);
            case "user":
                return deleteUser(id);
            default:
                return null;
        }

    }
    
    private Response deleteGroup(final Long id){
        try {
            forum.getGroupList().delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private Response deleteCourse(final Long id){
        try {
            forum.getCourseList().delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private Response deleteUser(final Long id){
        try {
            forum.getUserList().delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path(value = "{id}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response update(JsonObject j, @PathParam(value = "id") Long id) {
        switch(j.getString("type")){
            case "group":
                return updateGroup(j, id);
            case "course":
                return updateCourse(j, id);
            case "user":
                return updateUser(j, id);
            default:
                return null;
        }
    }
    
    private Response updateGroup(JsonObject j, Long id){
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
    
    private Response updateCourse(JsonObject j, Long id){
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
    
    private Response updateUser(JsonObject j, Long id){
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

        gU.add(forum.getUserList().find((long) j.getInt("userId")));
        
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
    
    @GET
    @Path(value = "range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findRange(@QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count, @QueryParam(value = "type")String type) {

        ICourseGroupList cGL = forum.getGroupList();
        IGroupUserList gUL = forum.getUserList();
        ICourseList cL = forum.getCourseList();
        
        switch(type){
            case "group":
            case "course":
            case "member":

        }
        
        
        return null;
    }

    private <T extends IDAO> Response findRange(int fst, int count, T list){
        Collection<T> groups = new ArrayList<>();
        for (Iterator<T> it = list.findRange(fst, count).iterator(); it.hasNext();) {
            T g = it.next();
            groups.add(g);
        }
        
        GenericEntity<Collection<T>> ge = new GenericEntity<Collection<T>>(groups) {
        };
        return Response.ok(ge).build();
    }
    
    private Response findCourseRange(int fst, int count){
        
    }
    
}

    
