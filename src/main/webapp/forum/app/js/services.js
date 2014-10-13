'use strict';

/* Services */

var groupListService = angular.module('GroupListService', []);

// Representing the remote RESTful GroupList
groupListService.factory('GroupListProxy', ['$http',
    function($http) {
        var url = "http://localhost:8080/codedark/webresources/forum";
        
        return{
            findAll: function() {
                return $http.get(url);
            },
            findRange: function(first, count) {
                return $http.get(url + "/range?fst=" + first + "&count=" + count);
            },
            find: function(id) {
//                return $http.get(url + "/" + id);
            },
            update: function(id, cc) {
//                return $http.put(url + "/" + id, product);
            },
            create: function(name,cc) {
//                return $http.post(url, product);
            },
            delete: function(id) {
//                return $http.delete(url + "/" + id);
            },
            count: function() {
                return $http.get(url + "/count");
            }
        }
    }]);