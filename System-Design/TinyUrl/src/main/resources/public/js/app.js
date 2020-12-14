// create the module and name it tinyapp
var tinyapp = angular.module('tinyApp', ['ngRoute']);

//configure routes
// configure our routes
    tinyapp.config(function($httpProvider,$routeProvider,$locationProvider) {
    	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';    	
    	$routeProvider
            // route for the home page
            .when('/api/', {
                templateUrl : '/views/home.html',
                controller  : 'mainController'
            })

            // route for the about page
            .when('/api/about', {
                templateUrl : '/views/about.html',
                controller  : 'aboutController'
            })

            // route for the login page
            .when('/api/login', {
                templateUrl : '/views/login.html',
                controller  : 'loginController'
            })
            // route for the signup page
            .when('/api/register', {
                templateUrl : '/views/register.html',
                controller  : 'regController'
            })
            
            .when('/user/home',{
            	templateUrl : '/views/userPage.html',
            	controller : 'UserController'
            })	
    	
            // use the HTML5 History API
            $locationProvider.html5Mode(true);
    });


    // create the controller and inject Angular's $scope
    tinyapp.controller('mainController', function($scope) {
        // create a message to display in our view
        $scope.message = 'inside mainController';
        $scope.loggedIn = false;
    });

    tinyapp.controller('aboutController', function($scope) {
        // create a message to display in our view
        $scope.message = 'This is about page of our project';
    });

    tinyapp.controller('loginController', function($scope,$http) {
        $scope.message = "inside loginController";
        $scope.login = function(){
            console.log($scope.email);
            console.log($scope.password);
            
            $http({
            	  		method: "POST",
            	  		url: "/api/login",
            	  		headers: { "Content-Type":"application/json" },
                  		data:{username:$scope.email,password:$scope.password}
            		}).then(function successCallback(response) {
                            console.log(response.data);
                            if(response.data.statusCode == 200)
                            {
                            	window.location.href = "/user/home";
                            }
                            
            		    }, function errorCallback(response) {
            		        console.log("error");
            		});
              };
        
        $scope.gLogout = function() {
    		$http.post('/api/google/logout').success(function(res) {
    			console.log(res);
    		}).error(function(error) {
    			console.log("Logout error : ", error);
    		});
    	};
        
        
    });

    tinyapp.controller('regController', function($scope,$http) {
        $scope.message = "inside regController";
        $scope.reg = function(){
                    console.log($scope.email);
                    console.log($scope.password);
                    console.log($scope.fName);
                    console.log($scope.lName);
                    $http({
                    	  		method: "POST",
                    	  		url: "/api/register",
                    	  		headers: { "Content-Type": "application/json" },
                          		data:{
                      		        fName:$scope.fName,
                      		        lName:$scope.lName,
                      		        email:$scope.email,
                      		        password:$scope.password
                      		      }
                    		}).then(function successCallback(response) {
                                    console.log(response);
                                    window.location.href = "/api/login";
                    		    }, function errorCallback(response) {
                    		        console.log(response);
                    		});
                      };
    });
    
    tinyapp.controller('UserController', function($scope,$http) {
        $scope.message = "inside UserController";
        console.log($scope.message);

        $scope.getProfile = function()
        {
            console.log("in Profile");
            $http({
                method:"GET",
                url:"/user/profile"
            }).then(function successCallback(response){
                console.log(response);
                $scope.name = response.data.fName;
                $scope.email = response.data.email;
            }, function errorCallback(err){
                console.log(err);
            });
        }
        $scope.getProfile();

        $scope.generate =function()
        {
            console.log("Generate url");
            $http({
                                method: "POST",
                                url: "/url/generate",
                                headers: { "Content-Type": "application/json" },
                                data:{
                                    src:$scope.src,
                                  }
                            }).then(function successCallback(response) {
                                    console.log(response);
                                    $scope.output = response.data.output;
                                }, function errorCallback(response) {
                                    console.log(response);
                            });
        };

        $scope.data = function()
        {
            $scope.loggedIn = true;

            $http({
                        method: "GET",
                        url: "/user/data",
                        headers: { "Content-Type":"application/json" }
                    }).then(function successCallback(response) {
                            console.log(response);
                            //$scope.urls = response;
                        }, function errorCallback(response) {
                            console.log(response);
                    });
        };
});
