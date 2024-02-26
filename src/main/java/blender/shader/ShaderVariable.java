package blender.shader;

public record ShaderVariable(
        String name,
        ShaderPort port,
        boolean used
) {

    public ShaderDataType type() {
        return port.type();
    }

    public boolean unused() {
        return !used;
    }

}