'use strict';


var shop = angular.module('Shop', [
    'ngRoute'
     // More here
]);


shop.config(['$routeProvider',
    function($routeProvider) {  // Injected object $routeProvider
        $routeProvider.
                when('/forum', {
                    templateUrl: 'partials/forum/forum.html',
                    controller: 'ForumController'
                }).
                when('/group', {
                    templateUrl: 'partials/group/group.html',
                    controller: 'GroupController'
                }).
                
                otherwise({
                    redirectTo: '/index.html'
                });

    }]);


