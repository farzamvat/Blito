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
        baseUrl : 'http://172.20.10.9:8085',
        redirectToUrlAfterLogin : {url : '/'}
    })
;

// http://213.32.14.78/