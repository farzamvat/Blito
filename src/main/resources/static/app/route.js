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
                templateUrl : 'app/views/pages/home.html'
            })
            .when('/recentEvents', {
                templateUrl : 'app/views/pages/eventList.html'

            })
            .when('/cinema', {
                templateUrl : 'app/views/pages/eventList.html'

            })
            .when('/theatre', {
                templateUrl : 'app/views/pages/eventList.html'

            })
            .when('/cafe', {
                templateUrl : 'app/views/pages/eventList.html'

            })
            .when('/cinemaExchange', {
                templateUrl : 'app/views/pages/eventList.html'

            })
            .when('/theatreExchange', {
                templateUrl : 'app/views/pages/eventList.html'

            })
            .when('/cafeExchange', {
                templateUrl : 'app/views/pages/eventList.html'

            })
            .when('/eventPage', {
                templateUrl : 'app/views/pages/eventPage.html'

            })
            .when('/bioPage', {
                templateUrl : 'app/views/pages/bioPage.html'

            })
            .when('/activateUser', {
                templateUrl : 'app/views/pages/activateUser.html'

            })
            .when('/profile', {
                templateUrl : 'app/views/pages/userProfile.html',
                authenticated: true
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