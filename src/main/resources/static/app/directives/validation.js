/**
 * Created by soroush on 6/11/17.
 */

angular.module('blitoDirectives')
    .directive('lengthValidation',  [function () {
        return {
            require: 'ngModel',
            link: function(scope, element, attr, dirCtrl) {
                function lengthValidation(value) {
                    if (value.split("").length > 7) {
                        dirCtrl.$setValidity('charE', true);
                    } else {
                        dirCtrl.$setValidity('charE', false);
                    }
                    return value;
                }

                dirCtrl.$parsers.push(lengthValidation);
            }
        }
    }])
    .directive('passwordValidation',  [function () {
        return {
            require: 'ngModel',
            link: function (scope, elem, attrs, ctrl) {
                var firstPassword = '#' + attrs.passwordValidation;
                elem.add(firstPassword).on('keyup', function () {
                    scope.$apply(function () {
                        var v = elem.val()===$(firstPassword).val();
                        ctrl.$setValidity('pwmatch', v);
                    });
                });
            }
        }
    }])
    //

    .directive('urlValidation',  [function () {
        return {
            require: 'ngModel',
            link: function(scope, element, attr, dirCtrl) {
                function urlValidation(value) {
                    var expression = /[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/gi;
                    var regex= new RegExp(expression);
                    if(value.match(regex)) {
                        dirCtrl.$setValidity('charE', true);
                    } else {
                        dirCtrl.$setValidity('charE', false);
                    }

                    return value;
                }

                dirCtrl.$parsers.push(urlValidation);
            }
        }
    }])
;

