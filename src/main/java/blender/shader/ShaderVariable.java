package blender.shader;

public record ShaderVariable(
        String name,
        ShaderPort port
) {

    public ShaderDataType type() {
        return port.type();
    }

}