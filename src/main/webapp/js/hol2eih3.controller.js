var initController = function($scope, $http, $filter){
	console.log("initController");
	$scope.readMoveTodayPatients = function(){
		var url = "/readMoveTodayPatients"
		if(parameters.date){
			var url = "/readMove-"+parameters.date+"-Patients"
		}
		console.log(url);
		$http({ method : 'GET', url : url
		}).success(function(data, status, headers, config) {
			console.log(data);
			$scope.moveTodayPatients = data;
		}).error(function(data, status, headers, config) {
			$scope.error = data;
		});
	}

	$scope.formatDateyyyyMMdd = function(d){
		return $filter('date')( d, "yyyy-MM-dd")
	}

	$scope.isMyDate = function(d){
		return parameters.date == $scope.formatDateyyyyMMdd(d);
	}

	
	$scope.today = new Date();
	console.log( $filter('date')( $scope.today, "yyyy-MM-dd"));
	$scope.last7day = [$scope.today];
	for (var i = 0; i < 7; i++) {
		$scope.last7day.push(new Date($scope.last7day[i].getTime() - (24*60*60*1000)));
	}
	$scope.last7day.reverse();

	if(parameters.date){
		$scope.paramDate = parameters.date;
	}
	
}
//  1  Запис надходжень/виписки хворих за сьогодні – saveMovePatients.html.
hol2eih3App.controller('SaveCopeTodayPatientsCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {
	initController($scope, $http, $filter);
	//  1.1  Зчитування надходження/виписки хворих на сьогодні – readTodayMovePatients
	$scope.readMoveTodayPatients();
	
	// 1.2   Запис надходження/виписки хворих на сьогодні – saveMoveTodayPatients
	$scope.saveMoveTodayPatients = function(){
		console.log("/saveMoveTodayPatients");
		$http({ method : 'POST', data : $scope.moveTodayPatients, url : "/saveMoveTodayPatients"
		}).success(function(data, status, headers, config){
			console.log(data);
			$scope.moveTodayPatients = data;
		}).error(function(data, status, headers, config) {
			$scope.error = data;
		});
	}

}]);
hol2eih3App.controller('MvPatientInWeekDayCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {
	console.log("/readMove-day-Patients");
	initController($scope, $http, $filter);
	//  1.1  Зчитування надходження/виписки хворих на сьогодні – readTodayMovePatients
	$scope.readMoveTodayPatients();
}]);

// 2  Показ кількості надходжень/виписки хворих за останні 7 днів – movePatients.html.
hol2eih3App.controller('MovePatientsCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {
	console.log("/readMovePatients");
	$scope.today = new Date();
	$scope.last7day = [$scope.today];
	for (var i = 0; i < 7; i++) {
		$scope.last7day.push(new Date($scope.last7day[i].getTime() - (24*60*60*1000)));
	}
	$scope.last7day.reverse();

	$scope.yyyyMMdd = function(d){
		return moment(d).format("YYYY-MM-DD");
	}

	// 2.1  Зчитування руху хворих за останні 7 днів – readMovePatients
	$scope.readMovePatients = function(){
		$http({ method : 'GET', url : "/readMovePatients"
		}).success(function(data, status, headers, config) {
			$scope.movePatients = data;
			console.log($scope.movePatients);
		}).error(function(data, status, headers, config) {
			$scope.error = data;
		});
	}
	$scope.readMovePatients();
	
}]);
// 3  План операцій – operationsPlan.html
hol2eih3App.controller('OperationsPlanCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {
	// 3.2  Зчитування плану операцій на сьогодні – readTodayOperationsPlan
	$scope.readTodayOperationsPlan = function(){
		$http({ method : 'GET', url : "/readTodayOperationsPlan"
		}).success(function(data, status, headers, config) {
			$scope.movePatients = data;
		}).error(function(data, status, headers, config) {
			$scope.error = data;
		});
	}
	$scope.readTodayOperationsPlan();
	// 3.1  Запис плану операцій на завтра – saveTomorrowOperationsPlan
	$scope.saveTomorrowOperationsPlan = function(){
		$http({ method : 'POST', data : $scope.moveTodayPatients, url : "/saveTomorrowOperationsPlan"
		}).success(function(data, status, headers, config){
			console.log(data);
			$scope.tomorrowOperationsPlan = data;
		}).error(function(data, status, headers, config) {
			$scope.error = data;
		});
	}

}]);
//4  Список операцій відділення по датах –  departmentOperationsList.html
hol2eih3App.controller('DepartmentOperationsListCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {
	// 4.1  Зчитування списку операцій за останні 7 днів –  readListOperationsPlan
	$scope.readListOperationsPlan = function(){
		$http({ method : 'GET', url : "/readListOperationsPlan"
		}).success(function(data, status, headers, config) {
			$scope.movePatients = data;
		}).error(function(data, status, headers, config) {
			$scope.error = data;
		});
	}
	$scope.readListOperationsPlan();
}]);
