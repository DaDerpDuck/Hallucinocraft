function initializeCoreMod() {
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

    var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
    var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
    var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode')

    /*
    This is a workaround to an Optifine patch which overwrites _end (to redirect calls to draw)
    This just posts the event to the event bus when draw is invoked
    I was forced to use a coremod since the method in question uses one of Optifine's classes which I of course do not have
    */
    return {
        'BufferUploader#draw': {
            'target': {
                'type': 'METHOD',
                'class': 'com.mojang.blaze3d.vertex.BufferUploader',
                'methodName': 'draw',
                'methodDesc': '(Ljava/nio/ByteBuffer;Lcom/mojang/blaze3d/vertex/VertexFormat$Mode;Lcom/mojang/blaze3d/vertex/VertexFormat;ILcom/mojang/blaze3d/vertex/VertexFormat$IndexType;IZLnet/optifine/render/MultiTextureData;)V'
            },
            'transformer': function(methodNode) {
                var list = ASMAPI.listOf(
                    new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/eventbus/api/IEventBus;"),
                    new TypeInsnNode(Opcodes.NEW, "com/daderpduck/hallucinocraft/events/hooks/RenderEvent$BufferUploadShaderEvent"),
                    new InsnNode(Opcodes.DUP),
                    new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/daderpduck/hallucinocraft/events/hooks/RenderEvent$BufferUploadShaderEvent", "<init>", "()V"),
                    new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraftforge/eventbus/api/IEventBus", "post", "(Lnet/minecraftforge/eventbus/api/Event;)Z")
                );
                methodNode.instructions.insert(list);

                return methodNode;
            }
        }
    }
}