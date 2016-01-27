var app = angular.module('app', ['ui.router','ui.bootstrap']);
app.run(function($rootScope){
    $rootScope.context = "";//TODO: add context root support
});
app.config(function($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('home', {
            url: '/{page}',
            templateUrl : 'pages/purchase_list.html',
            controller : 'PurchaseListController'
        })
        .state('category', {
            url: '/category/{id}/{page}',
            templateUrl : 'pages/category_purchase_list.html',
            controller : 'CategoryPurchasesController'
        })
        .state('purchase', {
            url: '/purchase/{id}',
            templateUrl : 'pages/purchase_detail.html',
            controller : 'PurchaseDetailController'
        })
        .state('customer', {
        	url : '/customer/{id}/{page}',
        	templateUrl : 'pages/customer_detail.html',
            controller  : 'CustomerDetailController'
        })
        .state('search',{
            url : '/search/?purchaseName&customer&minPrice&maxPrice&category/{page}',
        	templateUrl : 'pages/advanced_search.html',
            controller  : 'AdvancedSearchController'
        })
        .state('search-result', {
        	url : '/search/result/{search_text}/{page}',
        	templateUrl : 'pages/search_result.html',
            controller  : 'SearchResultController'
        });
	$urlRouterProvider.when('/category/{id}','/category/{id}/1');
	$urlRouterProvider.when('/customer/{id}','/customer/{id}/1');
	$urlRouterProvider.otherwise('/1');
});
app.directive('purchases', function () {
	return {
	    templateUrl: 'pages/purchase_list_template.html'
	}
});
app.controller('CategoryController', function($scope, $http) {
    $http.get('/category/all')
        .success(function(data) {
            $scope.categories = data;
    });
});
app.controller('SimpleSearchController', function($scope, $state) {
	$scope.search ={ text:""};
	$scope.submit = function(){
		if($scope.search.text)
			$state.transitionTo('search-result', {search_text:$scope.search.text});
		else 
			$state.transitionTo('search');
	};
});
app.controller('AdvancedSearchController', function($scope, $http, $state, $stateParams) {
	$scope.search = $stateParams;
    if(!angular.equals({}, $scope.search)){
        search($scope.search);
    }
    function search(params){
        $scope.currentPage =  $stateParams.page || 1;
        $scope.url='/search/advanced_paged?';
        if(params.purchaseName) {
            $scope.url+="&purchaseName="+params.purchaseName;
        }
        if(params.customer) {
            $scope.url+="&customer="+params.customer;
        }
        if(params.startDate) {
            $scope.url+="&startDate="+params.startDate.getTime();
        }
        if(params.endDate) {
            $scope.url+="&endDate="+params.endDate.getTime();
        }
        if(params.minPrice) {
            $scope.url+="&minPrice="+params.minPrice;
        }
        if(params.maxPrice) {
            $scope.url+="&maxPrice="+params.maxPrice;
        }
        if(params.category) {
            $scope.url+="&category="+params.category;
        }
        $scope.url+="&pageSize=";
        $scope.result=true;
        paginate($scope,$http,$state,$stateParams);
    }
	$scope.calendar1 = {
		    open: false
	};
	$scope.calendar2 = {
		    open: false
	};
	$scope.open1 = function() {
		$scope.calendar1.open = true;
	};
	$scope.open2 = function() {
		$scope.calendar2.open = true;
	};
	$http.get('/category/all')
    .success(function(data) {
        $scope.categories = data;
    });
	$scope.submit = function(){
        var stateParams = {};
        if($scope.search.purchaseName)
            stateParams.purchaseName=$scope.search.purchaseName;
        if($scope.search.customer)
            stateParams.customer=$scope.search.customer;
        if($scope.search.startDate) {
            //stateParams.startDate=$scope.search.startDate.getTime();
        }
        if($scope.search.endDate) {
            //stateParams.endDate=$scope.search.endDate.getTime();
        }
        if($scope.search.minPrice) {
            stateParams.minPrice=$scope.search.minPrice;
        }
        if($scope.search.maxPrice) {
            stateParams.maxPrice=$scope.search.maxPrice;
        }
        if($scope.search.category) {
            stateParams.category=$scope.search.category;
        }
        if(!angular.equals({}, $scope.search)){
            search($scope.search);
        }
        $state.transitionTo('search',stateParams,{notify:false});
    }
});
app.controller('PurchaseDetailController', function($scope, $http, $stateParams) {
    $http.get('/purchase/'+$stateParams.id)
        .success(function(data) {
            $scope.purchase = data;
        });
});
app.controller('SearchResultController', function($scope, $http, $stateParams, $state) {
	$scope.search ={ text: $stateParams.search_text};
    $scope.url='/search/simple_paged?text='+$scope.search.text+'&pageSize=';
    $scope.currentPage =  $stateParams.page || 1;
    paginate($scope,$http,$state,$stateParams);
});
app.controller('CategoryPurchasesController', function($scope, $http, $stateParams, $state) {
    $scope.url = '/category/' + $stateParams.id + '/purchases?pageSize=';
    $scope.currentPage = $stateParams.page || 1;
    $http.get("/category/"+$stateParams.id)
        .success(function(data) {
            $scope.category = data;
        });
    paginate($scope,$http,$state,$stateParams);
});
app.controller('PurchaseListController', function($scope, $http, $stateParams, $state) {
    $scope.url='/purchase/page?pageSize=';
    $scope.currentPage =  $stateParams.page || 1;
    paginate($scope,$http,$state,$stateParams);
});
app.controller('CustomerDetailController', function($scope, $http, $stateParams, $state){
    $http.get('/customer/'+$stateParams.id)
        .success(function(data) {
            $scope.customer = data;
            $scope.url='/customer/'+$stateParams.id+'/purchases?pageSize=';
        	$scope.currentPage = $stateParams.page || 1;
            paginate($scope,$http,$state,$stateParams);
    });
});
function paginate($scope,$http,$state,$stateParams){
    $scope.pageSize = 10;
    $scope.totalItems = 10000;
    $scope.url+=$scope.pageSize;
    getPage($scope.currentPage);
    function getPage(page){
        $http.get($scope.url+'&page='+(page-1))
            .success(function(data) {
                $scope.purchases = data.content;
                $scope.totalItems = data.totalElements;
            });
    }
    $scope.pageChanged = function(){
        getPage($scope.currentPage);
        $stateParams.page = $scope.currentPage;
        $state.transitionTo($state.current.name, $stateParams, { notify: false });
    };
}