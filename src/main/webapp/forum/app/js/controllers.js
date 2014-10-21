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

controllers.controller('GroupController', ['$scope', '$routeParams', '$location','DBProxy',
    function($scope, $location, $routeParams, DBProxy){
        
        DBProxy.findCourse(window.location.hash.substring(9))
            .success(function(course){
                $scope.course = course;
                alert($scope.course.name);
        });
        
    }]);

controllers.controller('CourseController', ['$scope', '$location', 'DBProxy',
    function($scope, $location, DBProxy){
        $scope.pageSize = '4';
        $scope.currentPage = 0;
        
//        $scope.courses = [
//            {cc:'tda111', name:'First course'},
//            {cc:'tda222', name:'Second course'},
//            {cc:'tda333', name:'Third course'},
//            {cc:'tda444', name:'Fourth course'}
//        ];
      /*  DBProxy.createCourse({cc:'111', name:'FirstCourse'});
        DBProxy.createCourse({cc:'222', name:'SecCourse'});
        DBProxy.createCourse({cc:'333', name:'ThirdCourse'});
        DBProxy.createCourse({cc:'444', name:'FourthCourse'});  */      
        
        DBProxy.countCourses()
                .success(function(count) {
                    $scope.count = count.value;
                    console.log("count: " + count.value);
                }).error(function() {
            console.log("count: error");
        });
        
        getRange();
//        $scope.$watch('currentPage', function() {
//            getRange();
//        });
        function getRange() {
            var fst = $scope.pageSize * $scope.currentPage;
            DBProxy.findRangeCourses(fst, $scope.pageSize)
                    .success(function(courses) {
                        $scope.courses = courses;
                    }).error(function() {
                console.log("findRangeCourses: error");
            });
        }

        
    }]);

controllers.controller('TestController',['$scope','$location','DBProxy',
    function($scope, $location, DBProxy){
        $scope.create = function(){
            DBProxy.create($scope.group);
        };
    }]);

controllers.controller('MenuController',['$scope','$location','DBProxy',
    function($scope, $location, DBProxy){
        
        $scope.menu = {
            loggedIn: function() {
                return getCookie("_username") !== "";
            },
            logout: function() {
                setCookie("_userssnbr", "", 365);
                setCookie("_username", "", 365);
            },
            getLoginName: function() {
                return getCookie("_username");
            },
            getBreadcrumb: function() {
                var cbc = window.location.hash.substring(2);
                if(cbc === "course") {
                    return "";
                }else {
                    return cbc;
                }
            },
            showBreadcrumb: function() {
                return window.location.hash.substring(2) !== "course";
            }
        };
    }]);

controllers.controller('LoginController',['$scope','$location','DBProxy',
    function($scope, $location, DBProxy){
        
            
        $scope.user = {
            login: function() {
                DBProxy.login($scope.user.ssnbr, $scope.user.pwd)
                        .success(function(){
                            console.log($scope.user.ssnbr + " " + $scope.user.pwd);
                            if (angular.isDefined($scope.user.ssnbr)) {
                                setCookie("_userssnbr", $scope.user.ssnbr, 365);
                                console.log("User ok, attempting to find name");
                                DBProxy.findUser($scope.user.ssnbr)
                                    .success(function(response){
                                        console.log("Found user: " + JSON.stringify(response));
                                        setCookie("_username", response.fname + " " + response.lname, 365);
                                    }).error(function() {
                                    console.log("Could not find user");
                                });
                            }
                            $location.path('/index');
                }).error(function(response) {
                    console.log("login failed");
                });
            },
            signUp: function(){
                DBProxy.createUser($scope.user)
                        .success(function(){
                            console.log("New user: "+ $scope.user);
                            $scope.user.login($scope.user.ssnbr, $scope.user.pwd); 
                });
            }
            
        };
    }]);

