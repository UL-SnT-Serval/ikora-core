package lu.uni.serval.ikora.core.error;

public abstract class Error {
    private final String message;

    protected Error(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
