$(function() {
    $('.search__toggler').click(function () {
        $('.search__input').toggle('slow');
    });

    $('.section_size_1 .slider').slick({
        slidesToShow: 4
    });

    $('.section_size_2 .slider').slick({
        slidesToShow: 2
    });
});
