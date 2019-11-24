package co.brick.kszerlag.jmovie.dto;

public class ResponseBase<T> {

    private boolean success;

    private ResponseError error;

    private T dto;

    public ResponseBase() {
        this.success = true;
    }

    private ResponseBase(ResponseError error) {
        this.error = error;
    }

    public static ResponseBase error(String errorCode, String msg) {
        return new ResponseBase(new ResponseError(errorCode, msg));
    }

    public T getDto() {
        return dto;
    }

    public void setDto(T dto) {
        this.dto = dto;
    }
}
