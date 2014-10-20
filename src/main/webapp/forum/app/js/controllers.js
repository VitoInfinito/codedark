'use strict';

/*
 * Controllers
 */

var setCookie = function(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
};

var getCookie = function(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') c = c.substring(1);
        if (c.indexOf(name) !== -1) return c.substring(name.length, c.length);
    }
    return "";
};

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

controllers.controller('CourseController', ['$scope', '$location', 'DBProxy',
    function($scope, $location, DBProxy){
        $scope.pageSize = '5';
        $scope.currentPage = 0;
        
        DBProxy.countCourses()
                .success(function(count) {
                    $scope.count = count.value;
                }).error(function() {
            console.log("count: error");
        });
        
        getRange();
        $scope.$watch('currentPage', function() {
            getRange();
        });
        function getRange() {
            var fst = $scope.pageSize * $scope.currentPage;
            DBProxy.findRange(fst, $scope.pageSize)
                    .success(function(courses) {
                        $scope.courses = courses;
                    }).error(function() {
                console.log("findRange: error");
            });
        }
        
    }]);

controllers.controller('TestController',['$scope','$location','DBProxy',
    function($scope, $location, DBProxy){
        $scope.create = function(){
            DBProxy.create($scope.group);
        };
    }]);

controllers.controller('LoginController',['$scope','$location','DBProxy',
    function($scope, $location, DBProxy){
        
        //var loginName = "test";
        $scope.user = {
            login: function(newLoginName, pwd) {
                console.log($scope.user.ssnbr + " " + $scope.user.pwd);
                DBProxy.login($scope.user)
                        .success(function(response){
                            //var status = response.status;
                            //alert(response);
                            console.log($scope.user.ssnbr + " " + $scope.user.pwd);
                            if (angular.isDefined(newLoginName)) {
                                setCookie("_username", newLoginName, 365);
                            }

                            $location.path('/index');
                }).error(function(response) {
                    alert(response);
                    console.log("login failed");
                });
                /*if (angular.isDefined(newLoginName)) {
                    setCookie("username", newLoginName, 365);
                }*/
            },
            getLoginName: function() {
                return getCookie("_username");
            },
            //Sign up
            signUp: function(){
                DBProxy.createUser($scope.user)
                        .success(function(){
                            console.log("New user: "+ $scope.user);
                            $scope.user.login($scope.user.ssnbr, $scope.user.pwd); 
                });
            }
            
        };
      
    }]);

