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
            .when('/recentEvents', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false

            })
            .when('/cinema', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false

            })
            .when('/theatre', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false

            })
            .when('/cafe', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false

            })
            .when('/cinemaExchange', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false

            })
            .when('/theatreExchange', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false

            })
            .when('/cafeExchange', {
                templateUrl : 'app/views/pages/eventList.html',
                authenticated: false

            })
            .when('/eventPage', {
                templateUrl : 'app/views/pages/eventPage.html',
                authenticated: false

            })
            .when('/bioPage', {
                templateUrl : 'app/views/pages/bioPage.html',
                authenticated: false

            })
            .when('/activateUser', {
                templateUrl : 'app/views/pages/activateUser.html',
                authenticated: false

            })
            .when('/profile', {
                templateUrl : 'app/views/pages/userProfile.html',
                authenticated: false
            })

            .otherwise({redirectTo: '/'})


    });

app.run(['$rootScope', 'Auth', '$location', 'AuthToken', function ($rootScope, Auth, $location) {
    $rootScope.$on('$routeChangeStart', function (event, next) {

        if(next.$$route.authenticated === true) {
            if(!Auth.isLoggedIn() && !Auth.loggedInRefresh()) {
                event.preventDefault();
                $location.path('/');
            }

        } else if (next.$$route.authenticated === false) {
            if(Auth.isLoggedIn() || Auth.loggedInRefresh()) {
                event.preventDefault();
                $location.path('/');
            }

        }

    });
}]);