package net.oscer.sendcloud.exception;

/**
 * @author kz
 * @date 2018-03-11
 */
public class ReceiverException extends Throwable implements SCException {

	private static final long serialVersionUID = 1L;

	private static final int ERROR_CODE = 301;

	public ReceiverException(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		return String.format("code:%d,message:%s", ERROR_CODE, super.getMessage());
	}

}