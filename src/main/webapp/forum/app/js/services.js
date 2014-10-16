'use strict';

/* Services */

var dbService = angular.module('DBService', []);

// Representing the remote RESTful GroupList
groupListService.factory('DBProxy', ['$http',
    function($http) {
        var url = "http://localhost:8080/codedark/webresources/forum";
        
        return{
            findAll: function(object) {
                return $http.get(url, object);
            },
            findRange: function(object, first, count) {
                return $http.get(url + "/range?fst=" + first + "&count=" + count, object);
            },
            find: function(object, id) {
//                return $http.get(url + "/" + id);
            },
            update: function(object, id) {
                return $http.put(url + "/" + id, object);
            },
            create: function(object) {
                return $http.post(url, object);
            },
            delete: function(object, id) {
                return $http.delete(url + "/" + id);
            },
            count: function(object) {
                return $http.get(url + "/count");
            }
        }
    }]);