'use strict';

/* Services */

var dbService = angular.module('DBService', []);

// Representing the remote RESTful GroupList
groupListService.factory('DBProxy', ['$http',
    function($http) {
        var url = "http://localhost:8080/codedark/webresources/forum";
        
        return{
            findAll: function() {
                return $http.get(url);
            },
            findRange: function(first, count) {
                return $http.get(url + "/range?fst=" + first + "&count=" + count);
            },
            find: function(object, id) {
//                return $http.get(url + "/" + id);
            },
            update: function(id, cc) {
//                return $http.put(url + "/" + id, product);
            },
            create: function(name,cc) {
                return $http.post(url, name, cc);
            },
            delete: function(id) {
//                return $http.delete(url + "/" + id);
            },
            count: function(object) {
                return $http.get(url + "/count");
            }
        }
    }]);