import bpy
import json

input_fields = ["name", "bl_socket_idname", "type", "default_value", "is_linked"]
output_fields = ["name", "bl_socket_idname", "type", "is_linked"]

library = {}


def convert_value(type, value):
    if type == "VALUE":
        return value

    if type == "VECTOR":
        return dict(zip("xyz", value))

    if type == "RGBA":
        return dict(zip("xyza", value))

    if type == "SHADER":
        raise ValueError(f"unsupported SHADER type with value={value}")

    if type == "ROTATION":
        return dict(zip("xyz", value))

    raise ValueError(f"unknown type={type} value={value}")


def convert_attribute(value):
    if isinstance(value, bpy.types.ShaderNodeTree):
        return value.name

    return value


for group in bpy.data.node_groups:
    if not group.name.startswith("OWM: "):
        continue

    group_name = group.name
    shader_info = library[group_name] = {
        "name": group_name,
        "nodes": {},
        "links": [],
    }

    print(group_name)
    for node in group.nodes:
        node_name = node.name
        node_info = shader_info["nodes"][node_name] = {
            "type": node.bl_idname,
            "name": node_name,
            "attributes": {},
            "inputs": [],
            "outputs": [],
        }

        print("\t" * 1, node_name)

        unique_keys = set(node.bl_rna.properties.keys()) - set(bpy.types.Node.bl_rna.properties.keys())
        print("\t" * 2, "Attributes")
        if len(unique_keys):
            for field in unique_keys:
                value = getattr(node, field)
                node_info["attributes"][field] = convert_attribute(value)
                print("\t" * 3, field, "=", value)

        print("\t" * 2, "Inputs")
        for index, input in enumerate(node.inputs):
            identifier = input.identifier

            if identifier == "__extend__":
                continue

            input_info = {
                "index": index,
                "identifier": identifier,
            }
            node_info["inputs"].append(input_info)

            print("\t" * 3, index, input)

            for field in input_fields:
                try:
                    value = getattr(input, field)
                    input_info[field] = value
                    print("\t" * 4, field, value)
                except AttributeError:
                    pass

            default_value = input_info.get("default_value")
            if default_value is not None:
                converted = convert_value(input_info["type"], default_value)
                input_info["default_value"] = converted

        print("\t" * 2, "Outputs")
        for index, output in enumerate(node.outputs):
            identifier = output.identifier

            if identifier == "__extend__":
                continue

            output_info = {
                "index": index,
                "identifier": identifier,
            }
            node_info["outputs"].append(output_info)

            print("\t" * 3, index, output)

            for field in output_fields:
                try:
                    value = getattr(output, field)
                    output_info[field] = value
                    print("\t" * 4, field, value)
                except AttributeError:
                    pass

for group in bpy.data.node_groups:
    if not group.name.startswith("OWM: "):
        continue

    group_name = group.name
    shader_info = library[group_name]

    print(group_name)
    for link in group.links:
        link_info = {
            "from_node": link.from_node.name,
            "from_socket": {
                "type": link.from_socket.type,
                "identifier": link.from_socket.identifier,
                "index": list(link.from_node.outputs).index(link.from_socket),
            },
            "to_node": link.to_node.name,
            "to_socket": {
                "type": link.to_socket.type,
                "identifier": link.to_socket.identifier,
                "index": list(link.to_node.inputs).index(link.to_socket),
            },
        }

        shader_info["links"].append(link_info)

print(json.dumps(library, indent=4))
print("\n" * 10)

path = "C:/Users/cacer/Desktop/workspaces/enzo/cheapwatch/src/main/resources/library-v2.json"
with open(path, "w") as fd:
    json.dump(library, fd, indent=4)
