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
    @Path(value = "group/members/{gName}")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response findMembers(@PathParam(value= "gName") String gName){
        log.log(Level.INFO, "INUTI FINDMEMBERS");
        log.log(Level.INFO, "gName: " + gName);
        
        List<GroupUser> gus = new ArrayList<>();
        CourseGroup cg = groupList.getByName(gName);
        log.log(Level.INFO, "Find members in group: " + cg);
        log.log(Level.INFO, "Members: " + cg.getMembers().toString());
        
//        for(GroupUser gu: userList.findAll()){
//            if(cg.getMembers().contains(gu)){
//                gus.add(gu);
//            }
//        }
//        log.log(Level.INFO, "members in group created: " + gus.toString());
//        
        return Response.ok().build();    
    }
    
    
    @GET
    @Path(value = "user/{id}")
    @Produces(value={MediaType.APPLICATION_JSON})
    public Response findUser(@PathParam(value= "id") Long id){
        GroupUser user = userList.getBySsnbr(id);
        log.log(Level.INFO, "Found user" + user.getFname() + " " + user.getLname() + " " + user.getSsnbr() +" "+user.getId());
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
        Course c = courseList.getByCC(ccode);
        if (c != null) {
            return Response.ok(new CourseWrapper(c)).build();
        } else {
            return Response.noContent().build();
        }
    }
    
//    @GET
//    @Path(value = "groups/{ccode}")
//    @Produces(value={MediaType.APPLICATION_JSON})
//    public Response findGroups(@PathParam(value= "ccode") String ccode) {
//        log.log(Level.INFO, "JAG ÄR I FINDGROUPS");
//        log.log(Level.INFO, ccode);
//        Course c = courseList.getByCC(ccode);
//    
//        Collection<CourseGroupWrapper> groups = new ArrayList<>();
//        for(CourseGroup g: groupList.findAll()){
//            if(g.getCourse().equals(c)){
//                groups.add(new CourseGroupWrapper(g));
//            }
//        }
//        
//        GenericEntity<Collection<CourseGroupWrapper>> ge = new GenericEntity<Collection<CourseGroupWrapper>>(groups) {
//        };
//        return Response.ok(ge).build();
//    }    

    @GET
    @Path(value = "countGroups/{ccode}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response countGroups(@PathParam(value= "ccode") String ccode) {
        Course c = courseList.getByCC(ccode);
        
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
            if(course.getCcode().toLowerCase().contains(search) || course.getName().toLowerCase().contains(search)) {
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
            String cc = j.getString("ccode");
            String name = j.getString("name");
            Long id = (long) j.getInt("id");
            log.log(Level.INFO, "Updating id: " + id);
            log.log(Level.INFO, "Updating: " + cc);
            Course updatedCourse = new Course(id, cc, name);
            log.log(Level.INFO, "updatedCourse: " + updatedCourse);
            courseList.update(updatedCourse);
        
            return Response.ok(new CourseWrapper(updatedCourse)).build();
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
    }
    @PUT
    @Path(value = "user")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response updateUser(JsonObject j){
        try{
            Long ssnbr = (long) j.getInt("ssnbr");
            String email = j.getString("email");
            String pwd = j.getString("pwd");
            String fname = j.getString("fname");
            String lname = j.getString("lname");
            log.log(Level.INFO, j.getString("admin"));
            String admin = j.getString("admin");
            //log.log(Level.INFO, ""+j.getInt("id"));
            Long id = (long) j.getInt("id");
            //Long id = userList.getBySsnbr(ssnbr).getId();

            
            GroupUser updatedUser = new GroupUser(id, ssnbr, email, pwd, fname, lname, admin.equals("admin"));
            userList.update(updatedUser);
            
            return Response.ok(updatedUser).build();
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
 
    @POST
    @Path(value = "group")
    public Response createGroup(JsonObject j){
        Course c = courseList.getByCC(j.getString("course"));
        
        String name = j.getString("name");
        
        GroupUser user = userList.getBySsnbr(Long.parseLong(j.getString("user"),10));
        
        int max = j.getInt("maxNbr");
        log.log(Level.INFO, ""+groupList.getByNameAndCourse(name, c.getCcode()));
        if(groupList.getByNameAndCourse(name, c.getCcode()) != null){
            return Response.status(Response.Status.CONFLICT).build();
        }
        
        CourseGroup cg = new CourseGroup(c, name, user, max);
        
        log.log(Level.INFO, c.toString());
        log.log(Level.INFO, name);
        log.log(Level.INFO, user.toString());
        log.log(Level.INFO, "" + max);
        log.log(Level.INFO, cg.toString());
        
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
        log.log(Level.INFO, j.getString("ssnbr"));
        log.log(Level.INFO, j.getString("email"));
        log.log(Level.INFO, j.getString("pwd"));
        log.log(Level.INFO, j.getString("fname"));
        log.log(Level.INFO, j.getString("lname"));
        log.log(Level.INFO, j.getString("admin"));
        GroupUser gu = new GroupUser(Long.parseLong(j.getString("ssnbr"), 10), j.getString("email"), j.getString("pwd"),
            j.getString("fname"), j.getString("lname"), j.getString("admin").equals("admin"));
        
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
    @Path(value = "courses/search")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response searchInCoursesWithRange(@QueryParam(value = "searchfield") String search, @QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
        search = search.toLowerCase();
        log.log(Level.INFO, "Searching courses with search: " + search);
        Collection<CourseWrapper> courses = new ArrayList<>();
        Iterator<Course> it = courseList.findAll().iterator();
        int counter = 0;
        while(it.hasNext() && count > 0) {
            Course c = it.next();
            if((c.getCcode().toLowerCase().contains(search) || c.getName().toLowerCase().contains(search)) && counter++ >= fst) {
                courses.add(new CourseWrapper(c));
                count--;
            }
        }
        
        GenericEntity<Collection<CourseWrapper>> ge = new GenericEntity<Collection<CourseWrapper>>(courses) {
        };
        return Response.ok(ge).build();
    }
    
    @GET
    @Path(value = "groups/{ccode}/range")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findRangeGroups(@PathParam(value= "ccode") String ccode, @QueryParam(value = "fst") int fst, @QueryParam(value = "count") int count) {
        log.log(Level.INFO, "INUTI FINDRANGEGROUPS i ForumResource");
        log.log(Level.INFO, "ccode: " + ccode);
        Course c = courseList.getByCC(ccode);
        log.log(Level.INFO, "1");
        Collection<CourseGroup> groups = new ArrayList<>();
        log.log(Level.INFO, "2");
        for(CourseGroup cg: groupList.findAll()){
            log.log(Level.INFO, "3");
            if(cg.getCourse().equals(c)){
                log.log(Level.INFO, "4");
                groups.add(cg);
                log.log(Level.INFO, "5");
                log.log(Level.INFO, cg.getgName());
                log.log(Level.INFO, (cg.getOwner()).toString());
                
            }
        }
        
//        Iterator<CourseGroup> it = groups.findRange(fst, count).iterator();
//        while(it.hasNext()) {
//            CourseGroup g = it.next();
//            groups.add(g);
//        }
//        
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
    @Path(value = "login")
    public Response login(@QueryParam("ssnbr") String ssnbr, @QueryParam("pwd") String pwd) {
        GroupUser u = userList.getBySsnbr(Long.parseLong(ssnbr, 10));
        if(u != null && u.getPwd().equals(pwd)) {
            log.log(Level.INFO, "Found user: " + u.toString());
            return Response.ok().build();
        }
        
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @GET
    @Path(value = "isadmin")
    public Response isAdmin(@QueryParam("ssnbr") String ssnbr) {
        GroupUser u = userList.getBySsnbr(Long.parseLong(ssnbr, 10));
        if(u.getBelongingTo().contains("admin")) {
            log.log(Level.INFO, "Found admin: " + u.toString());
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

    
