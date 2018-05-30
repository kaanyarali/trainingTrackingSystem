(function($) {
    let _workoutId = ""
    let _sessionId = ""
    let _currentSession = {}
    let _selectedMovement = {}
    $.fn.beginWorkout = function(workoutId) {
        $("#beginWorkoutBtn").attr("disabled", true)
        $.ajax({
          type: 'POST',
          url: "/workouts/" + workoutId + "/session/",
          dataType: 'json',
          success: function(data) {
            _sessionId = data['id']
            _currentSession = data
            $("#beginWorkoutBtn").hide()
            $("#endWorkoutBtn").fadeIn()
            $("#workoutStarted").fadeIn()
            $("#workoutStarted").text("Started " + moment(data["createdDate"]).fromNow())
            $(".logWorkCol").attr('disabled', false)
            $(".logWorkCol").fadeIn()
          }

        })
    };

    $.fn.endWorkout = function(workoutId,sessionId) {
        $("#endWorkoutBtn").attr("disabled", true)
        if (sessionId == -1)
            sessionId = _currentSession["id"]
        $.ajax({
          type: 'POST',
          url: "/workouts/" + workoutId + "/session/"+ sessionId +"/complete/",
          dataType: 'json',
          success: function(data) {
            $("#endWorkoutBtn").hide()
            $("#workoutStarted").hide()
            $("#beginWorkoutBtn").attr('disabled', false)
            $("#beginWorkoutBtn").fadeIn()
            $(".logWorkCol").fadeOut()
          }

        })
    };

    $.fn.prepareWorkout = function(workoutId) {
        _workoutId = workoutId
    }

    $.fn.prepareSession = function(workoutSession) {
        _currentSession = workoutSession
        _sessionId = workoutSession["id"]
        let created = moment(workoutSession["created"])
        $("#workoutStarted").text("Started " + created.fromNow())
    }

    $.fn.logWork = function(movement, points, targetSets) {
        _selectedMovement = {m: movement, p: points, t:targetSets}
        $("#myModal").modal()
    }

    $("#myModal").on('show.bs.modal', function (event) {
       // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
       // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
       var modal = $(this)
       modal.find('input').val('')
       modal.find('#movement').val(_selectedMovement.m)
       modal.find('.modal-title').text('Log work for ' + _selectedMovement.m)
    })

    $("#myModal .logBtn").click(function(event){
        var $inputs = $('.modal form :input');
        let values = {}
        $inputs.each(function() {
            values[this.name] = $(this).val();
        });
        values['rewardPoints'] = _selectedMovement.p * parseInt(values['numSets']) / _selectedMovement.t
        console.log("Reward points " + values['rewardPoints'])
        $.ajax({
          type: 'POST',
          url: "/workouts/" + _workoutId + "/session/"+ _sessionId,
          dataType: 'json',
          contentType: "application/json; charset=utf-8",
          data: JSON.stringify(values),
          success: function(data) {
            console.log(data)
            $("#"+_selectedMovement.m).attr('disabled', true)
            $("#myModal").modal('hide')
          }
        })
    })

}(jQuery));
