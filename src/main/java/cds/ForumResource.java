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
import static javax.ws.rs.client.Entity.json;
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
        Collection<CourseGroup> groups = new ArrayList<>();
        Iterator<CourseGroup> it = groupList.findAll().iterator();
        while (it.hasNext()) {
            groups.add(it.next());
        }
        
        GenericEntity<Collection<CourseGroup>> ge = new GenericEntity<Collection<CourseGroup>>(groups) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "allUsers")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAllUsers() {
        Collection<GroupUser> users = new ArrayList<>();
        Iterator<GroupUser> it = userList.findAll().iterator();
        while (it.hasNext()) {
            users.add(it.next());
        }
        
        GenericEntity<Collection<GroupUser>> ge = new GenericEntity<Collection<GroupUser>>(users) {
        };
        return Response.ok(ge).build();
    }
    @GET
    @Path(value = "allCourses")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findAllCourses() {
        Collection<Course> courses = new ArrayList<>();
        Iterator<Course> it = courseList.findAll().iterator();
        while (it.hasNext()) {
            courses.add(it.next());
        }
        
        GenericEntity<Collection<Course>> ge = new GenericEntity<Collection<Course>>(courses) {
        };
        return Response.ok(ge).build();
    }
    
    @PUT
    @Path(value = "join")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response joinGroup(@QueryParam(value= "ccode") String ccode, 
            @QueryParam(value= "gName") String gName, @QueryParam(value= "user") String user){
        log.log(Level.INFO, "INUTI JOINGROUP i ForumResource");
        CourseGroup cg = groupList.getByNameAndCourse(gName, ccode);

        log.log(Level.INFO, "Username: " + user);
        GroupUser gu = userList.find(user);
        log.log(Level.INFO, "User: " + gu);
        
        List<GroupUser> members = cg.getMembers();
        log.log(Level.INFO, "Members: " + members.toString());
        
        cg.getMembers().add(gu);
        groupList.update(cg);
        log.log(Level.INFO, "Members: " + members.toString());
        
        try{
            return Response.ok(cg).build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
       
    }   
    
    
    
    @PUT
    @Path(value = "leave")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response leaveGroup(@QueryParam(value= "ccode") String ccode, 
            @QueryParam(value= "gName") String gName, @QueryParam(value= "user") String user){
        log.log(Level.INFO, "INUTI LEAVEGROUP i ForumResource");
        CourseGroup cg = groupList.getByNameAndCourse(gName, ccode);
        
        
        GroupUser gu = userList.find(user);
        boolean isOwner = false;
        if(gu.equals(cg.getOwner())){
            isOwner = true;
        }
        
        List<GroupUser> members = cg.getMembers();
        members.remove(gu);
        
        groupList.update(cg);
        if(members.isEmpty()){
            deleteGroup(cg.getId());
            isOwner = false;
        }
        if(isOwner){
            cg.setOwner(members.get(0));
            groupList.update(cg);
            
        }
        
        try{
            return Response.ok(cg).build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GET
    @Path(value = "user/{id}")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response findUser(@PathParam(value= "id") String id){
        log.log(Level.INFO, "User: " + userList.find(id));
        GroupUser user = userList.find(id);

        if (user != null) {
            log.log(Level.INFO, "Found user: " + user.getFname() + " " + user.getLname());
            return Response.ok(user).build();
        } else {
            log.log(Level.INFO, "Did not find user");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    
    }
    
    @GET
    @Path(value = "group/{id}")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response findGroup( @PathParam(value= "id")Long id) {
        CourseGroup cg = groupList.find(id);
        if (cg != null) {
            return Response.ok(cg).build();
        } else {
            return Response.noContent().build();
        }
    }
    
    @GET
    @Path(value = "course/{ccode}")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response findCourse(@PathParam(value= "ccode") String ccode) {
        log.log(Level.INFO, "JAG ÄR I FINDCOURSE");
        log.log(Level.INFO, ccode);
        Course c = courseList.getById(ccode);
        if (c != null) {
            return Response.ok(c).build();
        } else {
            return Response.noContent().build();
        }
    } 

    @GET
    @Path(value = "countGroups/{ccode}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response countGroups(@PathParam(value= "ccode") String ccode) {
        Course c = courseList.getById(ccode);
        
        int count = 0;
        for(CourseGroup cg: groupList.findAll()){
            if(cg.getCourse().equals(c)){
                count++;
            }
        }
        JsonObject value = Json.createObjectBuilder().add("value", count).build();
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
    @Path(value = "countSearchedCourses")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response countSearchedCourses(@QueryParam(value = "searchfield") String search){
        int c = 0;
        log.log(Level.INFO, "Got in");
        for(Course course : courseList.findAll()) {
            if(course.getId().toLowerCase().contains(search) || course.getName().toLowerCase().contains(search)) {
                c++;
            }
        }
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
    public Response deleteCourse(@PathParam(value= "id") final String id){
        try {
            courseList.delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DELETE
    @Path(value = "user/{id}")
    public Response deleteUser(@PathParam(value= "id") final String id){
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
            log.log(Level.INFO, "Updating: " + cg1);
            CourseGroup cg = new CourseGroup(id, c, name, list, cg1.getOwner());
            groupList.update(cg);
            // Convert old to HTTP response
            return Response.ok(cg).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PUT
    @Path(value = "courseEdit")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response updateCourse(JsonObject j){
        try{
            log.log(Level.INFO, "Updating course" + j);
            String name = j.getString("name");
            log.log(Level.INFO, "JSON: " + j.toString());
            

            
            String id = j.getJsonObject("id").getString("value");
            
            //String id = j.getString("id.value");
            log.log(Level.INFO, "Updating id: " + id);
            Course updatedCourse = new Course(id, name);
            log.log(Level.INFO, "updatedCourse: " + updatedCourse);
            courseList.update(updatedCourse);
        
            return Response.ok(updatedCourse).build();
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
    }
    @PUT
    @Path(value = "user")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response updateUser(JsonObject j){
        log.log(Level.INFO, "in updateUser");
        try{
            
            String username = j.getJsonObject("id").getString("value");
            String email = j.getString("email");
            String pwd = j.getString("pwd");
            String fname = j.getString("fname");
            String lname = j.getString("lname");
            
            GroupUser updatedUser = new GroupUser(username, email, pwd, fname, lname);
            userList.update(updatedUser);
            
            return Response.ok(updatedUser).build();
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PUT
    @Path(value="admin/add/{id}")
    public Response addAdmin(@PathParam(value= "id") String id){
        log.log(Level.INFO, "In add admin");
        GroupUser user = userList.find(id);
        
        if(!user.getBelongingTo().contains("admin")){
                user.addUserBelongingToGroup("admin");
        }  
        userList.update(user);
            
        return Response.ok(user).build();
    }
    
    @PUT
    @Path(value="admin/rem/{id}")
    public Response removeAdmin(@PathParam(value= "id") String id){
        log.log(Level.INFO, "In remove admin");
        GroupUser user = userList.find(id);
        
        if(user.getBelongingTo().contains("admin")){
                user.remUserBelongingToGroup("admin");
        }  
        userList.update(user);
        
        return Response.ok(user).build();
    }
 
    @POST
    @Path(value = "group")
    public Response createGroup(JsonObject j){
        log.log(Level.INFO, "INUTI CREATEGROUP");
        Course c = courseList.find(j.getString("course"));
        String name = j.getString("name");
        GroupUser owner = userList.find(j.getString("user"));
        int max = j.getInt("maxNbr");
        
        //Check if group already exists
        log.log(Level.INFO, "Ska va null: "+groupList.getByNameAndCourse(name, c.getId()));
        if(groupList.getByNameAndCourse(name, c.getId()) != null){
            return Response.status(Response.Status.CONFLICT).build();
        }
        
        CourseGroup cg = new CourseGroup(c, name, owner, max);
//        log.log(Level.INFO, cg.toString());
          log.log(Level.INFO, cg.getCourse().toString());
          log.log(Level.INFO, cg.getgName());
          log.log(Level.INFO, cg.getOwner().toString());
          log.log(Level.INFO, cg.getMembers().toString());

        
        try {  
            groupList.create(cg);
            URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(cg.getId())).build(cg);
            return Response.created(uri).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
//        return Response.ok().build();
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
        log.log(Level.INFO, j.getString("id"));
        log.log(Level.INFO, j.getString("email"));
        log.log(Level.INFO, j.getString("pwd"));
        log.log(Level.INFO, j.getString("fname"));
        log.log(Level.INFO, j.getString("lname"));
        
        String username = j.getString("id");
        String email = j.getString("email");
        String pwd = j.getString("pwd");
        String fname = j.getString("fname"); 
        String lname = j.getString("lname");
        
        GroupUser newUser = new GroupUser(username, email, pwd, fname, lname);
                     
        try{
            userList.create(newUser);  
            URI uri = uriInfo.getAbsolutePathBuilder().path(newUser.getId()).build(newUser);
            return Response.created(uri).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
            
    @GET
    @Path(value = "courses/searchWithRange")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response searchInCoursesWithRange(@QueryParam(value = "searchfield") String search, @QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
        search = search.toLowerCase();
        log.log(Level.INFO, "Searching courses with search: " + search);
        Collection<Course> courses = new ArrayList<>();
        Iterator<Course> it = courseList.findAll().iterator();
        int counter = 0;
        while(it.hasNext() && count > 0) {
            Course c = it.next();
            if((c.getId().toLowerCase().contains(search) || c.getName().toLowerCase().contains(search)) && counter++ >= fst) {

                courses.add(c);
                count--;
            }
        }
        
        GenericEntity<Collection<Course>> ge = new GenericEntity<Collection<Course>>(courses) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "courses/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response searchInCourses(@QueryParam(value = "searchfield") String search) {
        search = search.toLowerCase();
        log.log(Level.INFO, "Searching courses with search: " + search);
        Collection<Course> courses = new ArrayList<>();
        Iterator<Course> it = courseList.findAll().iterator();
        while(it.hasNext()) {
            Course c = it.next();
            if((c.getId().toLowerCase().contains(search) || c.getName().toLowerCase().contains(search))) {
                courses.add(c);
            }
        }
        
        GenericEntity<Collection<Course>> ge = new GenericEntity<Collection<Course>>(courses) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "users/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response searchInUsers(@QueryParam(value = "searchfield") String search) {
        search = search.toLowerCase();
        log.log(Level.INFO, "Searching users with search: " + search);
        Collection<GroupUser> users = new ArrayList<>();
        Iterator<GroupUser> it = userList.findAll().iterator();
        while(it.hasNext()) {
            GroupUser u = it.next();
            String lAndFName = u.getFname() + " " + u.getLname();
            if(lAndFName.toLowerCase().contains(search) || u.getEmail().toLowerCase().contains(search) || u.getId().toString().contains(search)) {
                users.add(u);
            }
        }
        
        GenericEntity<Collection<GroupUser>> ge = new GenericEntity<Collection<GroupUser>>(users) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "groups/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response searchInGroups(@QueryParam(value = "searchfield") String search) {
        search = search.toLowerCase();
        log.log(Level.INFO, "Searching users with search: " + search);
        Collection<CourseGroup> groups = new ArrayList<>();
        Iterator<CourseGroup> it = groupList.findAll().iterator();
        while(it.hasNext()) {
            CourseGroup g = it.next();
            if(g.getgName().toLowerCase().contains(search) || g.getCourse().getId().toLowerCase().contains(search) || g.getCourse().getName().toLowerCase().contains(search)) {
                groups.add(g);
            }
        }
        
        GenericEntity<Collection<CourseGroup>> ge = new GenericEntity<Collection<CourseGroup>>(groups) {
        };
        return Response.ok(ge).build();
    }
    @GET
    @Path(value = "groups/{ccode}/range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findRangeGroups(@PathParam(value= "ccode") String ccode, @QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
        log.log(Level.INFO, "INUTI FINDRANGEGROUPS i ForumResource");
        
        Collection<CourseGroup> groups = new ArrayList<>();
        List<CourseGroup> oldGroups = groupList.getByCourse(ccode);
        
        int i = fst;
        
        while(i >= fst && i < fst+count && i < groupList.getByCourse(ccode).size()){
            groups.add(oldGroups.get(i));
            i++;
        }

        GenericEntity<Collection<CourseGroup>> ge = new GenericEntity<Collection<CourseGroup>>(groups) {
        };
        log.log(Level.INFO, "1");
        return Response.ok(ge).build();
//          return Response.ok().build();
    }
    
    @GET
    @Path(value = "courses/range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findCourseRange(@QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
        log.log(Level.INFO, "Entered findCourseRange with values: " + fst + " " + count);
        Collection<Course> courses = new ArrayList<>();
        Iterator<Course> it = courseList.findRange(fst, count).iterator();
        while(it.hasNext()) {
            Course c = it.next();
            courses.add(c);
        }
        GenericEntity<Collection<Course>> ge = new GenericEntity<Collection<Course>>(courses) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "users/range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findUserRange(@QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
        Collection<GroupUser> user = new ArrayList<>();
        Iterator<GroupUser> it = userList.findRange(fst, count).iterator();
        while(it.hasNext()) {
            GroupUser gu = it.next();
            user.add(gu);
        }
        
        GenericEntity<Collection<GroupUser>> ge = new GenericEntity<Collection<GroupUser>>(user) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value= "groups/{user}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findUserGroups(@PathParam("user") String uName){
        List<CourseGroup> groups = groupList.getByUser(uName);
        
        GenericEntity<Collection<CourseGroup>> ge = new GenericEntity<Collection<CourseGroup>>(groups){
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "login")
    public Response login(@QueryParam("id") String id, @QueryParam("pwd") String pwd) {
        GroupUser u = userList.find(id);
        
        if(u != null && u.getPwd().equals(pwd)) {
            log.log(Level.INFO, "Found user: " + u.toString());
            return Response.ok().build();
        }
        
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @GET
    @Path(value = "isadmin")
    public Response isAdmin(@QueryParam("id") String id) {
        GroupUser u = userList.find(id);
        if(u.getBelongingTo().contains("admin")) {
            log.log(Level.INFO, "Found admin: " + u.toString());
            return Response.ok().build();
        }
        
        return Response.status(Response.Status.UNAUTHORIZED).build();
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

    
