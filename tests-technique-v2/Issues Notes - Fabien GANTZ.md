# Technical Test ADEO – Fabien GANTZ

## Issues Notes:

### 1. Adding review does not work: **FIXED** : <br>
**Cause**: Events weren’t updated because the **updateEvent()** method in ***EventController*** was empty, so no actions were done when this was triggered. <br>
This is why changed ratings and comments disappeared when the user refreshed the web page. <br>
Besides, there was not even an ***update()*** method in ***EventService***. <br><br>
**Fix**: 
- First, I had to create an ***update()*** method in ***EventService***. <br>
In this first method I used a **findOne(id)** method from the Repository to get the *original Event*, and then I used the *updated Event* coming from the Front-end to do a quick mapping between the *original* and the *updated Event*, adding the new ratings and comments to the *original Event*. <br>
After this I saved the *original Event* with its 2 updated values.

~~~   
    /**
     * Updates the events
     * @param id id of the event
     * @param updatedEvent the updatedEvent
     */
    public void update(Long id, Event updatedEvent) {

        // Getting the event by Id in database
        final Event event = eventRepository.findOne(id);

        // Updating its values by the new ones
        event.setNbStars(updatedEvent.getNbStars());
        event.setComment(updatedEvent.getComment());

        eventRepository.save(event);
    }
~~~

- Secondly, I had to fill the **updateEvent()** already existing in ***EventController*** with the newly created ***update()*** method in ***EventService***. <br>
Thanks to this, the update information can go through the whole app, from the web page to the database.

~~~
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateEvent(@PathVariable Long id, @RequestBody Event event) { eventService.update(id, event); }
~~~
<br>

### 2. Using the delete button works but elements comes back when I refresh the page: **FIXED** : <br>
**Cause**: Deletion wasn’t done in database because the Spring **@Transactional** annotation in “EventRepository” was tagged as **@Transactional(readOnly = true)**.

> @Transactional(readOnly = true)

**Fix**: I had to remove the “readonly” annotation because this means the user has not the permission to write into the database, the user only had the right to read it. <br>
**@Transactional** alone, by default, allows us to both read and write into the database.
After this fix, we are now able to delete an event again in the app!

> @Transactional

<br><br>

**NB**: Libraries added only for the Unit Tests: JUnit, Mockito.
