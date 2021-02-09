package fr.axicer.furryattack.client.update;

import fr.axicer.furryattack.common.entity.Removable;
import fr.axicer.furryattack.common.entity.Updatable;

import java.util.HashSet;
import java.util.Set;

public class ClientUpdater implements Updatable, Removable {

    private final Set<Updatable> updatableSet;

    public ClientUpdater() {
        updatableSet = new HashSet<>();
    }

    public boolean addUpdatableItem(Updatable toUpdate) {
        return updatableSet.add(toUpdate);
    }

    public Updatable removeUpdatableItem(Updatable toRemove) {
        return updatableSet.remove(toRemove) ? toRemove : null;
    }

    @Override
    public void update() {
        updatableSet.forEach(Updatable::update);
    }

    @Override
    public void remove() {
        updatableSet.stream()
                .filter(updatable -> updatable.getClass().isAssignableFrom(Removable.class))
                .forEach(updatable -> ((Removable) updatable).remove());
        updatableSet.clear();
    }
}
