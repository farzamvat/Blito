/**
 * Created by soroush on 4/18/17.
 */

$(document).ready(function () {


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



    function draw() {
        requestAnimationFrame(draw);
        scrollEvent();
    }
    draw();
});


function scrollEvent(){
       var viewportTop = $(window).scrollTop();
        if($(window).width()) {
            $('[data-parallax="true"]').each(function () {
                var distance = (viewportTop) * $(this).attr('data-speed');
                if ($(this).attr('data-direction') === 'up') {
                    sym = '-';
                    $(this).css('transform', 'translate3d(0, ' + sym + distance + 'px,0)');

                } else if ($(this).attr('data-direction') === 'right') {
                    sym = '';
                    $(this).css('transform', 'translate3d(' + sym + distance + 'px, 0,0)');
                }
                else if ($(this).attr('data-direction') === 'left') {
                    sym = '-';
                    $(this).css('transform', 'translate3d(' + sym + distance + 'px, 0,0)');
                }
                else {
                    sym = '';
                    $(this).css('transform', 'translate3d(0, ' + sym + distance + 'px,0)');
                }
            });
        }
}
