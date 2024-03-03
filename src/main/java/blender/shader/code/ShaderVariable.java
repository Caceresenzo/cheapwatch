package blender.shader.code;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;

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