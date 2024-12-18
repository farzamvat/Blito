/**
 * Created by soroush on 4/21/17.
 */

angular.module('authServices', [])

    .service('Auth', [
        '$http',
        'AuthToken',
        'config',
        '$location',

        function($http, AuthToken, config, $location) {
        var authService = this;


        authService.login = function (loginData) {
            authService.saveAttemptUrl();
            return $http.post(config.baseUrl+'/api/blito/v1.0/login', loginData)
                .then(function (data) {
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
        authService.resendActivationEmail = function (userEmail) {
            var queryParam = {
                params : { email : userEmail}
            }
            return $http.get(config.baseUrl + '/api/blito/v1.0/retry-activation', queryParam);
        };
        authService.logout = function () {
            AuthToken.setToken();
        };


    }])
    .service('updateInfo', [
        '$http',
        'config',
        function ($http, config) {
        var updateInfo = this;
        updateInfo.updateData = function (updateData) {
            return $http.post(config.baseUrl + '/api/blito/v1.0/account/update-info', updateData);
        };
        updateInfo.changePasswordSubmit = function (changePassword) {
            return $http.post(config.baseUrl + '/api/blito/v1.0/account/change-password', changePassword);
        };
        updateInfo.resetPassword = function (forgetPassEmail) {
            var queryParam = {
                params : {email: forgetPassEmail}
            };
            return $http.get(config.baseUrl + '/api/blito/v1.0/forget-password', queryParam);
        };
    }])
    .service('userCreate', [
        '$http',
        'config',
        function ($http, config) {
        var userService = this;


        userService.create = function (regData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/register', regData);
        }

    }])

    .service('refresh', [
        '$http',
        'config',
        'AuthToken',
        function ($http, config, AuthToken) {
        var refresh = this;
        refresh.getTokenByRefresh = function () {
            var refreshToken = {
                params: {
                    refresh_token: AuthToken.getRefreshToken()
                }
            };

            return $http.get(config.baseUrl + '/api/blito/v1.0/refresh', refreshToken);
        }
    }])
    .service('AuthToken', [
        '$window',
        function($window) {
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
            if($window.localStorage.getItem('refresh-token')) {
                self.parseJwt = function (token) {
                    var base64Url = token.split('.')[1];
                    var base64 = base64Url.replace('-', '+').replace('_', '/');
                    return JSON.parse($window.atob(base64));
                };
                var expireTime = self.parseJwt($window.localStorage.getItem('refresh-token'));
                var timeStamp = Math.floor(Date.now() / 1000);
                var timeCheck = expireTime.exp - timeStamp;
                if(timeCheck < 10) {
                    return "logOut";
                } else {
                    return $window.localStorage.getItem('refresh-token');
                }
            } else {
                return false;
            }

        }
    }])

    .service('AuthInterceptors', [
        'AuthToken',
        '$q',
        '$injector',
        'config',
        '$location',

        function (AuthToken, $q, $injector, config, $location) {
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
                                    defer.reject(rejection);
                                });
                        })
                        .catch(function (data, status) {
                            inFlightAuthRequest = null;
                            AuthToken.setToken();
                            defer.reject(rejection);
                        });
                    break;
                case 404 :
                    $location.path('/not-found');
                    defer.reject(rejection);
                    break;
                case 400 :
                    defer.reject(rejection);
                    break;
                case 500 :
                    defer.reject(rejection);
                    break;
                default :
                    defer.reject(rejection);
                    break;
            }



            return defer.promise;

        }

    }])

    .service('userInfo', [
        function () {
        var userInfo = this;
        var userDataService = { firstname : 'کاربر', lastname : '', mobile : '', email : ''};

        userInfo.setData = function (userData) {
            userDataService.firstname = userData.firstname;
            userDataService.lastname = userData.lastname;
            userDataService.mobile = userData.mobile;
            userDataService.email = userData.email;
        };
        userInfo.getData = function () {
            return userDataService;
        }
    }])

;

