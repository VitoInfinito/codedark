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
                when('/login', {
                    templateUrl: 'partials/forum/login.html',
                    controller: 'LoginController'
                }).
                when('/signup', {
                    templateUrl: 'partials/forum/signup.html',
                    controller: 'LoginController'
                }).
                when('/course/:id/newgroup', {
                    templateUrl: 'partials/group/addGroup.html',
                    controller: 'GroupAddController'
                }).
                when('/user', {
                    templateUrl: 'partials/user/userProfile.html',
                    controller: 'UserProfileController'
                }).
                when('/editUserProfile', {
                    templateUrl: 'partials/user/editUserProfile.html',
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
                when('/hemligasidan/editUser/:username', {
                    templateUrl: 'partials/user/editUser.html',
                    controller: 'EditUserController'
                }).
                when('/hemligasidan/editCourse/:cc', {
                    templateUrl: 'partials/course/editCourse.html',
                    controller: 'EditCourseController'
                }).
                when('/hemligasidan/editGroup/:id', {
                    templateUrl: 'partials/group/editGroup.html',
                    controller: 'EditGroupController'
                }).
                        
                otherwise({
                    redirectTo: '/course'
                });

    }]);


