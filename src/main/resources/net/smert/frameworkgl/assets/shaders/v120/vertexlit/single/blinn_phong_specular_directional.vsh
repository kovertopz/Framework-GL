#version 120

////////////////////////////////////////////////////////////////////////////////
// http://www.glprogramming.com/red/chapter05.html                            //
//                                                                            //
// color = (matEmission + globalAmbient * matAmbient) +                       //
//         AttenuationFactor( 1.0 / ( Kc + Kl*d + Kq*d^2 ) ) *                //
//         [ (lightAmbient * matAmbient) +                                    //
//           (max(N.L,0) * lightDiffuse * matDiffuse) +                       //
//           (max(N.H,0)^matShininess * lightSpecular * matSpecular) ]        //
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// In/Out Variables                                                           //
////////////////////////////////////////////////////////////////////////////////
attribute vec4 in_Vertex;
attribute vec4 in_Color;
varying vec4 pass_Color;

////////////////////////////////////////////////////////////////////////////////
// Default Uniforms                                                           //
////////////////////////////////////////////////////////////////////////////////
uniform mat4 uProjectionViewModelMatrix;

////////////////////////////////////////////////////////////////////////////////
// Structs                                                                    //
////////////////////////////////////////////////////////////////////////////////
struct CustomLightSourceParameters
{
    float constantAttenuation;
    float linearAttenuation;
    float quadraticAttenuation;
    float radius;
    float spotInnerCutoffCos;
    float spotOuterCutoffCos;
    float spotExponent;
    vec3 spotEyeDirection;
    vec4 ambient;
    vec4 cameraEyePosition;
    vec4 diffuse;
    vec4 eyePosition;
    vec4 specular;
};

struct CustomMaterialParameters
{
    float shininess;
    vec4 ambient;
    vec4 diffuse;
    vec4 emission;
    vec4 specular;
};

////////////////////////////////////////////////////////////////////////////////
// Uniforms                                                                   //
////////////////////////////////////////////////////////////////////////////////
uniform float uColorMaterialAmbient = 1.0;
uniform float uColorMaterialDiffuse = 1.0;
uniform float uColorMaterialEmission = 0.0;
uniform float uColorMaterialSpecular = 0.0;
uniform CustomLightSourceParameters uLight;
uniform CustomMaterialParameters uMaterialLight;
uniform vec4 uGlobalAmbientLight;

////////////////////////////////////////////////////////////////////////////////
// Main                                                                       //
////////////////////////////////////////////////////////////////////////////////
void main(void)
{
    vec4 matAmbient = mix(uMaterialLight.ambient, in_Color, uColorMaterialAmbient);
    vec4 matDiffuse = mix(uMaterialLight.diffuse, in_Color, uColorMaterialDiffuse);
    vec4 matEmission = mix(uMaterialLight.emission, in_Color, uColorMaterialEmission);
    vec4 matSpecular = mix(uMaterialLight.specular, in_Color, uColorMaterialSpecular);

    // Transform normal into eye space. gl_NormalMatrix is the transpose of the
    // inverse of the upper leftmost 3x3 of gl_ModelViewMatrix.
    vec3 eyeNormal = normalize(gl_NormalMatrix * gl_Normal);

    // Calculate emission and global ambient light
    vec4 emissionAmbient = matEmission + (uGlobalAmbientLight * matAmbient);

    // Calculate ambient
    vec4 lightAmbient = uLight.ambient * matAmbient;

    // Normalize the eyePosition of the light which must be a direction
    vec3 eyeLightDir = normalize(uLight.eyePosition.xyz);

    // Calculate lambert term
    float NdotL = max(dot(eyeNormal, eyeLightDir), 0.0);

    // Calculate diffuse
    vec4 lightDiffuse = NdotL * (uLight.diffuse * matDiffuse);

    // Calculate specular
    vec4 lightSpecular = vec4(0.0);
    if ( NdotL > 0.0 )
    {
        // Transform the vertex into eye space
        vec4 eyeVertex = gl_ModelViewMatrix * in_Vertex;

        float NdotHV = max(dot(eyeNormal, uLight.halfVector.xyz), 0.000001);
        lightSpecular = pow(NdotHV, uMaterialLight.shininess) * (uLight.specular * matSpecular);
    }

    // No attenuation for a directional light
    pass_Color = emissionAmbient + (lightAmbient + lightDiffuse + lightSpecular);
    gl_Position = uProjectionViewModelMatrix * in_Vertex;
}