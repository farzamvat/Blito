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
            .when('/recent-events', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false

            })
            .when('/cinema', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false

            })
            .when('/theater', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false

            })
            .when('/cafe', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false

            })
            .when('/exchange-tickets', {
                templateUrl : 'app/views/pages/exchangeList.html',
                authenticated: false

            })
            .when('/event-page', {
                templateUrl : 'app/views/pages/eventPage.html',
                authenticated: false

            })
            .when('/bio-page', {
                templateUrl : 'app/views/pages/bioPage.html',
                authenticated: false
            })
            .when('/activate-user', {
                templateUrl : 'app/views/pages/activateUser.html',
                authenticated: false

            })
            .when('/user-profile', {
                templateUrl : 'app/views/pages/userProfile.html',
                authenticated: false
            })

            .otherwise({redirectTo: '/'})


    });

app.run(['$rootScope', 'Auth', '$location', function ($rootScope, Auth, $location) {
    $rootScope.$on('$routeChangeStart', function (event, next) {
        if(next.$$route.authenticated === true) {
            if(!Auth.isLoggedIn() && !Auth.loggedInRefresh()) {
                event.preventDefault();
                $location.path('/');
            }

        }
        // else if (next.$$route.authenticated === false) {
        //     if(Auth.isLoggedIn() || Auth.loggedInRefresh()) {
        //         event.preventDefault();
        //         $location.path('/');
        //     }
        //
        // }

    });
}]);