'use strict';


var forum = angular.module('Forum', [
    'ngRoute',
    'Controllers',
    'GroupListService'
]);


forum.config(['$routeProvider',
    function($routeProvider) {  
        $routeProvider.
                when('/forum', {
                    templateUrl: 'partials/forum/forum.html',
                    controller: 'ForumController'
                }).
                when('/group', {
                    templateUrl: 'partials/group/group.html',
                    controller: 'GroupController'
                }).
                when('/test', {
                    templateUrl: 'partials/frontTest/test.html',
                    controller: 'TestController'
                }).
                
                otherwise({
                    redirectTo: '/index.html'
                });

    }]);


