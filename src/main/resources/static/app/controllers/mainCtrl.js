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
                                      updateInfo,
                                      $routeParams) {
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
        var isOpen =false;
        $scope.DropDownMenue = function () {
            isOpen=!isOpen;
            if(isOpen) {
                $(angular.element(document.getElementsByClassName('dropdown-menu'))).slideDown(300);
            }
            else {
                $(angular.element(document.getElementsByClassName('dropdown-menu'))).slideUp(300);
            }
        };
        $rootScope.$on("$locationChangeStart", function(event, next, current) {
            $window.scroll(0,0);
            $("#navbar").removeClass("in");
            isOpen =true;
            $scope.DropDownMenue();
            main.checkRefreshTokenValue();
            if(Auth.isLoggedIn()) {
                main.setUserData();
            }
            switch ($location.path()) {
                case '/':
                    $scope.title = 'رویدادهای فرهنگی و هنری';
                    $scope.pageDescription = 'خرید بلیت کنسرت ، خرید بلیت تیاتر، خرید بلیط کنسرت ، خرید بلیط تیاتر، بلیت جشنواره فیلم فجر، بلیط جشنواره فیلم فجر، لاکی کلاور کافه ، کافه گالری شناخت، موسسه فرهنگی و هنری رایزن، اجرای کافه ای، کارگاه عکاسی ، جز، عمارت رو به رو، رویداد های فرهنگی و هنری، کافه گالری، وزرات ارشاد، بلیت تعویضی، اگهی بلیط، فیلم کوتاه ، جشنواره فیلم کوتاه ، جشنواره موسیقی، فرهنگ و هنر، کارگاه تیاتر، کارگاه نویسندگی ، کارگاه بازیگری، خانه نمایش دا ، کافه های تهران، کنسرت تهران، اجرای موسیقی، رویداد فرهنگی و هنری، پلتفرم داربست، گالری محسن، گالری تهران، گالری، تور گردشگری، تور طبیعت گردی، کافه گالری، ایران کنسرت ، تیوال ، ایوند ، اکران کافه ای، رویداد های شهر تهران ، بلیت، بلیط،تئاتر،بلیتو';
                    $scope.robotValue = 'index';
                    break;
                case '/not-found' :
                    $scope.title = 404;
                    $scope.robotValue = 'noindex';
                    break;
                case '/about-us' :
                    $scope.title = 'درباره ما';
                    $scope.robotValue = 'noindex';
                    break;
                case '/term-of-use' :
                    $scope.title = 'قوانین';
                    $scope.robotValue = 'noindex,nofollow';
                    break;
                case '/privacy-policy' :
                    $scope.title = 'حریم خصوصی';
                    $scope.robotValue = 'noindex,nofollow';
                    break;
                case '/entertainment' :
                    $scope.title = 'سرگرمی';
                    $scope.pageDescription = 'سرگرمی و تفریحی، بلیت سرگرمی، بلیط تفریحی، بلیت بازی،بلیت بورد،بازی بورد،کافه بازی،رویداد سرگرمی، رویداد تفریحی،بلیتو';
                    $scope.robotValue = 'index';
                    break;
                case '/exhibition' :
                    $scope.title = 'نمایشگاه';
                    $scope.pageDescription = 'بلیت گالری، بلیت نمایشگاه، بلیت هنری، بلیت اجرا، بلیت رایگان، اطلاع رسانی رویداد، نمایش رویداد، بلیت رویداد، بلیت فرهنگی، رویداد فرهنگی، رویداد هنری، بلیتو';
                    $scope.robotValue = 'index';
                    break;
                case '/tour' :
                    $scope.title = 'تور';
                    $scope.pageDescription = 'تور و گردشگری، رویداد تور، بلیت تور، بلیط تور، بلیت گردشگری، ایران گردی، گردش و تفریح،طبیعت گردی،بلیت ایران گردی،برنامه تور، بلیتو';
                    $scope.robotValue = 'index';
                    break;
                case '/theater' :
                    $scope.title = 'تئاتر';
                    $scope.pageDescription = 'بلیت تئاتر،بلیت نمایش، نمایش و تئاتر، اجرای تئاتر، بلیت تیوال، تئاتر تهران، تئاتر شهر، تئاتر تالار وحدت، نمایش خان، نمایش خانه دا،بلیتو';
                    $scope.robotValue = 'index';
                    break;
                case '/concert' :
                    $scope.title = 'کنسرت';
                    $scope.pageDescription = 'اجرای موسیقی، بلیت کنسرت، بلیت موسیقی، اجرای کافه ای،جز نایت، فستیوال مویسقی،رویداد های کافه ای، رویداد کافه،اجرای ساز،کافه شناخت، کافه موسیقی، کافه لاکی کلاور، کافه رایزن،بلیتو';
                    $scope.robotValue = 'index';
                    break;
                case '/cinema' :
                    $scope.title = 'سینما';
                    $scope.pageDescription = 'بلیت سینما، بلیت فیلم، بلیت فیلم کوتاه، بلیت نمایش، نمایش خانه،بلیت فیلم بلند، سینمای تهران،نمایش کافه،بلیتو';
                    $scope.robotValue = 'index';
                    break;
                case '/workshop' :
                    $scope.title = 'کارگاه';
                    $scope.pageDescription = 'بلیت کارگاه، کارگاه علمی، کارگاه هنری،کارگاه مدیریتی، کارگاه تبلیغاتی، یادگیری، ایوند، کارگاه استارتاپی،بلیتو';
                    $scope.robotValue = 'index';
                    break;
                case '/other' :
                    $scope.title = 'سایر';
                    $scope.pageDescription = 'بلیت رویداد، بلیت رویداد های تفریحی، رویداد تفریحی، رویداد هنری، رویداد سرگرمی،بلیتو';
                    $scope.robotValue = 'index';
                    break;
                case '/exchange-tickets' :
                    $scope.title = 'آگهی بلیت';
                    $scope.pageDescription = 'آگهی بلیت، فروض بلیت، تعویض بلیت، بلیت تعویضی، بلیت دیوار، بلیت فروشی، بلیت رایگان، بلیت فوری، بلیت کنسرت، بلیت تئاتر، بلیت موسیقی،بلیتو';
                    $scope.robotValue = 'index';
                    break;
                case '/activate-user' :
                    $scope.title = 'فعال سازی';
                    $scope.robotValue = 'noindex,nofollow';
                    break;
                case '/user-profile' :
                    $scope.title = 'صفحه کاربر';
                    $scope.robotValue = 'noindex,nofollow';
                    break;
                default :
                    if($location.path().indexOf('event-page') > -1) {
                        $scope.title = $location.path().replace('/event-page/','').replace( /\d+/,'');
                        $scope.pageDescription = 'توضیحات صفحه اصلی';
                        $scope.robotValue = 'index';
                    }
                    if($location.path().indexOf('exchange-page') > -1) {
                        $scope.title = $location.path().replace('/exchange-page/','').replace( /\d+/,'');
                        $scope.pageDescription = 'توضیحات صفحه اصلی';
                        $scope.robotValue = 'index';
                    }
                    if($location.path().indexOf('payment') > -1) {
                        $scope.title = 'صفحه بلیت';
                        $scope.pageDescription = 'توضیحات صفحه اصلی';
                        $scope.robotValue = 'noindex,nofollow';
                    }
                    break;
            }
        });
        $scope.registerOnce = false;
        $scope.regUser = function (regData) {
            $scope.registerOnce = true;
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
                    $scope.submitRegister = false;
                    $scope.registerErrorNotif = true;
                    document.getElementById("registerError").innerHTML= data.data.message;
                })
        };
        $scope.loginOnce = false;
        $scope.login = function (loginData) {
            $scope.loginOnce = true;
            main.loading = true;
            main.errorMsg = false;
            $scope.loginErrorNotif = false;
            $scope.loginSuccessNotif = false;

            $scope.submitLogin = true;

            Auth.login(loginData)
                .then(function (data, status) {
                    $scope.loginOnce = false;
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
                    $scope.loginOnce = false;
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

    });

