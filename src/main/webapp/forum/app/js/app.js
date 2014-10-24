'use strict';


var forum = angular.module('Forum', [
    'ngRoute',
    'Controllers',
    'DBService'
]);


forum.config(['$routeProvider',
    function($routeProvider) { 
        console.log("Locationpath");
        $routeProvider.
                when('/course', {
                    templateUrl: 'partials/course/courses.html',
                    controller: 'CourseController'
                }).
                when('/course/:cc', {
                    templateUrl: 'partials/group/group.html',
                    controller: 'GroupController'
                }).
                when('/group/:gName', {
                    templateUrl: 'partials/group/editGroup.html',
                    controller: 'GroupEditController'
                }).
                when('/group', {
                    templateUrl: 'partials/group/group.html',
                    controller: 'GroupController'
                }).
                when('/addGroup', {
                    templateUrl: 'partials/group/addGroup.html',
                    controller: 'GroupAddController'
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
                when('/course/:id/newgroup', {
                    templateUrl: 'partials/group/addGroup.html',
                    controller: 'GroupAddController'
                }).
                when('/user/:ssNbr', {
                    templateUrl: 'partials/user/userProfile.html',
                    controller: 'UserProfileController'
                }).
                when('/hemligasidan', {
                    templateUrl: 'partials/frontTest/admin.html',
                    controller: 'AdminController'
                }).
                when('/hemligasidan/newcourse', {
                    templateUrl: 'partials/courses/addCourse.html',
                    controller: 'AdminController'
                }).
                when('/hemligasidan/editUser/:ssnbr', {
                    templateUrl: 'partials/user/editUser.html',
                    controller: 'EditUserController'
                }).
                when('/hemligasidan/editCourse/:cc', {
                    templateUrl: 'partials/course/editCourse.html',
                    controller: 'EditCourseController'
                }).
                otherwise({
                    redirectTo: '/course'
                });

    }]);


