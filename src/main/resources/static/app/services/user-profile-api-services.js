/**
 * Created by soroush on 5/29/17.
 */

angular.module('userProfileApi', [])
    .service('uploadPhotoService', function ($http, config) {
        var uploadPhoto = this;

        uploadPhoto.upload = function (imageData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/images/upload', imageData)
        }
    })

    .service('eventCreateService', function ($http, config) {
        var eventCreate = this;

        eventCreate.submitEventForm = function (eventData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/events', eventData)
        }
    })
    .service('plannerCreateService', function ($http, config) {
        var plannerCreate = this;

        plannerCreate.submitPlanenerForm = function (eventData) {
            return $http.post(config.baseUrl+'/api/blito/v1.0/event-hosts', eventData)
        }
    })