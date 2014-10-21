package cds;


import cds.core.*;
import cds.persistence.AbstractEntity;
import cds.persistence.IDAO;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import javax.ws.rs.core.Response.Status;
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
    @EJB private ICourseList courseList;
    @EJB private IGroupUserList userList;
    @EJB private ICourseGroupList groupList;
    
    private final static Logger log = Logger.getAnonymousLogger();
    
    @PersistenceContext(unitName = "jpa_forum_pu")
    @Default
    EntityManager em;
    @Context
    private UriInfo uriInfo;
    
    @GET
    @Path(value = "testGet")
    public Response testGet(){
        
        return Response.ok("hi").build();
    }
    
    @GET
    @Path(value = "allGroups")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAllGroups() {
        Collection<CourseGroupWrapper> groups = new ArrayList<>();
        Iterator<CourseGroup> it = groupList.findAll().iterator();
        while (it.hasNext()) {
            groups.add(new CourseGroupWrapper(it.next()));
        }
        
        GenericEntity<Collection<CourseGroupWrapper>> ge = new GenericEntity<Collection<CourseGroupWrapper>>(groups) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "allUsers")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAllUsers() {
        Collection<GroupUserWrapper> users = new ArrayList<>();
        Iterator<GroupUser> it = userList.findAll().iterator();
        while (it.hasNext()) {
            users.add(new GroupUserWrapper(it.next()));
        }
        
        GenericEntity<Collection<GroupUserWrapper>> ge = new GenericEntity<Collection<GroupUserWrapper>>(users) {
        };
        return Response.ok(ge).build();
    }
    @GET
    @Path(value = "allCourses")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAllCourses() {
        Collection<CourseWrapper> courses = new ArrayList<>();
        Iterator<Course> it = courseList.findAll().iterator();
        while (it.hasNext()) {
            courses.add(new CourseWrapper(it.next()));
        }
        
        GenericEntity<Collection<CourseWrapper>> ge = new GenericEntity<Collection<CourseWrapper>>(courses) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "user/{id}")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response findUser(@PathParam(value= "id") Long id){
        GroupUserWrapper user = new GroupUserWrapper(userList.getBySsnbr(id));
        log.log(Level.INFO, "Found user" + user.getFname() + " " + user.getLname());
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
        CourseGroup cg = groupList.find(id);
        if (cg != null) {
            return Response.ok(new CourseGroupWrapper(cg)).build();
        } else {
            return Response.noContent().build();
        }
    }
    
    @GET
    @Path(value = "course/{id}")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response findCourse(@PathParam(value= "id") Long id) {
        Course c = courseList.find(id);
        if (c != null) {
            return Response.ok(new CourseWrapper(c)).build();
        } else {
            return Response.noContent().build();
        }
    }    

    @GET
    @Path(value = "countGroups")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response countGroups() {
        int c = groupList.count();
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }
    
    @GET
    @Path(value = "countCourses")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response countCourses(){
        int c = courseList.count();
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }
    
    @GET
    @Path(value = "countUsers")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response countUsers(){
        int c = userList.count();
        JsonObject value = Json.createObjectBuilder().add("value", c).build();
        return Response.ok(value).build();
    }
    
    @DELETE
    @Path(value = "group/{id}")
    public Response deleteGroup(@PathParam(value= "id") final Long id){
        try {
            groupList.delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DELETE
    @Path(value = "course/{id}")
    public Response deleteCourse(@PathParam(value= "id") final Long id){
        try {
            courseList.delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DELETE
    @Path(value = "user/{id}")
    public Response deleteUser(@PathParam(value= "id") final Long id){
        try {
            userList.delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path(value = "group/{id}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response updateGroup(JsonObject j, @PathParam(value= "id") Long id){
        try {
            String name = j.getString("gName");
            Course c = groupList.find(id).getCourse();
            CourseGroup cg1 = groupList.find(id);
            List<GroupUser> list = cg1.getMembers();
     
            list.add(userList.find((long)j.getInt("userId")));
            CourseGroup cg = new CourseGroup(id, c, name, list);
            groupList.update(cg);
            // Convert old to HTTP response
            return Response.ok(new CourseGroupWrapper(cg)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PUT
    @Path(value = "course/{id}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response updateCourse(JsonObject j, @PathParam(value= "id") Long id){
        try{
            String cc = j.getString("cc");
            String name = j.getString("name");
            Course updatedCourse = new Course(id, cc, name);
            courseList.update(updatedCourse);
        
            return Response.ok(new CourseWrapper(updatedCourse)).build();
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
    }
    @PUT
    @Path(value = "user/{id}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response updateUser(JsonObject j, @PathParam(value= "id") Long id){
        try{
            Integer ssnbrInt = j.getInt("ssnbr");
            Long ssnbr = ssnbrInt.longValue();
            String email = j.getString("email");
            String pwd = j.getString("pwd");
            String fname = j.getString("fname");
            String lname = j.getString("lname");

            GroupUser updatedUser = new GroupUser(id, ssnbr, email, pwd, fname, lname);
            return Response.ok(new GroupUserWrapper(updatedUser)).build();
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
 
    @POST
    @Path(value = "group")
    public Response createGroup(JsonObject j){
        Course c = courseList.getByCC(j.getString("course"));
        
        List<GroupUser> gU = new ArrayList<>();

        gU.add(userList.find((long) j.getInt("userId")));
        
        CourseGroup cg = new CourseGroup(c, j.getString("name"), gU);
        
        try {  
            groupList.create(cg);
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
            courseList.create(c);
        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(c.getId())).build(c);
            return Response.created(uri).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @POST
    @Path(value = "user")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    public Response createUser(JsonObject j){
        GroupUser gu = new GroupUser(Long.parseLong(j.getString("ssnbr"), 10), j.getString("email"), j.getString("pwd"),
            j.getString("fname"), j.getString("lname"));
        
        log.log(Level.INFO, "Logging ssnbr3: " + gu.toString());
        try{
            userList.create(gu);  
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
        Collection<CourseGroupWrapper> groups = new ArrayList<>();
        Iterator<CourseGroup> it = groupList.findRange(fst, count).iterator();
        while(it.hasNext()) {
            CourseGroup g = it.next();
            groups.add(new CourseGroupWrapper(g));
        }
        
        GenericEntity<Collection<CourseGroupWrapper>> ge = new GenericEntity<Collection<CourseGroupWrapper>>(groups) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "courses/range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findCourseRange(@QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
        log.log(Level.INFO, "Entered findCourseRange with values: " + fst + " " + count);
        Collection<CourseWrapper> courses = new ArrayList<>();
        Iterator<Course> it = courseList.findRange(fst, count).iterator();
        while(it.hasNext()) {
            Course c = it.next();
            courses.add(new CourseWrapper(c));
        }
        GenericEntity<Collection<CourseWrapper>> ge = new GenericEntity<Collection<CourseWrapper>>(courses) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "users/range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findUserRange(@QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
        Collection<GroupUserWrapper> user = new ArrayList<>();
        Iterator<GroupUser> it = userList.findRange(fst, count).iterator();
        while(it.hasNext()) {
            GroupUser gu = it.next();
            user.add(new GroupUserWrapper(gu));
        }
        
        GenericEntity<Collection<GroupUserWrapper>> ge = new GenericEntity<Collection<GroupUserWrapper>>(user) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "login")
    public Response login(@QueryParam("ssnbr") String ssnbr, @QueryParam("pwd") String pwd) {
        GroupUser u = userList.getBySsnbr(Long.parseLong(ssnbr, 10));
        if(u != null && u.getPwd().equals(pwd)) {
            log.log(Level.INFO, "Found user: " + u.toString());
            return Response.ok().build();
        }
        
        return Response.status(Response.Status.NOT_FOUND).build();
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

    
