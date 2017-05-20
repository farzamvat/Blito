/**
 * Created by soroush on 4/18/17.
 */

$(document).ready(function () {
    // window.addEventListener("scroll", function() {
    //     if (window.scrollY > 10) {
    //         $('.navbar').css("background-color", "rgba(50, 50, 50, 1)");
    //     }
    //     else {
    //         if (!(window.matchMedia('(max-width: 767px)').matches)) {
    //             $('.navbar').css("background-color", "rgba(30, 30, 30, 0.4)");
    //         }
    //     }
    // },false);


    $('.carousel-control').click(function ($e) {
        $e.preventDefault();
    })
    $('.slidingEventsImg').click(function ($e) {
        $e.preventDefault();
    })
    $('.signInButton').click(function ($e) {
        $e.preventDefault();
    })
    $('.tabAnchor').click(function ($e) {
        $e.preventDefault();
    })
    //Initialize tooltips
    $('.nav-tabs > li a[title]').tooltip();

    $('#buyTicket').on('hidden.bs.modal', function () {
        console.log("a");
        if($('#ticketPay3').hasClass('active')) {
            console.log("B");
            $('.progress-bar').css('width', '0');
            $('#ticketPay3').removeClass('active');
            $('#ticketPay1').addClass('active').addClass('in');
            $('#selectTicket').addClass('active');
            $('#paymentComplete').removeClass('active');


        }

    })


// for every slide in carousel, copy the next slide's item in the slide.
// Do the same for the next, next item.


    function draw() {
        requestAnimationFrame(draw);
        // Drawing code goes here
        scrollEvent();
    }
    draw();
});


function scrollEvent(){
    if(!is_touch_device()){
       var viewportTop = $(window).scrollTop(),
        windowHeight = $(window).height(),
        viewportBottom = windowHeight+viewportTop;

        if($(window).width())

            $('[data-parallax="true"]').each(function(){
                var distance = (viewportTop) * $(this).attr('data-speed');
                if($(this).attr('data-direction') === 'up') {
                    sym = '-';
                    $(this).css('transform','translate3d(0, ' + sym + distance +'px,0)');

                } else if($(this).attr('data-direction') === 'right') {
                    sym = '';
                    $(this).css('transform','translate3d(' + sym + distance +'px, 0,0)');
                }
                else if($(this).attr('data-direction') === 'left') {
                    sym = '-';
                    $(this).css('transform','translate3d(' + sym + distance +'px, 0,0)');
                }
                else {
                    sym = '';
                    $(this).css('transform','translate3d(0, ' + sym + distance +'px,0)');
                }
            });

    }
}


function is_touch_device() {
    return 'ontouchstart' in window // works on most browsers
        || 'onmsgesturechange' in window; // works on ie10
}