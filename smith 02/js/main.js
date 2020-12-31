$(function(){
	'use-strict';


	// side nav left
	$(".side-nav-left").sideNav({

		edge: 'left',
		closeOnClick: false

	});

	// side nav right
	$(".side-nav-right").sideNav({
		
		edge: 'right',
		closeOnClick: false

	});

	// slider
	$('.slider').slider({full_width: true});
    

	// tabs
	$('ul.tabs').tabs();

    // loader
    $('#fakeLoader').fakeLoader({

       zIndex: 999,
       spinner: "spinner1",
       bgColor: "#ffffff"

    });

	// checkout collapse
	$('.collapsible').collapsible({
        accordion: false
    });

	// portfolio image-popup
	$(".image-popup").magnificPopup({
        type: "image",
        removalDelay: 300,
        mainClass: "mfp-fade"
    });
   

    // testimonial
	$("#owl-testimonial").owlCarousel({

		slideSpeed : 300,
		paginationSpeed : 400,
		singleItem : true,

	})

});