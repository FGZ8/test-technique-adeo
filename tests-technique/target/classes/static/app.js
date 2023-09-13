'use strict';

angular.module('myevent', [
    'angular-input-stars'
])
.factory('EventService', EventService)
.controller('EventsController', EventsController);

function EventService($http){
    return {
        deleteEvent:deleteEvent,
        getEvents:getEvents,
        updateStars: updateStars,
        getEventByBandMember: getEventByBandMember
    };

    function deleteEvent(id){
        return $http.delete('/api/events/' + id);
    }

    function getEvents(){
        return $http.get('/api/events/')
            .then(getEventsComplete);

        function getEventsComplete(response){
            return response.data;
        }
    }

    function updateStars(event){
        return $http.put('/api/events/' + event.id, event);
    }

    function getEventByBandMember(query){
        return $http.get('api/events/search/' + query)
            .then(getFilteredEventsComplete);

        function getFilteredEventsComplete(response){
                        return response.data;
                    };
    }
}

function EventsController(EventService){
    var vm = this;
    vm.deleteEvent = deleteEvent;
    vm.updateStars = updateStars;

    activate();

    function activate() {
        return EventService.getEvents()
        .then(function(events) {
            vm.events = events;
            return vm.events;
        });
    }

    function deleteEvent(event){
        var index = vm.events.indexOf(event);
        return EventService.deleteEvent(event.id)
            .then(function() {
                vm.events.splice(index, 1);
            });
    }

    function updateStars(event){
        return EventService.updateStars(event);
    }

    function searchByBandMember(query) {
        return EventService.getFilteredEvents(query);
    }
}