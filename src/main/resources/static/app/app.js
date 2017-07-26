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
        'ngPersian'
    ])
    .config(function ($httpProvider) {
        $httpProvider.interceptors.push('AuthInterceptors');
        if (!$httpProvider.defaults.headers.get) {
            $httpProvider.defaults.headers.get = {};
        }
        $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
        $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
    })
    .constant('config', {
        baseUrl : 'http://192.168.200.78:8086',
        redirectToUrlAfterLogin : {url : '/'}
    })
;

// http://89.163.225.16/\
//192.168.202.19:8085