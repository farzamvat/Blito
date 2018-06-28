/**
 * Created by soroush on 4/18/17.
 */
var anyChartUi = require('../assets/css/anychart/anychart-ui.min.css');
var bootstrapCss = require('../bower_components/bootstrap/dist/css/bootstrap.min.css');
var fontAwesom = require('../assets/fonts/font-awesome-4.7.0/css/font-awesome.min.css');
var slickCss = require('../bower_components/slick-carousel/slick/slick.css');
var slickThemeCss = require('../bower_components/slick-carousel/slick/slick-theme.css');
var wizardStyle = require('../assets/css/wizardStyle.css');
var customCss = require('../assets/css/custom.css');
var tabProfileCss = require('../assets/css/tabProfile.css');
var textAngularCss = require('../node_modules/textangular/dist/textAngular.css');
var persianDatePickerCss = require('../bower_components/persian-datepicker/dist/css/persian-datepicker-0.4.5.min.css');
var persianDatePickerDarkCss = require('../bower_components/persian-datepicker/dist/css/theme/persian-datepicker-dark.css');

var jquery = require('../bower_components/jquery/dist/jquery.min.js');
var anyChartBase = require('../assets/js/anychart/anychart-base.min.js');
var anyChartMap = require('../assets/js/anychart/anychart-map.min.js');
var anyChartExport = require('../assets/js/anychart/anychart-exports.min.js');
var anyChartUi = require('../assets/js/anychart/anychart-ui.min.js');


var angular = require('angular');
var ngRoute = require('../bower_components/angular-route/angular-route.min.js');
var authServices = require("./services/auth-services.js");
var appRoutes = require("./route.js");
var persianDate = require('../bower_components/persian-datepicker/lib/persian-date.js');
var persianDateRq = require('../bower_components/persian-datepicker/dist/js/persian-datepicker-0.4.5.min.js');
var menuPagesModule = require("./controllers/mainCtrl.js");
var slick = require("../bower_components/angular-slick/dist/slick.min.js");
var bootstrapLib = require("../bower_components/bootstrap/dist/js/bootstrap.min.js");
var slickCarousel = require("../bower_components/slick-carousel/slick/slick.min.js");
var customJs = require("../assets/js/script.js")
var homePageModule = require("./controllers/homeCtrl.js");
var eventsPageModule = require("./controllers/eventsListPageCtrl.js");
var eventsPageMCtrl = require("./controllers/eventPageCtrl.js");
var angularPagination = require("../bower_components/angularUtils-pagination/dirPagination.js");
var bioPageModule = require("./controllers/bioPageCtrl.js");
var User = require("./controllers/activateUserCtrl.js");
var UserProfile = require("./controllers/userProfileCtrl.js");
var blitoDirectives = require("./directives/changeClassBySize.js");
var validationDirective = require("./directives/validation.js");
var seatMapDirectives = require("./directives/seatmap-directive.js");
var animationServices = require("./services/animate-services.js");
var UiServices = require("./services/ui-services.js");
var UiServicesSeatMap = require("./services/seatmap-services.js");
var userProfileApi = require("./services/user-profile-api-services.js");
var exchangesPageModule = require("./controllers/exchangeList.js");
var exchangesPageCtrl = require("./controllers/exchange-page-ctrl.js");
var homePageApi = require("./services/home-page-services.js");
var notFound = require("./controllers/not-found-ctrl.js");
var userTicketCtrl = require("./controllers/user-ticket-ctrl.js");
var endedEventCtrl = require("./controllers/ended-events-ctrl.js");
var ngPersianRq = require("../bower_components/persianjs/persian.js");
var ngPersian = require("../bower_components/angular-persian/dist/angularpersian.min.js");
var textAngularR = require("rangy");
var textAngularRS = require("textangular");
var textAngularS = require("textangular/dist/textAngular-sanitize.min");
var ngFileSaver = require("../bower_components/angular-file-saver/dist/angular-file-saver.min.js");
var ngFileSaverRq = require("../bower_components/angular-file-saver/dist/angular-file-saver.bundle.min.js");
var angulartics = require("../bower_components/angulartics/dist/angulartics.min.js");
var angularticsGoogle = require("../bower_components/angulartics-google-analytics/dist/angulartics-ga.min.js");

angular.module('Blito',
    [
        'ngRoute',
        'appRoutes',
        'menuPagesModule',
        'authServices',
        'slick',
        'homePageModule',
        'eventsPageModule',
        'angularUtils.directives.dirPagination',
        'bioPageModule',
        'User',
        'blitoDirectives',
        'animationServices',
        'UiServices',
        'userProfileApi',
        'exchangesPageModule',
        'homePageApi',
        'notFound',
        'ngPersian',
        'textAngular',
        'ngFileSaver',
        'angulartics',
        'angulartics.google.analytics'
    ])
    .config([
        '$httpProvider',
        '$analyticsProvider',
        function ($httpProvider, $analyticsProvider) {
        $httpProvider.interceptors.push('AuthInterceptors');
        if (!$httpProvider.defaults.headers.get) {
            $httpProvider.defaults.headers.get = {};
        }
        $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
        $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
        $analyticsProvider.settings.ga.account = 'UA-103668411-1';
        $analyticsProvider.firstPageview(true);
        $analyticsProvider.withBase(true);
    }])
    .constant('config', {
        baseUrl : 'http://89.163.225.84:8085/',
        redirectToUrlAfterLogin : {url : '/'}
    })
;
