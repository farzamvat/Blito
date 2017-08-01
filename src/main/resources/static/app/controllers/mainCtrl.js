/**
 * Created by soroush on 4/18/17.
 */
angular.module('menuPagesModule', [])
    .controller('mainCtrl', function ($rootScope,
                                      $scope,
                                      Auth,
                                      userCreate,
                                      $window,
                                      $timeout,
                                      $interval,
                                      $location,
                                      userInfo,
                                      AuthToken,
                                      refresh,
                                      config,
                                      dataService,
                                      updateInfo) {
        var main = this;
        main.checkingSession = false;
        $scope.loadPage = false;
        $scope.logout = function () {
            Auth.logout();
            main.logoutMenu = false;
            $window.location.assign('/');

        };

        main.checkSession = function () {
            if(AuthToken.getRefreshToken() === "logOut") {
                main.checkingSession = false;
                $scope.loggedIn = false;
                $scope.logout();
            } else if(AuthToken.getRefreshToken() !== "logOut" && AuthToken.getRefreshToken()) {
                main.checkingSession = true;
                $scope.loggedIn = true;
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
                        };
                        var expireTime = self.parseJwt(token);
                        var timeStamp = Math.floor(Date.now() / 1000);
                        var timeCheck = expireTime.exp - timeStamp;
                        if(timeCheck < 11) {
                            main.checkingSession = false;
                            $scope.loggedIn = false;
                            $scope.logout();
                            $interval.cancel(interval);
                        } else {

                        }
                    }
                }, 10000);
            }
        };
        main.checkSession();

        main.checkRefreshTokenValue = function () {
            if(!AuthToken.getRefreshToken() && AuthToken.getToken()) {
                Auth.logout();
                $scope.logoutMenu = false;
                $window.location.assign('/');
            }
        };
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
                })
        };
        if(main.checkingSession) {
            main.setUserData();
        }
        $rootScope.$on("$locationChangeStart", function(event, next, current) {
            $window.scroll(0,0);
            $("#navbar").removeClass("in");

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
            regData.mobile = dataService.persianToEnglishDigit(persianJs(regData.mobile).englishNumber().toString());
            userCreate.create(regData)
                .then(function (data, status) {
                    $scope.submitRegister = false;
                    main.successMsg = data.data.message;
                    userInfo.setData(data.config.data);
                    $location.path('/activate-user');
                    $("#registrationModal").modal("hide");
                })
                .catch(function (data, status) {
                    $scope.submitRegister = false;
                    $scope.registerErrorNotif = true;
                    document.getElementById("registerError").innerHTML= data.data.message;
                })
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
                    $scope.loggedIn = true;
                    main.checkSession();
                    $location.path(config.redirectToUrlAfterLogin.url);
                    main.setUserData();
                    $("#registrationModal").modal("hide");
                    $scope.Msg = "با موفقیت وارد شدید !";
                    $("#notification").modal("show");
                    $timeout(function () {
                        $("#notification").modal("hide");
                    },2000)
                })
                .catch(function (data, status) {
                    $scope.loginErrorNotif = true;
                    $scope.submitLogin = false;
                    document.getElementById("loginError").innerHTML = data.data.message;
                    $scope.submitLogin = false;
                    $scope.Msg = "ورود نا موفق بود";
                })
            ;
        };
        $scope.forgetPassModal = function () {
            $("#registrationModal").modal("hide");
            $("#forgetPass").modal("show");
            document.getElementById("successForgetPass").style.display = "none";
            document.getElementById("errorForgetPass").style.display = "none";
        };
        $scope.resetPasswordSubmit = function (email) {
            document.getElementById("successForgetPass").style.display = "none";
            document.getElementById("errorForgetPass").style.display = "none";
            document.getElementById("spinnerForgetPass").style.display = "inline";
            updateInfo.resetPassword(email)
                .then(function () {
                    document.getElementById("spinnerForgetPass").style.display = "none";
                    document.getElementById("successForgetPass").style.display = "block";
                })
                .catch(function (data) {
                    document.getElementById("spinnerForgetPass").style.display = "none";
                    document.getElementById("errorForgetPass").style.display = "block";
                    document.getElementById("errorForgetPass").innerHTML= data.data.message;
                })
        };

        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };
        var isOpen =false;
        $scope.DropDownMenue = function () {
            isOpen=!isOpen;
            if(isOpen)
                $(angular.element(document.getElementsByClassName('dropdown-menu'))).slideDown(300);
            else
                $(angular.element(document.getElementsByClassName('dropdown-menu'))).slideUp(300);
        };
    });

