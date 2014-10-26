'use strict';

/* Services */

var dbService = angular.module('DBService', []);

// Representing the remote RESTful GroupList
dbService.factory('DBProxy', ['$http',
    function($http) {
        var url = "http://localhost:8080/codedark/webresources/forum";
        
        return{
            findAllCourses: function() {
                return $http.get(url + "/allCourses");
            },
            findAllGroups: function(){
                return $http.get(url + "/allGroups");
            },
            findAllUsers: function(){
                return $http.get(url + "/allUsers");
            },
            searchInCoursesWithRange: function(search, fst, count) {
                return $http.get(url + "/courses/searchWithRange?searchfield=" + search + "&fst=" + fst + "&count=" + count);
            },
            searchInCourses: function(search) {
                return $http.get(url + "/courses/search?searchfield=" + search);
            },
            searchInUsers: function(search) {
                return $http.get(url + "/users/search?searchfield=" + search);
            },
            searchInGroups: function(search) {
                return $http.get(url + "/groups/search?searchfield=" + search);
            },
            findRangeCourses: function(first, count) {
                return $http.get(url + "/courses/range?fst=" + first + "&count=" + count);
            },
            findRangeGroups: function(ccode, first, count){
                return $http.get(url + "/groups/"+ccode+"/range?fst=" + first + "&count=" + count);
            },
            findRangeUsers: function(first, count){
                return $http.get(url + "/users/range?fst=" + first + "&count=" + count);
            },
            findCourse: function(ccode) {
                return $http.get(url + "/course/" + ccode);
            },
            findGroup: function(id){
                return $http.get(url + "/group/" + id);
            },
            findGroups: function(ccode){
                return $http.get(url + "/groups/" + ccode);
            },
            findUserGroups: function(user){
                console.log("services: " + user);
                return $http.get(url + "/groups/" + user);
            },
            findUser: function(id){
                return $http.get(url + "/user/" + id);
            },
            updateCourse: function(object) {
                return $http.put(url + "/courseEdit", object);
            },
            updateGroup: function(id, object){
                return $http.put(url + "/group/" + id, object);
            },
            joinGroup: function(ccode, gName, user){
                return $http.put(url + "/join?ccode=" + ccode + "&gName="+ gName + "&user=" + user);
            },
            joinRandomGroup: function(ccode, user){
                return $http.put(url + "/join/random?ccode=" + ccode + "&user=" + user);
            },
            leaveGroup: function(ccode, gName, user){
                return $http.put(url + "/leave?ccode=" + ccode + "&gName="+ gName + "&user=" + user);
            },
            updateUser: function(object){
                return $http.put(url + "/user/", object);
            },
            addAdmin: function(id){
                return $http.put(url + "/admin/add/" + id);
            },
            removeAdmin: function(id){
                return $http.put(url + "/admin/rem/" + id);
            },
            createCourse: function(object) {
                return $http.post(url + "/course", object);
            },
            createGroup: function(object) {
                return $http.post(url + "/group", object);
            },
            createUser: function(object) {
                return $http.post(url + "/user", object);
            },
            deleteCourse: function(id) {
                return $http.delete(url + "/course/" + id);
            },
            deleteGroup: function(id) {
                return $http.delete(url + "/group/" + id);
            },
            deleteUser: function(id) {
                return $http.delete(url + "/user/" + id);
            },
            countCourses: function() {
                return $http.get(url + "/countCourses");
            },
            countGroups: function(ccode) {
                return $http.get(url + "/countGroups/" + ccode);
            },
            countSearchedCourses: function(search) {
                return $http.get(url + "/countSearchedCourses/?searchfield=" + search);
            },
            countUsers: function() {
                return $http.get(url + "/countUsers");
            },
            login: function(id, pwd) {
                return $http.get(url + "/login?id=" + id + "&pwd=" + pwd);
            },
            isAdmin: function(id) {
                return $http.get(url + "/isadmin?id=" + id);
            }
        };
    }]);