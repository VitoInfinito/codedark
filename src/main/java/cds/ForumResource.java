package cds;


import cds.core.*;
import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
 *  Handles all data communication with frontend.
 *
 *
 * @author codedark
 */
@Path("forum")
public class ForumResource {
      
    @EJB private ICourseList courseList;
    @EJB private IGroupUserList userList;
    @EJB private ICourseGroupList groupList;
    
    private final static Logger log = Logger.getAnonymousLogger();
    
    @Context
    private UriInfo uriInfo;
    
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
        CourseGroup cg = groupList.getByNameAndCourse(gName, ccode);
        GroupUser gu = userList.find(user);
        cg.getMembers().add(gu);
        try{
            groupList.update(cg);
            return Response.ok(cg).build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PUT
    @Path(value = "join/random")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response joinRandomGroup(@QueryParam(value= "ccode") String ccode, @QueryParam(value= "user") String user){
        Random r = new Random();
        List<CourseGroup> cgl = groupList.getByCourse(ccode);
        List<CourseGroup> rcgl = new ArrayList<>();
        GroupUser gu = userList.find(user);
        for(CourseGroup cg : cgl) {
            if(cg.getMembers().size() <= cg.getmaxNbr() && !cg.getMembers().contains(gu)) {
                rcgl.add(cg);
            }
        }
        CourseGroup rcg;
        try{
            if(rcgl.size() > 0) {
                rcg = rcgl.get(r.nextInt(rcgl.size()));
                rcg.getMembers().add(gu);
                groupList.update(rcg);
                return Response.ok(rcg).build();
            }else {
                String alphabet = "0123456789abcdefghijklmnopqrstuvwxyz";
                StringBuilder sb = new StringBuilder();
                for(int i=0; i<12; i++) {
                    sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
                }
                rcg = new CourseGroup(courseList.getById(ccode), sb.toString(), gu, 5);
                groupList.create(rcg);
                return Response.ok(rcg).build();
            }
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PUT
    @Path(value = "leave")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response leaveGroup(@QueryParam(value= "ccode") String ccode, 
            @QueryParam(value= "gName") String gName, @QueryParam(value= "user") String user){
        CourseGroup cg = groupList.getByNameAndCourse(gName, ccode);
        GroupUser gu = userList.find(user);
        boolean isOwner = gu.equals(cg.getOwner());
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
        log.log(Level.INFO, "Attempting to find user {0}", id);
        GroupUser user = userList.find(id);
        if (user != null) {
            return Response.ok(user).build();
        } else {
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
        log.log(Level.INFO, "Attempting to find course {0}", ccode);
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
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @DELETE
    @Path(value = "course/{id}")
    public Response deleteCourse(@PathParam(value= "id") final String id){
        try {
            Course c = courseList.find(id);
            for(CourseGroup group: groupList.findAll()){
                if(group.getCourse().equals(c)){
                    groupList.delete(group.getId());
                }
            }
            courseList.delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @DELETE
    @Path(value = "user/{id}")
    public Response deleteUser(@PathParam(value= "id") final String id){
        try {
            List<CourseGroup> cgl = groupList.getByUser(id);
            GroupUser gu = userList.find(id);
            for(CourseGroup cg : cgl) {
                leaveGroup(cg.getCourse().getId(), cg.getgName(), id);
            }
            userList.delete(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path(value = "group/{id}")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response updateGroup(JsonObject j, @PathParam(value= "id") Long id){
        Course c = groupList.find(id).getCourse();
        CourseGroup cg1 = groupList.find(id);
        List<GroupUser> list = cg1.getMembers();
        log.log(Level.INFO, "Updating group {0}", cg1.getgName());
        try {
            CourseGroup cg = new CourseGroup(id, c, j.getString("gName"), list, cg1.getOwner(), j.getInt("maxNbr"));
            groupList.update(cg);
            return Response.ok(cg).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @PUT
    @Path(value = "courseEdit")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response updateCourse(JsonObject j){
        String name = j.getString("name");
        String id = j.getJsonObject("id").getString("value");
        log.log(Level.INFO, "Updating course {0}", name);
        try{
            Course updatedCourse = new Course(id, name);
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
        String username = j.getJsonObject("id").getString("value");
        String email = j.getString("email");
        String pwd = j.getString("pwd");
        String fname = j.getString("fname");
        String lname = j.getString("lname");
        log.log(Level.INFO, "Updating user {0}", username);
        try{
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
        log.log(Level.INFO, "Adding admin {0}", id);
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
        log.log(Level.INFO, "Removing admin {0}", id);
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
        log.log(Level.INFO, "Attempting to create a new group");
        Course c = courseList.find(j.getString("course"));
        String name = j.getString("name");
        GroupUser owner = userList.find(j.getString("user"));
        int max = j.getInt("maxNbr");
        //Check if group already exists
        if(groupList.getByNameAndCourse(name, c.getId()) != null){
            return Response.status(Response.Status.CONFLICT).build();
        }
        CourseGroup cg = new CourseGroup(c, name, owner, max);
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
        String username = j.getString("id");
        String email = j.getString("email");
        String pwd = j.getString("pwd");
        String fname = j.getString("fname"); 
        String lname = j.getString("lname");
        GroupUser newUser = new GroupUser(username, email, pwd, fname, lname);
        try{
            userList.create(newUser);
            return Response.ok(userList.find(newUser.getId())).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
            
    @GET
    @Path(value = "courses/searchWithRange")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response searchInCoursesWithRange(@QueryParam(value = "searchfield") String search, @QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
        search = search.toLowerCase();
        log.log(Level.INFO, "Searching courses with range and search: {0}", search);
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
        log.log(Level.INFO, "Searching courses with search: {0}", search);
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
        log.log(Level.INFO, "Searching users with search: {0}", search);
        Collection<GroupUser> users = new ArrayList<>();
        Iterator<GroupUser> it = userList.findAll().iterator();
        while(it.hasNext()) {
            GroupUser u = it.next();
            String lAndFName = u.getFname() + " " + u.getLname();
            if(lAndFName.toLowerCase().contains(search) || u.getEmail().toLowerCase().contains(search) || u.getId().contains(search)) {
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
        log.log(Level.INFO, "Searching users with search: {0}", search);
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
        Collection<CourseGroup> groups = new ArrayList<>();
        List<CourseGroup> oldGroups = groupList.getByCourse(ccode);
        int i = fst;
        while(i >= fst && i < fst+count && i < groupList.getByCourse(ccode).size()){
            groups.add(oldGroups.get(i));
            i++;
        }
        GenericEntity<Collection<CourseGroup>> ge = new GenericEntity<Collection<CourseGroup>>(groups) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "courses/range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findCourseRange(@QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
        log.log(Level.INFO, "Entered findCourseRange with values: {0} {1}", new Object[]{fst, count});
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
        log.log(Level.INFO, "Entered findUserRange with values: {0} {1}", new Object[]{fst, count});
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
            log.log(Level.INFO, "Found user: {0}", u.toString());
            return Response.ok().build();
        }
        
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @GET
    @Path(value = "isadmin")
    public Response isAdmin(@QueryParam("id") String id) {
        GroupUser u = userList.find(id);
        if(u != null) {
            if(u.getBelongingTo().contains("admin")) {
                log.log(Level.INFO, "Found admin: {0}", u.toString());
                return Response.status(Response.Status.OK).entity("User is admin").build();
            }else{
                return Response.status(Response.Status.OK).entity("User not admin").build();
            }
        }
        
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}

    
