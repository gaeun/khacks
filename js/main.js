function manageNavbar() {

    while (scrollP > heights[i]) {
        i++;
    }
    var dist = scrollP - heights[i - 1];
    var sectionHeight = heights[i] - heights[i - 1];
    var reldist = dist / sectionHeight;
    var sections = $('.section-title').length;
    var half_sections = sections * 2;
    var half_section_at = 2 * (i + reldist) - 1;
    var section_at = parseInt(half_section_at / 2 - 0.5);
    var percent_width = 100 * half_section_at / half_sections;

    $('#nav-slider').css('width', percent_width + "%");
    $('.section-link').removeClass('active');
    $('.bubble').removeClass('active');
    if (half_section_at > 1) {
        for (var j = 0; j <= section_at; j++) {
            $($('.bubble')[j]).addClass('active');
        }
        $($('.section-link')[section_at]).addClass('active');
    }
    $('#subtitle').html(' &#8212; ' + $($('.section-link')[section_at]).text());
    if (scrollP > $('#nav-top-tracker').position().top) {
        $('#nav-wrapper').addClass('navbar-fixed-top');
    } else {
        $('#nav-wrapper').removeClass('navbar-fixed-top');
    }
}

function setSplashHeight() {
    if (window.innerWidth <= 767) {
        $('#splash').css('height', window.innerHeight);
    } else {
        $('#splash').css('height', Math.max(500, window.innerHeight - $('#slider-wrapper').height()));
    }
}
var scrollEnabled = true;
$(document).scroll(function() {
    if (!scrollEnabled) {
        return;
    }
    scrollEnabled = false;
    manageNavbar();
    activatePageElements($(window).scrollTop());
    return setTimeout((function() {
        scrollEnabled = true;
    }), 15);
});
var resizeEnabled = true;
$(window).resize(function() {
    if (!resizeEnabled) {
        return;
    }
    resizeEnabled = false;
    setSplashHeight();
    return setTimeout((function() {
        resizeEnabled = true;
    }), 15);
});
$(document).ready(function() {
    setSplashHeight();
    $('a:not(.tab > a)').click(function() {
        $('html, body').animate({
            scrollTop: $($.attr(this, 'href')).offset().top - $('#slider-wrapper').height()
        }, 300);
        return false;
    });
    $('.content-current').each(function() {
        $(this).parent('.tab-content-wrap').css('height', $(this).height());
    });
    manageNavbar();
    $('.tab > a').click(function(e) {
        $(this).parents('ul').children('.tab').removeClass('tab-current');
        $(this).parent().addClass('tab-current');
        var target = $($(this).attr('href'));
        target.parent().children('.tab-content').removeClass('content-current');
        target.addClass('content-current');
        var contentWrap = target.parents('.tab-content-wrap');
        contentWrap.removeClass().addClass('tab-content-wrap');
        contentWrap.addClass('tab' + (target.index() + 1));
        contentWrap.animate({
            height: target.height()
        });
        return false;
    });
    $('.activate-count-up').on('activateIn', function() {
        var goal = parseInt($(this).text());
        var delay = 1000 / goal;
        var incr = 1;
        if (delay < 10) {
            delay = 10;
            incr = parseInt(goal / 100);
        }
        var startTime = new Date();
        var num = 0;
        $(this).text(0);
        var that = this;
        var countup = function() {
            if (num < goal) {
                num = num + incr;
                $(that).text(num);
                var now = new Date();
                if (now - startTime < 1000) {
                    setTimeout(countup, delay);
                } else {
                    $(that).text(goal);
                }
            }
        }
        setTimeout(countup, delay);
    });
    $('.scrollable').animate({
        scrollLeft: $('.content-list').width()
    }, 60000, "linear", function() {
        $('.scrollable').stop(true);
    });
    $(".scrollable").hover(function() {
        $(".scrollable").pause();
    }, function() {
        $(".scrollable").resume();
    });
    if (navigator.userAgent.indexOf('Safari') != -1 && navigator.userAgent.indexOf('Chrome') == -1) {
        $('#curve2').addClass('safari-fix');
    }
});
$(window).load(function() {
    manageNavbar();
});

function activatePageElements(scrollP) {
    $('.scroll-activate:not(.in)').each(function() {
        if ($(this).offset().top - scrollP - window.innerHeight + 100 < 0) {
            $(this).addClass('in').trigger('activateIn');
        }
    });
}
(function() {
    var e = jQuery,
        f = "jQuery.pause",
        d = 1,
        b = e.fn.animate,
        a = {};

    function c() {
        return new Date().getTime()
    }
    e.fn.animate = function(k, h, j, i) {
        var g = e.speed(h, j, i);
        g.complete = g.old;
        return this.each(function() {
            if (!this[f]) {
                this[f] = d++
            }
            var l = e.extend({}, g);
            b.apply(e(this), [k, e.extend({}, l)]);
            a[this[f]] = {
                run: true,
                prop: k,
                opt: l,
                start: c(),
                done: 0
            }
        })
    };
    e.fn.pause = function() {
        return this.each(function() {
            if (!this[f]) {
                this[f] = d++
            }
            var g = a[this[f]];
            if (g && g.run) {
                g.done += c() - g.start;
                if (g.done > g.opt.duration) {
                    delete a[this[f]]
                } else {
                    e(this).stop();
                    g.run = false
                }
            }
        })
    };
    e.fn.resume = function() {
        return this.each(function() {
            if (!this[f]) {
                this[f] = d++
            }
            var g = a[this[f]];
            if (g && !g.run) {
                g.opt.duration -= g.done;
                g.done = 0;
                g.run = true;
                g.start = c();
                b.apply(e(this), [g.prop, e.extend({}, g.opt)])
            }
        })
    }
})();