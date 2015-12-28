var app =angular.module('app', ['ngRoute']);
app.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            //templateUrl : 'pages/list.html',
            controller  : 'userController'
        })
        .when('/category/:id', {
            templateUrl : 'pages/purchase_list.html',
            controller  : 'purchaseListController'
        })
        .when('/purchase/:id', {
            templateUrl : 'pages/purchase_detail.html',
            controller  : 'purchaseDetailController'
        })
        .otherwise({redirectTo: "/"});
});
app.controller('CategoryController', function($scope, $http) {
    $http.get('/category/getall').
    success(function(data) {
        $scope.categories = data;
    });
});
app.controller('purchaseListController', function($scope, $http, $routeParams) {
    $http.get('/category/getpurchases?id='+$routeParams.id).
    success(function(data) {
        $scope.purchases = data;
    });
});
app.controller('purchaseDetailController', function($scope, $http, $routeParams) {
    $http.get('/purchase/getbyid?id='+$routeParams.id).
    success(function(data) {
        $scope.purchase = data;
    });
});