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
            searchInCourses: function(search) {
                return $http.get(url + "/courses/search?searchfield=" + search);
            },
            findRangeCourses: function(first, count) {
                return $http.get(url + "/courses/range?fst=" + first + "&count=" + count);
            },
            findRangeGroups: function(first, count){
                return $http.get(url + "/groups/range?fst=" + first + "&count=" + count);
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
            findUser: function(id){
                return $http.get(url + "/user/" + id);
            },
            updateCourse: function(id, object) {
                return $http.put(url + "/course/" + id, object);
            },
            updateGroup: function(id, object){
                return $http.put(url + "/group/" + id, object);
            },
            updateUser: function(id, object){
                return $http.put(url + "/user/" + id, object);
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
            countGroups: function() {
                return $http.get(url + "/countGroups");
            },
            countUsers: function() {
                return $http.get(url + "/countUsers");
            },
            login: function(ssnbr, pwd) {
                return $http.get(url + "/login?ssnbr=" + ssnbr + "&pwd=" + pwd);
            }
        };
    }]);