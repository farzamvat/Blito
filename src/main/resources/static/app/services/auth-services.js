/**
 * Created by soroush on 4/21/17.
 */

angular.module('authServices', [])

    .service('Auth', function($http, AuthToken, config, $location) {
        var authService = this;


        authService.login = function (loginData) {
            authService.saveAttemptUrl();
            return $http.post(config.baseUrl+'/api/blito/v1.0/login', loginData)
                .then(function (data) {
                    console.log(data)
                    AuthToken.setToken(data.data);
                    return data;
                })

        };

        authService.isLoggedIn = function () {
            if(AuthToken.getToken()) {
                return true
            } else {
                return false;
            }
        };
        authService.loggedInRefresh = function () {
            if(AuthToken.getRefreshToken()) {
                return true
            } else {
                return false;
            }
        }

        authService.saveAttemptUrl = function () {
            if($location.path() !== '/') {
                config.redirectToUrlAfterLogin.url = $location.path();
            }
        }



        authService.getUser = function () {
                return $http.get(config.baseUrl + '/api/blito/v1.0/account/user-info');

        };
        authService.logout = function () {
            AuthToken.setToken();
        };


    })
    .service('updateInfo', function ($http, config) {
        var updateInfo = this;
        updateInfo.updateData = function (updateData) {
            return $http.post(config.baseUrl + '/api/blito/v1.0/account/update-info', updateData);
        }
    })
    .service('userCreate', function ($http, config) {
        var userService = this;


        userService.create = function (regData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/register', regData);
        }

    })

    .service('refresh', function ($http, config, AuthToken) {
        var refresh = this;
        refresh.getTokenByRefresh = function () {
            var refreshToken = {
                params: {
                    refresh_token: AuthToken.getRefreshToken()
                }
            };

            return $http.get(config.baseUrl + '/api/blito/v1.0/refresh', refreshToken);
        }
    })
    .service('AuthToken', function($window) {
        var authTokenService = this;



        authTokenService.setToken = function (data) {
            if(data) {
                    $window.localStorage.setItem('token', data.accessToken);
                    $window.localStorage.setItem('refresh-token', data.refreshToken);

            } else {
                    $window.localStorage.removeItem('token');
                    $window.localStorage.removeItem('refresh-token');


            }
        };
        authTokenService.getToken = function () {

                return $window.localStorage.getItem('token');

        };
        authTokenService.getRefreshToken = function () {

                return $window.localStorage.getItem('refresh-token');
        }
    })

    .service('AuthInterceptors', function (AuthToken, $q, $injector, config, $location) {
        var AuthInterceptors = this;
        var inFlightAuthRequest = null;

        AuthInterceptors.request = function (config) {

            var token = AuthToken.getToken();

            if(token) {
                config.headers['X-AUTH-TOKEN'] = 'Bearer '+token;
            }
            return config;
        };
        AuthInterceptors.responseError = function(rejection){

            var defer = $q.defer();
            var refreshToken = {
                params : {
                    refresh_token: AuthToken.getRefreshToken()
                }
            };
            switch (rejection.status) {
                case 401 :
                    if(!inFlightAuthRequest) {
                        inFlightAuthRequest = $injector.get("$http").get(config.baseUrl + '/api/blito/v1.0/refresh', refreshToken)
                    }
                    inFlightAuthRequest
                        .then(function (data, status) {
                            inFlightAuthRequest = null;
                            AuthToken.setToken(data.data);
                            $injector.get("$http")(rejection.config)
                                .then(function(resp) {
                                    defer.resolve(resp);
                                })
                                .catch(function () {
                                    defer.reject();
                                });
                        })
                        .catch(function (data, status) {
                            inFlightAuthRequest = null;
                            AuthToken.setToken();
                            defer.reject();
                        })
                    console.log("401");
                    break;
                case 404 :
                    console.dir("404");
                    defer.reject(rejection);
                    break;
                case 400 :
                    // $location.path('/');
                    // AuthToken.setToken();
                    console.dir("400");
                    defer.reject(rejection);
                    break;
                case 500 :
                    console.dir("500");
                    defer.reject(rejection);
                    break;
                default :
                    console.dir("default");
                    defer.reject(rejection);
                    break;
            }



            return defer.promise;

        }

    })

    .service('userInfo', function () {
        var userInfo = this;
        var userDataService = { firstname : 'کاربر', lastname : '', mobile : '', email : ''};

        userInfo.setData = function (userData) {
            userDataService.firstname = userData.firstname;
            userDataService.lastname = userData.lastname;
            userDataService.mobile = userData.mobile;
            userDataService.email = userData.email;
        }
        userInfo.getData = function () {
            return userDataService;
        }
    })

;

