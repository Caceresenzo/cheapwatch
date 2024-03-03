package blender.shader.node.group;

import blender.shader.ShaderSocket;
import blender.shader.code.ShaderVariable;
import blender.shader.node.ShaderNode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
public class GroupInputShaderNode extends ShaderNode {

    @ToString.Exclude
    private final ShaderNodeGroup group;

    GroupInputShaderNode(ShaderNodeGroup group) {
        this.group = group;

        setName("Group Input");
    }

    @Override
    public List<ShaderSocket<?>> getInputs() {
        return Collections.emptyList();
    }

    @Override
    public List<ShaderSocket<?>> getOutputs() {
        return group.getInputs();
    }

    @Override
    public void generateCode(StringBuilder builder, List<ShaderVariable> inputs, List<ShaderVariable> outputs) {
        builder.append("// inputs:\n");

        final var size = outputs.size();
        for (int index = 0; index < size; index++) {
            final var port = getOutputs().get(index);
            final var variable = outputs.get(index);

            builder.append("// ")
                    .append(port.name())
                    .append(" = ")
                    .append(variable.name())
                    .append("\n");
        }
    }

}