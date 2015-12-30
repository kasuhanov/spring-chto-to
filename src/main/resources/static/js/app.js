var app =angular.module('app', ['ngRoute','ui.bootstrap']);

app.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl : 'pages/purchase_list.html',
            controller  : 'purchaseListController'
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
    $scope.pageSize = 5;
    $scope.totalItems = 0;
    $scope.currentPage = 1;
    var url='/purchase/getpage?pageSize='+$scope.pageSize;
    if($routeParams.id != undefined ) {
        url = '/category/' + $routeParams.id + '/purchases?pageSize=' + $scope.pageSize;
    }
    getPage($scope.currentPage);
    $scope.pageChanged = function(){
        getPage($scope.currentPage);
    };
    function getPage(page){
        $http.get(url+'&page='+(page-1)).
        success(function(data) {
            $scope.purchases = data.content;
            $scope.totalItems = data.totalElements;
        });
    }
});
app.controller('purchaseDetailController', function($scope, $http, $routeParams) {
    $http.get('/purchase/get/'+$routeParams.id).
    success(function(data) {
        $scope.purchase = data;
    });
});