var app =angular.module('app', ['ngRoute']);
app.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            //templateUrl : 'pages/list.html',
            controller  : 'userController'
        })
        .otherwise({redirectTo: "/"});
});
app.controller('CategoryController', function($scope, $http) {
    $http.get('/category/getall').
    success(function(data) {
        $scope.categories = data;
    });
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
    $http.get('/user/getall').
    success(function(data) {
        $scope.users = data;
    });
    $scope.destroy = function(id) {
        $http.delete('/user/delete?id='+id).
        success(function() {
            $http.get('/user/getall').
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
        $http.post('/user/create?name='+$scope.user.name+'&email='+$scope.user.email+'&age='+$scope.user.age).
            success(function(){
            $location.path("/");
        });
    }
});
app.controller('editController', function($scope, $http, $location, $routeParams) {
    $scope.title = "Edit user";
    $http.get('/user/getbyid?id='+$routeParams.id).
    success(function(data) {
        $scope.user = data;
    });
    $scope.save = function() {
        $http.post('/user/update?id='+$scope.user.id+'&name='+$scope.user.name+'&email='+$scope.user.email+'&age='+$scope.user.age).
        success(function(){
            $location.path("/");
        });
    }
});