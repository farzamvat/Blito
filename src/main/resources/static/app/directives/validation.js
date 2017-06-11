/**
 * Created by soroush on 6/11/17.
 */

angular.module('blitoDirectives')
    .directive('lengthValidation',  function () {
        return {
            require: 'ngModel',
            link: function(scope, element, attr, dirCtrl) {
                function lengthValidation(value) {
                    if (value.split("").length > 6) {
                        dirCtrl.$setValidity('charE', true);
                    } else {
                        dirCtrl.$setValidity('charE', false);
                    }
                    return value;
                }

                dirCtrl.$parsers.push(lengthValidation);
            }
        }
    })
    .directive('passwordValidation',  function () {
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
    })
;

