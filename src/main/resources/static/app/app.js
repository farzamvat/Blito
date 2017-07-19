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
        'notFound'
    ])
    .config(function ($httpProvider) {
        $httpProvider.interceptors.push('AuthInterceptors');
    })
    .constant('config', {
        baseUrl : 'http://89.163.225.16',
        redirectToUrlAfterLogin : {url : '/'}
    })
;

// http://89.163.225.16/\
//192.168.202.19:8085