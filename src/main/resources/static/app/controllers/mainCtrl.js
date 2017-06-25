/**
 * Created by soroush on 4/18/17.
 */
angular.module('menuPagesModule', [])
    .controller('mainCtrl', function ($rootScope, $scope, Auth, userCreate, $window, $timeout, $interval, $location, userInfo, AuthToken, refresh, config) {
        var main = this;
        main.checkingSession = false;
        $scope.loadPage = false;




        //checks users refresh-token every 10 second
        main.checkSession = function () {
            if(AuthToken.getRefreshToken()) {
                main.checkingSession = true;
                var interval = $interval(function () {
                    var token = AuthToken.getRefreshToken();
                    if(!token) {
                        $interval.cancel(interval);
                    } else {
                        if(!Auth.isLoggedIn())  {
                            refresh.getTokenByRefresh()
                                .then(function (data) {
                                    AuthToken.setToken(data.data);
                                })
                        }
                        self.parseJwt = function (token) {
                            var base64Url = token.split('.')[1];
                            var base64 = base64Url.replace('-', '+').replace('_', '/');
                            return JSON.parse($window.atob(base64));
                        }
                        var expireTime = self.parseJwt(token);
                        var timeStamp = Math.floor(Date.now() / 1000);
                        var timeCheck = expireTime.exp - timeStamp;
                        console.log(timeCheck);
                        if(timeCheck < 1) {
                            main.checkingSession = false;
                            $scope.logout();
                            $interval.cancel(interval);
                        } else {

                        }
                    }
                }, 10000);
            } else {
            }
        };
        main.checkSession();

        main.checkRefreshTokenValue = function () {
            if(!AuthToken.getRefreshToken() && AuthToken.getToken()) {
                Auth.logout();
                $scope.logoutMenu = false;
                $window.location.assign('/');
            }
        }
        main.checkRefreshTokenValue();
        // requests server for user info and sets data in service
        main.setUserData = function () {
            Auth.getUser()
                .then(function (data, status) {
                    $scope.logoutMenu = true;
                    main.userData = data.data;
                    userInfo.setData(main.userData);
                    $scope.firstname = main.userData.firstname;
                    $scope.lastname = main.userData.lastname;
                    $scope.mobile = main.userData.mobile;
                    $scope.userEmail = main.userData.email;
                },function (data, status) {

                }, function (data) {
                    console.log("updated");
                })
        };
        if(main.checkingSession) {
            main.setUserData();
         }



        // on route change
        $rootScope.$on("$locationChangeStart", function(event, next, current) {
            main.checkRefreshTokenValue();
            if(Auth.isLoggedIn()) {
                main.setUserData();
            }

        });



        $scope.regUser = function (regData) {
            main.loading = true;
            main.errorMsg = false;
            $scope.registerErrorNotif = false;
            $scope.submitRegister = true;
            $timeout(function () {
                userCreate.create(regData)
                    .then(function (data, status) {
                        $scope.submitRegister = false;
                        main.successMsg = data.data.message;
                        userInfo.setData(data.config.data);
                        $location.path('/activate-user');
                    })
                    .catch(function (data, status) {
                        $scope.submitRegister = false;
                        $scope.registerErrorNotif = true;

                        document.getElementById("registerError").innerHTML= data.data.message;
                        console.log(data);
                    })
            },200)
        };

        $scope.login = function (loginData) {
            main.loading = true;
            main.errorMsg = false;
            $scope.loginErrorNotif = false;
            $scope.loginSuccessNotif = false;

            $scope.submitLogin = true;

            Auth.login(loginData)
                .then(function (data, status) {
                    $scope.loginSuccessNotif = true;
                    $scope.submitLogin = false;
                    main.checkSession();
                    console.log(data);
                    $location.path(config.redirectToUrlAfterLogin.url);
                    main.setUserData();
                    console.log("ok");
                    })
                .catch(function (data, status) {
                    $scope.loginErrorNotif = true;
                    $scope.submitLogin = false;
                    console.log(data.data.message);
                    console.log(document.getElementById("loginError"));
                    document.getElementById("loginError").innerHTML = data.data.message;
                    $scope.submitLogin = false;
                    $scope.Msg = "ورود نا موفق بود";
                })
            ;
        };

        $scope.logout = function () {
            Auth.logout();
            main.logoutMenu = false;
            $window.location.assign('/');

        };

        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        }



        $scope.nextStep1 = function (eventInfo) {
            $scope.totalPrice = eventInfo.ticketNumber * 10000;
            angular.element(document.getElementsByClassName('progress-bar')).css('width', '50%');
            angular.element(document.getElementById('ticketPay1')).removeClass('active');
            angular.element(document.getElementById('selectTicket')).removeClass('active');
            angular.element(document.getElementById('ticketPay2')).addClass('active').addClass('in');
            angular.element(document.getElementById('payment')).addClass('active');
        }
        $scope.prevStep1 = function () {
            angular.element(document.getElementsByClassName('progress-bar')).css('width', '0');
            angular.element(document.getElementById('ticketPay1')).addClass('active').addClass('in');
            angular.element(document.getElementById('selectTicket')).addClass('active');
            angular.element(document.getElementById('ticketPay2')).removeClass('active');

            angular.element(document.getElementById('payment')).removeClass('active');
        }
        $scope.nextStep2 = function () {
            angular.element(document.getElementsByClassName('progress-bar')).css('width', '100%');
            angular.element(document.getElementById('ticketPay2')).removeClass('active');
            angular.element(document.getElementById('payment')).removeClass('active');
            angular.element(document.getElementById('ticketPay3')).addClass('active').addClass('in');
            angular.element(document.getElementById('paymentComplete')).addClass('active');

        }
        $scope.hideTicketPaymentModal = function () {
            $("#buyTicket").modal("hide");
        }


    });

