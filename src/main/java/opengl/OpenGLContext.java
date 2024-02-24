package opengl;

public interface OpenGLContext extends AutoCloseable {

	@Override
	void close();

}