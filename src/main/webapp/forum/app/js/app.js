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
                when('/course/:cc', {
                    templateUrl: 'partials/group/group.html',
                    controller: 'GroupController'
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
                when('/addCourse', {
                    templateUrl: 'partials/course/addCourse.html',
                    controller: 'CourseController'
                }).
                when('/hemligasidan', {
                    templateUrl: 'partials/frontTest/admin.html',
                    controller: 'AdminController'
                }).
               
             
                otherwise({
                    redirectTo: '/course'
                });

    }]);


