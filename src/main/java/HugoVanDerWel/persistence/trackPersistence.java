package HugoVanDerWel.persistence;

import HugoVanDerWel.services.Database;
import jakarta.inject.Inject;

public class trackPersistence {
    private Database db;

    @Inject
    public trackPersistence(Database db) {
        this.db = db;
    }
}
