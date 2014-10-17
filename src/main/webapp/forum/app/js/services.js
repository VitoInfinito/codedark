'use strict';

/* Services */

var dbService = angular.module('DBService', []);

// Representing the remote RESTful GroupList
groupListService.factory('DBProxy', ['$http',
    function($http) {
        var url = "http://localhost:8080/codedark/webresources/forum";
        
        return{
            findAllCourses: function() {
                return $http.get(url);
            },
            findAllGroups: function(){
                return $http.get(url);
            },
            findAllUsers: function(){
                return $http.get(url);
            },
            findRangeCourses: function(first, count) {
                return $http.get(url + "/range?fst=" + first + "&count=" + count);
            },
            findRangeGroups: function(first, count){
                return $http.get(url + "/range?fst=" + first + "&count=" + count);
            },
            findRangeUsers: function(first, count){
                return $http.get(url + "/range?fst=" + first + "&count=" + count);
            },
            findCourse: function(id) {
                return $http.get(url + "/course/" + id);
            },
            findGroup: function(id){
                return $http.get(url + "/group/" + id);
            },
            findUser: function(id){
                return $http.get(url + "/user/" + id);
            },
            updateCourse: function(id, object) {
                return $http.put(url + "/" + id, object);
            },
            updateGroup: function(id, object){
                return $http.put(url + "/" + id, object);
            },
            updateUser: function(id, object){
                return $http.put(url + "/" + id, object);
            },
            createCourse: function(object) {
                return $http.post(url, object);
            },
            createGroup: function(object) {
                return $http.post(url, object);
            },
            createUser: function(object) {
                return $http.post(url, object);
            },
            deleteCourse: function(id) {
                return $http.delete(url + "/" + id);
            },
            deleteGroup: function(id) {
                return $http.delete(url + "/" + id);
            },
            deleteUser: function(id) {
                return $http.delete(url + "/" + id);
            },
            countCourses: function() {
                return $http.get(url + "/countCourses");
            },
            countGroups: function() {
                return $http.get(url + "/countGroups");
            },
            countUsers: function() {
                return $http.get(url + "/countUsers");
            }
        };
    }]);