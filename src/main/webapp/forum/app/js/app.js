'use strict';


var forum = angular.module('Forum', [
    'ngRoute',
    'Controllers',
    'DBService'
]);


forum.config(['$routeProvider',
    function($routeProvider) {  
        $routeProvider.
                when('/course', {
                    templateUrl: 'partials/course/courses.html',
                    controller: 'CourseController'
                }).
                when('/group', {
                    templateUrl: 'partials/group/group.html',
                    controller: 'GroupController'
                }).
                when('/test', {
                    templateUrl: 'partials/frontTest/test.html',
                    controller: 'TestController'
                }).
                when('/login', {
                    templateUrl: 'partials/forum/login.html',
                    controller: 'LoginController'
                }).
                when('/signup', {
                    templateUrl: 'partials/forum/signup.html',
                    controller: 'LoginController'
                }).
                
                otherwise({
                    redirectTo: '/course'
                });

    }]);


