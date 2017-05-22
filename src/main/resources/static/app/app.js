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
        'ngMap',
        'animationServices',
        'mapServices'
    ])
    .config(function ($httpProvider) {
        $httpProvider.interceptors.push('AuthInterceptors');

    })
    .constant('config', {
        baseUrl : 'http://192.168.200.167:8085',
        redirectToUrlAfterLogin : {url : '/'}
    })
;