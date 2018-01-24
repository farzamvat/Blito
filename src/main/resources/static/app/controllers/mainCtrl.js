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
        var changeRouteAfterLogin = false;
        main.checkingSession = false;
        $scope.loadPage = false;
        $scope.logout = function () {
            Auth.logout();
            main.logoutMenu = false;
            $window.location.assign('/');

        };
            $scope.$on('$routeChangeStart', function () {
                $("#loading").modal("show");
            });
            $scope.$on('$routeChangeSuccess', function () {
                $("#loading").modal("hide");
            });

        $scope.makeNewExchange = function () {
            if($scope.loggedIn) {
                $location.path('/user-profile');
            } else {
                changeRouteAfterLogin = true;
                $("#registrationModal").modal("show");
            }
        };
        $scope.submitEventPlannerIntro = function () {
            if($scope.logoutMenu) {
                $location.path('/user-profile');
            } else {
                changeRouteAfterLogin = true;
                $("#registrationModal").modal("show");
            }
        };
        $scope.changeTabFix = function () {
            $scope.$on('$routeChangeSuccess', function () {
                $rootScope.$broadcast('firstTabBroadCast', []);
            });
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
                $(angular.element(document.getElementsByClassName('navbar-collapse'))).css("height","600px");
            }
            else {
                $(angular.element(document.getElementsByClassName('dropdown-menu'))).slideUp(300);
                if(!$scope.loggedIn) {
                    $(angular.element(document.getElementsByClassName('navbar-collapse'))).css("height", "210px");
                }
                else{
                    $(angular.element(document.getElementsByClassName('navbar-collapse'))).css("height", "260px");
                }
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
                    $scope.pageDescription = 'بلیتو مرکز برنامه‌های تفریحی و سرگرمی تهران می‌باشد که کنسرت‌های تهران، تئاترهای اکران شده شهر تهران و سرگرمی‌های متنوع از جمله بازی‌های برد را پوشش می‌دهد. ';
                    $scope.keyWord = 'برنامه تفریحی تهران، کنسرت‌های تهران، کنسرت‌های تهران ۹۶، کنسرت‌های این ماه، کنسرت‌های این هفته، کنسرت‌های امروز، مکان‌های تفریحی تهران، کنسرت راک تهران، مکان‌های دیدنی تهران، کارگاه‌ آموزشی فیلم سازی';
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
                    $scope.pageDescription = 'بلیتو برنامه‌های تفریحی و سرگرمی متنوعی را برای هر روز مخاطبانش در نظر گرفته. این برنامه‌ها شمال برد بازی، کافه‌های برد، استنداپ کامدی و برنامه‌های شاد و تفریحی';
                    $scope.keyWord = 'مکان‌های تفریحی و سرگرمی، مکان‌های تفریحی اطراف تهران، برنامه سرگرمی و تفریحی تهران، کافه برد، برد بازی، برد گیم';
                    $scope.robotValue = 'index';
                    break;
                case '/exhibition' :
                    $scope.title = 'نمایشگاه';
                    $scope.pageDescription = 'نمایشگاه‌های هنری و فرهنگی هر روزه در تهران برگزار می‌شوند که بلیتو با پوشش آنها برای مخاطبانش هر روزه برنامه‌های تفریحی و فرهنگی ایجاد می‌کند.';
                    $scope.keyWord = 'مکان‌های تفریحی و سرگرمی، نمایشگاه‌های تهران، نمایشگاه هنری، نمایشگاه آثار هنری، نمایشگاه نقاشی، مکان‌های هنری و فرهنگی';
                    $scope.robotValue = 'index';
                    break;
                case '/tour' :
                    $scope.title = 'تور';
                    $scope.pageDescription = 'تورهای متنوع و متفاوتی در وبسایت بلیتو برگزار می‌شود، که انواع مکان‌های دیدنی و تفریحی را مقصد خود قرار می‌دهد. این مکان‌ها شامل تمام طبیعت‌های ایران می‌شود و برنامه‌ای تفریحی و سرگرم‌ کننده برای شما فراهم می‌کند.';
                    $scope.keyWord = 'تور‌های ایران، تور‌های تفریحی، بلیط تور‌های ایران، تور شیراز، تور کویر، تور شمال، تور جنگل، تور‌های تفریحی جنگل، تور کویر مرنجاب، تور کویر مصر، تور کویر متین آباد، تور جنگل ابر، تور دریاچه چورت، تور قلعه الموت';
                    $scope.robotValue = 'index';
                    break;
                case '/theater' :
                    $scope.title = 'تئاتر';
                    $scope.pageDescription = 'بلیتو با برگزاری تئاتر‌های جدید در تهران فضای فرهنگی و هنری را برای مخاطبانش فراهم‌ می‌کند تا برای هر روز و هفتگی برنامه برای اوقات فراغت خود داشته باشند. تئاتر‌ شهر، تئاتر مستقل و دیگر مکان‌های اجرای تئاتر از جمله‌ مکان‌های برگزاری بلیتو می‌باشند.';
                    $scope.keyWord = 'بلیط تئاتر شهر، بلیط تئاتر کمدی، بلیط تئاتر تهران، تئاتر تهران، تئاتر‌های این هفته، تئاتر‌های امروز تهران، تئاتر‌ این ماه';
                    $scope.robotValue = 'index';
                    break;
                case '/concert' :
                    $scope.title = 'کنسرت';
                    $scope.pageDescription = ' برگزاری کنسرت‌ در تهران از جمله هدف اصلی بلیتو برای گسترش رویداد‌های فرهنگی و هنری تهران می‌باشد. کنسرت‌های راک، کنسرت جز و موسیقی‌های تلفیقی از جمله‌اصلی ترین نوع سبک بلیتو می‌باشد. بلیتو با برگزاری کنسرت‌های کافه‌ای برنامه‌ی هر روز شما را فراهم می‌کند.';
                    $scope.keyWord = 'کنسرت‌های تهران، کنسرت‌های جدید در تهران، کنسرت‌های تهران ۹۶، کنسرت‌های امروز تهران، کنسرت‌های این ماه، کنسرت‌های این هفته، کنسرت‌های راک تهران، بلیط کنسرت تهران';
                    $scope.robotValue = 'index';
                    break;
                case '/cinema' :
                    $scope.title = 'سینما';
                    $scope.pageDescription = 'اکران فیلم‌های متنوع و سرگرم‌ کننده در وبسایت بلیتو اوقات فراغت شما را پر می‌کند، نمایش انواع فیلم‌ها تمان مخاطبان را برای تمام سلیقه‌ها پوشش می‌دهد. اکران فیلم‌های کوتاه، ترسناک، ماجراجویی جدید صنعت سینما برنامه روزانه شما را تشکیل می‌دهد.';
                    $scope.keyWord = 'اکران فیلم کوتاه، فیلم‌های هالیوود، فیلم ترسناک، اکران فیلم‌های جدید، فیلم‌های جدید، نقد فیلم‌های جدید، اکران فیلم در تهران';
                    $scope.robotValue = 'index';
                    break;
                case '/workshop' :
                    $scope.title = 'کارگاه';
                    $scope.pageDescription = 'بلیتو با برگزاری کارگاه‌ها و آموزش‌های متنوع در زمینه‌های فرهنگی، هنری، سینما، فیلمسازی، نقاشی در این زمینه‌ فعالیت می‌کند.';
                    $scope.keyWord = 'اموزش فیلمسازی، کارگاه سینما، آموزش نقاشی، کارگاه موسیقی، کارگاه آموزش فیلمسازی، کارگاه آموزش موسیقی';
                    $scope.robotValue = 'index';
                    break;
                case '/other' :
                    $scope.title = 'سایر';
                    $scope.pageDescription = 'بلیتو در انواع رویداد‌های فرهنگی و هنری فعالیت می‌کند و برای انواع سلیقه‌ها به صورت روزانه و هفتگی برنامه‌های سرگرمی و تفریحی دارد';
                    $scope.keyWord = 'رویداد‌های فرهنگی و هنری، سرگرمی و تفریحی، بلیط کنسرت، بلیط تئاتر، بلیط سرگرمی و تفریحی';
                    $scope.robotValue = 'index';
                    break;
                case '/exchange-tickets' :
                    $scope.title = 'آگهی بلیت';
                    $scope.pageDescription = 'با آگهی بلیط کنسرت، تئاتر، سینما در وبسایت بلیتو بلیط خود را به فروش بگذارید. بلیتو با در اختیار گذاشتن بلیط شما در اختیار عموم به سرعت بلیط شما را با آگهی به فروش می‌رساند.';
                    $scope.keyWord = 'آگهی بلیط، آگهی بلیت، بلیط کنسرت تهران، بلیط تئاتر تهران، بلیط کنسرت، آگهی بلیط کنسرت، آگهی بلیط تئاتر';
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
                    if($location.path().indexOf('payment') > -1) {
                        $scope.title = 'صفحه بلیت';
                        $scope.pageDescription = '';
                        $scope.keyWord = '';
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
                    if(changeRouteAfterLogin) {
                        $location.path('/user-profile');
                    }
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
        $scope.resendActivationEmail = function () {
            $("#registrationModal").modal("hide");
            $("#resendEmail").modal("show");
            document.getElementById("successResendEmail").style.display = "none";
            document.getElementById("errorResendEmail").style.display = "none";
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
        $scope.resendEmailClickOnce = false;
        $scope.resendActivationEmailSubmit = function (email) {
            $scope.resendEmailClickOnce = true;
            document.getElementById("successResendEmail").style.display = "none";
            document.getElementById("errorResendEmail").style.display = "none";
            document.getElementById("spinnerResendEmail").style.display = "inline";
            Auth.resendActivationEmail(email)
                .then(function () {
                    $scope.resendEmailClickOnce = false;
                    document.getElementById("spinnerResendEmail").style.display = "none";
                    document.getElementById("successResendEmail").style.display = "block";
                })
                .catch(function (data) {
                    $scope.resendEmailClickOnce = false;
                    document.getElementById("spinnerResendEmail").style.display = "none";
                    document.getElementById("errorResendEmail").style.display = "block";
                    document.getElementById("errorResendEmail").innerHTML= data.data.message;
                })
        };

        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };

        $scope.closeDropDown = function () {
            $(angular.element(document.getElementsByClassName('dropdown-menu'))).slideUp(300);
            isOpen=false;
        };

    });

