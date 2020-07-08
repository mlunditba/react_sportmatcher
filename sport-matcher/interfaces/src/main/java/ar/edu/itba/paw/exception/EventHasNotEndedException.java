package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class EventHasNotEndedException extends EventNotFinishedException {

	public EventHasNotEndedException() {
		super("The event has not ended yet");
	}

}
