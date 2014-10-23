'use strict';

/*
 * Controllers
 */

var setCookie = function (cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
};

var getCookie = function (cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ')
            c = c.substring(1);
        if (c.indexOf(name) !== -1)
            return c.substring(name.length, c.length);
    }
    return "";
};

var controllers = angular.module('Controllers', []);

// General navigation controller (possibly useful?)
controllers.controller('NavigationCtrl', ['$scope', '$location',
    function ($scope, $location) {
        $scope.navigate = function (url) {
            $location.path(url);
        };
    }]);

controllers.controller('GroupController', ['$scope', '$routeParams', '$location', 'DBProxy',
    function ($scope, $location, $routeParams, DBProxy) {

        DBProxy.findCourse(window.location.hash.substring(9))
            .success(function(course){
                $scope.course = course;
        
                $scope.pageSize = '5';
                $scope.currentPage = 0;
        
                //COUNT
                DBProxy.countGroups($scope.course.ccode)
                    .success(function(count){
                        $scope.count = count.value;
                        console.log("groupCount: " + count.value);
                    }).error(function(){
                        console.log("groupCount: error");
                });
                
                //GETRANGE
                getRangeGroups();
        
//                $scope.$watch('currentPage', function() {
//                    getRange();
//                });
                function getRangeGroups(){
                    var fst = $scope.currentPage * $scope.pageSize;
                    DBProxy.findRangeGroups($scope.course.ccode, fst, $scope.pageSize)
                        .success(function(groups){
                            $scope.groups = groups;
                            console.log("groups: " + groups);
                        }).error(function(){
                            console.log("findRangeGroups: error");
                        });
                }
        
//                console.log("About to findGroups in GroupController");
//                DBProxy.findGroups($scope.course.ccode)
//                    .success(function(groups){
//                        $scope.groups = groups;
//                        console.log("groups: " + groups);
//                });
        });
        
//        $scope.pageSize = '15';
//        $scope.currentPage = 0;
//        
//        DBProxy.countGroups()
//                .success(function(count){
//                    $scope.count = count.value;
//                    console.log("groupCount: " + count.value);
//                }).error(function(){
//                    console.log("groupCount: error");
//        });
       
    }]);

controllers.controller('GroupAddController', ['$scope', '$routeParams', '$location', 'DBProxy',
    function ($scope, $location, $routeParams, DBProxy) {

        var wlh = window.location.hash;
        DBProxy.findCourse(wlh.substring(9, wlh.length - 9))
                .success(function (course) {
                    $scope.course = course;
//                alert($scope.course.name);
                });

        $scope.group = {
            createGroup: function (test) {
                $scope.group.user = getCookie("_userssnbr");
                $scope.group.course = $scope.course.ccode;
                DBProxy.createGroup($scope.group)
                        .success(function () {
                            console.log("Group created" + $scope.group);
                            //$location.path('/course' + $scope.group.course);
                            //$scope.group.createGroup();
                        }).error(function () {
                    console.log("errorsomething");
                });

            }
        };

    }]);

controllers.controller('CourseController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {
        $scope.pageSize = '15';
        $scope.currentPageSize = $scope.pageSize;
        $scope.currentPage = 0;
        
        
        var searchTimeout;

        $scope.course = {
            search: function () {
                clearTimeout(searchTimeout);
                searchTimeout = setTimeout(function() {$scope.currentPage = 0; getRange();}, 500);

            },
            select: function(course) {
                $location.path('/course/' + course);
            },
            isCourseListEmpty: function() {
                if(typeof $scope.courses !== 'undefined') {
                    return $scope.courses.length === 0;
                }else {
                    return true;
                }
                
            },
            searchfield: ""
        };

        /*   DBProxy.createCourse({cc:'111', name:'FirstCourse'});
         DBProxy.createCourse({cc:'222', name:'SecCourse'});
         DBProxy.createCourse({cc:'333', name:'ThirdCourse'});
         DBProxy.createCourse({cc:'444', name:'FourthCourse'});  
         DBProxy.createCourse({cc:'555', name:'FifthCourse'});*/


        DBProxy.countCourses()
                .success(function (count) {
                    $scope.count = count.value;
                    console.log("courseCount: " + count.value);
                }).error(function () {
            console.log("courseCount: error");
        });

        $scope.$watch('currentPage', function() {
            getRange();
        });
        function getRange() {
            var fst = $scope.pageSize * $scope.currentPage;
            DBProxy.searchInCoursesWithRange($scope.course.searchfield, fst, $scope.pageSize)
                    .success(function (courses) {
                        $scope.courses = courses;
                        $scope.currentPageSize = courses.length;
                        DBProxy.countSearchedCourses($scope.course.searchfield)
                                .success(function (response) {
                                    $scope.count = response.value;
                                }).error(function () {
                            console.log("error with countSearch");
                        });
                    }).error(function () {
                console.log("findRangeCourses: error");
            });
        };
    }]);

controllers.controller('TestController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {
        $scope.create = function () {
            DBProxy.create($scope.group);
        };
    }]);

controllers.controller('MenuController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {

        $scope.menu = {
            loggedIn: function () {
                return getCookie("_username") !== "";
            },
            logout: function () {
                setCookie("_userssnbr", "", 365);
                setCookie("_username", "", 365);
            },
            getLoginName: function () {
                return getCookie("_username");
            },
            getBreadcrumb: function () {
                var cbc = window.location.hash.substring(2);
                if (cbc === "course") {
                    return "";
                } else if (cbc.substring(0, 7) === "course/") {
                    if (endsWith(cbc, createGroupRef)) {
                        return cbc.substring(7, cbc.length - createGroupRef.length);
                    } else {
                        return cbc.substring(7);
                    }
                } else {
                    return cbc;
                }
            },
            showBreadcrumb: function () {
                return window.location.hash.substring(2) !== "course";
            }
        };
        var createGroupRef = "/newgroup";
        var endsWith = function (str, suffix) {
            return str.indexOf(suffix, str.length - suffix.length) !== -1;
        };
    }]);

controllers.controller('LoginController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {


        $scope.user = {
            login: function () {
                DBProxy.login($scope.user.ssnbr, $scope.user.pwd)
                        .success(function () {
                            console.log($scope.user.ssnbr + " " + $scope.user.pwd);
                            if (angular.isDefined($scope.user.ssnbr)) {
                                setCookie("_userssnbr", $scope.user.ssnbr, 365);
                                console.log("User ok, attempting to find name");
                                DBProxy.findUser($scope.user.ssnbr)
                                        .success(function (response) {
                                            console.log("Found user: " + JSON.stringify(response));
                                            setCookie("_username", response.fname + " " + response.lname, 365);
                                        }).error(function () {
                                    console.log("Could not find user");
                                });
                            }
                            $location.path('/index');
                        }).error(function (response) {
                    console.log("login failed");
                });
            },
            signUp: function () {
                DBProxy.createUser($scope.user)
                        .success(function () {
                            console.log("New user: " + $scope.user);
                            $scope.user.login();
                        });
            }

        };
    }]);
controllers.controller('AdminController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {

        DBProxy.isAdmin(getCookie("_userssnbr"))
                .success(function () {
                    
                }).error(function () {
                    $location.path("/course");
                    //alert("Access Denied");
                    
        });


        $scope.scraper = {
            courseScraper: function () {

                var items = retScrapedItems();
                //console.log("items: "+items.courseCode);
                $.each(items, function (index, val) {
                    console.log("something");
                    enterCourse(val.courseCode, val.courseName);

                });
            }
        };

        var enterCourse = function (code, cname) {
            var course = {
                cc: code,
                name: cname
            };
            DBProxy.createCourse(course)
                    .success(function () {
                        console.log("New Course: " + course.cc);
                    })
                    .error(function () {
                        console.log("Error when adding" + course.cc);
                    });
        };

        $scope.course = {
            createNewCourse: function () {
                var newCourse = {
                    cc: $scope.course.ccode,
                    name: $scope.course.name
                };

                DBProxy.createCourse(newCourse)
                        .success(function () {
                            $scope.course.status = 'Course ' + $scope.course.ccode + ' created effectively.';
                            console.log('Created course effectively ' + $scope.course.ccode);

                        }).error(function () {
                    $scope.course.status = 'ERROR! Course ' + $scope.course.ccode + ' not created.';
                    console.log('Could not create course ' + $scope.course.ccode);
                });

            }

        };

        getUsers();
        function getUsers() {
            DBProxy.findAllUsers()
                    .success(function (users) {
                        $scope.users = users;
                    }).error(function () {
                console.log("findAllUsers: error");
            });
        }

        getCourses();
        function getCourses() {
            DBProxy.findAllCourses()
                    .success(function (courses) {
                        $scope.courses = courses;
                    }).error(function () {
                console.log("findAllUsers: error");
            });
        }


    }]);

controllers.controller('EditUserController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {


        DBProxy.isAdmin(getCookie("_userssnbr"))
                .success(function () {
                    console.log("Admin!");
                }).error(function () {
                    $location.path("/course");
                    console.log("Access Denied!");
                    
        });
        
        var wlh = window.location.hash;
        DBProxy.findUser(wlh.substring(24))
                .success(function (user) {
                    $scope.user = user;
                    console.log('Editing ' + $scope.user.ssnbr);
                });

        $scope.user = {
            update: function () {
                console.log('Inside user.update() in AdminController');
                //TODO: Possible the wrong order
                DBProxy.updateUser($scope.user.id, $scope.user)
                        .success(function () {
                            alert('Updated!');
                            console.log('Updated ' + $scope.user.ssnbr);
                            $location.path('/hemligasidan');
                        }).error(function () {
                    console.log("updateUser: error");
                });
            }
        };


    }]);

controllers.controller('EditCourseController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {


        DBProxy.isAdmin(getCookie("_userssnbr"))
                .success(function () {
                    console.log("Admin!");
                }).error(function () {
                    $location.path("/course");
                    console.log("Access Denied!");
                    
        });
        //TODO: solve this by using pathquery instead
        var wlh = window.location.hash;
        DBProxy.findCourse(wlh.substring(26))
                .success(function (course) {
                    $scope.course = course;
                    console.log('Editing ' + $scope.course.cc);
                });

        $scope.course = {
            update: function () {
                console.log('Inside course.update() in AdminController');
                DBProxy.updateCourse($scope.course.ccode, $scope.course)
                        .success(function () {
                            alert('Updated!');
                            console.log('Updated ' + $scope.course.cc);
                            $location.path('/hemligasidan');
                        }).error(function () {
                    console.log("updateUser: error");
                });
            }
        };


    }]);
