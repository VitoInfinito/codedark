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

controllers.controller('GroupController', ['$scope', '$location', '$routeParams', 'DBProxy',
    function ($scope, $location, $routeParams, DBProxy) {
        
        $scope.pageSize = '5';
        $scope.currentPage = 0;
        
        $scope.group = {
            toggle: function (group) {
                console.log("gName of clicked group: " + group.gName);
                $('.toggleable').hide();
                $('#toggleable' + group.id.value).show();
                $scope.members = group.members;
                $scope.group.hasJoined = false;
                for(var i in group.members) {
                    if(group.members[i].id.value === getCookie('_userssnbr')) {
                        $scope.group.hasJoined = true;
                    }
                }
                
            },
            isFull: function(group){
                
                if (typeof $scope.group !== 'undefined') {
                    
                    return group.members.length === group.maxNbr ;
                } else {
                    return true;
                }
                
            },
            join: function (e, group) {
                console.log('Joining ' + group.gName);
                e.stopPropagation();

                //NEEDS FIXING - DOESN'T KEEP UPDATE & LENGTHPROBLEM
                var user = getCookie("_userssnbr");
                DBProxy.joinGroup(group.course.id.value, group.gName, user)
                        .success(function (foundGroup) {
                           getRangeGroups(foundGroup);
                        });
            },
            leave: function (e, group) {
                console.log('Leaving ' + group.gName);
                e.stopPropagation();

                //NEEDS FIXING - DOESN'T KEEP UPDATE & LENGTHPROBLEM
                var user = getCookie("_userssnbr");
                DBProxy.leaveGroup(group.course.id.value, group.gName, user)
                        .success(function (foundGroup) {
                           getRangeGroups(foundGroup);
                        });
            },
            hasJoined: false
        };
        
        var getRangeGroups = function(toggleGroup) {
            var fst = $scope.currentPage * $scope.pageSize;
            DBProxy.findRangeGroups($scope.course.id.value, fst, $scope.pageSize)
                    .success(function (groups) {
                        $scope.groups = groups;
                
                        //Still not working, trying to open the group that was selected
                        if(typeof toggleGroup !== 'undefined') {
                           $scope.group.toggle(toggleGroup); 
                        }
                        
                        console.log("groups: " + groups);
                    }).error(function () {
                console.log("findRangeGroups: error");
            });
        };
        
        DBProxy.findCourse($routeParams.cc)
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

controllers.controller('GroupAddController', ['$scope', '$location', '$routeParams','DBProxy',
    function ($scope, $location, $routeParams, DBProxy) {
        DBProxy.findCourse($routeParams.id)
                .success(function (course) {
                    $scope.course = course;
                });

        $scope.group = {
            createGroup: function () {
                $scope.group.user = getCookie("_userssnbr");
                $scope.group.course = $scope.course.id.value;

                DBProxy.createGroup($scope.group)
                        .success(function () {
                            console.log("Group created" + $scope.group.course);
                            $scope.group.status = 'Group ' + $scope.group.name + ' created effectively.';
                            
                            $location.path('/course/' + $scope.group.course);

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

controllers.controller('MenuController', ['$scope', '$location', '$routeParams', 'DBProxy',
    function ($scope, $location, $routeParams, DBProxy) {
        var admin = false;
        var checkIfAdmin = function () {
            DBProxy.isAdmin(getCookie("_userssnbr"))
                    .success(function (response) {
                        if(response === "User is admin") {
                            console.log("Admin found!");
                            admin = true;
                        }else {
                            console.log("Access Denied!");
                            admin = false;
                        }
                    }).error(function () {
                        console.log("Error when trying to locate admin");
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
                var breadCrumb = "";
                var bcr = "";
                if (cbc === "/course") {
                    breadCrumb = "";
                }else if (cbc.substring(0, 8) === "/course/") {
                    if (endsWith(cbc, "/newgroup")) {
                        breadCrumb = cbc.substring(8, cbc.length - 9);
                        bcr = "course/" + cbc.substring(8, cbc.length - 9);
                    } else {
                        breadCrumb = cbc.substring(8);
                    }
                }else if (cbc.substring(0,16) === "/editUserProfile" || 
                        cbc.substring(0,5) === "/user") {
                    breadCrumb = "Profile";
                    bcr = "user";
                }else if (cbc.substring(0,13) === "/hemligasidan") {
                    breadCrumb = "Admin";
                    if(cbc.substring(0,18) === "/hemligasidan/edit") {
                        bcr = "hemligasidan";
                    }
                }else {
                    breadCrumb = cbc.substring(1);
                }
                
                $scope.menu.getBreadcrumbRef = (bcr !== "") ? bcr : cbc.substring(1);
                return breadCrumb.charAt(0).toUpperCase() + breadCrumb.slice(1);;
            },
            showBreadcrumb: function () {
                return $location.url() !== "/course";
            },
            getBreadcrumbRef: ""
        };

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
                    $scope.user.status = 'Login failed, wrong username or password. Forgot password? Contact admin.';
                });
            },
            signUp: function () {
                console.log($scope.user);
                DBProxy.createUser($scope.user)
                        .success(function () {
                            console.log("New user: " + $scope.user);
                            $scope.user.login();
                        }).error(function(){
                            console.log("signup fail");
                        });
            }

        };
    }]);
controllers.controller('AdminController', ['$scope', '$location', 'DBProxy',
    function ($scope, $location, DBProxy) {

        $scope.count = 0;

        DBProxy.isAdmin(getCookie("_userssnbr"))
                .success(function (response) {
                    if(response === "User is admin") {
                            console.log("Admin found!");
                        }else {
                            console.log("Access Denied!");
                            $location.path("/course");
                        }
                }).error(function () {
                    $location.path("/course");
                    console.log("Error when trying to find admin");
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
                
                        $scope.users.forEach( function(user){
                            if( $.inArray('admin', user.belongingTo) !== -1 ){
                                user.isAdmin = "yes";
                            }else{
                                user.isAdmin = "no";
                            }
                        });
                
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
                    user: $scope.group.owner.id.value,
                    name: $scope.group.name,
                    course: $scope.group.course,
                    maxNbr: $scope.group.maxNbr
                };

                DBProxy.createGroup(newGroup)
                        .success(function () {
                            $scope.group.status = "Group " + $scope.group.name + " created successfuly";
                            getGroups();
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
            }, 
            signUp: function(user){
                DBProxy.createUser($scope.user)
                        .success(function (userCreated) {
                            console.log("New user: " + JSON.stringify(userCreated));
                            $scope.user.status = "User " + userCreated.id.value + "created effectively";
                        }).error(function(){
                            console.log("ERROR in createUser from admin");
                        });
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
                
                        if( $.inArray('admin', $scope.user.belongingTo) !== -1){
                            $scope.user.isAdmin = 'Admin';
                        }
                        
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
                console.log("Clicked on group " + group.gName);
                $('.toggleable').hide();
                $('#toggleable' + group.id.value).show();
                $scope.members = group.members;
                $scope.group.hasJoined = false;
                for(var i in group.members) {
                    if(group.members[i].id.value === getCookie('_userssnbr')) {
                        $scope.group.hasJoined = true;
                    }
                }
            },
            update: function() {
                
                $scope.user.admin = "gd";
                DBProxy.isAdmin($scope.uName)
                    .success(function (response) {
                        if(response === "User is admin") {
                            $scope.user.admin = "admin";
                            changeThings();
                        }
                        changeThings();
                }).error(function () {
                    console.log("Error when trying to find admin");
                });
            },
            leave: function (group) {
                console.log('Leaving ' + group.gName);
                DBProxy.leaveGroup(group.course.id.value, group.gName, $scope.user.id.value)
                        .success(function (foundGroup) {
                           getUserGroups();
                        });
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

controllers.controller('EditUserController', ['$scope', '$location', '$routeParams', 'DBProxy',
    function ($scope, $location, $routeParams, DBProxy) {

        $scope.userEdit = {
            update: function () {
                console.log("just entered update in useredit");
                
                if (typeof $scope.user.admin === 'undefined') {
                    $scope.user.admin = "";
                }
                
                console.log("filled admin with empty string");
                
                if( $('.isAdmin').prop('checked') ){
                    DBProxy.addAdmin($scope.user.id.value)
                            .success(function(user){
                                console.log("add admin: " + user.belongingTo);
                            }).error(function(){
                                console.log("ERROR in addAdmin: ");
                            });
                }else if( !$('.isAdmin').prop('checked') ){
                    DBProxy.removeAdmin($scope.user.id.value)
                            .success(function(user){
                                console.log("removed admin: " + user.belongingTo);
                            }).error(function(){
                                console.log("ERROR in addAdmin: ");
                            });
                }
                
                console.log("about to updateUser in contr");
                DBProxy.updateUser($scope.user)
                        .success(function () {
                            
                            console.log('Updated ' + $scope.user.id);
                            $location.path('/hemligasidan');
                        }).error(function () {
                    console.log("updateUser: error");
                });
            },
            isAdmin: function() {
                DBProxy.isAdmin(getCookie("_userssnbr"))
                .success(function (response) {
                    if(response === "User is admin") {
                        console.log("Admin!");
                    }else {
                        console.log("Access Denied!");
                        $location.path("/course");
                    }
                    
                }).error(function () {
                    console.log("Error when trying to find admin");
                });
                
                
                
                
            },
            delete: function() {
                console.log("in delete user");
                DBProxy.deleteUser($scope.user.id.value)
                        .success(function(){
                            console.log("User deleted");
                            $location.path('/hemligasidan');
                        }).error(function(){
                            console.log("ERROR IN DELETE USER");
                        });    
            },
            showDeleteModal: function() {
                console.log("in show delete modal");
                $("#dialog").html("Confirm Dialog Box");

                // TODO: Fix modal before delete!
                $("#dialog").dialog('open');
            }
        };

        DBProxy.findUser($routeParams.username)
                .success(function (user) {
                    $scope.user = user;
                    console.log('Editing ' + $scope.user.id);
                    
                    if( $.inArray('admin', $scope.user.belongingTo) !== -1 ){
                        $('.isAdmin').prop('checked', true);
                    }
                });

            $scope.userEdit.isAdmin();
    }]);

controllers.controller('EditCourseController', ['$scope', '$location', '$routeParams', 'DBProxy',
    function ($scope, $location, $routeParams, DBProxy) {
        $scope.courseEdit = {
            update: function () {
                DBProxy.updateCourse($scope.course)
                        .success(function () {
                            
                            console.log('Updated ' + $scope.course.id.value);
                            $location.path('/hemligasidan');
                        }).error(function () {
                    console.log("updateUser: error");
                });
            },
            delete: function () {
                DBProxy.deleteCourse($scope.course.id.value)
                    .success(function(){
                        console.log("Deleted the course!");
                        $location.path('/hemligasidan');
                    }).error(function(){
                        console.log("ERROR in delete course");
                    });
            }
        };
        
        DBProxy.findCourse($routeParams.cc)
                .success(function (course) {
                    $scope.course = course;
                    console.log('Editing ' + $scope.course.id.value);
                });

        DBProxy.isAdmin(getCookie("_userssnbr"))
                .success(function (response) {
                    if(response === "User is admin") {
                        console.log("Admin found!");
                    }else {
                        console.log("Access Denied!");
                        $location.path("/course");
                    }
                }).error(function () {
                    console.log("Error when trying to find user");
                });
    }]);

controllers.controller('EditGroupController', ['$scope', '$location', '$routeParams', 'DBProxy',
    function ($scope, $location, $routeParams, DBProxy) {
        console.log("Finding group with " + $routeParams.id);
        DBProxy.findGroup($routeParams.id)
                .success(function (group) {
                    $scope.group = group;
                    console.log('Successfully edited ' + $scope.group.gName);
                }).error(function () {
            console.log('Unable to get group');
        });
        $scope.groupEdit = {
            kick: function (member) {
                DBProxy.leaveGroup($scope.group.course.id.value, $scope.group.gName, member.id.value)
                        .success(function (group) {
                           console.log("Del: " +group.members);
                           $scope.group.members = group.members;
                           
                        }).error(function(){
                            console.log("Deletion did not work");
                        });
            },
            update: function () {
                console.log("Update!");
                
                DBProxy.updateGroup($scope.group.id.value,$scope.group).success(function () {
                    console.log("Successfuly updated " + $scope.group);
                    $location.path('/hemligasidan');
                    
                }).error(function () {
                    console.log("Error when updating " + $scope.group);
                });
            }
        };

    }]);
