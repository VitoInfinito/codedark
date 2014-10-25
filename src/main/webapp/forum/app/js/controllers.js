'use strict';


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

var newlyLoggedIn = true;
var delay = 300;

/*
 * Controllers
 */

var controllers = angular.module('Controllers', []);

// General navigation controller (possibly useful?)
controllers.controller('NavigationCtrl', ['$scope', '$location',
    function ($scope, $location) {
        $scope.navigate = function (url) {
            $location.path(url);
        };
    }]);

controllers.controller('GroupController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {
        
        $scope.pageSize = '5';
        $scope.currentPage = 0;

        $scope.group = {
            toggle: function (group) {
                console.log("gName of clicked group: " + group.gName);
                $('#toggleable' + group.id.value).collapse('toggle');
                $scope.members = group.members;
                console.log("");
            },
            join: function (e, group) {
                console.log('Join! ' + group.gName);
                e.stopPropagation();

                //NEEDS FIXING - DOESN'T KEEP UPDATE & LENGTHPROBLEM
                var user = getCookie("_userssnbr");
                DBProxy.joinGroup(group.course.id.value, group.gName, user)
                        .success(function (group) {
                           console.log("New members: " + group.members);
                           console.log("Old members: " + $scope.members);
                           $scope.members = group.members;
                           updateMemberNbr($scope.members);
                        });
            },
            updateMemberNbr: function(members){
                $parent.group.members.length = members.length;
            }
        };
        
        var getRangeGroups = function() {
            var fst = $scope.currentPage * $scope.pageSize;
            DBProxy.findRangeGroups($scope.course.id.value, fst, $scope.pageSize)
                    .success(function (groups) {
                        $scope.groups = groups;
                        console.log("groups: " + groups);
                    }).error(function () {
                console.log("findRangeGroups: error");
            });
        };

        DBProxy.findCourse(window.location.hash.substring(9))
                .success(function (course) {
                    $scope.course = course;
                    getRangeGroups();
                    //Updating count of total groups of course in database
                    DBProxy.countGroups($scope.course.id.value)
                            .success(function (count) {
                                $scope.count = count.value;
                                console.log("groupCount: " + count.value);
                                $scope.$watch('currentPage', function () {
                                    getRangeGroups();
                                });
                            }).error(function () {
                        console.log("groupCount: error");
                    });
                }).error(function() {
                    console.log("Error when finding course");
                });


        
    }]);

controllers.controller('GroupAddController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {


        var wlh = window.location.hash;
        DBProxy.findCourse(wlh.substring(9, wlh.length - 9))
                .success(function (course) {
                    $scope.course = course;
                });

        $scope.group = {
            createGroup: function () {
                $scope.group.user = getCookie("_userssnbr");
                $scope.group.course = $scope.course.id.value;
                var cc = $scope.group.course;
                DBProxy.createGroup($scope.group)
                        .success(function () {
                            console.log("Group created" + $scope.group.course);
//                            console.log($location.path("/index"));
                            $scope.group.status = 'Group ' + $scope.group.name + ' created effectively.';
                                $location.path('/index');

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
                searchTimeout = setTimeout(function () {
                    $scope.currentPage = 0;
                    getRange();
                }, delay);
            },
            select: function (courseId) {
                $location.path('/course/' + courseId);
            },
            isCourseListEmpty: function () {
                if (typeof $scope.courses !== 'undefined') {
                    return $scope.courses.length === 0;
                } else {
                    return true;
                }

            },
            searchfield: ""
        };


        DBProxy.countCourses()
                .success(function (count) {
                    $scope.count = count.value;
                    console.log("courseCount: " + count.value);
                }).error(function () {
            console.log("courseCount: error");
        });

        $scope.$watch('currentPage', function () {
            getRange();
        });
        var getRange = function () {
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
        }
        ;
    }]);

controllers.controller('TestController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {
        $scope.create = function () {
            DBProxy.create($scope.group);
        };
    }]);

controllers.controller('MenuController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {
        var admin = false;
        var checkIfAdmin = function () {
            DBProxy.isAdmin(getCookie("_userssnbr"))
                    .success(function () {
                        console.log("Admin!");
                        admin = true;
                    }).error(function () {

                console.log("Access Denied!");

            });
        };

        $scope.menu = {
            isAdmin: function () {
                if (newlyLoggedIn) {
                    checkIfAdmin();
                    newlyLoggedIn = false;
                }
                return admin;
            },
            loggedIn: function () {
                return getCookie("_username") !== "";
            },
            logout: function () {
                admin = false;
                setCookie("_userssnbr", "", 365);
                setCookie("_username", "", 365);
            },
            getLoginName: function () {
                return getCookie("_username");
            },
            getBreadcrumb: function () {
                var cbc = $location.url();
                if (cbc === "/course") {
                    return "";
                } else if (cbc.substring(0, 8) === "course/") {
                    if (endsWith(cbc, createGroupRef)) {
                        return cbc.substring(8, cbc.length - createGroupRef.length);
                    } else {
                        return cbc.substring(8);
                    }
                } else {
                    return cbc.substring(1);
                }
            },
            showBreadcrumb: function () {
                return $location.url() !== "/course";
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
                DBProxy.login($scope.user.id, $scope.user.pwd)
                        .success(function () {
                            if (angular.isDefined($scope.user.id)) {
                                setCookie("_userssnbr", $scope.user.id, 365);
                                console.log("User ok, attempting to find name: " + $scope.user.id);
                                DBProxy.findUser($scope.user.id)
                                        .success(function (response) {
                                            console.log("Found user: " + JSON.stringify(response));
                                            setCookie("_username", response.fname + " " + response.lname, 365);
                                            newlyLoggedIn = true;
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
                console.log($scope.user);
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

        $scope.count = 0;

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


        var searchUserTimeout;
        var getUsers = function () {
            DBProxy.searchInUsers($scope.user.searchfield)
                    .success(function (users) {
                        $scope.users = users;
                    }).error(function () {
                console.log("findAllUsers: error");
            });
        };

        var searchCourseTimeout;
        var getCourses = function () {
            DBProxy.searchInCourses($scope.course.searchfield)
                    .success(function (courses) {
                        $scope.courses = courses;
                    }).error(function () {
                console.log("findSearchedCourses: error");
            });
        };

        var searchGroupTimeout;
        var getGroups = function () {
            DBProxy.searchInGroups($scope.group.searchfield)
                    .success(function (groups) {
                        $scope.groups = groups;
                    }).error(function () {
                console.log("findSearchedGroups: error");
            });
        };

        $scope.course = {
            searchfield: "",
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

            },
            search: function () {
                clearTimeout(searchCourseTimeout);
                searchCourseTimeout = setTimeout(getCourses, delay);
            }
        };

        $scope.group = {
            searchfield: "",
            search: function () {

                console.log("groupsearch");
                clearTimeout(searchGroupTimeout);
                searchGroupTimeout = setTimeout(getGroups, delay);
            },
            createNewGroup: function () {
                var newGroup = {
                    owner: $scope.group.owner.id.value,
                    name: $scope.group.name,
                    course: $scope.group.course,
                    maxNbr: $scope.group.maxNbr
                };

                DBProxy.createGroup(newGroup)
                        .success(function () {
                            $scope.group.status = "Group " + $scope.group.name + " created successfuly";
                        }).error(function () {
                    $scope.group.status = "Error when creating " + $scope.group.name;
                });
            }
        };

        $scope.user = {
            searchfield: "",
            search: function () {
                clearTimeout(searchUserTimeout);
                searchUserTimeout = setTimeout(getUsers, delay);
            }
        };

        getUsers();
        getCourses();
        getGroups();

    }]);

controllers.controller('UserProfileController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {
        
        $scope.uName = getCookie("_userssnbr");

        findUser();
        function findUser() {
            DBProxy.findUser($scope.uName)
                    .success(function (user) {
                        $scope.user = user;
                    }).error(function () {
                console.log("findUser userprofilecontr: error");
            });
        }
        
        getUserGroups();
        function getUserGroups() {
            
            DBProxy.findUserGroups($scope.uName)
                    .success(function (groups) {
                        $scope.groups = groups;
                    }).error(function () {
                console.log("findUserGroups: error");
            });
        }
        
        $scope.group = {
            toggle: function (group) {
                console.log("in toggle group - profile");
                console.log("gName of clicked group: " + group.gName);
                $('#toggleable' + group.gName).collapse('toggle');
                $scope.members = group.members;
            },
            update: function() {
                
                $scope.user.admin = "gd";
                DBProxy.isAdmin($scope.uName)
                    .success(function () {
                        $scope.user.admin = "admin";
                        changeThings();
                    }).error(function () {
                        console.log("is not admin!!!");
                        changeThings();
                });
                console.log($scope.user.admin);
            }    
            
            
        };
        
        function changeThings(){    
            DBProxy.login($scope.uName, $scope.user.oldPwd)
                .success(function(){
                    $scope.user.pwd = $scope.user.newPwd;
                    DBProxy.updateUser($scope.user)
                        .success(function(user){
                            $scope.user = user;
                            $location.path('/user');
                        }).error(function(){
                            console.log("updateUser: error");
                        });
                }).error(function(){
                    alert("Wrong password!");
                    //kvar på samma sida och felmeddelande
                });
                
        }


    }]);

controllers.controller('EditUserController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {

        console.log("inside userEdit");
        

        $scope.userEdit = {
            
            update: function () {
                console.log('Inside user.update() in AdminController');
                if (typeof $scope.user.admin === 'undefined') {
                    $scope.user.admin = "";
                }
                $scope.user.admin = $scope.user.isAdmin;
                console.log($scope.user.admin);
                DBProxy.updateUser($scope.user)
                        .success(function () {
                            alert('Updated!');
                            console.log('Updated ' + $scope.user.id);
                            $location.path('/hemligasidan');
                        }).error(function () {
                    console.log("updateUser: error");
                });
            },
            isAdmin: function() {
                
                DBProxy.isAdmin(getCookie("_userssnbr"))
                .success(function () {
                    console.log("Admin!");
                    $('.isAdmin').prop('checked', true);
                   //$scope.user.isAdmin = true;
                }).error(function () {
                    $location.path("/course");
                console.log("Access Denied!");

                });
            }
        };
        var wlh = window.location.hash;
        DBProxy.findUser(wlh.substring(24))
                .success(function (user) {
                    $scope.user = user;
                    console.log('Editing ' + $scope.user.id);
                });

//        DBProxy.isAdmin(getCookie("_userssnbr"))
//                .success(function () {
//                    console.log("Admin!");
//                    $scope.user.isAdmin = true;
//                }).error(function () {
//            $location.path("/course");
//            console.log("Access Denied!");
//
//        });
            $scope.userEdit.isAdmin();
    }]);

controllers.controller('EditCourseController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {
        $scope.test3 = function () {

            console.log($scope.course);
        };

        console.log(1);
        $scope.courseEdit = {
            update: function () {
                console.log('Inside course.update() in AdminController');
                DBProxy.updateCourse($scope.course)
                        .success(function () {
                            alert('Updated!');
                            console.log('Updated ' + $scope.course.id.value);
                            $location.path('/hemligasidan');
                        }).error(function () {
                    console.log("updateUser: error");
                });
            },
            delete: function () {
                //DBProxy.deleteUser
            }
        };

        //TODO: solve this by using pathquery instead
        var wlh = window.location.hash;
        DBProxy.findCourse(wlh.substring(26))
                .success(function (course) {
                    $scope.course = course;
                    console.log('Editing ' + $scope.course.id.value);
                });

        DBProxy.isAdmin(getCookie("_userssnbr"))
                .success(function () {
                    console.log("Admin!");
                }).error(function () {
            $location.path("/course");
            console.log("Access Denied!");

        });
    }]);
controllers.controller('EditGroupController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {
        var wlh = window.location.hash;
        console.log("id: "+wlh.substring(25));
        DBProxy.findGroup(wlh.substring(25))
                .success(function (group) {
                    $scope.group = group;
                    console.log('Editing ' + $scope.group.gName);
                }).error(function () {
            console.log('Unable to get group');
        });
        $scope.groupEdit = {
            kick: function (member) {
                DBProxy.leaveGroup($scope.group.course.id.value, $scope.group.gName, member)
                        .success(function (group) {
                           console.log("Del: " +group.members);
                           
                        }).error(function(){
                            console.log("Deletion did not work");
                        });
            },
            update: function () {
                console.log("Update!");
                DBProxy.updateGroup($scope.group.id.value,$scope.group).success(function () {
                    console.log("Successfuly update: " + $scope.group);
                }).error(function () {
                    console.log("Error when updating: " + $scope.group);
                });
            }
        };

    }]);
