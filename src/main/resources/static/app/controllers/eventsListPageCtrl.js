/**
 * Created by soroush on 4/24/17.
 */

angular.module('eventsPageModule', [])
    .controller('eventsListPageCtrl', function ($scope, $location) {



        switch($location.path()) {
            case '/recent-events':
                $scope.pageTitle = "ورزشی";
                $scope.pageHeaderMenu = [{title : "سینما", link : '/cinema'},{title : "تئاتر", link : '/theater'},{title : "کافه", link : '/cafe'} , {title : "تور", link : '/cinema'}, {title : "ورزشی", link : '/cinema'}];
                break;
            case '/cinema':
                $scope.pageTitle = "سینما";
                $scope.pageHeaderMenu = [{title : "ورزشی", link : '/recent-events'},{title : "تئاتر", link : '/theater'},{title : "کافه", link : '/cafe'} , {title : "تور", link : '/cinema'}, {title : "ورزشی", link : '/cinema'}];
                break;
            case '/theater':
                $scope.pageTitle = "تئاتر";
                $scope.pageHeaderMenu = [{title : "ورزشی", link : '/recent-events'},{title : "سینما", link : '/cinema'},{title : "کافه", link : '/cafe'} , {title : "تور", link : '/cinema'}, {title : "ورزشی", link : '/cinema'}];
                break;
            case '/cafe':
                $scope.pageTitle = "کافه";
                $scope.pageHeaderMenu = [{title : "ورزشی", link : '/recent-events'},{title : "سینما", link : '/cinema'},{title : "تئاتر", link : '/theater'} , {title : "تور", link : '/cinema'}, {title : "ورزشی", link : '/cinema'}];
                break;
            case '/cinemaExchange':
                $scope.pageTitle = "بلیت های تعویض سینما";
                break;
            case '/theaterExchange':
                $scope.pageTitle = "بلیت های تعویض تئاتر";
                break;
            case '/cafeExchange':
                $scope.pageTitle = "بلیت های تعویض کافه";
                break;
            default:
                break;
        }


        $scope.eventList = [
            { title: "نام رویداد", img: "assets/img/a.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/d.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/e.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/f.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/a.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/a.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/a.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/c.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/a.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"},
            { title: "نام رویداد", img: "assets/img/b.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"}

        ];

        $scope.currentPage = 1;
    });
