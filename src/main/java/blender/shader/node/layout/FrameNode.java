package blender.shader.node.layout;

import blender.shader.ShaderSocket;
import blender.shader.code.ShaderVariable;
import blender.shader.node.ShaderNode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public void generateCode(StringBuilder builder, List<ShaderVariable> inputs, List<ShaderVariable> outputs) {}


    @Override
    public ShaderNode addLink(ShaderSocket<?> fromSocket, ShaderNode toNode, ShaderSocket<?> toSocket) {
        throw new UnsupportedOperationException();
    }

}