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
        'angulartics',
        'angulartics.google.analytics'
    ])
    .config(function ($httpProvider, $analyticsProvider) {
        $httpProvider.interceptors.push('AuthInterceptors');
        if (!$httpProvider.defaults.headers.get) {
            $httpProvider.defaults.headers.get = {};
        }
        $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
        $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
        $analyticsProvider.settings.ga.account = 'UA-103668411-1';
        $analyticsProvider.firstPageview(true);
        $analyticsProvider.withBase(true);
    })
    .constant('config', {
        baseUrl : 'http://blito.ir',
        redirectToUrlAfterLogin : {url : '/'}
    })
;