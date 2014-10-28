#version 330

////////////////////////////////////////////////////////////////////////////////
// In/Out Variables                                                           //
////////////////////////////////////////////////////////////////////////////////
in vec3 in_Normal;
in vec4 in_Vertex;
in vec4 in_Color;
in vec4 in_TexCoord0;
out vec3 pass_Normal;
out vec4 pass_Vertex;
out vec4 pass_Color;
out vec4 pass_TexCoord0;

////////////////////////////////////////////////////////////////////////////////
// Default Uniforms                                                           //
////////////////////////////////////////////////////////////////////////////////
uniform mat3 uNormalMatrix;
uniform mat4 uProjectionViewModelMatrix;
uniform mat4 uViewModelMatrix;

////////////////////////////////////////////////////////////////////////////////
// Main                                                                       //
////////////////////////////////////////////////////////////////////////////////
void main(void)
{
    // Transform normal into eye space. uNormalMatrix is the transpose of the
    // inverse of the upper leftmost 3x3 of uViewModelMatrix.
    vec3 eyeNormal = normalize(uNormalMatrix * in_Normal);

    // Transform the vertex into eye space
    vec4 eyeVertex = uViewModelMatrix * in_Vertex;

    pass_Color = in_Color;
    pass_Normal = eyeNormal;
    pass_TexCoord0 = in_TexCoord0;
    pass_Vertex = eyeVertex;
    gl_Position = uProjectionViewModelMatrix * in_Vertex;
}