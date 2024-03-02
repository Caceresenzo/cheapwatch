package blender.shader;

public record ShaderVariable(
        String name,
        ShaderSocket port,
        boolean used
) {

    public ShaderDataType type() {
        return port.type();
    }

    public boolean unused() {
        return !used;
    }

}