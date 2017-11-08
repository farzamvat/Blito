/**
 * Created by soroush on 4/18/17.
 */

var app = angular.module('appRoutes', ['ngRoute'])
    .config(function ($routeProvider, $locationProvider) {


        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });
        $routeProvider
            .when('/', {
                templateUrl : 'app/views/pages/home.html',
                authenticated: false
            })
            .when('/not-found', {
                templateUrl : 'app/views/error-pages/404page.html',
                authenticated: false
            })
            .when('/about-us', {
                templateUrl : 'app/views/pages/about-us.html',
                authenticated: false
            })
            .when('/term-of-use', {
                templateUrl : 'app/views/pages/term-of-use.html',
                authenticated: false
            })
            .when('/privacy-policy', {
                templateUrl : 'app/views/pages/privacy-policy.html',
                authenticated: false
            })
            .when('/entertainment', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false
            })
            .when('/exhibition', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false
            }).when('/event-host-page/:plannerLink', {
                templateUrl : 'app/views/pages/bioPage.html',
                authenticated: false
            })
            .when('/tour', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false
            })
            .when('/theater', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false
            })
            .when('/concert', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false
            })
            .when('/cinema', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false
            })
            .when('/workshop', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false
            })
            .when('/other', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false
            })
            .when('/exchange-tickets', {
                templateUrl : 'app/views/pages/exchangeList.html',
                authenticated: false
            })
            .when('/event-page/:eventLink', {
                templateUrl : 'app/views/pages/eventPage.html',
                authenticated: false
            })
            .when('/exchange-page/:exchangeLink', {
                templateUrl : 'app/views/pages/exchange-page.html',
                authenticated: false
            })
            .when('/activate-user', {
                templateUrl : 'app/views/pages/activateUser.html',
                authenticated: false
            })
            .when('/user-profile', {
                templateUrl : 'app/views/pages/userProfile.html',
                authenticated: true
            })
            .when('/payment/:trackCode', {
                templateUrl : 'app/views/pages/user-ticket.html',
                authenticated: false
            })
            .otherwise({redirectTo: '/not-found'})
    });

app.run(['$rootScope', 'Auth', '$location', function ($rootScope, Auth, $location) {
    $rootScope.$on('$routeChangeStart', function (event, next) {
        if(next.$$route.authenticated === true) {
            if(!Auth.isLoggedIn() && !Auth.loggedInRefresh()) {
                // event.preventDefault();
                $location.path('/');
            }

        }


    });
}]);