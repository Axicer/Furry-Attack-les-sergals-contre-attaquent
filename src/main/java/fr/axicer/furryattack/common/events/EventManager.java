package fr.axicer.furryattack.common.events;

import fr.axicer.furryattack.common.entity.Updatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventManager implements Updatable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventManager.class);
    private final Map<UUID, EventListener> listeners;
    private Map<UUID, EventListener> bufferedListeners;

    public EventManager() {
        listeners = new HashMap<>();
        bufferedListeners = new HashMap<>();
    }

    public UUID addListener(EventListener listener) {
        var id = UUID.randomUUID();
        listeners.put(id, listener);
        return id;
    }

    public boolean removeListener(UUID id) {
        return listeners.remove(id) != null;
    }

    public void sendEvent(AEvent event) {
        bufferedListeners.forEach((id, listener) ->
                Arrays.stream(listener.getClass().getMethods())
                        .filter(method -> method.getParameterTypes().length == 1)
                        .filter(method -> method.getParameterTypes()[0].isAssignableFrom(event.getClass()))
                        .forEach(method -> {
                            try {
                                method.invoke(listener, event);
                            } catch (Exception ex) {
                                LOGGER.error(ex.getMessage());
                            }
                        }));
    }

    @Override
    public void update() {
        bufferedListeners = new HashMap<>(listeners);
    }
}
