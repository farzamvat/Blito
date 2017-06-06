/**
 * Created by soroush on 5/2/17.
 */
angular.module('blitoDirectives', [])
    .directive('resizer', ['$window', function ($window) {
        return {
            restrict: 'A',
            link: function (scope, elem, attrs) {
                scope.isLarge = $window.innerWidth > 768 ? true : false;
                angular.element($window).on('resize', function () {
                    scope.$apply(function(){
                        scope.isLarge = $window.innerWidth > 768 ? true : false;

                    })
                });
            }
        }
    }]);