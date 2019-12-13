package org.ikora.error;

import org.ikora.model.Position;

public abstract class LocalError extends Error {
    private final Position position;

    public LocalError(String message, Position position){
        super(message);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
