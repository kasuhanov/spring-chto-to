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
    $http.get('/users').
    success(function(data) {
        $scope.users = data;
    });
    $scope.destroy = function(id) {
        $http.delete('/delete?id='+id).
        success(function(data) {
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
        $http.post('/create?name='+$scope.user.name+'&email='+$scope.user.email).
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
        $http.post('/update?id='+$scope.user.id+'&name='+$scope.user.name+'&email='+$scope.user.email).
        success(function(){
            $location.path("/");
        });
    }
});
/*

function EditCtrl($scope, $location, $routeParams) {
    $scope.title = "Edit Crew";

    $scope.person = $scope.crew[$routeParams.id];
    $scope.save = function() {
        $location.path("/");
    }
}

function NewCtrl($scope, $location) {
    $scope.title = "New Crew";

    $scope.person = {name: "", description: ""};
    $scope.save = function() {
        $scope.crew.push($scope.person);
        $location.path("/");
    }
}

function AppCtrl($scope, $location, $http){
    $http.get('/users').
    success(function(data) {
        $scope.users = data;
    });

    $scope.destroy = function() {
        if (confirm("Are you sure?")) {
            $scope.crew.splice(self.id, 1);
            $location.path("/");
        }
    };
}*/
