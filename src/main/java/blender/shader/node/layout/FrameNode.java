package blender.shader.node.layout;

import java.util.Collections;
import java.util.List;

import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.node.ShaderNode;
import lombok.ToString;

@ToString(callSuper = true)
public class FrameNode extends ShaderNode {

    @Override
    public List<ShaderSocket<?>> getInputs() {
        return Collections.emptyList();
    }

    @Override
    public List<ShaderSocket<?>> getOutputs() {
        return Collections.emptyList();
    }

    @Override
    public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {}


    @Override
    public ShaderNode addLink(ShaderSocket<?> fromSocket, ShaderNode toNode, ShaderSocket<?> toSocket) {
        throw new UnsupportedOperationException();
    }

}