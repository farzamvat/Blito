/**
 * Created by soroush on 5/2/17.
 */

angular.module('User')
    .controller('userProfileCtrl', function ($scope, userInfo, Auth, $timeout, mapMarkerService, updateInfo, $location, scrollAnimation, photoService, eventService, plannerService, exchangeService, dataService) {
        var userProfile = this;

        $scope.userData = userInfo.getData();
        $scope.updateInfoSpinner = false;

        $scope.updateSuccessNotif = false;
        $scope.updateFailNotif = false;
        $scope.showTimeForms = [{blitTypes : [{}]}];
        $scope.showTimeEditForms = [{blitTypes : [{}]}];

        $scope.showThumbnailProfile = false;
        $scope.showThumbnailEventPlanner = false;
        $scope.showThumbnailEventExchange = false;
        $scope.showThumbnailCover = false;
        $scope.uploadExchangePhoto = false;
        $scope.exchangePhotoSuccess = false;
        $scope.uploadEventPhoto = false;
        $scope.eventPhotoSuccess = false;
        $scope.createEventSpinner = false;
        $scope.createEventNotif = false;
        userProfile.showTimeNumber = 0;
        //==================================================== EDIT USER GET INFO =================================
        $scope.editInfo = angular.copy(userInfo.getData());
        $scope.editUserInfo = function () {
            Auth.getUser()
                .then(function (data, status) {
                    $scope.logoutMenu = true;
                    userProfile.userData = data.data;
                    userInfo.setData(userProfile.userData);
                    $scope.editInfo = angular.copy(userInfo.getData());

                },function (data, status) {

                }, function (data) {
                    console.log("updated");
                })
        };

        $scope.updateInfo = function (editInfo) {
            $scope.updateInfoSpinner = true;
            delete editInfo["email"];
            updateInfo.updateData(editInfo)
                .then(function (status, data) {
                    $scope.updateInfoSpinner = false;
                    $scope.updateSuccessNotif = true;
                    $timeout(function () {
                        $scope.updateSuccessNotif = false;
                    }, 3000);
                })
                .catch(function (status, data) {
                    $scope.updateInfoSpinner = false;
                    $scope.updateFailNotif = true;
                    $timeout(function () {
                        $scope.updateFailNotif = false;
                    }, 3000);
                })
        };

        //==================================================== ********* =================================
        //==================================================== PERSIAN DATE PICKER =================================
        userProfile.setDate = function (i) {
            $timeout(function () {
                $(".persianTime").pDatepicker({
                    timePicker: {
                        enabled: true
                    },
                    altField: '#persianDigitAlt',
                    altFormat: "YYYY MM DD HH:mm:ss",
                    altFieldFormatter: function (unixDate) {
                        // $scope.showTimeForms[i].date = unixDate;
                    }
                });
            }, 1000);

        };
        $timeout(function () {
            $(".persianTimeEventStart").pDatepicker({
                timePicker: {
                    enabled: true
                },
                altField: '#persianDigitAlt',
                altFormat: "YYYY MM DD HH:mm:ss",
                altFieldFormatter: function (unixDate) {
                    $scope.eventStartTime = unixDate;
                }
            });
            $(".persianTimeEventEnd").pDatepicker({
                timePicker: {
                    enabled: true
                },
                altField: '#persianDigitAlt',
                altFormat: "YYYY MM DD HH:mm:ss",
                altFieldFormatter: function (unixDate) {
                    $scope.eventEndTime = unixDate;
                }
            });
            $(".persianExchangeTime").pDatepicker({
                timePicker: {
                    enabled: true
                },
                altField: '#persianDigitAlt',
                altFormat: "YYYY MM DD HH:mm:ss",
                altFieldFormatter: function (unixDate) {
                    $scope.exchangeTime = unixDate;
                }
            });
        }, 1000);
        userProfile.setDate(userProfile.showTimeNumber);

        //==================================================== ********* =================================

        $scope.test = function () {
            $("#editEvent").modal("show");
        }
        //==================================================== IMAGE UPLOADS =================================
        var fileSelectProfile = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectProfile.type = 'file';
        fileSelectProfile.setAttribute('id', 'profileUpload') ;

        var fileSelectCover = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectCover.type = 'file';
        var fileSelectCoverEdit = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectCoverEdit.type = 'file';

        var fileSelectEventPlanner = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventPlanner.type = 'file';
        var fileSelectEventPlannerEdit = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventPlannerEdit.type = 'file';

        var fileSelectEventExchange = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventExchange.type = 'file';

        var fileSelectEditExchange = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEditExchange.type = 'file';

        var fileSelectEditEvent = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEditEvent.type = 'file';

        var fileSelectEventGalleryOne = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventGalleryOne.type = 'file';

        var fileSelectEventGalleryTwo = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventGalleryTwo.type = 'file';

        var fileSelectEventGalleryThree = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventGalleryThree.type = 'file';

        var fileSelectEventGalleryFour = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventGalleryFour.type = 'file';

        var fileSelectEventGalleryFive = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventGalleryFive.type = 'file';
        var fileSelectEventGallerySix = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventGallerySix.type = 'file';

        var fileSelectEventGalleryOneEdit = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventGalleryOneEdit.type = 'file';
        var fileSelectEventGalleryTwoEdit = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventGalleryTwoEdit.type = 'file';

        var fileSelectEventGalleryThreeEdit = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventGalleryThreeEdit.type = 'file';
        var fileSelectEventGalleryFourEdit = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventGalleryFourEdit.type = 'file';
        var fileSelectEventGalleryFiveEdit = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventGalleryFiveEdit.type = 'file';
        var fileSelectEventGallerySixEdit = document.createElement('input'); //input it's not displayed in html, I want to trigger it form other elements
        fileSelectEventGallerySixEdit.type = 'file';

        $scope.uploadGallerySixEdit = function() {
            fileSelectEventGallerySixEdit.click();
        }
        $scope.uploadGalleryFiveEdit = function() {
            fileSelectEventGalleryFiveEdit.click();
        }
        $scope.uploadGalleryFourEdit = function() {
            fileSelectEventGalleryFourEdit.click();
        }
        $scope.uploadGalleryThreeEdit = function() {
            fileSelectEventGalleryThreeEdit.click();
        }
        $scope.uploadGalleryTwoEdit = function() {
            fileSelectEventGalleryTwoEdit.click();
        }
        $scope.uploadGalleryOneEdit = function() {
            fileSelectEventGalleryOneEdit.click();
        }
        $scope.uploadGallerySix = function() {
            fileSelectEventGallerySix.click();
        }
        $scope.uploadGalleryFive = function() {
            fileSelectEventGalleryFive.click();
        }
        $scope.uploadGalleryFour = function() {
            fileSelectEventGalleryFour.click();
        }
        $scope.uploadGalleryThree = function() {
            fileSelectEventGalleryThree.click();
        }
        $scope.uploadGalleryOne = function() {
            fileSelectEventGalleryOne.click();
        }
        $scope.uploadGalleryTwo = function() {
            fileSelectEventGalleryTwo.click();
        }
        $scope.uploadPicProfile = function() {
            fileSelectProfile.click();
        }
        $scope.uploadPicCoverEdit = function() {
            fileSelectCoverEdit.click();
        }

        $scope.uploadPicCover = function() {
            fileSelectCover.click();
        }
        $scope.uploadPicEventPlanner = function() {
            fileSelectEventPlanner.click();
        }
        $scope.uploadPicEventPlannerEdit = function() {
            fileSelectEventPlannerEdit.click();
        }
        $scope.uploadPicEventExchange = function() {
            fileSelectEventExchange.click();
        }
        $scope.uploadPicEditExchange = function() {
            fileSelectEditExchange.click();
        }
        $scope.uploadPicEditEvent = function() {
            fileSelectEditEvent.click();
        }
        // $scope.deletePicEventEdit = function () {
        //     fileSelectEditEvent.value = "" ;
        // }
        $scope.photoGallerySixEdit = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("gallerySixEdit"))[0].src
            }
            $scope.uploadEventSixEditPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadEventSixEditPhoto = false;
                    $scope.eventPhotoSixEditSuccess = true;

                    $scope.gallerySixUUID = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.photoGalleryFiveEdit = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("galleryFiveEdit"))[0].src
            }
            $scope.uploadEventFiveEditPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadEventFiveEditPhoto = false;
                    $scope.eventPhotoFiveEditSuccess = true;

                    $scope.galleryFiveUUID = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.photoGalleryFourEdit = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("galleryFourEdit"))[0].src
            }
            $scope.uploadEventFourEditPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadEventFourEditPhoto = false;
                    $scope.eventPhotoFourEditSuccess = true;
                    $scope.galleryFourUUID = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.photoGalleryThreeEdit = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("galleryThreeEdit"))[0].src
            }
            $scope.uploadEventThreeEditPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadEventThreeEditPhoto = false;

                    $scope.eventPhotoThreeEditSuccess = true;

                    $scope.galleryThreeUUID = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.photoGalleryTwoEdit = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("galleryTwoEdit"))[0].src
            }
            $scope.uploadEventTwoEditPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadEventTwoEditPhoto = false;
                    $scope.eventPhotoTwoEditSuccess = true;

                    $scope.galleryTwoUUID = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.photoGalleryOneEdit = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("galleryOneEdit"))[0].src
            }
            $scope.uploadEventTwoEditPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadEventTwoEditPhoto = false;

                    $scope.eventPhotoOneEditSuccess = true;

                    $scope.galleryOneUUID = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.photoGallerySix = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("gallerySix"))[0].src
            }
            $scope.uploadEventSixPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadEventSixPhoto = false;
                    $scope.eventPhotoSixSuccess = true;
                    $scope.gallerySixUUID = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.photoGalleryFive = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("galleryFive"))[0].src
            }
            $scope.uploadEventFivePhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadEventFivePhoto = false;
                    $scope.eventPhotoFiveSuccess = true;
                    $scope.galleryFiveUUID = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }

        $scope.photoGalleryFour = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("galleryFour"))[0].src
            }
            $scope.uploadEventFourPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadEventFourPhoto = false;
                    $scope.eventPhotoFourSuccess = true;
                    $scope.galleryFourUUID = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }

        $scope.photoGalleryThree= function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("galleryThree"))[0].src
            }
            $scope.uploadEventThreePhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.eventPhotoThreeSuccess = true;
                    $scope.uploadEventThreePhoto = false;
                    $scope.galleryThreeUUID = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }

        $scope.photoGalleryTwo= function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("galleryTwo"))[0].src
            }
            $scope.uploadEventTwoPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.eventPhotoTwoSuccess = true;
                    $scope.uploadEventTwoPhoto = false;
                    $scope.galleryTwoUUID = data.data.imageUUID;
                    console.log($scope.galerryTwoUUID);
                    console.log(data);
                }, function (data, status) {
                    console.log(data);
                })
        }

        $scope.photoGalleryOne= function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("galleryOne"))[0].src
            }

            $scope.uploadEventOnePhoto = true;

            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.eventPhotoOneSuccess = true;
                    $scope.uploadEventOnePhoto = false;
                    $scope.galleryOneUUID = data.data.imageUUID;
                }, function (data, status) {
                    $scope.uploadEventOnePhoto = false;
                    console.log(data);
                })
        }
        $scope.photoUploadEditEvent= function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("profilePhotoUploadEditEvent"))[0].src
            }
            $scope.uploadEditEventPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.eventEditPhotoSuccess = true;
                    $scope.uploadEditEventPhoto = false;

                    $scope.eventEditImageId = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.photoUploadEditExchange = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("exchangePhotoUploadEdit"))[0].src
            }
            $scope.showThumbnailProfile = true;
            $scope.uploadExchangePhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadEditExchangePhoto = false;
                    $scope.exchangeEditPhotoSuccess = true;
                    $scope.exchangeEditImageId = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }

        $scope.photoUploadExchange = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("exchangePhotoUpload"))[0].src
            }
            $scope.uploadExchangePhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadExchangePhoto = false;
                    $scope.exchangePhotoSuccess = true;
                    $scope.exchangeImageId = data.data.imageUUID;
                    console.log(data);
                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.photoUploadEvent = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("profilePhotoUpload"))[0].src
            }
            $scope.uploadEventPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadEventPhoto = false;
                    $scope.eventPhotoSuccess = true;
                    $scope.eventImageId = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                    $scope.uploadEventPhoto = false;
                })
        }

        $scope.uploadPlannerPhoto = false;
        $scope.plannerPhotoSuccess = false;

        $scope.photoUploadPlanner = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementById("eventPlannerPhotoUpload"))[0].src
            }
            $scope.uploadPlannerPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadPlannerPhoto = false;
                    $scope.plannerPhotoSuccess = true;
                    $scope.plannerImageId = data.data.imageUUID;

                }, function (data, status) {
                    console.log(data);
                })
        }

        $scope.photoUploadPlannerEdit = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("eventPlannerPhotoUploadEdit"))[0].src
            }
            $scope.uploadPlannerPhoto = true;
            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadPlannerPhoto = false;
                    $scope.plannerEditPhotoSuccess = true;
                    $scope.plannerImageIdEdit = data.data.imageUUID;

                }, function (data, status) {
                    console.log(data);
                })
        }
        $scope.uploadCoverPhoto = false;
        $scope.coverPhotoSuccess = false;

        $scope.photoUploadPlannerCover = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("coverPhotoUpload"))[0].src
            }
            $scope.uploadCoverPhoto = true;

            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadCoverPhoto = false;
                    $scope.coverPhotoSuccess = true;
                    $scope.coverImageId = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }

        $scope.photoUploadPlannerCoverEdit = function () {
            var imageData = {
                encodedBase64 : angular.element(document.getElementsByClassName("coverPhotoUploadEdit"))[0].src
            }
            $scope.uploadCoverPhoto = true;

            photoService.upload(imageData)
                .then(function (data, status) {
                    $scope.uploadCoverPhoto = false;
                    $scope.coverEditPhotoSuccess = true;
                    $scope.coverImageIdEdit = data.data.imageUUID;
                }, function (data, status) {
                    console.log(data);
                })
        }
        fileSelectEventGallerySixEdit.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventGallerySixEdit.files[0], r = new FileReader();

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("gallerySixEdit"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventGalleryFiveEdit.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventGalleryFiveEdit.files[0], r = new FileReader();

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("galleryFiveEdit"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventGalleryFourEdit.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventGalleryFourEdit.files[0], r = new FileReader();

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("galleryFourEdit"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventGalleryThreeEdit.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventGalleryThreeEdit.files[0], r = new FileReader();

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("galleryThreeEdit"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventGalleryTwoEdit.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventGalleryTwoEdit.files[0], r = new FileReader();

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("galleryTwoEdit"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };

        fileSelectEventGalleryOneEdit.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventGalleryOneEdit.files[0], r = new FileReader();

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("galleryOneEdit"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventGallerySix.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventGallerySix.files[0], r = new FileReader();
            $scope.showThumbnailSix = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("gallerySix"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventGalleryFive.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventGalleryFive.files[0], r = new FileReader();
            $scope.showThumbnailFive = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("galleryFive"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventGalleryFour.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventGalleryFour.files[0], r = new FileReader();
            $scope.showThumbnailFour = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("galleryFour"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventGalleryThree.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventGalleryThree.files[0], r = new FileReader();
            $scope.showThumbnailThree = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("galleryThree"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventGalleryTwo.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventGalleryTwo.files[0], r = new FileReader();
            $scope.showThumbnailTwo = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("galleryTwo"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventGalleryOne.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventGalleryOne.files[0], r = new FileReader();
            $scope.showThumbnailOne = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("galleryOne"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };

        fileSelectEditEvent.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEditEvent.files[0], r = new FileReader();
            $scope.showThumbnailProfile = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("profilePhotoUploadEditEvent"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventPlannerEdit.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventPlannerEdit.files[0], r = new FileReader();
            $scope.showThumbnailProfile = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("eventPlannerPhotoUploadEdit"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectCoverEdit.onchange = function() { //set callback to action after choosing file
            var f = fileSelectCoverEdit.files[0], r = new FileReader();
            $scope.showThumbnailProfile = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("coverPhotoUploadEdit"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEditExchange.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEditExchange.files[0], r = new FileReader();
            $scope.showThumbnailProfile = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("exchangePhotoUploadEdit"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectProfile.onchange = function() { //set callback to action after choosing file
            var f = fileSelectProfile.files[0], r = new FileReader();
            $scope.showThumbnailProfile = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("profilePhotoUpload"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };

        fileSelectCover.onchange = function() { //set callback to action after choosing file
            var f = fileSelectCover.files[0], r = new FileReader();
            $scope.showThumbnailCover = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("coverPhotoUpload"))[0].src = base64Data;
                //here you can send data over your server as desired
            }

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };

        fileSelectEventPlanner.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventPlanner.files[0], r = new FileReader();
            $scope.showThumbnailEventPlanner = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementById("eventPlannerPhotoUpload"))[0].src = base64Data;
                //here you can send data over your server as desired
            };

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        fileSelectEventExchange.onchange = function() { //set callback to action after choosing file
            var f = fileSelectEventExchange.files[0], r = new FileReader();
            $scope.showThumbnailEventExchange = true;

            r.onloadend = function(e) { //callback after files finish loading
                var base64Data = e.target.result;
                $scope.$apply();
                angular.element(document.getElementsByClassName("exchangePhotoUpload"))[0].src = base64Data;
                //here you can send data over your server as desired
            };

            r.readAsDataURL(f); //once defined all callbacks, begin reading the file

        };
        //==================================================== ********* =================================
        //==================================================== SCROLL TO=================================
        $scope.scrollTo = function(id, section) {
            var old = $location.hash();
            $location.hash(id);
            $location.hash(old);
            scrollAnimation.scrollTo(id);

            $(angular.element(document.getElementById(section)).siblings()[0]).slideDown(300);
            $(angular.element(document.getElementById(section))).addClass('orangeBackground');

        }
        //==================================================== ********* =================================
        //==================================================== SHOW TIME FIELDS =================================

        $scope.addFieldTicketType=function(i){
            if($scope.showTimeForms[i].blitTypes.length < 5) {
                $scope.showTimeForms[i].blitTypes.push({})
            }
        };
        $scope.addFieldTicketTypeEdit=function(i){
            if($scope.showTimeEditForms[i].blitTypes.length < 5) {
                $scope.showTimeEditForms[i].blitTypes.push({})
            }
        };

        $scope.deleteFieldTicketTypeEdit=function(i){
            if( 1 < $scope.showTimeEditForms[i].blitTypes.length) {
                $scope.showTimeEditForms[i].blitTypes.splice(-1,1);
            }
        };
        $scope.deleteFieldTicketType=function(i){
            if( 1 < $scope.showTimeEditForms[i].blitTypes.length) {
                $scope.showTimeEditForms[i].blitTypes.splice(-1,1);
            }
        };
        $scope.addFieldShowTime=function(){
            if($scope.showTimeForms.length < 10) {
                userProfile.showTimeNumber++;
                userProfile.setDate(userProfile.showTimeNumber);
                $scope.showTimeForms.push({blitTypes : [{}]})
            }
        };
        $scope.addFieldShowTimeEdit=function(){
            if($scope.showTimeEditForms.length < 10) {
                userProfile.showTimeEditForms++;
                userProfile.setDateEdit();
                $scope.showTimeEditForms.push({blitTypes : [{}]})
            }
        };
        $scope.deleteFieldShowTimeEdit=function(){
            if( 1 < $scope.showTimeEditForms.length) {
                userProfile.showTimeEditForms--;
                $scope.showTimeEditForms.splice(-1,1);
            }
        };
        $scope.deleteFieldShowTime=function(){
            if( 1 < $scope.showTimeForms.length) {
                userProfile.showTimeNumber--;
                $scope.showTimeForms.splice(-1,1);
            }
        };
        //==================================================== ********* =================================
        //==================================================== EVENT PLANNER SUBMIT =================================
        $scope.submitPlannerSpinner = false;
        $scope.submitPlannerNotif = false;
        $scope.submitEventPlanner = function (plannerData) {
            $scope.submitPlannerNotif = false;
            var eventPlannerData = {
                hostName: plannerData.name,
                hostType: plannerData.type,
                images: [
                    {
                        imageUUID: $scope.plannerImageId,
                        type: "HOST_PHOTO"
                    },
                    {
                        imageUUID: $scope.coverImageId,
                        type: "HOST_COVER_PHOTO"
                    }
                ],
                instagramLink: plannerData.instagram,
                linkedinLink: plannerData.linkedin,
                telegramLink: plannerData.telegram,
                telephone: plannerData.mobile,
                twitterLink: plannerData.twitter,
                websiteLink: plannerData.website
            }
            if(!$scope.plannerImageId && !$scope.coverImageId){
                delete eventPlannerData.images;
            }
            $scope.submitPlannerSpinner = true;
            plannerService.submitPlannerForm(eventPlannerData)
                .then(function (data, status) {
                    $scope.submitPlannerSpinner = false;
                    $scope.submitPlannerNotif = true;
                    $scope.submitPlannerErrorNotif = false;
                    $scope.getPlannersData();
                    $scope.eventPlanner = [];
                    $scope.plannerImageId = '';
                    $scope.coverImageId = '';
                    angular.element(document.getElementsByClassName("coverPhotoUploadEdit"))[0].src = '';
                    angular.element(document.getElementsByClassName("eventPlannerPhotoUploadEdit"))[0].src = '';
                    console.log(data);
                }, function (data, status) {
                    $scope.submitPlannerErrorNotif = true;
                    document.getElementById("submitPlannerErrorNotif").innerHTML= data.data.message;
                    $scope.submitPlannerSpinner = false;
                    console.log(data);
                })
        };
        //==================================================== ********* =================================
        //==================================================== EVENT SUBMIT =================================
        $scope.submitEvent = function (eventFields) {
            $scope.createEventNotif = false;

            var latLng = mapMarkerService.getMarker();
            var newShowTime = angular.copy($scope.showTimeForms);
            newShowTime = newShowTime.map(function (item) {
                item.date = $scope.persianToMs(item.date);
                return item;
            });
            var eventSubmitData = {
                eventName : eventFields.name,
                eventType : eventFields.eventType,
                eventHostId : eventFields.eventPlanner.eventHostId,
                address : eventFields.address,
                aparatDisplayCode : eventFields.aparatLink,
                blitSaleEndDate : $scope.persianToMs(eventFields.ticketEndTime),
                blitSaleStartDate : $scope.persianToMs(eventFields.ticketStartTime),
                description : eventFields.description,
                members : eventFields.members,
                eventDates : newShowTime,
                images : [
                    {imageUUID : $scope.eventImageId, type : "EVENT_PHOTO"},
                    {imageUUID : $scope.galleryOneUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryTwoUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryThreeUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryFourUUID, type : "GALLERY"},
                    {imageUUID : $scope.galleryFiveUUID, type : "GALLERY"},
                    {imageUUID : $scope.gallerySixUUID, type : "GALLERY"}
                ],
                latitude : latLng.lat,
                longitude : latLng.lng
            };
            eventSubmitData.images = eventSubmitData.images.filter(function (item) {
                if(item.imageUUID === null || item.imageUUID === undefined) {
                    return false;
                } else {
                    return true;
                }
            });
            console.log(eventSubmitData);
            if(!$scope.eventImageId){
                delete eventSubmitData.images;
            }
            $scope.createEventSpinner = true;

            eventService.submitEventForm(eventSubmitData)
                .then(function (data, status) {
                    // $scope.submitEventForm.$setPristine();
                    $scope.createEventSpinner = false;
                    $scope.createEventNotif = true;
                    $scope.createEventErrorNotif = false;
                    console.log(data);
                    $scope.getUserEvents();
                    $scope.eventFields = [];
                }, function (data, status) {
                    console.log(data);
                    $scope.createEventErrorNotif = true;
                    document.getElementById("createEventErrorNotif").innerHTML= data.data.message;
                    $scope.createEventSpinner = false;
                })
        };
        //==================================================== ********* =================================
        //==================================================== EXCHANGE SUBMIT =================================
        $scope.submitExchangeSpinner = false;
        $scope.submitExchangeNotif = false;

        $scope.submitExchangeTicket = function (exchangeFields) {
            $scope.submitExchangeNotif = false;
            var latLng = mapMarkerService.getMarker();
            $scope.submitExchangeSpinner = true;
            var exchangeData = {
                blitCost: exchangeFields.price,
                description: exchangeFields.description,
                email: $scope.userData.email,
                eventAddress: exchangeFields.address,
                eventDate:  $scope.exchangeTime,
                image: {
                    imageUUID: $scope.exchangeImageId,
                    type: "EXCHANGEBLIT_PHOTO"
                },
                isBlitoEvent: exchangeFields.isBlito,
                phoneNumber: $scope.userData.mobile,
                latitude: latLng.lat,
                longitude: latLng.lng,
                title: exchangeFields.name,
                type: exchangeFields.type
            };
            if(!$scope.exchangeImageId){
                delete exchangeData.image;
            }
            console.log(exchangeData);
            exchangeService.submitExchangeForm(exchangeData)
                .then(function (data, status) {
                    console.log(data);
                    $scope.submitExchangeSpinner = false;
                    $scope.submitExchangeNotif = true;
                    $scope.submitExchangeErrorNotif = false;
                    $scope.getExchangeData();
                    $scope.exchange = [];
                    $scope.exchangeImageId = '';
                    angular.element(document.getElementsByClassName("exchangePhotoUpload"))[0].src = '';

                }, function (data, status) {
                    $scope.submitExchangeErrorNotif = true;
                    $scope.submitExchangeSpinner = false;
                    document.getElementById("submitExchangeErrorNotif").innerHTML= data.data.message;
                    console.log(data);
                })
        };
        //==================================================== ********* =================================
        $scope.showTime = [];
        $scope.dropDownTabToggleEvent = function (event) {
            $scope.getPlannersData();
            $scope.getUserEvents();
            mapMarkerService.initMap(document.getElementById('map'));
            $(angular.element(document.getElementById('toggleExchange'))).slideUp(300);
            $(angular.element(event.currentTarget).siblings()[0]).slideDown(300);
        }
        $scope.dropDownTabToggleExchange = function (event) {
            $scope.getExchangeData();
            mapMarkerService.initMap(document.getElementById('mapExchange'));
            $(angular.element(document.getElementById('toggleEvent'))).slideUp(300);
            $(angular.element(event.currentTarget).siblings()[0]).slideDown(300);
        }
        $scope.closeTabDropDowns = function () {
            $(angular.element(document.getElementsByClassName('tabDropDown'))).slideUp(300);
        }

        $scope.toggleBody = function (section) {
            $(angular.element(document.getElementById(section)).siblings()[0]).slideToggle(300);
            $(angular.element(document.getElementById(section))).toggleClass('orangeBackground');
        }

        $scope.exchangeTickets = [];
        //==================================================== EDIT EVENT =================================
        $scope.editEventFields = {};

        $scope.editEvent = function (index) {
            $scope.editEventNotif = false;
            $scope.eventEditPhotoSuccess = false;
            $scope.eventPhotoOneEditSuccess = false;
            $scope.eventPhotoTwoEditSuccess = false;
            $scope.eventPhotoThreeEditSuccess = false;
            $scope.eventPhotoFourEditSuccess = false;
            $scope.eventPhotoFiveEditSuccess = false;
            $scope.eventPhotoSixEditSuccess = false;

            console.log($scope.userEventsEdit[index]);
            angular.element(document.getElementsByClassName("profilePhotoUploadEditEvent"))[0].src = "";
            angular.element(document.getElementsByClassName("galleryOneEdit"))[0].src = "";
            angular.element(document.getElementsByClassName("galleryTwoEdit"))[0].src = "";
            angular.element(document.getElementsByClassName("galleryThreeEdit"))[0].src = "";
            angular.element(document.getElementsByClassName("galleryFourEdit"))[0].src = "";
            angular.element(document.getElementsByClassName("galleryFiveEdit"))[0].src = "";
            angular.element(document.getElementsByClassName("gallerySixEdit"))[0].src = "";
            gallery = [];
            $scope.eventEditPhotoSuccess = false;

            $scope.showTimeEditForms = angular.copy($scope.userEventsEdit[index].eventDates);

            $scope.editEventFields = {
                eventId : $scope.userEventsEdit[index].eventId,
                eventName : $scope.userEventsEdit[index].eventName,
                eventType : $scope.userEventsEdit[index].eventType,
                description : $scope.userEventsEdit[index].description,
                address : $scope.userEventsEdit[index].address,
                aparatDisplayCode : $scope.userEventsEdit[index].aparatDisplayCode,
                eventHostId : $scope.userEventsEdit[index].eventHostId,
                eventLink : $scope.userEventsEdit[index].eventLink,
                members : $scope.userEventsEdit[index].members
            };
            $scope.dateClass = function (classNumber) {
                return "classDate"+classNumber;
            };
            $("#editEvent").modal("show");
            var imageUUID, gallery = [];
            $scope.userEventsEdit[index].images.forEach(function (image) {
                if(image.type === "EVENT_PHOTO") {
                    imageUUID = image.imageUUID;
                } else {
                    gallery.push(image.imageUUID);
                }
            });
            $scope.eventEditImageId = imageUUID;

            photoService.download(imageUUID)
                .then(function (data, status) {
                    angular.element(document.getElementsByClassName("profilePhotoUploadEditEvent"))[0].src = data.data.encodedBase64;
                }, function (data, status) {
                    console.log(data);
                })
                .catch(function (data, status) {
                    console.log(status);
                });
            if(gallery.length >= 1) {
                $scope.galleryOneUUID = gallery[0];
                console.log(gallery[0]);
                photoService.download(gallery[0])
                    .then(function (data, status) {
                        angular.element(document.getElementsByClassName("galleryOneEdit"))[0].src = data.data.encodedBase64;
                    }, function (data, status) {
                        console.log(data);
                    })
                    .catch(function (data, status) {
                        console.log(status);
                    });
            }
            if(gallery.length >= 2) {
                $scope.galleryTwoUUID = gallery[1];
                photoService.download(gallery[1])
                    .then(function (data, status) {
                        angular.element(document.getElementsByClassName("galleryTwoEdit"))[0].src = data.data.encodedBase64;
                    }, function (data, status) {
                        console.log(data);
                    })
                    .catch(function (data, status) {
                        console.log(status);
                    });
            }
            if(gallery.length >= 3) {
                $scope.galleryThreeUUID = gallery[2];
                photoService.download(gallery[2])
                    .then(function (data, status) {
                        angular.element(document.getElementsByClassName("galleryThreeEdit"))[0].src = data.data.encodedBase64;
                    }, function (data, status) {
                        console.log(data);
                    })
                    .catch(function (data, status) {
                        console.log(status);
                    });
            }
            if(gallery.length >= 4) {
                console.log("photo");
                $scope.galleryFourUUID = gallery[3];
                photoService.download(gallery[3])
                    .then(function (data, status) {
                        angular.element(document.getElementsByClassName("galleryFourEdit"))[0].src = data.data.encodedBase64;
                    }, function (data, status) {
                        console.log(data);
                    })
                    .catch(function (data, status) {
                        console.log(status);
                    });
            }
            if(gallery.length >= 5) {
                console.log("photo");
                $scope.galleryFiveUUID = gallery[4];
                photoService.download(gallery[4])
                    .then(function (data, status) {
                        angular.element(document.getElementsByClassName("galleryFiveEdit"))[0].src = data.data.encodedBase64;
                    }, function (data, status) {
                        console.log(data);
                    })
                    .catch(function (data, status) {
                        console.log(status);
                    });
            }
            if(gallery.length === 6) {
                console.log("photo");
                $scope.gallerySixUUID = gallery[5];
                photoService.download(gallery[5])
                    .then(function (data, status) {
                        angular.element(document.getElementsByClassName("gallerySixEdit"))[0].src = data.data.encodedBase64;
                    }, function (data, status) {
                        console.log(data);
                    })
                    .catch(function (data, status) {
                        console.log(status);
                    });
            }
            $timeout(function () {
                for(var i = 0 ; i < $scope.userEventsEdit[index].eventDates.length; i++) {
                    $(".classDate"+i).pDatepicker({
                        timePicker: {
                            enabled: true
                        },
                        altField: '#persianDigitAlt',
                        altFormat: "YYYY MM DD HH:mm:ss",
                        altFieldFormatter: function (unixDate) {
                        }
                    });
                    $(".classDate"+i).pDatepicker("setDate",$scope.persianToArrayInt($scope.persianToArray(persianDate($scope.userEventsEdit[index].eventDates[i].date).pDate)));
                }
                $(".blitSaleEndDate").pDatepicker({
                    timePicker: {
                        enabled: true
                    },
                    altField: '#persianDigitAlt',
                    altFormat: "YYYY MM DD HH:mm:ss",
                    altFieldFormatter: function (unixDate) {
                    }
                });
                $(".blitSaleStartDate").pDatepicker({
                    timePicker: {
                        enabled: true
                    },
                    altField: '#persianDigitAlt',
                    altFormat: "YYYY MM DD HH:mm:ss",
                    altFieldFormatter: function (unixDate) {
                    }
                });
                $(".blitSaleEndDate").pDatepicker("setDate",$scope.persianToArrayInt($scope.persianToArray(persianDate($scope.userEventsEdit[index].blitSaleEndDate).pDate)));
                $(".blitSaleStartDate").pDatepicker("setDate",$scope.persianToArrayInt($scope.persianToArray(persianDate($scope.userEventsEdit[index].blitSaleStartDate).pDate)));
                console.log($scope.persianToArray(persianDate($scope.userEventsEdit[index].blitSaleEndDate)));
            }, 500);
            userProfile.setDateEdit = function () {
                $timeout(function () {
                    $(".persianEditEventTimeShowTime").pDatepicker({
                        timePicker: {
                            enabled: true
                        },
                        altField: '#persianDigitAlt',
                        altFormat: "YYYY MM DD HH:mm:ss",
                        altFieldFormatter: function (unixDate) {

                        }
                    });
                }, 1000)
            };
            userProfile.setDateEdit();

            $timeout(function () {
                mapMarkerService.initMap(document.getElementById('editEventMap'));
                console.log($scope.userEventsEdit[index]);
                mapMarkerService.setMarker($scope.userEventsEdit[index].latitude, $scope.userEventsEdit[index].longitude);
            },500);
        };

        $scope.editEventSubmit = function (editEventData) {
            var sendingData = angular.copy(editEventData);
            var latLong = mapMarkerService.getMarker();
            $scope.newShowTimeEditForms = angular.copy($scope.showTimeEditForms);

            $scope.newShowTimeEditForms.map(function (item) {
                var newData = item.date.replace(/[^\w\s]/gi , ' ').split(" ");
                newData.pop();
                newData = newData.map(function (item) {
                    return parseInt(item);
                });
                item.date = persianDate(newData).gDate.getTime();
                return item;
            });
            sendingData.blitSaleEndDate = $scope.persianToMs(editEventData.blitSaleEndDate);
            sendingData.blitSaleStartDate = $scope.persianToMs(editEventData.blitSaleStartDate);
            sendingData.eventDates = $scope.newShowTimeEditForms;
            sendingData.latitude = latLong.lat;
            sendingData.longitude = latLong.lng;

            sendingData.images = [
                {imageUUID : $scope.eventEditImageId, type : "EVENT_PHOTO"},
                {imageUUID : $scope.galleryOneUUID, type : "GALLERY"},
                {imageUUID : $scope.galleryTwoUUID, type : "GALLERY"},
                {imageUUID : $scope.galleryThreeUUID, type : "GALLERY"},
                {imageUUID : $scope.galleryFourUUID, type : "GALLERY"},
                {imageUUID : $scope.galleryFiveUUID, type : "GALLERY"},
                {imageUUID : $scope.gallerySixUUID, type : "GALLERY"}
            ];
            if(!$scope.eventEditImageId){
                delete sendingData.images;
            }
            sendingData.images = sendingData.images.filter(function (item) {
                if(item.imageUUID === null || item.imageUUID === undefined) {
                    return false;
                } else {
                    return true;
                }
            });
            console.log(sendingData);
            $scope.editEventSpinner = true;
            eventService.editEvent(sendingData)
                .then(function (data, status) {
                    $scope.editEventSpinner = false;
                    $scope.editEventNotif = true;
                    $scope.editEventErrorNotif = false;
                    console.log(data);
                    $scope.getUserEvents();
                })
                .catch(function (data, status) {
                    $scope.editEventErrorNotif = true;
                    $scope.editEventSpinner = false;
                    document.getElementById("editEventErrorNotif").innerHTML= data.data.message;
                    console.log(data);
                })
        };

        //==================================================== ********* =================================
        $scope.persianToArray = function (date) {
            var dateArray = [];
            dateArray.push(date.year);
            dateArray.push(date.month);
            dateArray.push(date.date);
            dateArray.push(date.hours);
            dateArray.push(date.minutes);
            dateArray.push(date.seconds);
            dateArray.push(date.milliseconds);
            return dateArray;
        };
        $scope.persianToArrayInt = function (date) {
            date = date.map(function (item) {
                return parseInt(item);
            });

            return date;
        };
        $scope.persianToMs = function (date) {
            var newData = date.replace(/[^\w\s]/gi , ' ').split(" ");
            newData.pop();
            newData = newData.map(function (item) {
                return parseInt(item);
            });
            date = persianDate(newData).gDate.getTime();
            return date;
        };

        //==================================================== EDIT HOST =================================
        $scope.eventPlannerEdit = {};
        $scope.editHost = function (index) {


            $scope.submitPlannerEditNotif = false;
            $scope.plannerEditPhotoSuccess = false;
            $scope.coverEditPhotoSuccess = false;
            $scope.eventPlannerEdit = $scope.eventHosts[index];
            console.log($scope.eventHosts[index]);
            $("#editHost").modal("show");
            $scope.eventHosts[index].images.forEach(function (item) {
                if(item.type === "HOST_PHOTO") {
                    $scope.plannerImageIdEdit = item.imageUUID;
                } else if (item.type === "HOST_COVER_PHOTO") {
                    $scope.coverImageIdEdit = item.imageUUID;
                }
            });
            photoService.download($scope.eventHosts[index].images[0].imageUUID)
                .then(function (data, status) {

                    angular.element(document.getElementsByClassName("eventPlannerPhotoUploadEdit"))[0].src = data.data.encodedBase64;
                }, function (data, status) {
                    console.log(data);
                })
                .catch(function (data, status) {
                    console.log(status);
                });

            photoService.download($scope.eventHosts[index].images[1].imageUUID)
                .then(function (data, status) {

                    angular.element(document.getElementsByClassName("coverPhotoUploadEdit"))[0].src = data.data.encodedBase64;
                }, function (data, status) {
                    console.log(data);
                })
                .catch(function (data, status) {
                    console.log(status);
                })
        };
        $scope.editHostSubmit = function (editHostData) {
            editHostData.images.forEach(function (item) {
                if(item.type === "HOST_PHOTO") {
                    item.imageUUID = $scope.plannerImageIdEdit;
                } else if (item.type === "HOST_COVER_PHOTO") {
                    item.imageUUID = $scope.coverImageIdEdit;
                }
            });
            if(!$scope.plannerImageIdEdit && !$scope.coverImageIdEdit){
                delete editHostData.images;
            }
            console.log(editHostData);
            $scope.editPlannerSpinner = true;
            plannerService.editPlannerForm(editHostData)
                .then(function (data, status) {
                    console.log(data);
                    $scope.submitPlannerEditNotif = true;
                    $scope.editPlannerSpinner = false;
                    $scope.submitPlannerEditErrorNotif = false;
                    $scope.getPlannersData();
                })
                .catch(function (data, status) {
                    $scope.submitPlannerEditErrorNotif = true;
                    $scope.editPlannerSpinner = false;
                    document.getElementById("submitPlannerEditErrorNotif").innerHTML= data.data.message;
                    console.log(data);
                })
        };
        //==================================================== ********* =================================
        //==================================================== EDIT EXCHANGE =================================
        $scope.editExchangeNotif = false;
        $scope.editExchangeSpinner = false;

        $scope.editExchange = function (index) {
            $scope.editExchangeNotif = false;
            $scope.exchangeEditPhotoSuccess = false;

            $scope.exchangeEdit = {
                title : $scope.exchangeEditTickets[index].title,
                blitCost : $scope.exchangeEditTickets[index].blitCost,
                isBlitoEvent : $scope.exchangeEditTickets[index].blitoEvent,
                eventDate : persianDate($scope.exchangeEditTickets[index].eventDate).pDate,
                type : $scope.exchangeEditTickets[index].type,
                exchangeBlitId : $scope.exchangeEditTickets[index].exchangeBlitId,
                description :  $scope.exchangeEditTickets[index].description,
                eventAddress : $scope.exchangeEditTickets[index].eventAddress
            };
            var latlng = {
                lat : $scope.exchangeEditTickets[index].latitude,
                lng : $scope.exchangeEditTickets[index].longitude
            };

            $(".persianEditExchangeTime").pDatepicker({
                timePicker: {
                    enabled: true
                },
                altField: '#persianDigitAlt',
                altFormat: "YYYY MM DD HH:mm:ss",
                altFieldFormatter: function (unixDate) {
                    $scope.editExchangeDate = unixDate;
                }
            });
            $(".persianEditExchangeTime").pDatepicker("setDate",$scope.persianToArrayInt($scope.persianToArray(persianDate($scope.exchangeEditTickets[index].eventDate).pDate)));

            $scope.exchangeEditImageId = $scope.exchangeEditTickets[index].image.imageUUID;

            photoService.download($scope.exchangeEditTickets[index].image.imageUUID)
                .then(function (data, status) {

                    angular.element(document.getElementsByClassName("exchangePhotoUploadEdit"))[0].src = data.data.encodedBase64;
                }, function (data, status) {
                    console.log(data);
                })
                .catch(function (data, status) {
                    console.log(status);
                });
            console.log(latlng);
            mapMarkerService.initMap(document.getElementById('mapExchangeEdit'));
            mapMarkerService.setMarker(latlng.lat, latlng.lng);

            $("#editExchange").modal("show");
        };

        $scope.editExchangeTicket = function (editExchangeData) {
            var latLng = mapMarkerService.getMarker();
            $scope.editExchangeSpinner = true;
            editExchangeData.eventDate = $scope.editExchangeDate;
            editExchangeData.email = $scope.userData.email;
            editExchangeData.exchangeBlitId = $scope.exchangeEdit.exchangeBlitId;
            editExchangeData.latitude = latLng.lat;
            editExchangeData.longitude = latLng.lng;
            editExchangeData.phoneNumber = $scope.userData.mobile;
            editExchangeData.image = {
                imageUUID : $scope.exchangeEditImageId,
                type : "EXCHANGEBLIT_PHOTO"
            };
            if(!$scope.exchangeEditImageId){
                delete editExchangeData.image;
            }
            exchangeService.editExchangeForm(editExchangeData)
                .then(function (data, status) {
                    $scope.editExchangeNotif = true;
                    $scope.editExchangeSpinner = false;
                    $scope.editExchangeErrorNotif = false;
                    $scope.getExchangeData();
                    console.log(data);
                }, function (data, status) {
                    $scope.editExchangeErrorNotif = true;
                    $scope.editExchangeSpinner = false;
                    document.getElementById("editExchangeErrorNotif").innerHTML= data.data.message;

                    console.log(data);
                })
        };
        //==================================================== ********* =================================
        //==================================================== GET DATA =================================
        $scope.getExchangeData = function () {
            exchangeService.getExchangeTickets()
                .then(function (data, status) {
                    $scope.exchangeTickets = data.data.content;
                    $scope.exchangeEditTickets = angular.copy(data.data.content);
                    $scope.exchangeTickets = $scope.exchangeTickets.map(function (ticket) {
                        ticket.operatorState = dataService.operatorStatePersian(ticket.operatorState);
                        ticket.state = dataService.stateTypePersian(ticket.state);
                        ticket.type = dataService.eventTypePersian(ticket.type);
                        ticket.eventDate = persianDate(ticket.eventDate).pDate;
                        return ticket;
                    })
                    $timeout(function () {
                        $(".persianTimeExchange").pDatepicker({
                            timePicker: {
                                enabled: true
                            },
                            altField: '#persianDigitAlt',
                            altFormat: "YYYY MM DD HH:mm:ss",
                            altFieldFormatter: function (unixDate) {
                            }
                        });
                    },1000)

                }, function (data, status) {
                    console.log(data);
                })
        };
        $scope.getDataUserEvents = [];
        $scope.getUserEvents = function () {
            eventService.getUserEvents()
                .then(function (data, status) {

                    $scope.userEvents = data.data.content;
                    $scope.userEventsEdit = angular.copy(data.data.content);
                    $scope.userEvents = $scope.userEvents.map(function (event) {
                        event.eventType = dataService.eventTypePersian(event.eventType);
                        event.blitSaleEndDate = persianDate(event.blitSaleEndDate).pDate;
                        event.blitSaleStartDate = persianDate(event.blitSaleStartDate).pDate;
                        event.operatorState = dataService.operatorStatePersian(event.operatorState);
                        return event;
                    });
                    $scope.getDataUserEvents = angular.copy($scope.userEvents);
                    $scope.getDataUserEvents = $scope.getDataUserEvents.map(function (event) {
                        event.eventState = dataService.stateTypePersian(event.eventState);
                        return event;
                    });
                    console.log($scope.getDataUserEvents);

                }, function (data, status) {
                    console.log(data);
                })
        };
        $scope.getPlannersData = function () {
            plannerService.getPlanners()
                .then(function (data, status) {
                    plannerService.setPlanners(data.data.content)
                    $scope.eventHosts = data.data.content;
                }, function (data, status) {
                    console.log(data);
                })
        };
        //==================================================== ********* =================================
    });
