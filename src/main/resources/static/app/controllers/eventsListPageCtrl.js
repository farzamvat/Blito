/**
 * Created by soroush on 4/24/17.
 */

angular.module('eventsPageModule', [])
    .controller('eventsListPageCtrl', function ($scope, $location) {



        switch($location.path()) {
            case '/recentEvents':
                $scope.pageTitle = "آخرین رویدادها";
                break;
            case '/cinema':
                $scope.pageTitle = "سینما";
                break;
            case '/theatre':
                $scope.pageTitle = "تئاتر";
                break;
            case '/cafe':
                $scope.pageTitle = "کافه";
                break;
            case '/cinemaExchange':
                $scope.pageTitle = "بلیت های تعویض سینما";
                break;
            case '/theatreExchange':
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
            { title: "نام رویداد", img: "assets/img/b.jpeg", link: "/eventPage", desc: " توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویداد توضیحات در مورد رویدادتوضیحات در مورد رویداد توضیحات در مورد رویداد"}

        ];

        $scope.currentPage = 1;
    });