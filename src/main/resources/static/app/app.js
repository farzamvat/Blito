/**
 * Created by soroush on 4/18/17.
 */

angular.module('Blito',
    [
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
        'angular-google-analytics'
    ])
    .config(function ($httpProvider, AnalyticsProvider) {
        $httpProvider.interceptors.push('AuthInterceptors');
        if (!$httpProvider.defaults.headers.get) {
            $httpProvider.defaults.headers.get = {};
        }
        $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
        $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
        AnalyticsProvider.setAccount('UA-103668411-1');
    })
    .run(['Analytics', function (Analytics) {}])
    .constant('config', {
        baseUrl : 'https://bili.to',
        redirectToUrlAfterLogin : {url : '/'}
    })
;

// http://89.163.225.16/\
//192.168.202.19:8085