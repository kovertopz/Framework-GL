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
// Constants                                                                  //
////////////////////////////////////////////////////////////////////////////////
#define MAX_LIGHTS 32

////////////////////////////////////////////////////////////////////////////////
// In/Out Variables                                                           //
////////////////////////////////////////////////////////////////////////////////
attribute vec3 in_Normal;
attribute vec4 in_Vertex;
attribute vec4 in_Color;
attribute vec4 in_TexCoord0;
varying vec4 pass_Color;
varying vec4 pass_TexCoord0;

////////////////////////////////////////////////////////////////////////////////
// Default Uniforms                                                           //
////////////////////////////////////////////////////////////////////////////////
uniform mat3 uNormalMatrix;
uniform mat4 uProjectionViewModelMatrix;
uniform mat4 uViewModelMatrix;

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
uniform int uNumberOfLights = 0;
uniform CustomLightSourceParameters uLights[MAX_LIGHTS];
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

    // Transform normal into eye space. uNormalMatrix is the transpose of the
    // inverse of the upper leftmost 3x3 of uViewModelMatrix.
    vec3 eyeNormal = normalize(uNormalMatrix * in_Normal);

    // Transform the vertex into eye space
    vec4 eyeVertex = uViewModelMatrix * in_Vertex;

    // Calculate emission and global ambient light
    vec4 emissionAmbient = matEmission + (uGlobalAmbientLight * matAmbient);

    // No light by default
    vec4 lightAmbient = vec4(0.0);
    vec4 lightDiffuse = vec4(0.0);
    vec4 lightSpecular = vec4(0.0);

    for ( int i = 0; i < uNumberOfLights; i++ )
    {
        // Calculate the light direction
        vec3 eyeLightDir = normalize(uLights[i].eyePosition.xyz);

        // Calculate ambient
        lightAmbient += uLights[i].ambient * matAmbient;

        // Calculate lambert term
        float NdotL = max(dot(eyeNormal, eyeLightDir), 0.0);

        // Calculate diffuse
        lightDiffuse += NdotL * (uLights[i].diffuse * matDiffuse);

        // Calculate specular
        if ( NdotL > 0.000001 )
        {
            vec3 halfVector = normalize(eyeLightDir - eyeVertex.xyz);
            float NdotHV = max(dot(eyeNormal, halfVector), 0.0);
            lightSpecular += pow(NdotHV, uMaterialLight.shininess) * (uLights[i].specular * matSpecular);
        }
    }

    // No attenuation for a directional light
    pass_Color = emissionAmbient + (lightAmbient + lightDiffuse + lightSpecular);
    pass_TexCoord0 = in_TexCoord0;
    gl_Position = uProjectionViewModelMatrix * in_Vertex;
}