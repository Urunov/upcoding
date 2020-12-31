$(function(){
	'use-strict';
    
    // skill
    AnimateCircle("skill-1", 0.95);
    AnimateCircle("skill-2", 0.9);
    AnimateCircle("skill-3", 0.8);
    AnimateCircle("skill-4", 0.7);

    function AnimateCircle(container_id, animatePercentage) {
        var startColor = '#83acda';
        var endColor = '#83acda';

        var element = document.getElementById(container_id);
        var circle = new ProgressBar.Circle(element, {
            color: '#333',
            trailColor: '#eee',
            trailWidth: 7,
            duration: 2000,
            easing: 'bounce',
            strokeWidth: 7,
            text: {
                value: (animatePercentage * 100) + " %",
                className: 'progressbar__label'
            },
            // Set default step function for all animate calls
            step: function (state, circle) {
                circle.path.setAttribute('stroke', state.color);
            }
        });

        circle.animate(animatePercentage, {
            from: {
                color: startColor
            },
            to: {
                color: endColor
            }
        });
    }

});