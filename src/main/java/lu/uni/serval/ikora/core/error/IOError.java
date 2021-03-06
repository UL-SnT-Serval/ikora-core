package lu.uni.serval.ikora.core.error;

import lu.uni.serval.ikora.core.model.Source;

public class IOError extends Error {
    private Source source;

    public IOError(String message, Source source) {
        super(message);
        this.source = source;
    }

    public Source getSource() {
        return source;
    }
}
