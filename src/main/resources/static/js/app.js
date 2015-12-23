var app =angular.module('app', ['ngRoute']);
app.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl : 'pages/list.html',
            controller  : 'userController'
        })
        .when('/new', {
            templateUrl : 'pages/edit.html',
            controller  : 'newController'
        })
        .when('/edit/:id', {
            templateUrl : 'pages/edit.html',
            controller  : 'editController'
        })
        .otherwise({redirectTo: "/"});
});
app.controller('userController', function($scope, $http) {
    $scope.ord = 'name';
    $scope.nameHeader = 'Name';
    $scope.emailHeader = 'Email';
    $scope.ageHeader = 'Age';
    $scope.currentPage = 0;
    $scope.pageSize = 5;
    $scope.numberOfPages=function(){
        return Math.ceil($scope.users.length/$scope.pageSize);
    };
    $http.get('/users').
    success(function(data) {
        $scope.users = data;
    });
    $scope.destroy = function(id) {
        $http.delete('/delete?id='+id).
        success(function() {
            $http.get('/users').
            success(function(data) {
                $scope.users = data;
            });
        });
    };
});
app.controller('newController', function($scope, $http, $location) {
    $scope.title = "New User";
    $scope.user = {name: "",email:""};
    $scope.save = function() {
        $http.post('/create?name='+$scope.user.name+'&email='+$scope.user.email+'&age='+$scope.user.age).
            success(function(){
            $location.path("/");
        });
    }
});
app.controller('editController', function($scope, $http, $location, $routeParams) {
    $scope.title = "Edit user";
    $http.get('/getbyid?id='+$routeParams.id).
    success(function(data) {
        $scope.user = data;
    });
    $scope.save = function() {
        $http.post('/update?id='+$scope.user.id+'&name='+$scope.user.name+'&email='+$scope.user.email+'&age='+$scope.user.age).
        success(function(){
            $location.path("/");
        });
    }
});
app.filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    }
});