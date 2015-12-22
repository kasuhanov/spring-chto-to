angular.module('lab', [])
    .controller('User', function($scope, $http) {
        $http.get('/users').
        success(function(data) {
            $scope.users = data;
        });
    });