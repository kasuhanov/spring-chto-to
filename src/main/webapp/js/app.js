var app = angular.module('app', ['ngSanitize','ngMessages','ui.router','ui.bootstrap']);
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
            url : '/search/?purchaseName&customer&minPrice&maxPrice&category&type&completed&startDate&endDate/{page}',
        	templateUrl : 'pages/advanced_search.html',
            controller  : 'AdvancedSearchController'
        })
        .state('search-result', {
        	url : '/search/result/{search_text}/{page}',
        	templateUrl : 'pages/search_result.html',
            controller  : 'SearchResultController'
        })
        .state('registration',{
            url : '/registration/',
        	templateUrl : 'pages/registration.html',
            controller  : 'RegistrationController'
        })
        .state('verification',{
            url : '/verify/{token}',
        	templateUrl : 'pages/verification.html',
            controller  : 'VerificationController'
        })
        .state('recovery',{
            url : '/recovery/',
        	templateUrl : 'pages/recovery.html',
            controller  : 'RecoveryController'
        })
        .state('recovery-pas',{
            url : '/recovery/{token}',
        	templateUrl : 'pages/recovery-pas.html',
            controller  : 'RecoveryPasController'
        });
	$urlRouterProvider.when('/category/{id}','/category/{id}/1');
	$urlRouterProvider.when('/customer/{id}','/customer/{id}/1');
	$urlRouterProvider.otherwise('/1');
});
app.controller('RecoveryPasController', function($scope, $stateParams, $rootScope, $http){
	$scope.user = {
		password:"",
		confirmPassword:""
	};
	$scope.recover = function(){
		$scope.timeout = false;
		$scope.failed = false;
		$scope.success = false;
		$http.post("recovery/" + $stateParams.token, $scope.user.password)
		.success(function(data){
			$scope.success = true;
		})
		.error(function(data, status, header, config){
			if(status == 409){
				$scope.timeout = true;
			}else{
				$scope.failed = true;	
			}
		});
	};
	$scope.resend = function (){
		$http.get("resend-password/"+$stateParams.token)
		.success(function(data){
			$scope.sendSuccess = true;
		})
		.error(function(data, status, header, config){
			$scope.failed = true
		});
	};
});
app.controller('RecoveryController', function($scope, $rootScope, $http){
	$scope.user = {
		email:""
	};
	$scope.recover = function(){
		$scope.noSuchEmail = false;
		$scope.failed = false;
		$scope.success = false;
		$http.post("recovery",$scope.user.email)
		.success(function(data){
			$scope.success = true;
		})
		.error(function(data, status, header, config){
			if(status == 409){
				$scope.noSuchEmail = true;
			}else{
				$scope.failed = true;
			}
		});
	};
});
app.controller('VerificationController', function($scope, $stateParams, $http) {
	$http.get("verify/"+$stateParams.token)
		.success(function(data){
			$scope.success = true;
		})
		.error(function(data, status, header, config){
			if(status == 409){
				$scope.timeout = true;
			}else{
				$scope.failed = true;	
			}
		});
	$scope.resend = function (){
		$http.get("resend/"+$stateParams.token)
		.success(function(data){
			$scope.sendSuccess = true;
		})
		.error(function(data, status, header, config){
			$scope.failed = true
		});
	}
});
app.controller('CabinetController', function($scope, $rootScope, $http){
	$scope.logOut = function(){
		$http.post('logout', {}).success(function() {
		    $rootScope.authenticated = false;
		  })
		  .error(function(){
			  $rootScope.authenticated = false;
		  });
	}
});
app.controller('LoginController', function($scope, $http, $rootScope) {
	$scope.user = {
			email:"",
			password:""
	};
	$scope.login = function(){
		$http.post("login",$scope.user)
		.success(function(data){
			$scope.failed = false;
			$rootScope.authenticated = true;
		})
		.error(function(data, status, header, config){
			$scope.failed = true;
		});
	};
});
app.controller('RegistrationController', function($scope, $rootScope, $http){
	$scope.user = {
		email:"",
		password:"",
		confirmPassword:""
	};
	$scope.registrate = function(){
		$scope.emailExists = false;
		$scope.failed = false;
		$scope.success = false;
		$http.post("registrate",$scope.user)
		.success(function(data){
			$scope.success = true;
		})
		.error(function(data, status, header, config){
			if(status == 409){
				$scope.emailExists = true;
			}else{
				$scope.failed = true;
			}
		});
	};
});
app.controller('CategoryController', function($scope, $http) {
    $http.get('category/all')
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
	if($stateParams.startDate !== undefined)
		$scope.search.startDate = new Date(Number($stateParams.startDate));
	if($stateParams.endDate !== undefined)
		$scope.search.endDate = new Date(Number($stateParams.endDate));
    if(!angular.equals({}, $scope.search)){
        search($scope.search);
    }
    function search(params){
        $scope.currentPage =  $stateParams.page || 1;
        $scope.url='search/advanced_paged?';
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
        if(params.type) {
            $scope.url+="&type="+params.type;
        }
        if(params.completed) {
            $scope.url+="&completed="+params.completed;
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
	$http.get('category/all')
    .success(function(data) {
        $scope.categories = data;
    });
	$scope.submit = function(){
        var stateParams = {};
        $scope.currentPage = 1;
        $stateParams.page  = 1;
        if($scope.search.purchaseName)
            stateParams.purchaseName=$scope.search.purchaseName;
        if($scope.search.customer)
            stateParams.customer=$scope.search.customer;
        if($scope.search.startDate) {
            stateParams.startDate=$scope.search.startDate.getTime();
        }
        if($scope.search.endDate) {
            stateParams.endDate=$scope.search.endDate.getTime();
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
        if($scope.search.type) {
            stateParams.type=$scope.search.type;
        }
        if($scope.search.completed) {
            stateParams.completed=$scope.search.completed;
        }
        if(!angular.equals({}, $scope.search)){
            search($scope.search);
        }
        $state.transitionTo('search',stateParams,{notify:false});
    }
});
app.controller('PurchaseDetailController', function($scope, $http, $stateParams) {
    $http.get('purchase/'+$stateParams.id)
        .success(function(data) {
            $scope.purchase = data;
        });
});
app.controller('SearchResultController', function($scope, $http, $stateParams, $state) {
	$scope.search ={ text: $stateParams.search_text};
    $scope.url='search/simple_paged?text='+$scope.search.text+'&pageSize=';
    $scope.currentPage =  $stateParams.page || 1;
    paginate($scope,$http,$state,$stateParams);
});
app.controller('CategoryPurchasesController', function($scope, $http, $stateParams, $state) {
    $scope.url = 'category/' + $stateParams.id + '/purchases?pageSize=';
    $scope.currentPage = $stateParams.page || 1;
    $http.get("category/"+$stateParams.id)
        .success(function(data) {
            $scope.category = data;
        });
    paginate($scope,$http,$state,$stateParams);
});
app.controller('PurchaseListController', function($scope, $http, $stateParams, $state) {
    $scope.url='purchase/page?pageSize=';
    $scope.currentPage =  $stateParams.page || 1;
    paginate($scope,$http,$state,$stateParams);
});
app.controller('CustomerDetailController', function($scope, $http, $stateParams, $state){
    $http.get('customer/'+$stateParams.id)
        .success(function(data) {
            $scope.customer = data;
            $scope.url='customer/'+$stateParams.id+'/purchases?pageSize=';
        	$scope.currentPage = $stateParams.page || 1;
            paginate($scope,$http,$state,$stateParams);
    });
});
function paginate($scope,$http,$state,$stateParams){
    $scope.pageSize = 10;
    $scope.totalItems = 100000;
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
app.directive('purchases', function () {
	return {
	    templateUrl: 'pages/purchase_list_template.html'
	}
});
app.directive("compareTo", function(){
	return {
        require: "ngModel",
        scope: {
            otherModelValue: "=compareTo"
        },
        link: function(scope, element, attributes, ngModel) {
             
            ngModel.$validators.compareTo = function(modelValue) {
                return modelValue == scope.otherModelValue;
            };
 
            scope.$watch("otherModelValue", function() {
                ngModel.$validate();
            });
        }
    };
});