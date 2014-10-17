'use strict';

/*
 * Controllers
 */

var controllers = angular.module('Controllers', []);

// General navigation controller (possibly useful?)
controllers.controller('NavigationCtrl', ['$scope', '$location',
    function($scope, $location) {
        $scope.navigate = function(url) {
            $location.path(url);
        };
    }]);

controllers.controller('GroupController', ['$scope', '$location','DBProxy',
    function($scope, $location, DBProxy){
        //TODO: Fill with buisness
    }]);

controllers.controller('ForumController', ['$scope', '$location', 'DBProxy',
    function($scope, $location, DBProxy){
        //TODO: Fill with buisness
    }]);

controllers.controller('TestController',['$scope','$location','DBProxy',
    function($scope, $location, DBProxy){
        $scope.create = function(){
            DBProxy.create($scope.group);
        };
    }]);

controllers.controller('LoginController',['$scope','$location','DBProxy',
    function($scope, $location, DBProxy){
        //TODO: Fill with buisness
    }]);

controllers.controller('SignupController',['$scope','$location','DBProxy',
    function($scope, $location, DBProxy){
        $scope.createUser = function(){
            DBProxy.createUser($scope.user)
                    .success(function(){
                        console.log("New user: "+ $scope.user);
                        $location.path('/forum');
            });
        };
    }]);
